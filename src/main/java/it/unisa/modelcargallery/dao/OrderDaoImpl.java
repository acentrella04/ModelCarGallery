package it.unisa.modelcargallery.dao;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Map;

import javax.sql.DataSource;

import it.unisa.modelcargallery.model.ProductBean;

public class OrderDaoImpl implements OrderDao {

    private final DataSource ds;

    public OrderDaoImpl(DataSource ds) {
        this.ds = ds;
    }

    @Override
    public int saveOrder(String name,String surname,String address,int numberAddress,String mail,Map<Integer, ProductBean> products,Map<Integer, Integer> quantities) throws SQLException {

        String insertOrder = "INSERT INTO orders(name, surname, address, numberaddress, mail)VALUES (?, ?, ?, ?, ?)";

        String insertItem = "INSERT INTO order_items(order_id, product_code, product_name, unit_price, quantity)VALUES (?, ?, ?, ?, ?)";

        Connection connection = null;
        try {
            connection = ds.getConnection();
            connection.setAutoCommit(false);

            int orderId;

            try (PreparedStatement psOrder =connection.prepareStatement(insertOrder,Statement.RETURN_GENERATED_KEYS)) {
                psOrder.setString(1, name);
                psOrder.setString(2, surname);
                psOrder.setString(3, address);
                psOrder.setInt(4, numberAddress);
                psOrder.setString(5, mail);

                psOrder.executeUpdate();

                try (ResultSet generatedKeys =psOrder.getGeneratedKeys()) {
                    if (!generatedKeys.next()) {
                        throw new SQLException("ID dell'ordine non recuperato");
                    }
                    orderId = generatedKeys.getInt(1);
                }
            }
            try (PreparedStatement psItem = connection.prepareStatement(insertItem)) {

                for (ProductBean product : products.values()) {
                    int quantity = quantities.get(product.getCode());

                    psItem.setInt(1, orderId);
                    psItem.setInt(2, product.getCode());
                    psItem.setString(3, product.getName());

                    psItem.setBigDecimal(
                            4,
                            BigDecimal.valueOf(product.getPrice()));

                    psItem.setInt(5, quantity);

                    psItem.addBatch();
                }

                psItem.executeBatch();
            }

            connection.commit();

            return orderId;

        } catch (SQLException e) {

            if (connection != null) {
                try {
                    connection.rollback();
                } catch (SQLException rollbackException) {
                    e.addSuppressed(rollbackException);
                }
            }

            throw e;

        } finally {

            if (connection != null) {
                try {
                    connection.setAutoCommit(true);
                    connection.close();
                } catch (SQLException e) {
                    System.err.println(
                            "Errore chiusura connessione: "
                            + e.getMessage());
                }
            }
        }
    }
}