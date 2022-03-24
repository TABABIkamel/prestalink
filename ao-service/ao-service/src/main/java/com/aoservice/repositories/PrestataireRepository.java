package com.aoservice.repositories;

import com.aoservice.entities.Prestataire;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface PrestataireRepository extends JpaRepository<Prestataire,Long> {
     public Prestataire findByPrestataireUsername(String usernamePrestataire);

}
