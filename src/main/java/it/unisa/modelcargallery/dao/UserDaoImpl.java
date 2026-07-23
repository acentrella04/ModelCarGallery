package it.unisa.modelcargallery.dao;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;

import javax.sql.DataSource;

import it.unisa.modelcargallery.model.UserBean;

public class UserDaoImpl implements UserDao {

	private static final String TABLE_NAME = "users";
	private DataSource ds = null;

	public UserDaoImpl(DataSource ds) {
		this.ds = ds;
	}

	public static String toDigiset(String password) {
		try {
			MessageDigest md = MessageDigest.getInstance("SHA-512");
			byte[] digisetBytes = md.digest(password.getBytes(StandardCharsets.UTF_8));
			StringBuilder sb = new StringBuilder();

			for (byte b : digisetBytes) {
				sb.append(String.format("%02x", b));
			}
			return sb.toString();
		} catch (NoSuchAlgorithmException e) {
			throw new RuntimeException("Algoritmo SHA-512 non disponibile", e);
		}
	}

	@Override
	public synchronized void doSave(UserBean users) throws SQLException {
		String insertSQL = "INSERT INTO " + TABLE_NAME + " (username, password_hash, role) VALUES (?, ?, ?)";
		try (Connection connection = ds.getConnection();
				PreparedStatement preparedStatement = connection.prepareStatement(insertSQL)) {
			preparedStatement.setString(1, users.getUsername());
			preparedStatement.setString(2, users.getPasswordHash());
			preparedStatement.setString(3, users.getRole());
			preparedStatement.executeUpdate();
		}
	}

	@Override
	public synchronized UserBean doRetrieveByUsername(String username) throws SQLException {

		String selectSQL = "SELECT * FROM " + TABLE_NAME + " WHERE username = ?";
		try (Connection connection = ds.getConnection();
				PreparedStatement preparedStatement = connection.prepareStatement(selectSQL)) {
			preparedStatement.setString(1, username);
			try (ResultSet rs = preparedStatement.executeQuery()) {
				if (rs.next()) {
					UserBean bean = new UserBean();

					bean.setId(rs.getInt("id"));
					bean.setUsername(rs.getString("username"));
					bean.setPasswordHash(rs.getString("password_hash"));
					bean.setRole(rs.getString("role"));

					return bean;
				}
			}
		}
		return null;
	}

	@Override
	public synchronized boolean doDelete(int id) throws SQLException {
		String deleteSQL = "DELETE FROM " + TABLE_NAME + " WHERE id  = ?";
		try (Connection connection = ds.getConnection();
				PreparedStatement preparedStatement = connection.prepareStatement(deleteSQL)) {
			preparedStatement.setInt(1, id);
			int result = preparedStatement.executeUpdate();
			return result != 0;
		}
	}

	@Override
	public synchronized List<UserBean> doRetrieveAll(String order) throws SQLException {
		List<UserBean> products = new LinkedList<>();
		String selectSQL = "SELECT * FROM " + TABLE_NAME;
		if (order != null && !order.isEmpty()) {
			selectSQL += " ORDER BY " + order;
		}
		try (Connection connection = ds.getConnection();
				PreparedStatement preparedStatement = connection.prepareStatement(selectSQL);
				ResultSet rs = preparedStatement.executeQuery()) {
			while (rs.next()) {
				UserBean bean = new UserBean();
				bean.setId(rs.getInt("id"));
				bean.setUsername(rs.getString("username"));
				bean.setPasswordHash(rs.getString("password_hash"));
				bean.setRole(rs.getString("role"));
				products.add(bean);
			}
		}
		return products;
	}
}
