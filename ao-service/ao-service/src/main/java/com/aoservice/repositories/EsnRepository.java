package com.aoservice.repositories;

import com.aoservice.entities.Esn;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EsnRepository extends JpaRepository<Esn,Long> {
    Esn findByEsnUsernameRepresentant(String username);
}
