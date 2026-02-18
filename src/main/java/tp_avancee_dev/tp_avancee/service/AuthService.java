package tp_avancee_dev.tp_avancee.service;

import tp_avancee_dev.tp_avancee.db.EntityManagerUtil;
import tp_avancee_dev.tp_avancee.model.User;
import tp_avancee_dev.tp_avancee.repository.UserRepository;
import jakarta.persistence.EntityManager;
import jakarta.ws.rs.NotAuthorizedException;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class AuthService {

    private final UserRepository userRepository;

    // Stockage mémoire des tokens : Token -> UserId
    private static final Map<String, Long> tokenStore = new HashMap<>();

    public AuthService() {
        this.userRepository = new UserRepository();
    }

    /**
     * Méthode pour l'API REST : Vérifie les credentials et retourne un TOKEN.
     */
    public String login(String username, String password) {
        // 1. On vérifie les infos et on récupère le user
        User user = authenticate(username, password);

        if (user == null) {
            throw new NotAuthorizedException("Invalid credentials");
        }

        String token = UUID.randomUUID().toString();

        tokenStore.put(token, user.getId());

        return token;
    }

    /**
     * Méthode interne et pour les Servlets/JSP : Vérifie les credentials et retourne un USER.
     */
    public User authenticate(String username, String password) {
        EntityManager em = EntityManagerUtil.createEntityManager();
        try {
            User user = userRepository.findByUsername(em, username);

            if (user != null && user.getPassword().equals(password)) {
                return user;
            }
            return null;
        } finally {
            em.close();
        }
    }

    public Long getUserIdFromToken(String token) {
        return tokenStore.get(token);
    }
}