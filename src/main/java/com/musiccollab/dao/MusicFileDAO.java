package com.musiccollab.dao;

import com.musiccollab.models.MusicFile;
import com.musiccollab.connection.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MusicFileDAO {
    public void addMusicFile(MusicFile musicFile) throws SQLException {
        String query = "INSERT INTO music_files (file_name, file_type, file_size, uploaded_by, project_id) VALUES (?, ?, ?, ?, ?)";
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setString(1, musicFile.getFileName());
            ps.setString(2, musicFile.getFileType());
            ps.setLong(3, musicFile.getFileSize());
            ps.setLong(4, musicFile.getUploadedBy().getId());
            ps.setObject(5, musicFile.getProject() != null ? musicFile.getProject().getId() : null, Types.BIGINT);
            ps.executeUpdate();
        }
    }

    public List<MusicFile> getFilesByProjectId(Long projectId) throws SQLException {
        String query = "SELECT * FROM music_files WHERE project_id = ?";
        List<MusicFile> files = new ArrayList<>();
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setLong(1, projectId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                files.add(mapToMusicFile(rs));
            }
        }
        return files;
    }

    private MusicFile mapToMusicFile(ResultSet rs) throws SQLException {
        MusicFile musicFile = new MusicFile();
        musicFile.setId(rs.getLong("id"));
        musicFile.setFileName(rs.getString("file_name"));
        musicFile.setFileType(rs.getString("file_type"));
        musicFile.setFileSize(rs.getLong("file_size"));
        musicFile.setUploadDate(rs.getTimestamp("upload_date"));
        return musicFile;
    }
}
