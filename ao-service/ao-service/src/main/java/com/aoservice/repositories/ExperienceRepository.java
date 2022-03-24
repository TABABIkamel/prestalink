package com.aoservice.repositories;

import com.aoservice.entities.Experience;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ExperienceRepository extends JpaRepository<Experience,Long> {

    @Query("SELECT exp FROM Experience exp join exp.prestataire pres where pres.prestataireUsername=:usernamePrestataire ")
    List<Experience> getExperiencePrestataire(@Param("usernamePrestataire") String usernamePrestataire);

}
