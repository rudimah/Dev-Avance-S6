package org.univ_paris8.iut.montreuil.qdev.tp2025.gr07.jeuquizz.dev_avance.servlet;

import org.univ_paris8.iut.montreuil.qdev.tp2025.gr07.jeuquizz.dev_avance.entity.User;
import org.univ_paris8.iut.montreuil.qdev.tp2025.gr07.jeuquizz.dev_avance.repository.UserRepository;
import org.univ_paris8.iut.montreuil.qdev.tp2025.gr07.jeuquizz.dev_avance.util.JPAUtil;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;
import javax.persistence.EntityManager;

@WebServlet("/login")
public class AuthServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        // Affiche le formulaire de login
        req.getRequestDispatcher("/WEB-INF/jsp/login.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String username = req.getParameter("username");
        String pass = req.getParameter("password");

        // Note: Idéalement, utilisez un UserService. Ici, on fait simple via le Repo directement pour l'exemple.
        EntityManager em = JPAUtil.getEntityManager();
        try {
            UserRepository userRepo = new UserRepository(em);
            User user = userRepo.findByUsername(username);

            // Vérification basique (En prod : Hachage de mot de passe obligatoire !)
            if (user != null && user.getPassword().equals(pass)) {
                HttpSession session = req.getSession();
                session.setAttribute("user", user); // On stocke l'utilisateur en session
                resp.sendRedirect(req.getContextPath() + "/annonces");
            } else {
                req.setAttribute("error", "Identifiants invalides");
                req.getRequestDispatcher("/WEB-INF/jsp/login.jsp").forward(req, resp);
            }
        } finally {
            em.close();
        }
    }
}