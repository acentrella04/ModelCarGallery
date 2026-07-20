package it.unisa.modelcargallery.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;

import javax.sql.DataSource;

import it.unisa.modelcargallery.model.ProductBean;

public class ProductDaoImpl implements ProductDao {

    private static final String TABLE_NAME = "product";
    private DataSource ds = null;

    public ProductDaoImpl(DataSource ds) {
        this.ds = ds;
    }

    @Override
    public synchronized void doSave(ProductBean product) throws SQLException {
        String insertSQL = "INSERT INTO " + TABLE_NAME
                + " (name, description, price, quantity) VALUES (?, ?, ?, ?)";
        try (Connection connection = ds.getConnection();
        		PreparedStatement preparedStatement = connection.prepareStatement(insertSQL)) {
            preparedStatement.setString(1, product.getName());
            preparedStatement.setString(2, product.getDescription());
            preparedStatement.setFloat(3, product.getPrice());
            preparedStatement.setInt(4, product.getQuantity());
            preparedStatement.executeUpdate();
        }
    }
    
    @Override
    public synchronized boolean doUpdateImage(ProductBean product) throws SQLException {
        String sql = "UPDATE " + TABLE_NAME + " SET immagine_copertina = ?, mime_type = ? WHERE code = ?";
        try (Connection conn = ds.getConnection();
        		PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, product.getImmagine_copertina());
            ps.setString(2, product.getMimeType());
            ps.setInt(3, product.getCode());
            int rowsUpdated = ps.executeUpdate();
            return rowsUpdated != 0;
        }
    }
    @Override
    public synchronized void doUpdate(ProductBean product)
            throws SQLException {

        String updateSQL =
            "UPDATE product SET " +
            "name = ?, " +
            "description = ?, " +
            "price = ?, " +
            "quantity = ? " +
            "WHERE code = ?";

        try (Connection connection = ds.getConnection();
             PreparedStatement preparedStatement =
                 connection.prepareStatement(updateSQL)) {

            preparedStatement.setString(
                1,
                product.getName()
            );

            preparedStatement.setString(
                2,
                product.getDescription()
            );

            preparedStatement.setFloat(
                3,
                product.getPrice()
            );

            preparedStatement.setInt(
                4,
                product.getQuantity()
            );

            preparedStatement.setInt(
                5,
                product.getCode()
            );

            preparedStatement.executeUpdate();
        }
    }
    @Override
    public synchronized ProductBean doRetrieveByKey(int code) throws SQLException {
        ProductBean bean = new ProductBean();
        String selectSQL = "SELECT * FROM " + TABLE_NAME + " WHERE code = ?";
        try (Connection connection = ds.getConnection();
        		PreparedStatement preparedStatement = connection.prepareStatement(selectSQL)) {
            preparedStatement.setInt(1, code);
            try (ResultSet rs = preparedStatement.executeQuery()) {
                while (rs.next()) {
                    bean.setCode(rs.getInt("code"));
                    bean.setName(rs.getString("name"));
                    bean.setDescription(rs.getString("description"));
                    bean.setPrice(rs.getFloat("price"));
                    bean.setQuantity(rs.getInt("quantity"));
                    bean.setImmagine_copertina(rs.getString("immagine_copertina"));
                    bean.setMimeType(rs.getString("mime_type"));
                }
            }
        }
        return bean;
    }

    @Override
    public synchronized boolean doDelete(int code) throws SQLException {
        String deleteSQL = "DELETE FROM " + TABLE_NAME + " WHERE code = ?";
        try (Connection connection = ds.getConnection();
        		PreparedStatement preparedStatement = connection.prepareStatement(deleteSQL)) {
            preparedStatement.setInt(1, code);
            int result = preparedStatement.executeUpdate();
            return result != 0;
        }
    }

    @Override
    public synchronized List<ProductBean> doRetrieveAll(String order) throws SQLException {
        List<ProductBean> products = new LinkedList<>();
        String selectSQL = "SELECT * FROM " + TABLE_NAME;
        if (order != null && !order.isEmpty()) {
            selectSQL += " ORDER BY " + order;
        }
        try (Connection connection = ds.getConnection();
        		PreparedStatement preparedStatement = connection.prepareStatement(selectSQL);
        		ResultSet rs = preparedStatement.executeQuery()) {
            while (rs.next()) {
                ProductBean bean = new ProductBean();
                bean.setCode(rs.getInt("code"));
                bean.setName(rs.getString("name"));
                bean.setDescription(rs.getString("description"));
                bean.setPrice(rs.getFloat("price"));
                bean.setQuantity(rs.getInt("quantity"));
                bean.setImmagine_copertina(rs.getString("immagine_copertina"));
                bean.setMimeType(rs.getString("mime_type"));
                products.add(bean);
            }
        }
        return products;
    }
}


