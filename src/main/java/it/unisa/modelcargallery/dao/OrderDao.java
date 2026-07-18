package it.unisa.modelcargallery.dao;

import java.sql.SQLException;
import java.util.Collection;

import it.unisa.modelcargallery.model.OrderBean;

public interface OrderDao {

    public void doSave(OrderBean order) throws SQLException;
    
    public boolean doDelete(int code) throws SQLException;
    
    public OrderBean doRetrieveByKey(int code) throws SQLException;
	
	public Collection<OrderBean> doRetrieveAll(String order) throws SQLException;
    
}