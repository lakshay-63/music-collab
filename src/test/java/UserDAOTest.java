package com.musiccollab.dao;

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

class UserDAOTest {

    private UserDAO userDAO;
    private Connection mockConnection;
    private PreparedStatement mockPreparedStatement;
    private ResultSet mockResultSet;

    @BeforeEach
    void setUp() throws SQLException {
        userDAO = new UserDAO();
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
    void testAddUser_Success() throws SQLException {
        User user = new User();
        user.setUsername("testuser");
        user.setEmail("testuser@example.com");
        user.setPassword("password123");
        user.setRole("musician");

        when(mockConnection.prepareStatement(anyString())).thenReturn(mockPreparedStatement);

        userDAO.addUser(user);

        verify(mockPreparedStatement, times(1)).setString(1, "testuser");
        verify(mockPreparedStatement, times(1)).setString(2, "testuser@example.com");
        verify(mockPreparedStatement, times(1)).setString(3, "password123");
        verify(mockPreparedStatement, times(1)).setString(4, "musician");
        verify(mockPreparedStatement, times(1)).executeUpdate();
    }

    @Test
    void testGetUserById_Found() throws SQLException {
        when(mockConnection.prepareStatement(anyString())).thenReturn(mockPreparedStatement);
        when(mockPreparedStatement.executeQuery()).thenReturn(mockResultSet);

        when(mockResultSet.next()).thenReturn(true);
        when(mockResultSet.getLong("id")).thenReturn(1L);
        when(mockResultSet.getString("username")).thenReturn("testuser");
        when(mockResultSet.getString("email")).thenReturn("testuser@example.com");
        when(mockResultSet.getString("password")).thenReturn("password123");
        when(mockResultSet.getString("role")).thenReturn("musician");

        User user = userDAO.getUserById(1L);

        assertNotNull(user);
        assertEquals(1L, user.getId());
        assertEquals("testuser", user.getUsername());
        assertEquals("testuser@example.com", user.getEmail());
        assertEquals("password123", user.getPassword());
        assertEquals("musician", user.getRole());
    }

    @Test
    void testGetUserById_NotFound() throws SQLException {
        when(mockConnection.prepareStatement(anyString())).thenReturn(mockPreparedStatement);
        when(mockPreparedStatement.executeQuery()).thenReturn(mockResultSet);

        when(mockResultSet.next()).thenReturn(false);

        User user = userDAO.getUserById(1L);

        assertNull(user);
    }

    @Test
    void testGetAllUsers_Success() throws SQLException {
        when(mockConnection.prepareStatement(anyString())).thenReturn(mockPreparedStatement);
        when(mockPreparedStatement.executeQuery()).thenReturn(mockResultSet);

        when(mockResultSet.next()).thenReturn(true, true, false);
        when(mockResultSet.getLong("id")).thenReturn(1L, 2L);
        when(mockResultSet.getString("username")).thenReturn("user1", "user2");
        when(mockResultSet.getString("email")).thenReturn("user1@example.com", "user2@example.com");
        when(mockResultSet.getString("password")).thenReturn("password1", "password2");
        when(mockResultSet.getString("role")).thenReturn("musician", "producer");

        List<User> users = userDAO.getAllUsers();

        assertNotNull(users);
        assertEquals(2, users.size());

        assertEquals(1L, users.get(0).getId());
        assertEquals("user1", users.get(0).getUsername());
        assertEquals("user1@example.com", users.get(0).getEmail());
        assertEquals("password1", users.get(0).getPassword());
        assertEquals("musician", users.get(0).getRole());

        assertEquals(2L, users.get(1).getId());
        assertEquals("user2", users.get(1).getUsername());
        assertEquals("user2@example.com", users.get(1).getEmail());
        assertEquals("password2", users.get(1).getPassword());
        assertEquals("producer", users.get(1).getRole());
    }

    @Test
    void testGetAllUsers_Empty() throws SQLException {
        when(mockConnection.prepareStatement(anyString())).thenReturn(mockPreparedStatement);
        when(mockPreparedStatement.executeQuery()).thenReturn(mockResultSet);

        when(mockResultSet.next()).thenReturn(false);

        List<User> users = userDAO.getAllUsers();

        assertNotNull(users);
        assertTrue(users.isEmpty());
    }
}
