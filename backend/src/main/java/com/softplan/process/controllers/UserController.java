package com.softplan.process.controllers;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import javax.validation.Valid;

import com.softplan.process.repository.UserRepository;
import com.softplan.process.repository.RoleRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.softplan.process.models.Role;
import com.softplan.process.models.User;
import com.softplan.process.models.ERole;
import com.softplan.process.payload.request.SignupRequest;

@CrossOrigin(origins = "*", maxAge = 3600, exposedHeaders="X-Total-Count")
@RestController
@RequestMapping("/api/users")
public class UserController {
    
    @Autowired
    PasswordEncoder encoder;
    
    @Autowired
    RoleRepository roleRepository;
    
    @Autowired
    private UserRepository repository;
    
    @GetMapping
    @PreAuthorize("hasRole('ROLE_ADMINISTRADOR')")
    public ResponseEntity<?> findAll() {
        List<User> users = repository.findAll();
        HttpHeaders headers = new HttpHeaders();
        headers.set("X-Total-Count", String.valueOf(users.size()));
        return new ResponseEntity<>(users, headers, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_ADMINISTRADOR')")
    public ResponseEntity<?> findById(@PathVariable("id") Long id) {
        try {
            Optional<User> user = repository.findById(id);
            return new ResponseEntity<>(user, HttpStatus.OK);
        } catch (Exception err) {
            return new ResponseEntity<>(HttpStatus.PRECONDITION_FAILED);
        }
    }

    @GetMapping("/username/{name}")
    @PreAuthorize("hasRole('ROLE_ADMINISTRADOR')")
    public ResponseEntity<?> findByUsername(@PathVariable("name") String username) {
        try {
            Optional<User> userFind = repository.findByUsername(username);
            User user = userFind.get();
            return username.equals(user.getUsername()) ? new ResponseEntity<>(user, HttpStatus.OK) : new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (Exception err) {
            return new ResponseEntity<>(HttpStatus.PRECONDITION_FAILED);
        }
    }

    @PostMapping
    @PreAuthorize("hasRole('ROLE_ADMINISTRADOR')")
    public ResponseEntity<?> insert(@RequestBody SignupRequest newUser) {
        try {

            Set<String> strRoles = newUser.getRole();
            Set<Role> roles = new HashSet<>();
        
            User user = new User(newUser.getUsername(), newUser.getEmail(), encoder.encode(newUser.getPassword()));
        
            if (strRoles == null) {
                Role userRole = roleRepository.findByName(ERole.ROLE_FINALIZADOR).orElseThrow(() -> new RuntimeException("Error: Role is not found."));
                roles.add(userRole);
            } else {
                strRoles.forEach(role -> {
                    System.out.println(role);
                    switch (role) {
                    case "admin":
                        Role adminRole = roleRepository.findByName(ERole.ROLE_ADMINISTRADOR).orElseThrow(() -> new RuntimeException("Error: Role is not found."));
                        roles.add(adminRole);
    
                        break;
                    case "triador":
                        Role modRole = roleRepository.findByName(ERole.ROLE_TRIADOR).orElseThrow(() -> new RuntimeException("Error: Role is not found."));
                        roles.add(modRole);
    
                        break;
                    default:
                        Role userRole = roleRepository.findByName(ERole.ROLE_FINALIZADOR).orElseThrow(() -> new RuntimeException("Error: Role is not found."));
                        roles.add(userRole);
                    }
                });
            }

            user.setRoles(roles);
		    repository.save(user);

            return new ResponseEntity<>(user, HttpStatus.OK);
        } catch (Exception err) {
            System.out.println(err.getMessage());
            return new ResponseEntity<>(HttpStatus.PRECONDITION_FAILED);
        }
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_ADMINISTRADOR')")
    public ResponseEntity<?> update(@PathVariable(value = "id") long id, @Valid @RequestBody User newUser) {
        try {
            Optional<User> oldUser = repository.findById(id);

            if (oldUser.isPresent()) {
                User user = oldUser.get();
                user.setUsername(newUser.getUsername());
                if(!encoder.matches(newUser.getPassword(), user.getPassword())){                
                    user.setPassword(encoder.encode(newUser.getPassword()));
                }
                user.setEmail(newUser.getEmail());
                
                user.setRoles(newUser.getRoles());
    
                repository.save(user);
    
                return new ResponseEntity<User>(user, HttpStatus.OK);
            } else {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        } catch (Exception err) {
            return new ResponseEntity<>(HttpStatus.PRECONDITION_FAILED);
        }
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_ADMINISTRADOR')")
    public ResponseEntity<?> delete(@PathVariable(value = "id") long id)
    {
        try {
            Optional<User> user = repository.findById(id);

            if (user.isPresent()) {
                repository.delete(user.get());

                return new ResponseEntity<>(HttpStatus.OK);
            } else {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        } catch (Exception err) {
            return new ResponseEntity<>(HttpStatus.PRECONDITION_FAILED);
        }
    }
}   
