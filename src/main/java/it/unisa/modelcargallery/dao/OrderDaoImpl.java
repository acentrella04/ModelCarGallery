package it.unisa.modelcargallery.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import javax.sql.DataSource;

import it.unisa.modelcargallery.model.OrderBean;
import it.unisa.modelcargallery.model.OrderItemBean;

public class OrderDaoImpl implements OrderDao {

    private static final String TABLE_NAME = "orders";

    private final DataSource ds;

    public OrderDaoImpl(DataSource ds) {
        this.ds = ds;
    }

    @Override
    public void doSave(OrderBean order) throws SQLException {

        String insertOrderSQL =
            "INSERT INTO " + TABLE_NAME + " (" +
            "user_id, name, surname, address, numberaddress, " +
            "total, mail, payment_method" +
            ") VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection connection = ds.getConnection();
             PreparedStatement preparedStatement =
                 connection.prepareStatement(
                     insertOrderSQL,
                     Statement.RETURN_GENERATED_KEYS
                 )) {

            preparedStatement.setInt(1, order.getUserId());
            preparedStatement.setString(2, order.getName());
            preparedStatement.setString(3, order.getSurname());
            preparedStatement.setString(4, order.getAddress());
            preparedStatement.setInt(5, order.getNumberAddress());
            preparedStatement.setFloat(6, order.getTotal());
            preparedStatement.setString(7, order.getMail());
            preparedStatement.setString(8, order.getPaymentMethod());

            preparedStatement.executeUpdate();

            try (ResultSet generatedKeys =
                    preparedStatement.getGeneratedKeys()) {

                if (generatedKeys.next()) {
                    order.setId(generatedKeys.getInt(1));
                } else {
                    throw new SQLException(
                        "Impossibile recuperare l'ID dell'ordine"
                    );
                }
            }
        }

        saveOrderItems(order);
    }

    private void saveOrderItems(OrderBean order)
            throws SQLException {

        String insertItemSQL =
            "INSERT INTO order_items (" +
            "order_id, product_code, product_name, " +
            "unit_price, quantity" +
            ") VALUES (?, ?, ?, ?, ?)";

        try (Connection connection = ds.getConnection();
             PreparedStatement preparedStatement =
                 connection.prepareStatement(insertItemSQL)) {

            for (OrderItemBean item : order.getItems()) {

                preparedStatement.setInt(
                    1,
                    order.getId()
                );

                preparedStatement.setInt(
                    2,
                    item.getProductCode()
                );

                preparedStatement.setString(
                    3,
                    item.getProductName()
                );

                preparedStatement.setFloat(
                    4,
                    item.getUnitPrice()
                );

                preparedStatement.setInt(
                    5,
                    item.getQuantity()
                );

                preparedStatement.executeUpdate();
            }
        }
    }

    @Override
    public OrderBean doRetrieveByKey(int id)
            throws SQLException {

        OrderBean order = null;

        String selectSQL =
            "SELECT * FROM " + TABLE_NAME + " WHERE id = ?";

        try (Connection connection = ds.getConnection();
             PreparedStatement preparedStatement =
                 connection.prepareStatement(selectSQL)) {

            preparedStatement.setInt(1, id);

            try (ResultSet rs =
                    preparedStatement.executeQuery()) {

                if (rs.next()) {
                    order = createOrderBean(rs);

                    order.setItems(
                        retrieveItemsByOrderId(id)
                    );
                }
            }
        }

        return order;
    }

    @Override
    public Collection<OrderBean> doRetrieveByUserId(int userId)
            throws SQLException {

        List<OrderBean> orders =
            new LinkedList<OrderBean>();

        String selectSQL =
            "SELECT * FROM " + TABLE_NAME +
            " WHERE user_id = ?" +
            " ORDER BY order_date DESC";

        try (Connection connection = ds.getConnection();
             PreparedStatement preparedStatement =
                 connection.prepareStatement(selectSQL)) {

            preparedStatement.setInt(1, userId);

            try (ResultSet rs =
                    preparedStatement.executeQuery()) {

                while (rs.next()) {
                    OrderBean order = createOrderBean(rs);

                    order.setItems(
                        retrieveItemsByOrderId(order.getId())
                    );

                    orders.add(order);
                }
            }
        }

        return orders;
    }

    @Override
    public Collection<OrderBean> doRetrieveAll(
            String from,
            String to,
            String mail)
            throws SQLException {

        List<OrderBean> orders =
            new LinkedList<OrderBean>();

        String selectSQL =
            "SELECT * FROM " + TABLE_NAME +
            " WHERE 1 = 1";

        if (from != null && !from.isEmpty()) {
            selectSQL += " AND DATE(order_date) >= ?";
        }

        if (to != null && !to.isEmpty()) {
            selectSQL += " AND DATE(order_date) <= ?";
        }

        if (mail != null && !mail.isEmpty()) {
            selectSQL += " AND mail = ?";
        }

        selectSQL += " ORDER BY order_date DESC";

        try (Connection connection = ds.getConnection();
             PreparedStatement preparedStatement =
                 connection.prepareStatement(selectSQL)) {

            int parameterIndex = 1;

            if (from != null && !from.isEmpty()) {
                preparedStatement.setString(
                    parameterIndex,
                    from
                );

                parameterIndex++;
            }

            if (to != null && !to.isEmpty()) {
                preparedStatement.setString(
                    parameterIndex,
                    to
                );

                parameterIndex++;
            }

            if (mail != null && !mail.isEmpty()) {
                preparedStatement.setString(
                    parameterIndex,
                    mail
                );
            }

            try (ResultSet rs =
                    preparedStatement.executeQuery()) {

                while (rs.next()) {
                    OrderBean order = createOrderBean(rs);

                    order.setItems(
                        retrieveItemsByOrderId(order.getId())
                    );

                    orders.add(order);
                }
            }
        }

        return orders;
    }

    private List<OrderItemBean> retrieveItemsByOrderId(
            int orderId)
            throws SQLException {

        List<OrderItemBean> items =
            new LinkedList<OrderItemBean>();

        String selectSQL =
            "SELECT * FROM order_items " +
            "WHERE order_id = ?";

        try (Connection connection = ds.getConnection();
             PreparedStatement preparedStatement =
                 connection.prepareStatement(selectSQL)) {

            preparedStatement.setInt(1, orderId);

            try (ResultSet rs =
                    preparedStatement.executeQuery()) {

                while (rs.next()) {
                    OrderItemBean item =
                        new OrderItemBean();

                    item.setId(
                        rs.getInt("id")
                    );

                    item.setOrderId(
                        rs.getInt("order_id")
                    );

                    item.setProductCode(
                        rs.getInt("product_code")
                    );

                    item.setProductName(
                        rs.getString("product_name")
                    );

                    item.setUnitPrice(
                        rs.getFloat("unit_price")
                    );

                    item.setQuantity(
                        rs.getInt("quantity")
                    );

                    items.add(item);
                }
            }
        }

        return items;
    }

    private OrderBean createOrderBean(ResultSet rs)
            throws SQLException {

        OrderBean order = new OrderBean();

        order.setId(
            rs.getInt("id")
        );

        order.setUserId(
            rs.getInt("user_id")
        );

        order.setOrderDate(
            rs.getTimestamp("order_date")
        );

        order.setName(
            rs.getString("name")
        );

        order.setSurname(
            rs.getString("surname")
        );

        order.setAddress(
            rs.getString("address")
        );

        order.setNumberAddress(
            rs.getInt("numberaddress")
        );

        order.setTotal(
            rs.getFloat("total")
        );

        order.setMail(
            rs.getString("mail")
        );

        order.setPaymentMethod(
            rs.getString("payment_method")
        );

        return order;
    }

    @Override
    public boolean doDelete(int id) throws SQLException {

        String deleteSQL =
            "DELETE FROM " + TABLE_NAME + " WHERE id = ?";

        try (Connection connection = ds.getConnection();
             PreparedStatement preparedStatement =
                 connection.prepareStatement(deleteSQL)) {

            preparedStatement.setInt(1, id);

            int result =
                preparedStatement.executeUpdate();

            return result != 0;
        }
    }
}