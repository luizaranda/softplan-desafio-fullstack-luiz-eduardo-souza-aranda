package com.softplan.process.controllers;

import java.util.List;
import java.util.Optional;

import javax.validation.Valid;

import com.softplan.process.repository.ProcessRepository;

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

import com.softplan.process.models.Process;

@CrossOrigin(origins = "*", maxAge = 3600, exposedHeaders="X-Total-Count")
@RestController
@RequestMapping("/api/process")
public class ProcessController {
    
    @Autowired
    PasswordEncoder encoder;
    
    @Autowired
    private ProcessRepository repository;
    
    @GetMapping
    @PreAuthorize("hasRole('ROLE_TRIADOR') or hasRole('ROLE_FINALIZADOR') or hasRole('ROLE_ADMINISTRADOR')")
    public ResponseEntity<?> findAll() {
        List<Process> process = repository.findAll();
        HttpHeaders headers = new HttpHeaders();
        headers.set("X-Total-Count", String.valueOf(process.size()));
        return new ResponseEntity<>(process, headers, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_TRIADOR') or hasRole('ROLE_FINALIZADOR') or hasRole('ROLE_ADMINISTRADOR')")
    public ResponseEntity<?> findById(@PathVariable("id") Long id) {
        try {
            Optional<Process> process = repository.findById(id);
            return new ResponseEntity<>(process, HttpStatus.OK);
        } catch (Exception err) {
            return new ResponseEntity<>(HttpStatus.PRECONDITION_FAILED);
        }
    }

    @GetMapping("/find/{number}")
    @PreAuthorize("hasRole('ROLE_TRIADOR') or hasRole('ROLE_FINALIZADOR')")
    public ResponseEntity<?> findByNumber(@PathVariable("number") String number) {
        try {
            Optional<Process> processFind = repository.findByNumber(number);
            Process process = processFind.get();
            return number.equals(process.getNumber()) ? new ResponseEntity<>(process, HttpStatus.OK) : new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (Exception err) {
            return new ResponseEntity<>(HttpStatus.PRECONDITION_FAILED);
        }
    }

    @PostMapping
    @PreAuthorize("hasRole('ROLE_TRIADOR') or hasRole('ROLE_ADMINISTRADOR')")
    public ResponseEntity<?> insert(@RequestBody Process process) {
        try {
            repository.save(process);
            return new ResponseEntity<>(process, HttpStatus.OK);
        } catch (Exception err) {
            return new ResponseEntity<>(HttpStatus.PRECONDITION_FAILED);
        }
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_TRIADOR') or hasRole('ROLE_FINALIZADOR') or hasRole('ROLE_ADMINISTRADOR')")
    public ResponseEntity<?> update(@PathVariable(value = "id") long id, @Valid @RequestBody Process newProcess) {
        try {
            Optional<Process> oldProcess = repository.findById(id);

            if (oldProcess.isPresent()) {
                Process process = oldProcess.get();
                process.setNumber(newProcess.getNumber());
                process.setAuthor(newProcess.getAuthor());
                process.setDefendant(newProcess.getDefendant());
                process.setDistributionDate(newProcess.getDistributionDate());
    
                repository.save(process);
    
                return new ResponseEntity<Process>(process, HttpStatus.OK);
            } else {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        } catch (Exception err) {
            System.out.println(err.getMessage());
            return new ResponseEntity<>(HttpStatus.PRECONDITION_FAILED);
        }
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_ADMINISTRADOR')")
    public ResponseEntity<?> delete(@PathVariable(value = "id") long id)
    {
        try {
            Optional<Process> process = repository.findById(id);

            if (process.isPresent()) {
                repository.delete(process.get());

                return new ResponseEntity<>(HttpStatus.OK);
            } else {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        } catch (Exception err) {
            return new ResponseEntity<>(HttpStatus.PRECONDITION_FAILED);
        }
    }
}   
