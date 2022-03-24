package com.aoservice.repositories;

import com.aoservice.entities.Education;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EducationRepository extends JpaRepository<Education,Long> {
    @Query("SELECT educ FROM Education educ join educ.prestataire pres where pres.prestataireUsername=:usernamePrestataire ")
    List<Education> getEducationPrestataire(@Param("usernamePrestataire") String usernamePrestataire);
}
