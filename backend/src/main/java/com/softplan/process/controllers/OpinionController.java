package com.softplan.process.controllers;

import java.util.List;
import java.util.Optional;

import javax.validation.Valid;

import com.softplan.process.repository.OpinionRepository;
import com.softplan.process.repository.ProcessRepository;
import com.softplan.process.security.services.UserDetailsImpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.softplan.process.models.Opinion;
import com.softplan.process.models.User;
import com.softplan.process.models.Process;
import com.softplan.process.payload.request.OpinionRequest;

@CrossOrigin(origins = "*", maxAge = 3600, exposedHeaders="X-Total-Count")
@RestController
@RequestMapping("/api/opinion")
public class OpinionController {
    
    @Autowired
    PasswordEncoder encoder;
    
    @Autowired
    private OpinionRepository repository;
    
    @Autowired
    private ProcessRepository repositoryProcess;
    
    @GetMapping
    @PreAuthorize("hasRole('ROLE_TRIADOR') or hasRole('ROLE_FINALIZADOR') or hasRole('ROLE_ADMINISTRADOR')")
    public ResponseEntity<?> findAll() {

        UserDetailsImpl principal = (UserDetailsImpl)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        HttpHeaders headers = new HttpHeaders();
        
        if(principal.getAuthorities().toString().equals("ROLE_ADMINISTRADOR")){
            List<Opinion> opnion = repository.findAll();
            
            headers.set("X-Total-Count", String.valueOf(opnion.size()));
            return new ResponseEntity<>(opnion, headers, HttpStatus.OK);
        } else {
            User user = new User(principal.getUsername(), principal.getEmail(), principal.getPassword());
            user.setId(principal.getId());

            List<Opinion> opnion = repository.findAllPending(user);
            headers.set("X-Total-Count", String.valueOf(opnion.size()));
            return new ResponseEntity<>(opnion, headers, HttpStatus.OK);
        }        
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_TRIADOR') or hasRole('ROLE_FINALIZADOR') or hasRole('ROLE_ADMINISTRADOR')")
    public ResponseEntity<?> findById(@PathVariable("id") Long id) {
        try {
            Optional<Opinion> opinion = repository.findById(id);
            return new ResponseEntity<>(opinion, HttpStatus.OK);
        } catch (Exception err) {
            return new ResponseEntity<>(HttpStatus.PRECONDITION_FAILED);
        }
    }

    @PostMapping
    @PreAuthorize("hasRole('ROLE_TRIADOR') or hasRole('ROLE_FINALIZADOR') or hasRole('ROLE_ADMINISTRADOR')")
    public ResponseEntity<?> insert(@RequestBody OpinionRequest newOpinion) {
        try {
            UserDetailsImpl principal = (UserDetailsImpl)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            User user = new User(principal.getUsername(), principal.getEmail(), principal.getPassword());
            user.setId(principal.getId());

            Opinion opinion = new Opinion();
            Optional<Process> process = repositoryProcess.findById(Long.parseLong(newOpinion.getProcessId()));
            opinion.setDescription(newOpinion.getDescription());
            opinion.setAproved(Boolean.parseBoolean(newOpinion.getAproved()));
            opinion.setProcess(process.get());
            opinion.setUser(user);

            repository.save(opinion);
            return new ResponseEntity<>(opinion, HttpStatus.OK);
        } catch (Exception err) {
            System.out.println(err);
            return new ResponseEntity<>(HttpStatus.PRECONDITION_FAILED);
        }
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_TRIADOR') or hasRole('ROLE_FINALIZADOR') or hasRole('ROLE_ADMINISTRADOR')")
    public ResponseEntity<?> update(@PathVariable(value = "id") long id, @Valid @RequestBody Opinion newOpinion) {
        try {
            Optional<Opinion> oldOpinion = repository.findById(id);

            if (oldOpinion.isPresent()) {
                Opinion opinion = oldOpinion.get();
                opinion.setDescription(newOpinion.getDescription());
                opinion.setAproved(newOpinion.getAproved());
    
                repository.save(opinion);
    
                return new ResponseEntity<Opinion>(opinion, HttpStatus.OK);
            } else {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        } catch (Exception err) {
            return new ResponseEntity<>(HttpStatus.PRECONDITION_FAILED);
        }
    }
}   
