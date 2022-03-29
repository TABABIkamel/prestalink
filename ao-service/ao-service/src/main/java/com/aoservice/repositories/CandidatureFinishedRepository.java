package com.aoservice.repositories;

import com.aoservice.entities.CandidatureFinished;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CandidatureFinishedRepository extends JpaRepository<CandidatureFinished,Long> {
    CandidatureFinished findByIdTask(String idTask);
}
