<%--
  Created by IntelliJ IDEA.
  User: hrahman
  Date: 29/01/2026
  Time: 16:28
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html>
<head>
  <title>Liste des Annonces</title>
  <style>
    table { border-collapse: collapse; width: 100%; margin-top: 20px; }
    th, td { border: 1px solid #ddd; padding: 12px; text-align: left; }
    th { background-color: #f2f2f2; }
    .btn-edit { color: blue; margin-right: 10px; }
    .btn-delete { color: red; }
  </style>
</head>
<body>

<div>
  <a href="AnnonceAdd"> Ajouter annonce</a>
</div>
<hr>

<c:choose>
  <c:when test="${not empty annonces}">
    <table>
      <thead>
      <tr>
        <th>Titre</th>
        <th>Adresse</th>
        <th>Email</th>
        <th>Date de publication</th>
        <th>Actions</th>
      </tr>
      </thead>
      <tbody>
      <c:forEach items="${annonces}" var="a">
        <tr>
          <td><strong>${a.title}</strong></td>
          <td>${a.adress}</td>
          <td>${a.mail}</td>
          <td>${a.date}</td>
          <td>
              <%-- Liens vers les servlets Update et Delete avec l'ID en paramètre --%>
            <a href="AnnonceUpdate?id=${a.id}" class="btn-edit">Modifier</a>
            <a href="AnnonceDelete?id=${a.id}" class="btn-delete"
               onclick="return confirm('Voulez-vous vraiment supprimer cette annonce ?');">
              Supprimer
            </a>
          </td>
        </tr>
      </c:forEach>
      </tbody>
    </table>
  </c:when>
  <c:otherwise>
    <p>Aucune annonce</p>
  </c:otherwise>
</c:choose>

</body>
</html>