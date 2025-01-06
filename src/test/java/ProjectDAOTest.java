package com.musiccollab.dao;

import com.musiccollab.models.Project;
import com.musiccollab.models.User;
import com.musiccollab.connection.DBConnection;
import org.junit.jupiter.api.*;
import org.mockito.Mockito;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ProjectDAOTest {

    private ProjectDAO projectDAO;
    private Connection mockConnection;
    private PreparedStatement mockPreparedStatement;
    private ResultSet mockResultSet;

    @BeforeEach
    void setUp() throws SQLException {
        projectDAO = new ProjectDAO();
        mockConnection = mock(Connection.class);
        mockPreparedStatement = mock(PreparedStatement.class);
        mockResultSet = mock(ResultSet.class);

        DBConnection.setMockConnection(mockConnection);
    }

    @AfterEach
    void tearDown() {
        DBConnection.clearMockConnection();
    }

    @Test
    void testAddProject_Success() throws SQLException {
        User createdBy = new User();
        createdBy.setId(1L);

        Project project = new Project();
        project.setName("Project A");
        project.setDescription("Description A");
        project.setCreatedBy(createdBy);

        when(mockConnection.prepareStatement(anyString())).thenReturn(mockPreparedStatement);

        projectDAO.addProject(project);

        verify(mockPreparedStatement, times(1)).setString(1, "Project A");
        verify(mockPreparedStatement, times(1)).setString(2, "Description A");
        verify(mockPreparedStatement, times(1)).setLong(3, 1L);
        verify(mockPreparedStatement, times(1)).executeUpdate();
    }

    @Test
    void testGetProjectById_Found() throws SQLException {
        when(mockConnection.prepareStatement(anyString())).thenReturn(mockPreparedStatement);
        when(mockPreparedStatement.executeQuery()).thenReturn(mockResultSet);

        when(mockResultSet.next()).thenReturn(true);
        when(mockResultSet.getLong("id")).thenReturn(1L);
        when(mockResultSet.getString("name")).thenReturn("Project A");
        when(mockResultSet.getString("description")).thenReturn("Description A");
        when(mockResultSet.getLong("created_by")).thenReturn(2L);

        Project project = projectDAO.getProjectById(1L);

        assertNotNull(project);
        assertEquals(1L, project.getId());
        assertEquals("Project A", project.getName());
        assertEquals("Description A", project.getDescription());
        assertEquals(2L, project.getCreatedBy().getId());
    }

    @Test
    void testGetProjectById_NotFound() throws SQLException {
        when(mockConnection.prepareStatement(anyString())).thenReturn(mockPreparedStatement);
        when(mockPreparedStatement.executeQuery()).thenReturn(mockResultSet);

        when(mockResultSet.next()).thenReturn(false);

        Project project = projectDAO.getProjectById(1L);

        assertNull(project);
    }

    @Test
    void testGetAllProjects_Success() throws SQLException {
        when(mockConnection.prepareStatement(anyString())).thenReturn(mockPreparedStatement);
        when(mockPreparedStatement.executeQuery()).thenReturn(mockResultSet);

        when(mockResultSet.next()).thenReturn(true, true, false);
        when(mockResultSet.getLong("id")).thenReturn(1L, 2L);
        when(mockResultSet.getString("name")).thenReturn("Project A", "Project B");
        when(mockResultSet.getString("description")).thenReturn("Description A", "Description B");
        when(mockResultSet.getLong("created_by")).thenReturn(2L, 3L);

        List<Project> projects = projectDAO.getAllProjects();

        assertNotNull(projects);
        assertEquals(2, projects.size());

        assertEquals(1L, projects.get(0).getId());
        assertEquals("Project A", projects.get(0).getName());
        assertEquals("Description A", projects.get(0).getDescription());
        assertEquals(2L, projects.get(0).getCreatedBy().getId());

        assertEquals(2L, projects.get(1).getId());
        assertEquals("Project B", projects.get(1).getName());
        assertEquals("Description B", projects.get(1).getDescription());
        assertEquals(3L, projects.get(1).getCreatedBy().getId());
    }

    @Test
    void testGetAllProjects_Empty() throws SQLException {
        when(mockConnection.prepareStatement(anyString())).thenReturn(mockPreparedStatement);
        when(mockPreparedStatement.executeQuery()).thenReturn(mockResultSet);

        when(mockResultSet.next()).thenReturn(false);

        List<Project> projects = projectDAO.getAllProjects();

        assertNotNull(projects);
        assertTrue(projects.isEmpty());
    }
}
