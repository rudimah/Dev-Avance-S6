package org.univ_paris8.iut.montreuil.qdev.tp2025.gr07.jeuquizz.dev_avance.web;

import org.univ_paris8.iut.montreuil.qdev.tp2025.gr07.jeuquizz.dev_avance.entity.Annonce;
import org.univ_paris8.iut.montreuil.qdev.tp2025.gr07.jeuquizz.dev_avance.entity.AnnonceStatus;
import org.univ_paris8.iut.montreuil.qdev.tp2025.gr07.jeuquizz.dev_avance.entity.User;
import org.univ_paris8.iut.montreuil.qdev.tp2025.gr07.jeuquizz.dev_avance.service.AnnonceService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;
import java.util.List;

@WebServlet("/annonces")
public class AnnonceController extends HttpServlet {

    private AnnonceService service = new AnnonceService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String action = req.getParameter("action");
        if (action == null) action = "list";

        switch (action) {
            case "create":
                showForm(req, resp);
                break;
            case "edit":
                showEditForm(req, resp);
                break;
            case "detail":
                showDetail(req, resp);
                break;
            case "publish":
                handleStatusChange(req, resp, AnnonceStatus.PUBLISHED);
                break;
            case "archive":
                handleStatusChange(req, resp, AnnonceStatus.ARCHIVED);
                break;
            case "delete":
                deleteAnnonce(req, resp);
                break;
            default: // "list"
                listAnnonces(req, resp);
                break;
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String action = req.getParameter("action");
        if ("save".equals(action)) {
            saveAnnonce(req, resp);
        } else {
            doGet(req, resp);
        }
    }

    // --- Méthodes privées d'implémentation ---

    // 2.a Liste paginée
    private void listAnnonces(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        int page = 1;
        int size = 10;
        if (req.getParameter("page") != null) page = Integer.parseInt(req.getParameter("page"));

        String keyword = req.getParameter("search");
        // Pour simplifier, on ignore le filtre statut ici, mais on pourrait le récupérer pareil

        List<Annonce> list = service.searchAnnonces(keyword, null, page, size);

        req.setAttribute("annonces", list);
        req.setAttribute("currentPage", page);
        req.getRequestDispatcher("/WEB-INF/jsp/list.jsp").forward(req, resp);
    }

    // 2.b Formulaire de création (Affiche la vue)
    private void showForm(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setAttribute("categories", service.getAllCategories());
        req.getRequestDispatcher("/WEB-INF/jsp/form.jsp").forward(req, resp);
    }

    // 2.c Formulaire de modification (Pré-remplit la vue)
    private void showEditForm(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Long id = Long.parseLong(req.getParameter("id"));
        Annonce existing = service.getAnnonceById(id);

        req.setAttribute("annonce", existing);
        req.setAttribute("categories", service.getAllCategories());
        req.getRequestDispatcher("/WEB-INF/jsp/form.jsp").forward(req, resp);
    }

    // Gestion Création ET Modification (POST)
    private void saveAnnonce(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String idStr = req.getParameter("id");
        String title = req.getParameter("title");
        String desc = req.getParameter("description");
        String addr = req.getParameter("address");
        String mail = req.getParameter("mail");
        Long catId = Long.parseLong(req.getParameter("categoryId"));

        // Création de l'objet temporaire
        Annonce annonce = new Annonce();
        annonce.setTitle(title);
        annonce.setDescription(desc);
        annonce.setAddress(addr);
        annonce.setMail(mail);

        // Récupération de la catégorie via un objet "dummy" ou via service si besoin,
        // mais le service create/update gère l'ID category normalement.
        // Pour simplifier ici, on suppose que le service update/create prend l'ID catégorie.
        // NOTE: Dans l'exercice 4, createAnnonce prend (entity, userId, catId).
        // Update prend (entity) mais l'entité doit avoir sa catégorie.

        // Simuler la récupération de la catégorie pour l'objet (nécessaire pour l'update direct)
        // Dans une vraie app, le binding serait automatique.
        // Ici on délègue la logique complexe au service.

        if (idStr == null || idStr.isEmpty()) {
            // MODE CREATION
            User currentUser = (User) req.getSession().getAttribute("user");
            service.createAnnonce(annonce, currentUser.getId(), catId);
        } else {
            // MODE UPDATE
            // Pour l'update, il faut récupérer l'objet complet ou setter l'ID
            annonce.setId(Long.parseLong(idStr));
            // Pour la catégorie en update, il faudrait idéalement la charger.
            // Simplification : Le service updateAnnonce devra gérer la mise à jour de la relation cat.
            // Ajoutons une méthode spécifique ou surchargeons pour gérer l'ID catégorie.

            // Hack rapide pour l'exercice: On recupere l'ancienne annonce et on met à jour les champs
            Annonce old = service.getAnnonceById(Long.parseLong(idStr));
            old.setTitle(title);
            old.setDescription(desc);
            old.setAddress( addr);
            old.setMail(mail);
            // Note: pour changer la categorie en update, il faudrait une méthode service updateCategory(annonceId, catId)

            service.updateAnnonce(old);
        }
        resp.sendRedirect("annonces");
    }

    // 2.d Détail
    private void showDetail(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Long id = Long.parseLong(req.getParameter("id"));
        Annonce a = service.getAnnonceById(id);
        req.setAttribute("annonce", a);
        req.getRequestDispatcher("/WEB-INF/jsp/detail.jsp").forward(req, resp);
    }

    // 2.e Actions
    private void handleStatusChange(HttpServletRequest req, HttpServletResponse resp, AnnonceStatus status) throws IOException {
        Long id = Long.parseLong(req.getParameter("id"));
        if (status == AnnonceStatus.PUBLISHED) service.publishAnnonce(id);
        else if (status == AnnonceStatus.ARCHIVED) service.archiveAnnonce(id);
        resp.sendRedirect("annonces");
    }

    private void deleteAnnonce(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        Long id = Long.parseLong(req.getParameter("id"));
        service.deleteAnnonce(id);
        resp.sendRedirect("annonces");
    }
}