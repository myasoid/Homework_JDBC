package Orders.DAO;


import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public final class DBConnection {
    private static final String DB_DRIVER = "com.mysql.jdbc.Driver";
    private static final String DB_CONNECTION = "jdbc:mysql://localhost:3306/DB_Orders";
    private static final String DB_USER = "root";
    private static final String DB_PASSWORD = "root";


    private static final String CREATE_TABLE_SQL_Products = "\n" +
            "CREATE TABLE IF NOT EXISTS Products (\n" +
            "            `id` INT NOT NULL AUTO_INCREMENT PRIMARY KEY,\n" +
            "            `description` VARCHAR(100) NULL);";

    private static final String CREATE_TABLE_SQL_Clients = "\n" +
            "CREATE TABLE IF NOT EXISTS Clients (\n" +
            "             `id` INT NOT NULL AUTO_INCREMENT PRIMARY KEY,\n" +
            "             `description` VARCHAR(100) NULL);";

    private static final String CREATE_TABLE_SQL_Orders = "\n" +
            "CREATE TABLE IF NOT EXISTS Orders (\n" +
            "  `id` INT NOT NULL,\n" +
            "  `date` DATETIME NULL,\n" +
            "  `client_id` INT NULL,\n" +
            "  `product_id` INT NULL,\n" +
            "  `quantity` INT NULL,\n" +
            "  `amount` DECIMAL(18,2) NULL,\n" +
            "  PRIMARY KEY (`id`),\n" +
            "  INDEX `fk_Orders_1_idx` (`client_id` ASC),\n" +
            "  INDEX `product_id_idx` (`product_id` ASC),\n" +
            "  CONSTRAINT `client_id`\n" +
            "    FOREIGN KEY (`client_id`)\n" +
            "    REFERENCES `DB_Orders`.`Clients` (`id`)\n" +
            "    ON DELETE NO ACTION\n" +
            "    ON UPDATE NO ACTION,\n" +
            "  CONSTRAINT `product_id`\n" +
            "    FOREIGN KEY (`product_id`)\n" +
            "    REFERENCES `DB_Orders`.`Products` (`id`)\n" +
            "    ON DELETE NO ACTION\n" +
            "    ON UPDATE NO ACTION);";


    public static Connection getDBConnection() {
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
            st.addBatch(CREATE_TABLE_SQL_Products);
            st.addBatch(CREATE_TABLE_SQL_Clients);
            st.addBatch(CREATE_TABLE_SQL_Orders);
            st.executeBatch();
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

}
