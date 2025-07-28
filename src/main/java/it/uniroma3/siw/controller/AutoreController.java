package it.uniroma3.siw.controller;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
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
import it.uniroma3.siw.model.Brano;
import it.uniroma3.siw.service.AutoreService;
import it.uniroma3.siw.service.BranoService;
///*import jakarta.transaction.Transactional;*/
import jakarta.validation.Valid;

@Controller
public class AutoreController {

    private final BranoService branoService;
	
	@Autowired AutoreService autoreService;

    AutoreController(BranoService branoService) {
        this.branoService = branoService;
    }
	
	@GetMapping("/autore/{id}")
	public String mostraAutore(@PathVariable("id") Long id, Model model) {
		Autore autore=this.autoreService.getAutoreById(id);
		model.addAttribute("autore", autore);
		model.addAttribute("brani", autore.getBrani());
		return "common/autore.html";
	}
	
	@GetMapping("/autore")
	public String mostraAutori(Model model) {
		model.addAttribute("autori", this.autoreService.getAllAutori());
		return "common/autori.html";
	}
	
	@GetMapping("/admin/formNuovoAutore")
	public String formNuovoAutore(Model model) {
		model.addAttribute("autore", new Autore());
		return "admin/formNuovoAutore.html";
	}
	
	@PostMapping("/admin/autore")
	public String nuovoAutore(@Valid @ModelAttribute("autore") Autore autore, BindingResult bindingResult, @RequestParam("fileImmagine") MultipartFile fileImmagine, Model model)  throws IOException{
		if(bindingResult.hasErrors()) {
			return "admin/formNuovoAutore.html";
		}
		else {
			this.autoreService.save(autore);
			if (fileImmagine != null && !fileImmagine.isEmpty()) {
		        String uploadDir = "uploads/images/";
		        Files.createDirectories(Paths.get(uploadDir));

		        String fileName = autore.getId() + "_" + fileImmagine.getOriginalFilename();
		        Path filePath = Paths.get(uploadDir, fileName);
		        Files.copy(fileImmagine.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);
		        System.out.println("Salvo immagine in: " + filePath.toAbsolutePath());

		        autore.setImmagine(fileName);
		        this.autoreService.save(autore); // Secondo salvataggio per aggiornare con immagine
		    }
			return "redirect:/autore/" + autore.getId();
		}
	}
	
	@GetMapping("/admin/aggiornaAutori")
	public String aggiornaAutori(Model model) {
		model.addAttribute("autori", this.autoreService.getAllAutori());
		return "admin/aggiornaAutori.html";
	}
	
	@GetMapping("/admin/eliminaAutore/{id}")
	public String eliminaAutore(@PathVariable("id") Long id, Model model) {
		Autore autore= this.autoreService.getAutoreById(id);
		for(Brano brano: autore.getBrani()) {
			brano.getAutori().remove(autore);
			this.branoService.save(brano);
		}
		this.autoreService.deleteAutoreById(id);
		model.addAttribute("autori", this.autoreService.getAllAutori());
		return "admin/aggiornaAutori.html";
	}
	
	
	@GetMapping("/admin/formModificaAutore/{id}")
	public String formModificaAutore(@PathVariable("id") Long id, Model model) {
		model.addAttribute("autore", this.autoreService.getAutoreById(id));
		return "admin/formModificaAutore.html";
	}
	
	@PostMapping("/admin/modificaAutore/{id}")
	public String modificaAutore(@PathVariable("id") Long id, @Valid @ModelAttribute("autore") Autore autoreModificato, BindingResult bindingResult, @RequestParam("fileImmagine") MultipartFile immagine, Model model) throws IOException {
		if(bindingResult.hasErrors()) {
//			System.out.println("Errore nel binding: " + bindingResult.getAllErrors());
			model.addAttribute("autore", this.autoreService.getAutoreById(id));
			return "admin/formModificaAutore.html";
		}
		else {
			Autore autoreEsistente= this.autoreService.getAutoreById(id);
			autoreEsistente.setNome(autoreModificato.getNome());
			autoreEsistente.setCognome(autoreModificato.getCognome());
			autoreEsistente.setDataNascita(autoreModificato.getDataNascita());
			autoreEsistente.setDataMorte(autoreModificato.getDataMorte());
			autoreEsistente.setNazionalita(autoreModificato.getNazionalita());
			
			 if (immagine != null && !immagine.isEmpty()) {
		            String uploadDir = "uploads/images/";
		            Files.createDirectories(Paths.get(uploadDir));
		            
		            
		            if (autoreEsistente.getImmagine() != null && !autoreEsistente.getImmagine().isEmpty()) {
		                Path oldFilePath = Paths.get(uploadDir, autoreEsistente.getImmagine());
		                Files.deleteIfExists(oldFilePath);
		            }
		            String fileName = id + "_" + immagine.getOriginalFilename();
		            Path filePath = Paths.get(uploadDir, fileName);
		            Files.copy(immagine.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);
		            autoreEsistente.setImmagine(fileName);
			 }
			 
			this.autoreService.save(autoreEsistente);
			model.addAttribute("autori", this.autoreService.getAllAutori());
			
			return "admin/aggiornaAutori.html";
		}
	}
	
	@GetMapping("/admin/modificaBraniDiAutore/{id}")
	public String modificaBraniDiAutore(@PathVariable("id") Long id, Model model) {
		Autore autore= this.autoreService.getAutoreById(id);
		
		model.addAttribute("autore", autore);
		model.addAttribute("brani", autore.getBrani());
		
		return "admin/modificaBraniDiAutore.html";
	}
	
//	@Transactional
	@GetMapping("/admin/rimuoviBranoDaAutore/{idAutore}/{idBrano}")
	public String rimuoviBranoDaAutore(@PathVariable("idAutore") Long idAutore, @PathVariable("idBrano") Long idBrano, Model model) {
		Autore autore= this.autoreService.getAutoreById(idAutore);
		Brano brano= this.branoService.getBranoById(idBrano);
		
		autore.getBrani().remove(brano);
		brano.getAutori().remove(autore);
		
		this.autoreService.save(autore);
		this.branoService.save(brano);
		
		return "redirect:/admin/modificaBraniDiAutore/" + autore.getId();
	}
	
	@GetMapping("/admin/aggiungiAltriBraniAdAutore/{id}")
	public String aggiungiAltriBraniAdAutore(@PathVariable("id") Long id, Model model) {
		model.addAttribute("autore", this.autoreService.getAutoreById(id));
		model.addAttribute("brani", this.braniDaAggiungere(id));
		return "admin/aggiungiAltriBraniAdAutore.html";
	}
	
	@GetMapping("/admin/aggiungiBranoAdAutore/{idAutore}/{idBrano}")
	public String aggiungiBranoAdAutore(@PathVariable("idAutore") Long idAutore, @PathVariable("idBrano") Long idBrano, Model model) {
		Autore autore= this.autoreService.getAutoreById(idAutore);
		Brano brano= this.branoService.getBranoById(idBrano);
		
		autore.getBrani().add(brano);
		brano.getAutori().add(autore);
		
		this.autoreService.save(autore);
		this.branoService.save(brano);
		
		model.addAttribute("autore", autore);
		model.addAttribute("brani", this.braniDaAggiungere(idAutore));
		
		return "redirect:/admin/aggiungiAltriBraniAdAutore/" + autore.getId();
	}
	
	private List<Brano> braniDaAggiungere(Long idAutore){
		List<Brano> brani= new ArrayList<>();
		
		for(Brano b: this.branoService.findBraniNonInAutore(idAutore)) {
			brani.add(b);
		}
		return brani;
	}
}
