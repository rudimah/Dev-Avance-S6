package org.univ_paris8.iut.montreuil.qdev.tp2025.gr07.jeuquizz.dev_avance.repository;

import org.univ_paris8.iut.montreuil.qdev.tp2025.gr07.jeuquizz.dev_avance.entity.Annonce;
import org.univ_paris8.iut.montreuil.qdev.tp2025.gr07.jeuquizz.dev_avance.entity.AnnonceStatus;
import org.univ_paris8.iut.montreuil.qdev.tp2025.gr07.jeuquizz.dev_avance.entity.Category;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import java.util.List;

public class AnnonceRepository extends AbstractRepository<Annonce> {

    public AnnonceRepository(EntityManager em) {
        super(em, Annonce.class);
    }

    /**
     * 2.b Recherche par mot-clé (Titre ou Description)
     */
    public List<Annonce> findByKeyword(String keyword) {
        String jpql = "SELECT a FROM Annonce a WHERE LOWER(a.title) LIKE :kw OR LOWER(a.description) LIKE :kw";
        TypedQuery<Annonce> query = em.createQuery(jpql, Annonce.class);
        // On ajoute les % pour le LIKE SQL
        query.setParameter("kw", "%" + keyword.toLowerCase() + "%");
        return query.getResultList();
    }

    /**
     * 2.c Filtrage par catégorie et statut
     */
    public List<Annonce> findByCategoryAndStatus(Category category, AnnonceStatus status) {
        String jpql = "SELECT a FROM Annonce a WHERE a.category = :cat AND a.status = :stat";
        TypedQuery<Annonce> query = em.createQuery(jpql, Annonce.class);
        query.setParameter("cat", category);
        query.setParameter("stat", status);
        return query.getResultList();
    }

    /**
     * 2.d Pagination des résultats (Tous les résultats paginés)
     * @param page Numéro de la page (commence à 1)
     * @param size Nombre d'éléments par page
     */
    public List<Annonce> findAllPaginated(int page, int size) {
        String jpql = "SELECT a FROM Annonce a ORDER BY a.date DESC"; // Tri par date décroissante
        TypedQuery<Annonce> query = em.createQuery(jpql, Annonce.class);

        // Calcul de l'offset (position de départ)
        int firstResult = (page - 1) * size;

        query.setFirstResult(firstResult); // OFFSET
        query.setMaxResults(size);         // LIMIT

        return query.getResultList();
    }

    /**
     * Méthode combinée (Bonus) : Recherche + Pagination + Filtre
     * Exemple d'utilisation de JPQL dynamique
     */
    public List<Annonce> search(String keyword, AnnonceStatus status, int page, int size) {
        StringBuilder jpql = new StringBuilder("SELECT a FROM Annonce a WHERE 1=1");

        if (keyword != null && !keyword.isEmpty()) {
            jpql.append(" AND (LOWER(a.title) LIKE :kw OR LOWER(a.description) LIKE :kw)");
        }
        if (status != null) {
            jpql.append(" AND a.status = :stat");
        }
        jpql.append(" ORDER BY a.date DESC");

        TypedQuery<Annonce> query = em.createQuery(jpql.toString(), Annonce.class);

        if (keyword != null && !keyword.isEmpty()) {
            query.setParameter("kw", "%" + keyword.toLowerCase() + "%");
        }
        if (status != null) {
            query.setParameter("stat", status);
        }

        query.setFirstResult((page - 1) * size);
        query.setMaxResults(size);

        return query.getResultList();
    }
}