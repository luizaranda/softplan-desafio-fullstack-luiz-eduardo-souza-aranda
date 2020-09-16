package com.softplan.process.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.softplan.process.models.Process;

@Repository
public interface ProcessRepository extends JpaRepository<Process, Long> {
	
	Optional<Process> findById(Long id);

	Optional<Process> findByNumber(String number);
}