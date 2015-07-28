package Orders.DAO;

import Orders.Entity.Product;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;


public class ProductDAO implements EntityDAO<Product> {

    public void insert(Product e) {
        Connection conn = DBConnection.getDBConnection();

        if (conn == null) {
            return;
        }

        PreparedStatement ps = null;
        try {

            ps = conn.prepareStatement("SELECT * FROM DB_Orders.Products where id = ?");
            ps.setLong(1, e.getId());
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                ps = conn.prepareStatement("UPDATE Products set description = ? where id = ?");
                ps.setString(1, e.getDescription());
                ps.setLong(2, e.getId());
                ps.executeUpdate();
            } else {
                ps = conn.prepareStatement("INSERT INTO Products(id, description) VALUES(?, ?)");
                ps.setLong(1, e.getId());
                ps.setString(2, e.getDescription());
                ps.executeUpdate();
            }

        } catch (SQLException ex) {
            ex.printStackTrace();
        } finally {
            if (ps != null) {
                try {
                    ps.close();
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
        }
    }

    public List<Product> select(String filter) {

        Connection conn = DBConnection.getDBConnection();

        PreparedStatement ps = null;

        List<Product> result = new ArrayList<Product>();

        try {
            String query;
            if ("".equals(filter)) {
                query = "SELECT id, description FROM Products";
            } else {
                query = "SELECT id, description FROM Products " + filter;
            }
            ps = conn.prepareStatement(query);

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                result.add(new Product(rs.getLong(1), rs.getString(2)));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (ps != null) {
                try {
                    ps.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
        return result;
    }

    public List<Product> select() {
        return select("");
    }

    public Product getByID(long id) {
        List<Product> list = select("where id=" + id);
        if (list.size() > 0) {
            return list.get(0);
        }
        return null;
    }

}
