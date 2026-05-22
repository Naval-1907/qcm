package model;

/**
 * Représente un résultat d'examen enregistré pour un étudiant.
 * Ce POJO contient la relation vers l'étudiant et la note obtenue.
 */
public class Examen {
    private String numExam;
    private String numEtudiant;
    private String anneeUniv;
    private double note;

    public Examen() {
        // Constructeur vide nécessaire pour les manipulations.
    }

    public Examen(String numExam, String numEtudiant, String anneeUniv, double note) {
        this.numExam = numExam;
        this.numEtudiant = numEtudiant;
        this.anneeUniv = anneeUniv;
        this.note = note;
    }

    public String getNumExam() {
        return numExam;
    }

    public void setNumExam(String numExam) {
        this.numExam = numExam;
    }

    public String getNumEtudiant() {
        return numEtudiant;
    }

    public void setNumEtudiant(String numEtudiant) {
        this.numEtudiant = numEtudiant;
    }

    public String getAnneeUniv() {
        return anneeUniv;
    }

    public void setAnneeUniv(String anneeUniv) {
        this.anneeUniv = anneeUniv;
    }

    public double getNote() {
        return note;
    }

    public void setNote(double note) {
        this.note = note;
    }
}
