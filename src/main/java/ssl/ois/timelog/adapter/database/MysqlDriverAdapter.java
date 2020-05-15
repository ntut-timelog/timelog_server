package ssl.ois.timelog.adapter.database;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class MysqlDriverAdapter {
    private String host;
    private String database;
    private String user;
    private String password;

    public MysqlDriverAdapter(String host,
                              String database,
                              String user,
                              String password) {
        this.host = host;
        this.database = database;
        this.user = user;
        this.password = password;
    }

    public Connection getConnection() throws SQLException {
        final String url = this.host + "/" + this.database;
        return DriverManager.getConnection(url, this.user, this.password);
    }

    public void closeConnection(Connection connection) throws SQLException {
        connection.close();
    }
}
