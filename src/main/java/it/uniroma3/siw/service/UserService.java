package it.uniroma3.siw.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import it.uniroma3.siw.repository.UserRepository;
import it.uniroma3.siw.model.User;


@Service
public class UserService {
	
	@Autowired
	protected UserRepository userRepository;
	
	@Transactional
	public User getUser(Long id) {
		Optional<User> result= this.userRepository.findById(id);
		return result.orElse(null);
	}
	
	@Transactional
	public User saveUser(User user) {
		return this.userRepository.save(user);
	}
	
	@Transactional
	public List<User> getAllUser(){
		List<User> result= new ArrayList<>();
		Iterable<User> iterable= this.userRepository.findAll();
		for(User u: iterable) {
			result.add(u);
		}
		return result;
	}
	
	
	
	
}
