<%--
  Created by IntelliJ IDEA.
  User: hrahman
  Date: 05/02/2026
  Time: 16:53
  To change this template use File | Settings | File Templates.
--%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head><title>Liste des Annonces</title></head>
<body>
<h1>Les Annonces</h1>
<a href="annonces?action=create">Nouvelle Annonce</a>

<form action="annonces" method="get">
  <input type="text" name="search" placeholder="Recherche...">
  <button type="submit">Filtrer</button>
</form>

<table border="1">
  <tr>
    <th>Titre</th>
    <th>Catégorie</th>
    <th>Statut</th>
    <th>Auteur</th>
    <th>Actions</th>
  </tr>
  <c:forEach items="${annonces}" var="a">
    <tr>
      <td>${a.title}</td>
      <td>${a.category.label}</td>
      <td>${a.status}</td>
      <td>${a.author.username}</td>
      <td>
        <a href="annonces?action=detail&id=${a.id}">Voir</a>
        <a href="annonces?action=edit&id=${a.id}">Editer</a>
        <c:if test="${a.status == 'DRAFT'}">
          <a href="annonces?action=publish&id=${a.id}">Publier</a>
        </c:if>
        <a href="annonces?action=delete&id=${a.id}" onclick="return confirm('Sûr ?')">Supprimer</a>
      </td>
    </tr>
  </c:forEach>
</table>

<div>
  <a href="annonces?page=${currentPage - 1}">Précédent</a>
  Page ${currentPage}
  <a href="annonces?page=${currentPage + 1}">Suivant</a>
</div>
</body>
</html>