package Dao;

import java.sql.SQLException;

public interface Dao<T> {

    void loadDbObjects() throws SQLException;
    void addObject(T t) throws SQLException;
    void modifyObject(T t) throws SQLException;
    void removeObject(T t) throws SQLException;

}
