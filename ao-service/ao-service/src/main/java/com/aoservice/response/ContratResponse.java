package com.aoservice.response;

public class ContratResponse {

    private String urlContrat;
    private String titreAppelOffre;
    private String nomInternaute;

    public ContratResponse() {
    }

    public ContratResponse(String urlContrat, String titreAppelOffre, String nomInternaute) {
        this.urlContrat = urlContrat;
        this.titreAppelOffre = titreAppelOffre;
        this.nomInternaute = nomInternaute;
    }

    public String getUrlContrat() {
        return urlContrat;
    }

    public void setUrlContrat(String urlContrat) {
        this.urlContrat = urlContrat;
    }

    public String getTitreAppelOffre() {
        return titreAppelOffre;
    }

    public void setTitreAppelOffre(String titreAppelOffre) {
        this.titreAppelOffre = titreAppelOffre;
    }

    public String getNomInternaute() {
        return nomInternaute;
    }

    public void setNomInternaute(String nomInternaute) {
        this.nomInternaute = nomInternaute;
    }
}
