package az.his.dsmanager;

import org.apache.ibatis.jdbc.ScriptRunner;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class DataSchemaManager {

    private DataSource dataSource;
    private int dBVersionId;

    public DataSchemaManager(String jndiName) throws NamingException {
        InitialContext context = new InitialContext();
        dataSource = (DataSource) context.lookup(jndiName);
    }

    public String getDBVersion() throws SQLException {
        Connection connection = dataSource.getConnection();
        PreparedStatement statement
                = connection.prepareStatement("SELECT val FROM sysParameters WHERE name = 'db.version'");
        ResultSet resultSet;
        try {
            resultSet = statement.executeQuery();
            if(!resultSet.first()) return "0";
            String ret = resultSet.getString(1);
            resultSet.close();
            statement.close();

            statement = connection.prepareStatement("SELECT val FROM sysParameters WHERE name = 'db.versionid'");

            resultSet = statement.executeQuery();
            if(!resultSet.first()) return "0";
            dBVersionId = Integer.parseInt(resultSet.getString(1));
            resultSet.close();
            statement.close();

            return ret;
        } catch (SQLException e) {
            dBVersionId = 0;
            return "0";
        } finally {
            connection.close();
        }
    }

    public int getDBVersionId() {
        return dBVersionId;
    }


    public void upgrade(List<Version> versions) {
        Connection connection;
        try {
            connection = dataSource.getConnection();
            ScriptRunner runner = new ScriptRunner(connection);

            for (Version version : versions) {
                InputStream stream = getClass().getClassLoader().getResourceAsStream("db/" + version.getFileName());
                Reader reader = new InputStreamReader(stream);
                runner.runScript(reader);
                stream.close();
            }

            connection.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
