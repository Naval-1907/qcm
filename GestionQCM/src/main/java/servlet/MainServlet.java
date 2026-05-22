package servlet;

import dao.DataStore;
import dao.AdminDAO;
import dao.EtudiantDAO;
import dao.ExamenDAO;
import dao.QcmDAO;
import model.Etudiant;
import model.Examen;
import model.Qcm;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Servlet principale qui contrôle les actions de l'application Gestion des Questionnaires.
 * Elle utilise des DAO JDBC pour accéder à la base de données PostgreSQL.
 */
@WebServlet("/main")
public class MainServlet extends HttpServlet {
    private final EtudiantDAO etudiantDAO = new EtudiantDAO();
    private final QcmDAO qcmDAO = new QcmDAO();
    private final ExamenDAO examenDAO = new ExamenDAO();
    private final AdminDAO adminDAO = new AdminDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("action");
        if (action == null) {
            action = "dashboard";
        }

        switch (action) {
            case "login":
                // Affiche la page de connexion (GET)
                request.getRequestDispatcher("login.jsp").forward(request, response);
                break;
            case "gestion":
                afficherGestion(request, response);
                break;
            case "editEtudiant":
                preparerEditionEtudiant(request, response);
                break;
            case "deleteEtudiant":
                supprimerEtudiant(request, response);
                break;
            case "editQcm":
                preparerEditionQcm(request, response);
                break;
            case "deleteQcm":
                supprimerQcm(request, response);
                break;
            case "niveau":
                afficherListeParNiveau(request, response);
                break;
            case "classement":
                afficherClassement(request, response);
                break;
            case "examen":
                afficherExamen(request, response);
                break;
            default:
                afficherDashboard(request, response);
                break;
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("action");
        if (action == null) {
            action = "dashboard";
        }

        switch (action) {
            case "login":
                // Traitement de la soumission du formulaire de connexion
                traiterLogin(request, response);
                break;
            case "logout":
                // Déconnexion via POST
                traiterLogout(request, response);
                break;
            case "saveEtudiant":
                enregistrerEtudiant(request, response);
                break;
            case "saveQcm":
                enregistrerQcm(request, response);
                break;
            case "deleteEtudiant":
                supprimerEtudiant(request, response);
                break;
            case "deleteQcm":
                supprimerQcm(request, response);
                break;
            case "startExam":
                demarrerExamen(request, response);
                break;
            case "answerQuestion":
                traiterReponseQuestion(request, response);
                break;
            default:
                afficherDashboard(request, response);
                break;
        }
    }

    private void afficherDashboard(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setAttribute("totalEtudiants", etudiantDAO.getTotalEtudiants());
        request.setAttribute("totalQcm", qcmDAO.getTotalQcms());
        request.setAttribute("moyenneExam", examenDAO.getAverageExamNote());
        request.getRequestDispatcher("dashboard.jsp").forward(request, response);
    }

    private void afficherGestion(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String recherche = request.getParameter("searchQuery");
        request.setAttribute("etudiants", etudiantDAO.findEtudiants(recherche));
        request.setAttribute("qcms", qcmDAO.getAllQcms());
        request.setAttribute("searchQuery", recherche);
        request.setAttribute("niveaux", etudiantDAO.getAllNiveaux());
        request.getRequestDispatcher("gestionEtudiantsQcm.jsp").forward(request, response);
    }

    private void preparerEditionEtudiant(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String num = request.getParameter("num_etudiant");
        Etudiant etudiant = etudiantDAO.getEtudiantByNumero(num);
        if (etudiant != null) {
            request.setAttribute("editEtudiant", etudiant);
        }
        afficherGestion(request, response);
    }

    private void supprimerEtudiant(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String num = request.getParameter("num_etudiant");
        // Vérifier si l'étudiant possède des examens enregistrés
        int exams = examenDAO.countExamsByStudent(num);
        if (exams > 0) {
            // Bloquer la suppression et informer l'utilisateur
            request.setAttribute("errorMessage", "Impossible de supprimer cet étudiant car il possède un historique d'examens.");
            afficherGestion(request, response);
            return;
        }
        etudiantDAO.deleteEtudiant(num);
        request.setAttribute("successMessage", "Étudiant supprimé avec succès.");
        afficherGestion(request, response);
    }

    private void preparerEditionQcm(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String num = request.getParameter("num_quest");
        Qcm qcm = qcmDAO.getQcmByNumero(num);
        if (qcm != null) {
            request.setAttribute("editQcm", qcm);
        }
        afficherGestion(request, response);
    }

    private void supprimerQcm(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String num = request.getParameter("num_quest");
        qcmDAO.deleteQcm(num);
        request.setAttribute("successMessage", "QCM supprimé avec succès.");
        afficherGestion(request, response);
    }

    private void afficherListeParNiveau(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String niveau = request.getParameter("niveau");
        if (niveau == null || niveau.isEmpty()) {
            niveau = "L1";
        }
        List<Etudiant> etudiants = etudiantDAO.filterEtudiantsParNiveau(niveau);
        request.setAttribute("selectedLevel", niveau);
        request.setAttribute("effectif", etudiants.size());
        request.setAttribute("filteredEtudiants", etudiants);
        request.setAttribute("niveaux", etudiantDAO.getAllNiveaux());
        request.getRequestDispatcher("listeParNiveau.jsp").forward(request, response);
    }

    private void afficherClassement(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        List<Map<String, Object>> classement = examenDAO.getClassementParMerite();
        request.setAttribute("classementList", classement);
        request.getRequestDispatcher("classement.jsp").forward(request, response);
    }

    private void afficherExamen(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        String examStep = (String) session.getAttribute("examStep");
        if (examStep == null) {
            examStep = "start";
        }

        request.setAttribute("examStep", examStep);
        request.setAttribute("examMessage", session.getAttribute("examMessage"));
        request.setAttribute("examResult", session.getAttribute("examResult"));

        if ("question".equals(examStep)) {
            List<Qcm> questions = (List<Qcm>) session.getAttribute("currentExamQuestions");
            Integer currentIndex = (Integer) session.getAttribute("currentExamIndex");
            if (questions != null && currentIndex != null && currentIndex < questions.size()) {
                request.setAttribute("currentQuestion", questions.get(currentIndex));
                request.setAttribute("questionNumber", currentIndex + 1);
                request.setAttribute("totalQuestions", questions.size());
            } else {
                session.setAttribute("examStep", "start");
                request.setAttribute("examStep", "start");
            }
        }

        request.getRequestDispatcher("examen.jsp").forward(request, response);
    }

    private void enregistrerEtudiant(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Etudiant etudiant = new Etudiant();
        etudiant.setNumEtudiant(request.getParameter("num_etudiant"));
        etudiant.setNom(request.getParameter("nom"));
        etudiant.setPrenoms(request.getParameter("prenoms"));
        etudiant.setNiveau(request.getParameter("niveau"));
        etudiant.setAdrEmail(request.getParameter("adr_email"));

        // Déterminer si l'opération est une création ou une mise à jour
        String actionType = request.getParameter("actionType"); // 'create' ou 'update'
        String originalNum = request.getParameter("originalNumEtudiant");
        if ("update".equalsIgnoreCase(actionType)) {
            // Mise à jour : utiliser la méthode dédiée pour gérer un changement éventuel de clé primaire
            // (originalNum contient l'identifiant avant modification)
            etudiantDAO.updateEtudiant(originalNum, etudiant);
            request.setAttribute("successMessage", "Étudiant mis à jour avec succès.");
        } else {
            // Création : insert ou on-duplicate
            etudiantDAO.saveOrUpdateEtudiant(etudiant);
            request.setAttribute("successMessage", "Étudiant enregistré avec succès.");
        }
        afficherGestion(request, response);
    }

    private void enregistrerQcm(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Qcm qcm = new Qcm();
        qcm.setNumQuest(request.getParameter("num_quest"));
        qcm.setQuestion(request.getParameter("question"));
        qcm.setReponse1(request.getParameter("reponse1"));
        qcm.setReponse2(request.getParameter("reponse2"));
        qcm.setReponse3(request.getParameter("reponse3"));
        qcm.setReponse4(request.getParameter("reponse4"));
        qcm.setBonneReponse(request.getParameter("bonne_reponse"));

        // Détecter création vs mise à jour
        String actionType = request.getParameter("actionType"); // 'create' ou 'update'
        String originalNumQuest = request.getParameter("originalNumQuest");
        if ("update".equalsIgnoreCase(actionType)) {
            // Appel de la méthode d'update dédiée pour gérer le cas où le numéro de question aurait changé
            qcmDAO.updateQcm(originalNumQuest, qcm);
            request.setAttribute("successMessage", "QCM mis à jour avec succès.");
        } else {
            qcmDAO.saveOrUpdateQcm(qcm);
            request.setAttribute("successMessage", "QCM enregistré avec succès.");
        }
        afficherGestion(request, response);
    }

    private void demarrerExamen(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String numEtudiant = request.getParameter("num_etudiant");
        String anneeUniv = request.getParameter("annee_univ");
        HttpSession session = request.getSession();

        Etudiant etudiant = etudiantDAO.getEtudiantByNumero(numEtudiant);
        if (etudiant == null || anneeUniv == null || anneeUniv.trim().isEmpty()) {
            request.setAttribute("examStep", "start");
            request.setAttribute("examMessage", "Veuillez renseigner un numéro d'étudiant valide et une année universitaire.");
            request.getRequestDispatcher("examen.jsp").forward(request, response);
            return;
        }

        // Sélectionner les questions en protégeant le cas où la banque est insuffisante
        List<Qcm> questions;
        try {
            questions = qcmDAO.selectRandomQuestions(10);
        } catch (IllegalStateException ex) {
            // Banque insuffisante : informer l'utilisateur et afficher la page de démarrage
            request.setAttribute("examStep", "start");
            request.setAttribute("examMessage", "Impossible de démarrer l'examen : " + ex.getMessage());
            request.getRequestDispatcher("examen.jsp").forward(request, response);
            return;
        }

        session.setAttribute("currentExamQuestions", questions);
        session.setAttribute("currentExamIndex", 0);
        session.setAttribute("currentExamAnswers", new ArrayList<String>());
        session.setAttribute("currentExamStudent", etudiant);
        session.setAttribute("currentExamYear", anneeUniv);
        session.setAttribute("examStep", "question");

        request.setAttribute("examStep", "question");
        request.setAttribute("currentQuestion", questions.get(0));
        request.setAttribute("questionNumber", 1);
        request.setAttribute("totalQuestions", questions.size());
        request.getRequestDispatcher("examen.jsp").forward(request, response);
    }

    /**
     * Handle login POST. Exemple simple utilisant des identifiants codés en dur.
     * En production : valider via base de données, hasher les mots de passe,
     * appliquer un mécanisme de verrouillage, et utiliser HTTPS.
     */
    private void traiterLogin(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String username = request.getParameter("username");
        String password = request.getParameter("password");

        // Vérification en base via AdminDAO (PreparedStatement pour éviter les injections SQL)
        boolean ok = false;
        try {
            ok = adminDAO.verifierIdentifiants(username, password);
        } catch (RuntimeException e) {
            // Log l'erreur en production et afficher un message générique
            request.setAttribute("loginError", "Erreur lors de la vérification des identifiants.");
            request.getRequestDispatcher("login.jsp").forward(request, response);
            return;
        }

        if (ok) {
            // Authentification réussie : création de session
            request.getSession(true).setAttribute("isAdmin", Boolean.TRUE);
            request.getSession().setAttribute("username", username);
            request.getSession().setMaxInactiveInterval(30 * 60); // 30 minutes
            response.sendRedirect(request.getContextPath() + "/main?action=gestion");
        } else {
            // Échec d'authentification
            request.setAttribute("loginError", "Identifiants invalides.");
            request.getRequestDispatcher("login.jsp").forward(request, response);
        }
    }

    /**
     * Déconnexion : invalide la session et renvoie vers la page de login.
     */
    private void traiterLogout(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        if (request.getSession(false) != null) {
            request.getSession(false).invalidate();
        }
        // Forward to login with a parameter indicating logout
        response.sendRedirect(request.getContextPath() + "/login.jsp?logout=1");
    }

    private void traiterReponseQuestion(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        List<Qcm> questions = (List<Qcm>) session.getAttribute("currentExamQuestions");
        Integer currentIndex = (Integer) session.getAttribute("currentExamIndex");
        List<String> answers = (List<String>) session.getAttribute("currentExamAnswers");
        Etudiant etudiant = (Etudiant) session.getAttribute("currentExamStudent");
        String anneeUniv = (String) session.getAttribute("currentExamYear");

        if (questions == null || currentIndex == null || answers == null || etudiant == null || anneeUniv == null) {
            session.setAttribute("examStep", "start");
            request.setAttribute("examStep", "start");
            request.setAttribute("examMessage", "La session de l'examen a expiré ou est invalide.");
            request.getRequestDispatcher("examen.jsp").forward(request, response);
            return;
        }

        String selectedAnswer = request.getParameter("selectedAnswer");
        if (selectedAnswer == null) {
            request.setAttribute("examStep", "question");
            request.setAttribute("currentQuestion", questions.get(currentIndex));
            request.setAttribute("questionNumber", currentIndex + 1);
            request.setAttribute("totalQuestions", questions.size());
            request.setAttribute("examMessage", "Veuillez choisir une réponse avant de continuer.");
            request.getRequestDispatcher("examen.jsp").forward(request, response);
            return;
        }

        answers.add(selectedAnswer);
        currentIndex++;
        session.setAttribute("currentExamIndex", currentIndex);
        session.setAttribute("currentExamAnswers", answers);

        if (currentIndex >= questions.size()) {
            double note = calculerNote(questions, answers);
            String numExam = examenDAO.generateNextExamId();
            Examen examen = new Examen(numExam, etudiant.getNumEtudiant(), anneeUniv, note);
            examenDAO.registerExamen(examen);

            String message = String.format("Bravo %s %s ! Votre note finale est %.1f/10.", etudiant.getPrenoms(), etudiant.getNom(), note);
            session.setAttribute("examStep", "finished");
            session.setAttribute("examResult", message);
            session.setAttribute("examMessage", "Le résultat a été enregistré et un email de confirmation a été simulé.");
            DataStore.sendEmailSimulation(etudiant.getAdrEmail(), message + " Année universitaire : " + anneeUniv);

            request.setAttribute("examStep", "finished");
            request.setAttribute("examResult", message);
            request.setAttribute("examMessage", "Le résultat a été enregistré et un email de confirmation a été simulé.");
            request.getRequestDispatcher("examen.jsp").forward(request, response);
            return;
        }

        request.setAttribute("examStep", "question");
        request.setAttribute("currentQuestion", questions.get(currentIndex));
        request.setAttribute("questionNumber", currentIndex + 1);
        request.setAttribute("totalQuestions", questions.size());
        request.getRequestDispatcher("examen.jsp").forward(request, response);
    }

    private double calculerNote(List<Qcm> questions, List<String> answers) {
        int bons = 0;
        for (int i = 0; i < questions.size() && i < answers.size(); i++) {
            if (questions.get(i).getBonneReponse().equals(answers.get(i))) {
                bons++;
            }
        }
        double note = bons * 10.0 / questions.size();
        return Math.round(note * 10.0) / 10.0;
    }
}
