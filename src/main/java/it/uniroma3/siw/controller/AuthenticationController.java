package it.uniroma3.siw.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import it.uniroma3.siw.model.Credentials;
import it.uniroma3.siw.model.User;
import it.uniroma3.siw.service.AutoreService;
import it.uniroma3.siw.service.BranoService;
import it.uniroma3.siw.service.CredentialsService;
import it.uniroma3.siw.service.UserService;
import jakarta.validation.Valid;

@Controller
public class AuthenticationController {
	
	@Autowired
	private CredentialsService credentialsService;
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private BranoService branoService;
	
	@Autowired
	private AutoreService autoreService;
	
	@GetMapping("/register")
	public String mostraFormDiRegistrazione(Model model) {
		model.addAttribute("user", new User());
		model.addAttribute("credentials", new Credentials());
		return "formDiRegistrazione.html";
	}
	
	@GetMapping("/login")
	public String mostraFormLogin(Model model) {
		return "formDiLogin.html";
	}
	
	@GetMapping("/")
	public String index(Model model) {
		Authentication authentication= SecurityContextHolder.getContext().getAuthentication();
		if(authentication instanceof AnonymousAuthenticationToken) {
			model.addAttribute("ultimiBrani", this.branoService.getUltimiBraniInseriti(3));
			model.addAttribute("ultimiAutori", this.autoreService.getUltimiAutoriInseriti(3));
			return "index.html";
		}
		else {
			UserDetails userDetails= (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			Credentials credentials= this.credentialsService.getCredentials(userDetails.getUsername());
			if(credentials.getRole().equals(Credentials.ADMIN_ROLE)) {
				model.addAttribute("ultimiBrani", this.branoService.getUltimiBraniInseriti(3));
				model.addAttribute("ultimiAutori", this.autoreService.getUltimiAutoriInseriti(3));

				return "admin/indexAdmin.html";
			}
			else if(credentials.getRole().equals(Credentials.USER_ROLE)) {
				model.addAttribute("ultimiBrani", this.branoService.getUltimiBraniInseriti(3));
				model.addAttribute("ultimiAutori", this.autoreService.getUltimiAutoriInseriti(3));

				return "user/indexUser.html";
			}
			else {
				model.addAttribute("ultimiBrani", this.branoService.getUltimiBraniInseriti(3));
				model.addAttribute("ultimiAutori", this.autoreService.getUltimiAutoriInseriti(3));

				return "index.html";
			}
		}
	}
	
	@GetMapping("/success")
	public String defaultDopoLogin(Model model) {
		UserDetails userDetails= (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		Credentials credentials= this.credentialsService.getCredentials(userDetails.getUsername());
		
/*		System.out.println("Ruolo dell'utente loggato: " + credentials.getRole());
		
		System.out.println("Ruolo effettivo nel DB: '" + credentials.getRole() + "'");
		System.out.println("Costante ADMIN_ROLE: '" + Credentials.ADMIN_ROLE + "'");
		System.out.println("Match? " + credentials.getRole().equals(Credentials.ADMIN_ROLE));
		System.out.println("Costante ADMIN_ROLE: '" + Credentials.USER_ROLE + "'");
		System.out.println("Match? " + credentials.getRole().equals(Credentials.USER_ROLE));
*/		
		if(credentials.getRole().equals(Credentials.ADMIN_ROLE)) {
			model.addAttribute("ultimiBrani", this.branoService.getUltimiBraniInseriti(3));
			model.addAttribute("ultimiAutori", this.autoreService.getUltimiAutoriInseriti(3));
			return "admin/indexAdmin";
		}
		if(credentials.getRole().equals(Credentials.USER_ROLE)) {
			model.addAttribute("ultimiBrani", this.branoService.getUltimiBraniInseriti(3));
			model.addAttribute("ultimiAutori", this.autoreService.getUltimiAutoriInseriti(3));
			return "user/indexUser.html";
		}
		else {
			model.addAttribute("ultimiBrani", this.branoService.getUltimiBraniInseriti(3));
			model.addAttribute("ultimiAutori", this.autoreService.getUltimiAutoriInseriti(3));
			return "index.html";
		}
	}
	
	@PostMapping("/register")
	public String registerUser(@Valid @ModelAttribute("user") User user, BindingResult userBindingResult, 
			@Valid @ModelAttribute("credentials") Credentials credentials, BindingResult credBindingResult, Model model) {
		
/*		System.out.println("User: " + user);
	    System.out.println("Credentials: " + credentials);
	    System.out.println("User errors: " + userBindingResult.hasErrors());
	    System.out.println("Credentials errors: " + credBindingResult.hasErrors());
	    
	    if(userBindingResult.hasErrors())
	    	userBindingResult.getAllErrors().forEach(error -> System.out.println(error));
	    
	    if(credBindingResult.hasErrors())
	    	credBindingResult.getAllErrors().forEach(error -> System.out.println(error));
*/	    
		if(!userBindingResult.hasErrors() && !credBindingResult.hasErrors()) {
			this.userService.saveUser(user);
			credentials.setUser(user);
			this.credentialsService.saveCredentials(credentials);
			model.addAttribute("user", user);
			return "registrazioneConSuccesso.html";
		}
		else {
			return "formDiRegistrazione.html";
		}
	}

}
