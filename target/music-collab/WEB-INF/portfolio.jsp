<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
    <title>Musician Portfolio</title>
</head>
<body>
    <h1>Portfolio of ${portfolio.musician.username}</h1>
    <p><strong>Bio:</strong> ${portfolio.bio}</p>

    <h2>Update Bio</h2>
    <form action="portfolios" method="post">
        <input type="hidden" name="musicianId" value="${portfolio.musician.id}">
        <label for="bio">Bio:</label>
        <textarea id="bio" name="bio" required>${portfolio.bio}</textarea>
        <br>
        <button type="submit">Update Bio</button>
    </form>
</body>
</html>
