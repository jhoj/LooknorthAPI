package fo.looknorth.looknorthapi.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Class <code>MariaDBDAOFactory</code> uses the Factory Method design pattern
 * to create a connection to a specific MariaDB database.
 *
 * NB Add mariadbclient.jar to project
 * 
 * @author jakuphoj
 * @version 2.0
 */
public class MariadbDAOFactory extends DAOFactory {

    private static final String URL = "jdbc:mariadb://localhost:3306/looknorth";
    private static final String USERNAME = "root";
    private static final String PASSWORD = "1qaz2wsx";

    /**
     * Factory method to return a connection to a specific Mariadb database.
     * @return the Mariadb database connection
     * @throws SQLException
     */
    public static Connection createConnection() throws SQLException {
        return DriverManager.getConnection(URL, USERNAME, PASSWORD);
    }

    @Override
    public LooknorthDAO getLooknorthDAO() {
        return new MariadbLooknorthDAO();
    }
}
