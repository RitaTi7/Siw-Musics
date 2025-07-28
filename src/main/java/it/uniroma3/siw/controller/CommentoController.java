package it.uniroma3.siw.controller;

import org.springframework.beans.factory.annotation.Autowired;
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

import it.uniroma3.siw.model.Credentials;
import it.uniroma3.siw.model.Brano;
import it.uniroma3.siw.model.Commento;
import it.uniroma3.siw.model.User;
import it.uniroma3.siw.service.CredentialsService;
import it.uniroma3.siw.service.BranoService;
import it.uniroma3.siw.service.CommentoService;
import it.uniroma3.siw.service.UserService;
import jakarta.validation.Valid;

@Controller
public class CommentoController {
	
	@Autowired CommentoService commentoService;
	@Autowired BranoService branoService;
	@Autowired CredentialsService credentialsService;
	@Autowired UserService userService;
	
	
	@GetMapping("/user/formNuovoCommento/{idBrano}")				
	public String formNuovaCommento(@PathVariable("idBrano") Long id, Model model) {
		model.addAttribute("commento", new Commento());
		model.addAttribute("brano", this.branoService.getBranoById(id));
		return "user/formNuovoCommento.html";
	}
	
	@PostMapping("/user/commento/{idBrano}")												//l'ordine è importante!!!!
	public String nuovoCommento (@PathVariable("idBrano") Long id, @Valid @ModelAttribute("commento") Commento commento, BindingResult bindingResult, Model model) {
		System.out.println(">>>> ENTRATO NEL POST MAPPING <<<<");

		if(bindingResult.hasErrors()) {
			System.out.println("Errore nel binding: " + bindingResult.getAllErrors());
			model.addAttribute("brano", this.branoService.getBranoById(id));
			return "user/formNuovoCommento.html";	
		}
		else {
			System.out.println("Errore nel binding: " + bindingResult.getAllErrors());
			UserDetails userDetails= (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			Credentials credentials= this.credentialsService.getCredentials(userDetails.getUsername());
			User user= credentials.getUser();
			
			commento.setUsernameUtente(credentials.getUsername());
			commento.setUser(user);
			user.getCommenti().add(commento);
			
		    Brano brano= this.branoService.getBranoById(id);
			commento.setBrano(brano);		
			brano.getCommenti().add(commento);
			this.commentoService.save(commento);
			return "redirect:/brano/" + brano.getId();
		}
	}
	
	@GetMapping("/eliminaCommento/{idCommento}/{idBrano}")
	public String eliminaCommento(@PathVariable("idCommento") Long idCommento, @PathVariable("idBrano") Long idBrano, Model model) {
		Commento commento= this.commentoService.getCommentoById(idCommento);
		Brano brano= this.branoService.getBranoById(idBrano);
		UserDetails userDetails= (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		Credentials credentials= this.credentialsService.getCredentials(userDetails.getUsername());
		User user= credentials.getUser();
		
		if(!credentials.getRole().equals(Credentials.ADMIN_ROLE)) {
			if( ! (credentials.getRole().equals(Credentials.USER_ROLE) && user.equals(commento.getUser()))) {
				return "redirect:/brano/" + brano.getId();
			}	
		}
		
		brano.getCommenti().remove(commento);
		this.branoService.save(brano);
		
		user.getCommenti().remove(commento);
		this.userService.saveUser(user);
		
		this.commentoService.deleteCommentoById(idCommento);
		
		return "redirect:/brano/" + brano.getId();
	}
	
	
	
	
	

}
