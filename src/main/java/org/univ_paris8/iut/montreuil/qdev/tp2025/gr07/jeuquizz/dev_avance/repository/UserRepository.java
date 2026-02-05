package org.univ_paris8.iut.montreuil.qdev.tp2025.gr07.jeuquizz.dev_avance.repository;

import org.univ_paris8.iut.montreuil.qdev.tp2025.gr07.jeuquizz.dev_avance.entity.User;
import javax.persistence.EntityManager;

public class UserRepository extends AbstractRepository<User> {
    public UserRepository(EntityManager em) {
        super(em, User.class);
    }

    // Méthode spécifique utile pour le login
    public User findByUsername(String username) {
        try {
            return em.createQuery("SELECT u FROM User u WHERE u.username = :username", User.class)
                    .setParameter("username", username)
                    .getSingleResult();
        } catch (Exception e) {
            return null;
        }
    }
}