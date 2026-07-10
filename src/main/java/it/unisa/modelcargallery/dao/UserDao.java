package it.unisa.modelcargallery.dao;

import java.sql.SQLException;
import java.util.Collection;

import it.unisa.modelcargallery.model.UserBean;

public interface UserDao {
	
	public void doSave(UserBean users) throws SQLException;

	public boolean doDelete(int code) throws SQLException;

	public UserBean doRetrieveByUsername(String username) throws SQLException;
	
	public Collection<UserBean> doRetrieveAll(String user) throws SQLException;

}



