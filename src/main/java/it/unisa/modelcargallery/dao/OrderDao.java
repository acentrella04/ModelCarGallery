package it.unisa.modelcargallery.dao;

import java.sql.SQLException;
import java.util.Collection;

import it.unisa.modelcargallery.model.OrderBean;

public interface OrderDao {

    void doSave(OrderBean order) throws SQLException;

    OrderBean doRetrieveByKey(int id) throws SQLException;

    Collection<OrderBean> doRetrieveByUserId(int userId)throws SQLException;

    Collection<OrderBean> doRetrieveAll(String from,String to,String mail)throws SQLException;

    boolean doDelete(int id) throws SQLException;
}