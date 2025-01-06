package com.musiccollab.dao;

import com.musiccollab.connection.DBConnection;
import com.musiccollab.models.Musician;
import com.musiccollab.models.Portfolio;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;

import java.sql.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class PortfolioDAOTest {

    private PortfolioDAO portfolioDAO;
    private Connection mockConnection;
    private PreparedStatement mockPreparedStatement;
    private ResultSet mockResultSet;

    @BeforeEach
    void setUp() {
        portfolioDAO = new PortfolioDAO();
        mockConnection = mock(Connection.class);
        mockPreparedStatement = mock(PreparedStatement.class);
        mockResultSet = mock(ResultSet.class);
    }

    @Test
    void testAddPortfolio() throws Exception {
        Portfolio portfolio = new Portfolio();
        Musician musician = new Musician();
        musician.setId(1L);

        portfolio.setMusician(musician);
        portfolio.setBio("This is a test bio");

        try (MockedStatic<DBConnection> mockedDBConnection = mockStatic(DBConnection.class)) {
            mockedDBConnection.when(DBConnection::getConnection).thenReturn(mockConnection);
            when(mockConnection.prepareStatement(anyString())).thenReturn(mockPreparedStatement);

            portfolioDAO.addPortfolio(portfolio);

            verify(mockPreparedStatement, times(1)).setLong(1, 1L);
            verify(mockPreparedStatement, times(1)).setString(2, "This is a test bio");
            verify(mockPreparedStatement, times(1)).executeUpdate();
        }
    }

    @Test
    void testGetPortfolioByMusicianId() throws Exception {
        Long musicianId = 1L;

        try (MockedStatic<DBConnection> mockedDBConnection = mockStatic(DBConnection.class)) {
            mockedDBConnection.when(DBConnection::getConnection).thenReturn(mockConnection);
            when(mockConnection.prepareStatement(anyString())).thenReturn(mockPreparedStatement);
            when(mockPreparedStatement.executeQuery()).thenReturn(mockResultSet);

            // Mock the result set
            when(mockResultSet.next()).thenReturn(true); // Single row
            when(mockResultSet.getLong("id")).thenReturn(1L);
            when(mockResultSet.getString("bio")).thenReturn("This is a test bio");

            Portfolio portfolio = portfolioDAO.getPortfolioByMusicianId(musicianId);

            assertNotNull(portfolio);
            assertEquals(1L, portfolio.getId());
            assertEquals("This is a test bio", portfolio.getBio());

            verify(mockPreparedStatement, times(1)).setLong(1, musicianId);
            verify(mockPreparedStatement, times(1)).executeQuery();
        }
    }

    @Test
    void testGetPortfolioByMusicianId_NoResult() throws Exception {
        Long musicianId = 2L;

        try (MockedStatic<DBConnection> mockedDBConnection = mockStatic(DBConnection.class)) {
            mockedDBConnection.when(DBConnection::getConnection).thenReturn(mockConnection);
            when(mockConnection.prepareStatement(anyString())).thenReturn(mockPreparedStatement);
            when(mockPreparedStatement.executeQuery()).thenReturn(mockResultSet);

            // Mock the result set with no results
            when(mockResultSet.next()).thenReturn(false);

            Portfolio portfolio = portfolioDAO.getPortfolioByMusicianId(musicianId);

            assertNull(portfolio);

            verify(mockPreparedStatement, times(1)).setLong(1, musicianId);
            verify(mockPreparedStatement, times(1)).executeQuery();
        }
    }
}
