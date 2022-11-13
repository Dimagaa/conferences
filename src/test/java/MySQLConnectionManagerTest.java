import com.conferences.dao.ConnectionManager;
import com.conferences.dao.impl.mysql.MySQLConnectionManager;
import com.conferences.exceptions.DaoException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.sql.DataSource;

import java.sql.Connection;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class MySQLConnectionManagerTest {
    private ConnectionManager connectionManager;
    private final DataSource dataSource = mock(DataSource.class);
    private final Connection connection = mock(Connection.class);

    @BeforeEach
    public void setUp() {
        connectionManager = MySQLConnectionManager.getInstance();
        connectionManager.setDataSource(dataSource);
    }

    @Test
    void getConnectionManagerNotNull() {
        assertNotNull(connectionManager);
    }

    @Test
    void testGetConnection() throws DaoException, SQLException {
        when(dataSource.getConnection()).thenReturn(connection);
        connectionManager.getConnection();
        verify(dataSource, times(1)).getConnection();
    }

    @Test
    void testGetConnectionWithException() throws SQLException {
        when(dataSource.getConnection()).thenThrow(SQLException.class);
        assertThrows(DaoException.class, () -> connectionManager.getConnection());
        verify(dataSource, times(1)).getConnection();

    }

    @Test
    void testGetTransaction() throws DaoException, SQLException {
        when(dataSource.getConnection()).thenReturn(connection);
        doNothing().when(connection).setAutoCommit(anyBoolean());
        connectionManager.getTransaction();
        verify(dataSource, times(1)).getConnection();
        verify(connection, times(1)).setAutoCommit(false);
    }

    @Test
    void testGetTransactionWithException() throws SQLException {
        when(dataSource.getConnection()).thenReturn(connection);
        doThrow(SQLException.class).when(connection).setAutoCommit(false);
        try {
            connectionManager.getTransaction();
        } catch (DaoException e) {
            verify(dataSource, times(1)).getConnection();
            verify(connection, times(1)).setAutoCommit(false);
        }
    }

    @Test
    void testRollback() throws DaoException, SQLException {
        doNothing().when(connection).rollback();
        connectionManager.rollback(connection);
        verify(connection, times(1)).rollback();
    }

    @Test
    void testRollbackWithException() throws SQLException {
        doThrow(SQLException.class).when(connection).rollback();
        assertThrows(DaoException.class, () -> connectionManager.rollback(connection));
        verify(connection, times(1)).rollback();
    }

    @Test
    void testCommit() throws DaoException, SQLException {
        doNothing().when(connection).commit();
        connectionManager.commit(connection);
        verify(connection, times(1)).commit();
    }

    @Test
    void testCommitWithException() throws SQLException {
        doThrow(SQLException.class).when(connection).commit();
        assertThrows(DaoException.class, () -> connectionManager.commit(connection));
        verify(connection, times(1)).commit();
    }

    @Test
    void testClose() throws DaoException, SQLException {
        doNothing().when(connection).close();
        connectionManager.close(connection);
        verify(connection, times(1)).close();
    }

    @Test
    void testCloseWithException() throws SQLException {
        doThrow(SQLException.class).when(connection).close();
        assertThrows(DaoException.class, () -> connectionManager.close(connection));
        verify(connection, times(1)).close();
    }
}
