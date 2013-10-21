package az.his.dsmanager;

import org.apache.ibatis.jdbc.ScriptRunner;
import org.apache.log4j.Logger;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;
import java.io.IOException;
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
    private static final Logger log = Logger.getLogger(DataSchemaManager.class);

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
            if (!resultSet.first()) return "0";
            String ret = resultSet.getString(1);
            resultSet.close();
            statement.close();

            statement = connection.prepareStatement("SELECT val FROM sysParameters WHERE name = 'db.versionid'");

            resultSet = statement.executeQuery();
            if (!resultSet.first()) return "0";
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
        try (Connection connection = dataSource.getConnection()){
            connection.setAutoCommit(false);
            ScriptRunner runner = new ScriptRunner(connection);

            for (Version version : versions) {
                log.info("Sending script " + version.getFileName() + " for version " + version.getName() + " ("
                        + version.getId() + ")");

                sendScript(runner, version.getFileName());
                updateVersionId(connection, version);

                connection.commit();
            }
        } catch (Exception e) {
            log.error("Upgrade error", e);
        }
    }

    private void sendScript(ScriptRunner runner, String fileName) throws IOException {
        InputStream stream = getClass().getClassLoader().getResourceAsStream("db/" + fileName);
        Reader reader = new InputStreamReader(stream);
        runner.runScript(reader);

        stream.close();
    }

    private void updateVersionId(Connection connection, Version version) throws SQLException {
        PreparedStatement stmt = connection
                .prepareStatement("UPDATE sysParameters SET val = ? WHERE name= 'db.versionid'");
        stmt.setString(1, Integer.toString(version.getId()));
        stmt.execute();

        stmt = connection
                .prepareStatement("UPDATE sysParameters SET val = ? WHERE name= 'db.version'");
        stmt.setString(1, version.getName());
        stmt.execute();
    }

    public void installDump(Version version) {
        try (Connection connection = dataSource.getConnection()){
            connection.setAutoCommit(false);
            ScriptRunner runner = new ScriptRunner(connection);

            log.info("Sending dump " + version.getDumpName() + " for version " + version.getName() + " ("
                    + version.getId() + ")");

            sendScript(runner, version.getDumpName());
            updateVersionId(connection, version);

            connection.commit();
        } catch (Exception e) {
            log.error("Upgrade error", e);
        }
    }
}
