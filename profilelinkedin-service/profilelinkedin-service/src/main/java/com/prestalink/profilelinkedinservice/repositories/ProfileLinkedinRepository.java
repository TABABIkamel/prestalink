package com.prestalink.profilelinkedinservice.repositories;
import com.prestalink.profilelinkedinservice.documents.ProfilLinkedin;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProfileLinkedinRepository extends MongoRepository<ProfilLinkedin,Long> {

}
