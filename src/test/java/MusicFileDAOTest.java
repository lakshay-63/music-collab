package com.musiccollab.dao;

import com.musiccollab.connection.DBConnection;
import com.musiccollab.models.MusicFile;
import com.musiccollab.models.User;
import com.musiccollab.models.Project;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;

import java.sql.*;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

class MusicFileDAOTest {

    private MusicFileDAO musicFileDAO;
    private Connection mockConnection;
    private PreparedStatement mockPreparedStatement;
    private ResultSet mockResultSet;

    @BeforeEach
    void setUp() {
        musicFileDAO = new MusicFileDAO();
        mockConnection = mock(Connection.class);
        mockPreparedStatement = mock(PreparedStatement.class);
        mockResultSet = mock(ResultSet.class);
    }

    @Test
    void testAddMusicFile() throws Exception {
        MusicFile musicFile = new MusicFile();
        User user = new User();
        user.setId(1L);

        Project project = new Project();
        project.setId(2L);

        musicFile.setFileName("test.mp3");
        musicFile.setFileType("audio/mp3");
        musicFile.setFileSize(12345L);
        musicFile.setUploadedBy(user);
        musicFile.setProject(project);

        try (MockedStatic<DBConnection> mockedDBConnection = mockStatic(DBConnection.class)) {
            mockedDBConnection.when(DBConnection::getConnection).thenReturn(mockConnection);
            when(mockConnection.prepareStatement(anyString())).thenReturn(mockPreparedStatement);

            musicFileDAO.addMusicFile(musicFile);

            verify(mockPreparedStatement, times(1)).setString(1, "test.mp3");
            verify(mockPreparedStatement, times(1)).setString(2, "audio/mp3");
            verify(mockPreparedStatement, times(1)).setLong(3, 12345L);
            verify(mockPreparedStatement, times(1)).setLong(4, 1L);
            verify(mockPreparedStatement, times(1)).setLong(5, 2L);
            verify(mockPreparedStatement, times(1)).executeUpdate();
        }
    }

    @Test
    void testGetFilesByProjectId() throws Exception {
        Long projectId = 2L;

        try (MockedStatic<DBConnection> mockedDBConnection = mockStatic(DBConnection.class)) {
            mockedDBConnection.when(DBConnection::getConnection).thenReturn(mockConnection);
            when(mockConnection.prepareStatement(anyString())).thenReturn(mockPreparedStatement);
            when(mockPreparedStatement.executeQuery()).thenReturn(mockResultSet);

            // Mock the result set
            when(mockResultSet.next()).thenReturn(true, true, false); // Two rows
            when(mockResultSet.getLong("id")).thenReturn(1L, 2L);
            when(mockResultSet.getString("file_name")).thenReturn("file1.mp3", "file2.mp3");
            when(mockResultSet.getString("file_type")).thenReturn("audio/mp3", "audio/wav");
            when(mockResultSet.getLong("file_size")).thenReturn(12345L, 54321L);
            when(mockResultSet.getTimestamp("upload_date")).thenReturn(
                    new Timestamp(System.currentTimeMillis()),
                    new Timestamp(System.currentTimeMillis()));

            List<MusicFile> files = musicFileDAO.getFilesByProjectId(projectId);

            assertNotNull(files);
            assertEquals(2, files.size());

            MusicFile file1 = files.get(0);
            assertEquals(1L, file1.getId());
            assertEquals("file1.mp3", file1.getFileName());
            assertEquals("audio/mp3", file1.getFileType());
            assertEquals(12345L, file1.getFileSize());

            MusicFile file2 = files.get(1);
            assertEquals(2L, file2.getId());
            assertEquals("file2.mp3", file2.getFileName());
            assertEquals("audio/wav", file2.getFileType());
            assertEquals(54321L, file2.getFileSize());

            verify(mockPreparedStatement, times(1)).setLong(1, projectId);
            verify(mockPreparedStatement, times(1)).executeQuery();
        }
    }
}
