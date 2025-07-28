package it.uniroma3.siw.controller;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import it.uniroma3.siw.model.Autore;
import it.uniroma3.siw.model.Credentials;
import it.uniroma3.siw.model.Brano;
import it.uniroma3.siw.model.Commento;
import it.uniroma3.siw.service.AutoreService;
import it.uniroma3.siw.service.CredentialsService;
import it.uniroma3.siw.service.BranoService;
//import jakarta.transaction.Transactional;
import jakarta.validation.Valid;

@Controller
public class BranoController {
	
	@Autowired BranoService branoService;
	@Autowired AutoreService autoreService;
	@Autowired CredentialsService credentialsService;
	
	@GetMapping("/brano/{id}")
	public String mostraBrano(@PathVariable("id") Long id, Model model) {
		Brano brano=this.branoService.getBranoById(id);
		Set<Autore> autori= brano.getAutori();
		List<Commento> commenti= brano.getCommenti();
		
		Authentication authentication= SecurityContextHolder.getContext().getAuthentication();
		
		if(!(authentication instanceof AnonymousAuthenticationToken)) {
			UserDetails userDetails= (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			Credentials credentials= this.credentialsService.getCredentials(userDetails.getUsername());
			model.addAttribute("loggedUser", credentials.getUser());
		}
	    
		model.addAttribute("brano", brano);
		model.addAttribute("autori", autori);
		model.addAttribute("commenti", commenti);
		return "common/brano.html";
	}
	
	@GetMapping("/brano")
	public String mostraBrani(Model model) {
		model.addAttribute("brani", this.branoService.getAllBrani());
		
		return "common/brani.html";
	}
	
	
	@GetMapping("/admin/formNuovoBrano")
	public String formNuovoBrano(Model model) {
		model.addAttribute("brano", new Brano());
		return "admin/formNuovoBrano.html";
	}
	
	
	@PostMapping("/admin/brano")
	public String nuovoBrano(@RequestParam("fileImmagini") List<MultipartFile> immagini,
			@Valid @ModelAttribute("brano") Brano brano, BindingResult bindingResult, Model model) throws IOException {
		if(bindingResult.hasErrors()) {
//			System.out.println("Errore nel binding: " + bindingResult.getAllErrors());
			return "admin/formNuovoBrano";
		}
		else {
			this.branoService.save(brano);
			String uploadDir = "uploads/images/";
			Files.createDirectories(Paths.get(uploadDir));
			
			for (MultipartFile file : immagini) {
	            if (!file.isEmpty()) {
	                // Nome originale (potresti volerlo modificare per evitare conflitti)
	               // String fileName = file.getOriginalFilename();
	                String fileName = brano.getId() + "_"+ file.getOriginalFilename();
	                
	                // Salva fisicamente il file
	                Path filePath = Paths.get(uploadDir, fileName);
	                Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);
	                System.out.println("Salvo immagine in: " + filePath.toAbsolutePath());
	                
	                brano.getImmagini().add(fileName);
	            }
	        }
			this.branoService.save(brano);
			return "redirect:/brano/" + brano.getId();
		}
	}

	@GetMapping("/admin/aggiornaBrani")
	public String aggiornaBrani(Model model) {
		model.addAttribute("brani", this.branoService.getAllBrani());
		return "admin/aggiornaBrani.html";
	}
	
	@GetMapping("/admin/eliminaBrano/{id}")
	public String eliminaBrano(@PathVariable ("id") Long id, Model model) {		
		this.branoService.deleteBranoById(id);
		model.addAttribute("brani", this.branoService.getAllBrani());
		return "admin/aggiornaBrani.html";
	}
	
	
	@GetMapping("/admin/formModificaBrano/{id}")
	public String formModificaBrano(@PathVariable("id") Long id, Model model) {
		model.addAttribute("brano", this.branoService.getBranoById(id));
		return "admin/formModificaBrano.html";
	}
	
	
	@PostMapping("/admin/modificaBrano/{id}")
	public String modificaBrano(@PathVariable("id") Long id, @Valid @ModelAttribute("brano") Brano branoModificato, 
			BindingResult bindingResult, @RequestParam(value = "fileImmagini", required = false) List<MultipartFile> immaginiNuove,
		    @RequestParam(value = "immaginiDaRimuovere", required = false) List<String> immaginiDaRimuovere, Model model) throws IOException{
		if(bindingResult.hasErrors())
			return "admin/formModificaBrano.html";
		else {
			Brano branoEsistente= this.branoService.getBranoById(id);
			branoEsistente.setTitolo(branoModificato.getTitolo());
			branoEsistente.setAnno(branoModificato.getAnno());
			
			if(immaginiDaRimuovere != null) {
		        for(String imgName : immaginiDaRimuovere) {
		            branoEsistente.getImmagini().remove(imgName);
		            
		            // Opzionale: elimina il file dal filesystem
		            Path filePath = Paths.get("uploads/images/", imgName);
		            Files.deleteIfExists(filePath);
		        }
		    }
			
			if(immaginiNuove != null) {
		        String uploadDir = "uploads/images/";
		        Files.createDirectories(Paths.get(uploadDir));

		        for(MultipartFile file : immaginiNuove) {
		            if(!file.isEmpty()) {
		                String fileName = branoEsistente.getId() + "_" + file.getOriginalFilename();
		                Path filePath = Paths.get(uploadDir, fileName);
		                Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);
		                branoEsistente.getImmagini().add(fileName);
		            }
		        }
		    }
			this.branoService.save(branoEsistente);
			model.addAttribute("brani", this.branoService.getAllBrani());
			return "admin/aggiornaBrani.html";
		}
	}
	
	@GetMapping("/admin/modificaAutoriDiBrano/{id}")
	public String modificaAutoriDiBrano(@PathVariable("id") Long id, Model model) {
		Brano brano= this.branoService.getBranoById(id);
		Set<Autore> autori= brano.getAutori();
		
		model.addAttribute("brano", brano);
		model.addAttribute("autori", autori);
		
		return "admin/modificaAutoriDiBrano.html";
	}
	

	@GetMapping("/admin/rimuoviAutoreDaBrano/{idBrano}/{idAutore}")
	public String rimuoviAutoreDaBrano(@PathVariable("idBrano") Long idBrano, @PathVariable Long idAutore, Model model) {
		Brano brano= branoService.getBranoById(idBrano);
		Autore autore= autoreService.getAutoreById(idAutore);

		
		brano.getAutori().remove(autore);
		autore.getBrani().remove(brano);
		
		this.branoService.save(brano);
		this.autoreService.save(autore);
		
		return "redirect:/admin/modificaAutoriDiBrano/"+ brano.getId();
	}
	
	@GetMapping("/admin/aggiungiAltriAutoriABrano/{id}")
	public String aggiungiAltriAutoriABrano(@PathVariable("id") Long id, Model model) {
		model.addAttribute("brano", this.branoService.getBranoById(id));
		model.addAttribute("autori", this.autoriDaAggiungere(id));
		return "admin/aggiungiAltriAutoriABrano.html";
	}
	
	@GetMapping("/admin/aggiungiAutoreABrano/{idBrano}/{idAutore}")
	public String aggiungiAutoreABrano(@PathVariable("idBrano") Long idBrano, @PathVariable("idAutore") Long idAutore, Model model) {
		Brano brano= this.branoService.getBranoById(idBrano);
		Autore autore= this.autoreService.getAutoreById(idAutore);
		brano.getAutori().add(autore);
		autore.getBrani().add(brano);
		
		this.branoService.save(brano);
		this.autoreService.save(autore);
		
		model.addAttribute("brano", brano);
		model.addAttribute("autori", this.autoriDaAggiungere(idBrano));
		
		return "redirect:/admin/aggiungiAltriAutoriABrano/" + brano.getId();
	}
	

	
	
	private List<Autore> autoriDaAggiungere(Long idBrano){
		List<Autore> autori= new ArrayList<>();
		for(Autore a: this.autoreService.findAutoriNonInBrano(idBrano)) {
			autori.add(a);
		}
		
		return autori;
	}
}
