package Orders.DAO;

import Orders.Entity.Client;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;


public class ClientDAO implements EntityDAO<Client> {

    public void insert(Client e) {
        Connection conn = DBConnection.getDBConnection();

        if (conn == null) {
            return;
        }

        PreparedStatement ps = null;
        try {

            ps = conn.prepareStatement("SELECT * FROM DB_Orders.Clients where id = ?");
            ps.setLong(1, e.getId());
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                ps = conn.prepareStatement("UPDATE Clients set description = ? where id = ?");
                ps.setString(1, e.getDescription());
                ps.setLong(2, e.getId());
                ps.executeUpdate();
            } else {
                ps = conn.prepareStatement("INSERT INTO Clients(id, description) VALUES(?, ?)");
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

    public List<Client> select(String filter) {

        Connection conn = DBConnection.getDBConnection();

        PreparedStatement ps = null;

        List<Client> result = new ArrayList<Client>();

        try {
            String query;
            if ("".equals(filter)) {
                query = "SELECT id, description FROM Clients";
            } else {
                query = "SELECT id, description FROM Clients " + filter;
            }
            ps = conn.prepareStatement(query);

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                result.add(new Client(rs.getLong(1), rs.getString(2)));
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

    public List<Client> select() {
        return select("");
    }

    public Client getByID(long id) {
        List<Client> list = select("where id=" + id);
        if (list.size() > 0) {
            return list.get(0);
        }
        return null;
    }

}
