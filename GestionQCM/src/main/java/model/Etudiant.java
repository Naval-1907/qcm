package model;

/**
 * Représente un étudiant du projet Gestion des Questionnaires.
 * Ce POJO contient les informations de base nécessaires à l'application.
 */
public class Etudiant {
    private String numEtudiant;
    private String nom;
    private String prenoms;
    private String niveau;
    private String adrEmail;

    public Etudiant() {
        // Constructeur vide nécessaire pour certaines opérations de JSP/Servlet.
    }

    public Etudiant(String numEtudiant, String nom, String prenoms, String niveau, String adrEmail) {
        this.numEtudiant = numEtudiant;
        this.nom = nom;
        this.prenoms = prenoms;
        this.niveau = niveau;
        this.adrEmail = adrEmail;
    }

    public String getNumEtudiant() {
        return numEtudiant;
    }

    public void setNumEtudiant(String numEtudiant) {
        this.numEtudiant = numEtudiant;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getPrenoms() {
        return prenoms;
    }

    public void setPrenoms(String prenoms) {
        this.prenoms = prenoms;
    }

    public String getNiveau() {
        return niveau;
    }

    public void setNiveau(String niveau) {
        this.niveau = niveau;
    }

    public String getAdrEmail() {
        return adrEmail;
    }

    public void setAdrEmail(String adrEmail) {
        this.adrEmail = adrEmail;
    }
}
