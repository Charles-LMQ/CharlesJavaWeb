package cool.charles.framework.helper;

import cool.charles.framework.util.CollectionUtil;
import cool.charles.framework.util.PropsUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanHandler;
import org.apache.commons.dbutils.handlers.BeanListHandler;
import org.apache.commons.dbutils.handlers.MapListHandler;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
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

    public static <T> T queryEntity(Class<T> entityClass, String sql, Object... params) {
        T entity;
        try{
            Connection connection = getConnection();
            entity = QUERY_RUNNER.query(connection, sql, new BeanHandler<T>(entityClass), params);

        } catch (SQLException e) {
            log.error("Query entity failure, {}", e);
            throw new RuntimeException(e);
        } finally {
            closeConnection();
        }

        return entity;
    }

    public static List<Map<String, Object>> executeQuery(String sql, Object... params) {
        List<Map<String,Object>> result;
        try {
            Connection connection = getConnection();
            result = QUERY_RUNNER.query(connection, sql, new MapListHandler(),params);
        } catch (Exception e) {
            log.error("Execute query failure, {}", e);
            throw new RuntimeException(e);
        }
        return result;
    }

    public static int executeUpdate(String sql, Object... params) {
        int rows = 0;
        try {
            Connection connection = getConnection();
            rows = QUERY_RUNNER.update(connection, sql, params);
        } catch (SQLException e) {
            log.error("Execute update failure {}", e);
            throw new RuntimeException(e);
        } finally {
            closeConnection();
        }
        return rows;
    }

    public static <T> boolean insertEntity(Class<T> entityClass, Map<String, Object> fieldMap) {
        if(CollectionUtil.isEmpty(fieldMap)) {
            log.error("Cannot insert entity: fieldMap is empty");
            return false;
        }

        String sql = "INSERT TO " + getTableName(entityClass);
        StringBuilder columns = new StringBuilder("(");
        StringBuilder values = new StringBuilder(")");

        for(String fieldName : fieldMap.keySet()) {
            columns.append(fieldName).append(", ");
            values.append("?, ");
        }

        columns.replace(columns.lastIndexOf(", "), columns.length(),")");
        values.replace(columns.lastIndexOf(", "), values.length(),")");

        sql += columns + " VALUES " + values;

        Object[] params = fieldMap.values().toArray();

        return executeUpdate(sql, params) == 1;
    }

    public static <T> boolean updateEntity(Class<T> entityClass, long id, Map<String, Object> fieldMap) {
        if(CollectionUtil.isEmpty(fieldMap)) {
            log.error("Cannot update entity: field map cannot be empty");
            return false;
        }

        String sql = "UPDATE " + getTableName(entityClass) + " SET ";
        StringBuilder columns = new StringBuilder();

        for (String fieldName : fieldMap.keySet()) {
            columns.append(fieldName).append("=?, ");
        }

        sql += columns.substring(0, columns.lastIndexOf(", ")) + " WHERE id=?";
        List<Object> paramList = new ArrayList<Object>();
        paramList.addAll(fieldMap.values());
        paramList.add(id);
        Object[] params = paramList.toArray();

        return executeUpdate(sql, params) == 1;

    }

    public static <T> boolean deleteEntity(Class<T> entityClass, long id) {
        String sql = "DELETE FROM " + getTableName(entityClass) + "WHERE id=?";
        return executeUpdate(sql, id) == 1;
    }

    private static String getTableName(Class<?> entityClass) {
        return entityClass.getSimpleName();
    }


    public static void beginTransaction() {
        Connection connection = getConnection();
        if(connection != null) {
            try{
                connection.setAutoCommit(false);
            } catch (SQLException e){
                log.error("Begin transaction failure, {}", e);
                throw new RuntimeException(e);
            }finally {
                CONNECTION_HOLDER.set(connection);
            }
        }
    }


    public static void commitTransaction() {
        Connection connection = getConnection();
        if(connection != null) {
            try {
                connection.commit();
                connection.close();
            } catch (SQLException e) {
                log.error("Commit transaction failure, {}", e);
                throw new RuntimeException(e);
            } finally {
                CONNECTION_HOLDER.remove();
            }
        }
    }

    public static void rollbackTransaction() {
        Connection connection = getConnection();
        if(connection != null) {
            try {
                connection.rollback();
                connection.close();
            } catch (SQLException e) {
                log.error("Rollback transaction failure, {}", e);
                throw new RuntimeException(e);
            } finally {
                CONNECTION_HOLDER.remove();
            }
        }
    }
}
