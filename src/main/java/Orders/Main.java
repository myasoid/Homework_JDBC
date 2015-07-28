package Orders;

import Orders.DAO.ClientDAO;
import Orders.DAO.OrderDAO;
import Orders.DAO.ProductDAO;
import Orders.Entity.Client;
import Orders.Entity.Order;
import Orders.Entity.Product;

import java.util.Date;

public class Main {

    public static void main(String[] args) {

        Product product = new Product(1, "Some product");
        ProductDAO productDAO = new ProductDAO();
        productDAO.insert(product);
        System.out.println(productDAO.select());

        Client client = new Client(2, "Client");
        ClientDAO clientDAO = new ClientDAO();
        clientDAO.insert(client);
        System.out.println(clientDAO.select());


        Order order = new Order(3, new Date(), client, product, 3, 435);
        OrderDAO orderDAO = new OrderDAO();
        orderDAO.insert(order);
        System.out.println(orderDAO.select());

        /*
        [Product{id=1, description='Some product'}]
        [Product{id=2, description='Client'}]
        [Order{id=3, date=2015-07-29, client=Product{id=2, description='Client'}, product=Product{id=1, description='Some product'}, quantity=3, amount=435.0}]
        */

    }
}
