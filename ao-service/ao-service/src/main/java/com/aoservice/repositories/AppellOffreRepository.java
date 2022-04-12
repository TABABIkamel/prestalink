package com.aoservice.repositories;

import com.aoservice.entities.AppelOffre;
import com.aoservice.entities.Education;
import com.aoservice.entities.Esn;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
    public interface AppellOffreRepository extends JpaRepository<AppelOffre,Long> {
    @Query("SELECT ao FROM AppelOffre ao where ao.id=:idPost ")
    AppelOffre getAoById(@Param("idPost") Long idPost);
    AppelOffre findByRefAo(String refAo);
}
