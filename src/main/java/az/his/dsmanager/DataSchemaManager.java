package az.his.dsmanager;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DataSchemaManager {

    private static DataSource dataSource;
    private static int dBVersionId;

    public static void init(String jndiName) throws NamingException {
        InitialContext context = new InitialContext();
        dataSource = (DataSource) context.lookup(jndiName);
    }

    public static String getDBVersion() throws SQLException {
        Connection connection = dataSource.getConnection();
        PreparedStatement statement
                = connection.prepareStatement("SELECT value FROM sysParameters WHERE name = 'db.version'");
        ResultSet resultSet;
        try {
            resultSet = statement.executeQuery();
            if(!resultSet.first()) return "0";
            String ret = resultSet.getString(1);
            resultSet.close();
            statement.close();

            statement = connection.prepareStatement("SELECT value FROM sysParameters WHERE name = 'db.versionid'");

            resultSet = statement.executeQuery();
            if(!resultSet.first()) return "0";
            dBVersionId = Integer.parseInt(resultSet.getString(1));
            resultSet.close();
            statement.close();

            return ret;
        } catch (SQLException e) {
            dBVersionId = 0;
            return "0";
        }
    }

    public static int getDBVersionId() {
        return dBVersionId;
    }
}
