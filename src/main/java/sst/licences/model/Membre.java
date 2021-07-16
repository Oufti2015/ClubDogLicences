package sst.licences.model;

import lombok.Data;

import java.time.LocalDate;

@Data
public class Membre {
    private String nom;
    private String prenom;
    private String rue;
    private String num;
    private String codePostal;
    private String localite;
    private String telephone;
    private String gsm;
    private String email;
    private LocalDate dateDeNaissance;
    private String codePays;
    private String langue;
    private String licence;
    private boolean comite;
    private LocalDate affiliation;
}
