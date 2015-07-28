package Apartments;


import java.sql.*;

public class Main {

    private static final String DB_DRIVER = "com.mysql.jdbc.Driver";
    private static final String DB_CONNECTION = "jdbc:mysql://localhost:3306/DB_Apartments";
    private static final String DB_USER = "root";
    private static final String DB_PASSWORD = "root";

    private static final String CREATE_TABLE_SQL = "CREATE TABLE IF NOT EXISTS Apartments (\n" +
            "  `id` INT NOT NULL AUTO_INCREMENT PRIMARY KEY,\n" +
            "  `postcode` VARCHAR(100) NULL,\n" +
            "  `country` VARCHAR(45) NULL,\n" +
            "  `street` VARCHAR(45) NULL,\n" +
            "  `house_number` VARCHAR(45) NULL,\n" +
            "  `apartment_number` VARCHAR(45) NULL,\n" +
            "  `area` DECIMAL(10,2) NULL,\n" +
            "  `number_of_rooms` INT(3) NOT NULL,\n" +
            "  `price` DECIMAL(18,2) NULL)";

    private static Connection getDBConnection() {

        Connection dbConnection = null;

        try {
            Class.forName(DB_DRIVER);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            return null;
        }

        try {
            dbConnection = DriverManager.getConnection(DB_CONNECTION, DB_USER, DB_PASSWORD);
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }

        Statement st = null;
        try {
            st = dbConnection.createStatement();
            st.execute(CREATE_TABLE_SQL);
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        } finally {
            if (st != null) {
                try {
                    st.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }

        return dbConnection;
    }

    public static void main(String[] args) {

        Connection conn = getDBConnection();
        if (conn == null) {
            return;
        }

        // inserting data
        PreparedStatement ps = null;
        try {
             ps = conn.prepareStatement("INSERT INTO Apartments (postcode, country, street, house_number, apartment_number, area, number_of_rooms, price) VALUES(?, ?, ?, ?, ?, ?, ?, ?)");
            for (int i = 0; i < 10; i++) {
                ps.setString(1, "postcode" + i);
                ps.setString(2, "country" + i);
                ps.setString(3, "street" + i);
                ps.setString(4, "house_number" + i);
                ps.setString(5, "apartment_number" + i);
                ps.setDouble(6, i * 10);
                ps.setInt(7, i);
                ps.setFloat(8, i * 100);
                ps.executeUpdate();
            }

        } catch (SQLException e){
            e.printStackTrace();
        }        finally {
            if (ps != null) {
                try {
                    ps.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }

        try {
            ps = conn.prepareStatement("SELECT * FROM DB_Apartments.Apartments where area > 40 and street like '%8'");

            ResultSet rs = ps.executeQuery();
            ResultSetMetaData md = rs.getMetaData(); //

            for (int i = 1; i <= md.getColumnCount(); i++)
                System.out.print(md.getColumnName(i) + "\t\t");
            System.out.println();

            while (rs.next()) {
                for (int i = 1; i <= md.getColumnCount(); i++) {
                    System.out.print(rs.getString(i) + "\t\t");
                }
                System.out.println();
            }

        } catch (SQLException e){
            e.printStackTrace();
        }        finally {
            if (ps != null) {
                try {
                    ps.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }

        try {
            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
