package com.aoservice.repositories;

import com.aoservice.entities.UrlContract;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UrlContractRepository extends JpaRepository<UrlContract,Long> {
}
