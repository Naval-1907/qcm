package model;

/**
 * Représente une question de type QCM du projet Gestion des Questionnaires.
 * Ce POJO contient la question, ses quatre propositions et la bonne réponse.
 */
public class Qcm {
    private String numQuest;
    private String question;
    private String reponse1;
    private String reponse2;
    private String reponse3;
    private String reponse4;
    private String bonneReponse;

    public Qcm() {
        // Constructeur vide utilisé par la servlet et les formulaires JSP.
    }

    public Qcm(String numQuest, String question, String reponse1, String reponse2, String reponse3, String reponse4, String bonneReponse) {
        this.numQuest = numQuest;
        this.question = question;
        this.reponse1 = reponse1;
        this.reponse2 = reponse2;
        this.reponse3 = reponse3;
        this.reponse4 = reponse4;
        this.bonneReponse = bonneReponse;
    }

    public String getNumQuest() {
        return numQuest;
    }

    public void setNumQuest(String numQuest) {
        this.numQuest = numQuest;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String getReponse1() {
        return reponse1;
    }

    public void setReponse1(String reponse1) {
        this.reponse1 = reponse1;
    }

    public String getReponse2() {
        return reponse2;
    }

    public void setReponse2(String reponse2) {
        this.reponse2 = reponse2;
    }

    public String getReponse3() {
        return reponse3;
    }

    public void setReponse3(String reponse3) {
        this.reponse3 = reponse3;
    }

    public String getReponse4() {
        return reponse4;
    }

    public void setReponse4(String reponse4) {
        this.reponse4 = reponse4;
    }

    public String getBonneReponse() {
        return bonneReponse;
    }

    public void setBonneReponse(String bonneReponse) {
        this.bonneReponse = bonneReponse;
    }
}
