package cool.charles.framework.helper;

import cool.charles.framework.util.PropsUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanListHandler;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.List;
import java.util.Properties;

@Slf4j
public final class DatabaseHelper {

    private static final String DRIVER;
    private static final String URL;
    private static final String USERNAME;
    private static final String PASSWORD;

    private static final QueryRunner QUERY_RUNNER = new QueryRunner();

    static {
        Properties config = PropsUtil.loadProps("config.properties");
        DRIVER = config.getProperty("jdbc.driver");
        URL = config.getProperty("jdbc.url");
        USERNAME = config.getProperty("jdbc.username");
        PASSWORD = config.getProperty("jdbc.password");

        try{
            Class.forName(DRIVER);
        }catch (ClassNotFoundException e) {
            log.error("Load jdbc drive failure, {}",e);
        }
    }

    private static final ThreadLocal<Connection> CONNECTION_HOLDER = new ThreadLocal<Connection>();

    public static Connection getConnection() {
        Connection connection = CONNECTION_HOLDER.get();
        if(connection == null) {
            try{
                connection = DriverManager.getConnection(URL, USERNAME,PASSWORD);
            }catch (SQLException e) {
                log.error("Get connection failure, {}", e);
            } finally {
                CONNECTION_HOLDER.set(connection);
            }
        }

        return connection;
    }

    public static void closeConnection() {
        Connection connection = CONNECTION_HOLDER.get();
        if(connection !=null) {
            try{
                connection.close();
            } catch(SQLException e) {
                log.error("Close connection failure, {}", e);
                throw new RuntimeException(e);
            } finally {
                CONNECTION_HOLDER.remove();
            }
        }
    }

    public static  <T> List<T> queryEntityList(Class<T> entityClass,  String sql, Object... params) {
        List<T> entityList;
        try {
            Connection connection = getConnection();
            entityList = QUERY_RUNNER.query(connection,sql, new BeanListHandler<T>(entityClass), params);
        } catch(SQLException e) {
            log.error("Query entity list failure", e);
            throw new RuntimeException(e);
        } finally {
            closeConnection();
        }

        return entityList;
    }


    public static void beginTransaction() {
        Connection connection = getC
    }
}
