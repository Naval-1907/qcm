package servlet;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * Filtre d'authentification qui protège les routes d'administration.
 *
 * Cycle de vie :
 * - init(FilterConfig) : appelé une fois au déploiement pour initialiser le filtre.
 * - doFilter(ServletRequest, ServletResponse, FilterChain) : exécuté pour chaque requête matching.
 * - destroy() : appelé lors de l'arrêt du filtre.
 *
 * Sécurité des sessions :
 * - Le filtre vérifie la session HTTP pour un attribut `isAdmin`.
 * - Les sessions doivent être invalidées lors de la déconnexion pour éviter l'usurpation.
 */
@WebFilter(urlPatterns = {"/main"})
public class AuthenticationFilter implements Filter {

    // Actions qui nécessitent l'authentification admin
    private final Set<String> protectedActions = new HashSet<>(Arrays.asList("gestion", "classement", "niveau"));

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        // Initialisation si nécessaire (chargement de paramètres, logger, etc.)
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse res = (HttpServletResponse) response;

        String action = req.getParameter("action");

        // Allow if action is not protected (public pages), or if performing login/logout
        if (action == null || !protectedActions.contains(action) || "login".equals(action) || "logout".equals(action)) {
            chain.doFilter(request, response);
            return;
        }

        HttpSession session = req.getSession(false);
        boolean isAdmin = false;
        if (session != null) {
            Object attr = session.getAttribute("isAdmin");
            if (attr instanceof Boolean) {
                isAdmin = (Boolean) attr;
            }
        }

        if (isAdmin) {
            // L'utilisateur est authentifié, on poursuit
            chain.doFilter(request, response);
        } else {
            // Non authentifié : redirection vers la page de connexion
            // On peut stocker l'URL cible en session pour rediriger après login si souhaité
            res.sendRedirect(req.getContextPath() + "/login.jsp");
        }
    }

    @Override
    public void destroy() {
        // Cleanup si nécessaire
    }
}
