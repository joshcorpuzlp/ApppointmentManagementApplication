package Dao;

public interface Dao<T> {

    void loadDbObjects();
    void addObject(T t);
    void modifyObject(T t);
    void removeObject(T t);

}
