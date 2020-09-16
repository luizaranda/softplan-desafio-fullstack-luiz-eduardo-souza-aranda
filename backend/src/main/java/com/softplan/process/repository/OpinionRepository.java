package com.softplan.process.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.softplan.process.models.Opinion;
import com.softplan.process.models.User;

@Repository
public interface OpinionRepository extends JpaRepository<Opinion, Long> {
    
    @Query(value = "select * from process p,opinion o where p.id = o.process_id and o.aproved is false/* and o.user_id = ?1*/", nativeQuery = true)
	List<Opinion> findAllPending(User user);

}