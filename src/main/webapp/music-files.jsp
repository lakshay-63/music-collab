<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
    <title>Manage Music Files</title>
</head>
<body>
    <h1>Music Files</h1>
    <table border="1">
        <thead>
        <tr>
            <th>ID</th>
            <th>File Name</th>
            <th>File Type</th>
            <th>File Size</th>
            <th>Uploaded By</th>
        </tr>
        </thead>
        <tbody>
        <c:forEach var="file" items="${musicFiles}">
            <tr>
                <td>${file.id}</td>
                <td>${file.fileName}</td>
                <td>${file.fileType}</td>
                <td>${file.fileSize} bytes</td>
                <td>${file.uploadedBy.username}</td>
            </tr>
        </c:forEach>
        </tbody>
    </table>

    <h2>Upload New Music File</h2>
    <form action="music-files" method="post" enctype="multipart/form-data">
        <label for="file">Choose File:</label>
        <input type="file" id="file" name="file" required>
        <br>
        <label for="uploadedBy">Uploaded By (User ID):</label>
        <input type="number" id="uploadedBy" name="uploadedBy" required>
        <br>
        <button type="submit">Upload File</button>
    </form>
</body>
</html>
