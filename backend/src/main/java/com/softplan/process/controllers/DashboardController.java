package com.softplan.process.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/dashboard")
public class DashboardController {

	@GetMapping("/all")
	public String allAccess() {
		return "Public Content.";
	}
	
	@GetMapping("/finalizador")
	@PreAuthorize("hasRole('FINALIZADOR') or hasRole('TRIADOR') or hasRole('ADMINISTRADOR')")
	public String userAccess() {
		return "User Content.";
	}

	@GetMapping("/triador")
	@PreAuthorize("hasRole('TRIADOR') or hasRole('ADMINISTRADOR')")
	public String moderatorAccess() {
		return "Moderator Board.";
	}

	@GetMapping("/users")
	@PreAuthorize("hasRole('ADMINISTRADOR')")
	public ResponseEntity<?> adminAccess() {
		UserController userController = new UserController();
		return userController.findAll();
	}
}