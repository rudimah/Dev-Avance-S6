package tp_avancee_dev.tp_avancee.repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import tp_avancee_dev.tp_avancee.model.Annonce;
import tp_avancee_dev.tp_avancee.model.Status;

import java.util.List;

public class AnnonceRepository {

    public Annonce findById(EntityManager em, Long id) {
        return em.find(Annonce.class, id);
    }

    public Annonce findByIdWithRelations(EntityManager em, Long id) {
        List<Annonce> annonces = em.createQuery(
                        "SELECT a FROM Annonce a " +
                                "LEFT JOIN FETCH a.category " +
                                "LEFT JOIN FETCH a.author " +
                                "WHERE a.id = :id",
                        Annonce.class
                )
                .setParameter("id", id)
                .setMaxResults(1)
                .getResultList();
        return annonces.isEmpty() ? null : annonces.get(0);
    }

    public List<Annonce> findAllPaginated(EntityManager em, int page, int size) {
        int safePage = Math.max(page, 1);
        int safeSize = Math.max(size, 1);
        int offset = (safePage - 1) * safeSize;

        TypedQuery<Annonce> query = em.createQuery(
                "SELECT a FROM Annonce a ORDER BY a.createdAt DESC", // Correction ici
                Annonce.class
        );
        query.setFirstResult(offset);
        query.setMaxResults(safeSize);
        return query.getResultList();
    }

    public void create(EntityManager em, Annonce annonce) {
        em.persist(annonce);
    }

    public Annonce update(EntityManager em, Annonce annonce) {
        return em.merge(annonce);
    }

    public void delete(EntityManager em, Long id) {
        Annonce annonce = em.find(Annonce.class, id);
        if (annonce != null) {
            em.remove(annonce);
        }
    }

    public List<Annonce> searchByKeywordPaginated(EntityManager em, String keyword, int page, int size) {
        int safePage = Math.max(page, 1);
        int safeSize = Math.max(size, 1);
        int offset = (safePage - 1) * safeSize;
        String searchTerm = keyword == null ? "" : keyword.trim().toLowerCase();

        TypedQuery<Annonce> query = em.createQuery(
                "SELECT a FROM Annonce a " +
                        "WHERE LOWER(a.title) LIKE :term " +
                        "OR LOWER(a.description) LIKE :term " +
                        "OR LOWER(a.adress) LIKE :term " +
                        "ORDER BY a.createdAt DESC", // Correction ici
                Annonce.class
        );

        query.setParameter("term", "%" + searchTerm + "%");
        query.setFirstResult(offset);
        query.setMaxResults(safeSize);
        return query.getResultList();
    }
}