package com.musiccollab.dao;

import com.musiccollab.models.Project;
import com.musiccollab.models.User;
import com.musiccollab.connection.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ProjectDAO {
    public void addProject(Project project) throws SQLException {
        String query = "INSERT INTO projects (name, description, created_by) VALUES (?, ?, ?)";
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setString(1, project.getName());
            ps.setString(2, project.getDescription());
            ps.setLong(3, project.getCreatedBy().getId());
            ps.executeUpdate();
        }
    }

    public Project getProjectById(Long id) throws SQLException {
        String query = "SELECT * FROM projects WHERE id = ?";
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setLong(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return mapToProject(rs);
            }
        }
        return null;
    }

    public List<Project> getAllProjects() throws SQLException {
        String query = "SELECT * FROM projects";
        List<Project> projects = new ArrayList<>();
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement ps = connection.prepareStatement(query);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                projects.add(mapToProject(rs));
            }
        }
        return projects;
    }

    private Project mapToProject(ResultSet rs) throws SQLException {
        Project project = new Project();
        project.setId(rs.getLong("id"));
        project.setName(rs.getString("name"));
        project.setDescription(rs.getString("description"));
        User createdBy = new User();
        createdBy.setId(rs.getLong("created_by"));
        project.setCreatedBy(createdBy);
        return project;
    }
}
