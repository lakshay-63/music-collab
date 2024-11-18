package com.musiccollab.dao;

import com.musiccollab.models.Portfolio;
import com.musiccollab.connection.DBConnection;

import java.sql.*;

public class PortfolioDAO {
    public void addPortfolio(Portfolio portfolio) throws SQLException {
        String query = "INSERT INTO portfolios (musician_id, bio) VALUES (?, ?)";
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setLong(1, portfolio.getMusician().getId());
            ps.setString(2, portfolio.getBio());
            ps.executeUpdate();
        }
    }

    public Portfolio getPortfolioByMusicianId(Long musicianId) throws SQLException {
        String query = "SELECT * FROM portfolios WHERE musician_id = ?";
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setLong(1, musicianId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                Portfolio portfolio = new Portfolio();
                portfolio.setId(rs.getLong("id"));
                portfolio.setBio(rs.getString("bio"));
                return portfolio;
            }
        }
        return null;
    }
}
