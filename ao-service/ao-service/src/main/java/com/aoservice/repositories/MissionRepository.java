package com.aoservice.repositories;

import com.aoservice.entities.AppelOffre;
import com.aoservice.entities.Mission;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface MissionRepository extends JpaRepository<Mission,Long> {

    @Query("SELECT mission FROM Mission mission join mission.appelOffre appelOffre where appelOffre.id=:idAppelOffre ")
    Mission getMissionByIdAppelOffre(@Param("idAppelOffre") Long idAppelOffre);

}
