package Orders.DAO;

import java.util.List;

public interface EntityDAO<T> {
    public void insert(T e);

    public List<T> select();

    public List<T> select(String filter);

    public T getByID(long id);
}
