package org.univ_paris8.iut.montreuil.qdev.tp2025.gr07.jeuquizz.dev_avance.web;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@WebFilter("/*") // Intercepte tout
public class SecurityFilter implements Filter {

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse resp = (HttpServletResponse) response;
        HttpSession session = req.getSession(false);

        String path = req.getRequestURI().substring(req.getContextPath().length());

        // Pages publiques (Login, Ressources statiques css/js)
        boolean isPublic = path.startsWith("/login") || path.startsWith("/logout") || path.startsWith("/css");
        boolean isLoggedIn = (session != null && session.getAttribute("user") != null);

        if (isLoggedIn || isPublic) {
            chain.doFilter(request, response); // On laisse passer
        } else {
            resp.sendRedirect(req.getContextPath() + "/login"); // Redirection vers login
        }
    }
}