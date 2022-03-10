package com.prestalink.profilelinkedinservice.documents;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.HashMap;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@Document(value="Profile")
public class ProfilLinkedin {
    @Id
    private String idProfile;
    private String profileTitle;
    private String location;
    private int nbrConnexion;
    private String nomProfile;
    private HashMap<String,String> experience;
    private HashMap<String,String> education;

}
