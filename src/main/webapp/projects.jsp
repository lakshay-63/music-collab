<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
    <title>Manage Projects</title>
</head>
<body>
    <h1>Projects</h1>
    <table border="1">
        <thead>
        <tr>
            <th>ID</th>
            <th>Name</th>
            <th>Description</th>
            <th>Created By</th>
        </tr>
        </thead>
        <tbody>
        <c:forEach var="project" items="${projects}">
            <tr>
                <td>${project.id}</td>
                <td>${project.name}</td>
                <td>${project.description}</td>
                <td>${project.createdBy.username}</td>
            </tr>
        </c:forEach>
        </tbody>
    </table>

    <h2>Add New Project</h2>
    <form action="projects" method="post">
        <label for="name">Name:</label>
        <input type="text" id="name" name="name" required>
        <br>
        <label for="description">Description:</label>
        <textarea id="description" name="description" required></textarea>
        <br>
        <label for="createdBy">Created By (User ID):</label>
        <input type="number" id="createdBy" name="createdBy" required>
        <br>
        <button type="submit">Add Project</button>
    </form>
</body>
</html>
