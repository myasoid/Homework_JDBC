package Orders.DAO;


import Orders.Entity.Client;
import Orders.Entity.Order;
import Orders.Entity.Product;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class OrderDAO implements EntityDAO<Order> {

    public void insert(Order e) {
        Connection conn = DBConnection.getDBConnection();

        if (conn == null) {
            return;
        }

        PreparedStatement ps = null;
        try {

            ps = conn.prepareStatement("SELECT * FROM DB_Orders.Orders where id = ?");
            ps.setLong(1, e.getId());
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                ps = conn.prepareStatement("UPDATE Orders set date = ?, client_id = ?, product_id = ?, quantity = ?, amount = ?  where id = ?");
                ps.setDate(1, new java.sql.Date(e.getDate().getTime()));
                ps.setLong(2, e.getClient().getId());
                ps.setLong(3, e.getProduct().getId());
                ps.setInt(4, e.getQuantity());
                ps.setDouble(5, e.getAmount());
                ps.executeUpdate();
            } else {
                ps = conn.prepareStatement("INSERT INTO Orders(id, date, client_id, product_id, quantity, amount) VALUES(?, ?, ?, ?, ?, ?)");
                ps.setLong(1, e.getId());
                ps.setDate(2, new java.sql.Date(e.getDate().getTime()));
                ps.setLong(3, e.getClient().getId());
                ps.setLong(4, e.getProduct().getId());
                ps.setInt(5, e.getQuantity());
                ps.setDouble(6, e.getAmount());
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

    public List<Order> select(String filter) {

        Connection conn = DBConnection.getDBConnection();

        PreparedStatement ps = null;

        List<Order> result = new ArrayList<Order>();

        try {
            String query;
            if ("".equals(filter)) {
                query = "SELECT id, date, client_id, product_id, quantity, amount FROM Orders";
            } else {
                query = "SELECT id, date, client_id, product_id, quantity, amount FROM Orders " + filter;
            }
            ps = conn.prepareStatement(query);

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                Client client = new ClientDAO().getByID(rs.getLong(3));
                Product product = new ProductDAO().getByID(rs.getLong(4));
                result.add(new Order(rs.getLong(1), rs.getDate(2), client, product, rs.getInt(5), rs.getDouble(6)));
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

    public List<Order> select() {
        return select("");
    }

    public Order getByID(long id) {
        List<Order> list = select("where id=" + id);
        if (list.size() > 0) {
            return list.get(0);
        }
        return null;
    }

}
