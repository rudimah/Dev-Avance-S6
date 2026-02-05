<%--
  Created by IntelliJ IDEA.
  User: hrahman
  Date: 05/02/2026
  Time: 16:53
  To change this template use File | Settings | File Templates.
--%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html>
<body>
<h1>${empty annonce ? 'Créer une annonce' : 'Modifier l\'annonce'}</h1>

<form action="annonces" method="post">
    <input type="hidden" name="action" value="save">
    <input type="hidden" name="id" value="${annonce.id}">

    <label>Titre:</label>
    <input type="text" name="title" value="${annonce.title}" required><br>

    <label>Description:</label>
    <textarea name="description">${annonce.description}</textarea><br>

    <label>Catégorie:</label>
    <select name="categoryId">
        <c:forEach items="${categories}" var="cat">
            <option value="${cat.id}" ${cat.id == annonce.category.id ? 'selected' : ''}>
                    ${cat.label}
            </option>
        </c:forEach>
    </select><br>

    <label>Adresse:</label>
    <input type="text" name="address" value="${annonce.address}"><br>

    <label>Email Contact:</label>
    <input type="email" name="mail" value="${annonce.mail}"><br>

    <button type="submit">Enregistrer</button>
</form>
</body>
</html>