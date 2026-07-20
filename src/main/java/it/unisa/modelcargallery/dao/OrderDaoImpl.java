package it.unisa.modelcargallery.dao;


import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import javax.sql.DataSource;

import it.unisa.modelcargallery.model.OrderBean;

public class OrderDaoImpl implements OrderDao {
	
	private static final String TABLE_NAME = "orders";
    private final DataSource ds;

    public OrderDaoImpl(DataSource ds) {
        this.ds = ds;
    }
@Override
public void doSave(OrderBean order) throws SQLException{
	String insertSQL = "INSERT INTO " + TABLE_NAME
            + " (name, surname, address, numberaddress, unit_price, quantity, mail) VALUES (?, ?, ?, ?, ?, ?, ?)";
    try (Connection connection = ds.getConnection();
    		PreparedStatement preparedStatement = connection.prepareStatement(insertSQL,PreparedStatement.RETURN_GENERATED_KEYS)) {
        preparedStatement.setString(1, order.getName());
        preparedStatement.setString(2, order.getSurname());
        preparedStatement.setString(3, order.getAddress());
        preparedStatement.setString(4, order.getNumberAddress());
        preparedStatement.setFloat(5, order.getUnitPrice());
        preparedStatement.setInt(6, order.getQuantity());
        preparedStatement.setString(7, order.getMail());
        preparedStatement.executeUpdate();
        try (ResultSet generatedKeys = preparedStatement.getGeneratedKeys()) {
            if (generatedKeys.next()) {
                order.setId(generatedKeys.getInt(1));
            }
        }
    }
}
@Override    
public synchronized boolean doDelete(int code) throws SQLException{
	String deleteSQL = "DELETE FROM " + TABLE_NAME + " WHERE id = ?";
    try (Connection connection = ds.getConnection();
    		PreparedStatement preparedStatement = connection.prepareStatement(deleteSQL)) {
        preparedStatement.setInt(1, code);
        int result = preparedStatement.executeUpdate();
        return result != 0;
    }
}
@Override   
public synchronized OrderBean doRetrieveByKey(int code) throws SQLException{
	OrderBean bean = new OrderBean();
    String selectSQL = "SELECT * FROM " + TABLE_NAME + " WHERE id = ?";
    try (Connection connection = ds.getConnection();
    		PreparedStatement preparedStatement = connection.prepareStatement(selectSQL)) {
        preparedStatement.setInt(1, code);
        try (ResultSet rs = preparedStatement.executeQuery()) {
            while (rs.next()) {
                bean.setId(rs.getInt("id"));
                bean.setName(rs.getString("name"));
                bean.setSurname(rs.getString("surname"));
                bean.setAddress(rs.getString("address"));
                bean.setNumberAddress(rs.getString("numberaddress"));
                bean.setUnitPrice(rs.getFloat("unit_price"));
                bean.setQuantity(rs.getInt("quantity"));
                bean.setMail(rs.getString("mail"));
            }
        }
    }
    return bean;
    }
@Override 
public Collection<OrderBean> doRetrieveAll(String order) throws SQLException{
	List<OrderBean> orders = new LinkedList<>();
    String selectSQL = "SELECT * FROM " + TABLE_NAME;
    if (order != null && !order.isEmpty()) {
        selectSQL += " ORDER BY " + order;
    }
    try (Connection connection = ds.getConnection();
    		PreparedStatement preparedStatement = connection.prepareStatement(selectSQL);
    		ResultSet rs = preparedStatement.executeQuery()) {
        while (rs.next()) {
            OrderBean bean = new OrderBean();
            bean.setId(rs.getInt("id"));
            bean.setName(rs.getString("name"));
            bean.setSurname(rs.getString("surname"));
            bean.setAddress(rs.getString("address"));
            bean.setNumberAddress(rs.getString("numberaddress"));
            bean.setUnitPrice(rs.getFloat("unit_price"));
            bean.setQuantity(rs.getInt("quantity"));
            bean.setMail(rs.getString("mail"));
            orders.add(bean);
        }
    }
    return orders;
	}
}