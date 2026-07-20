package it.unisa.modelcargallery.model;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

public class OrderBean implements Serializable {

    private static final long serialVersionUID = 1L;

    private int id;
    private int userId;

    private Timestamp orderDate;

    private String name;
    private String surname;
    private String address;
    private int numberAddress;

    private float total;
    private String mail;
    private String paymentMethod;

    private List<OrderItemBean> items;

    public OrderBean() {
        items = new ArrayList<OrderItemBean>();
    }

    public void addItem(OrderItemBean item) {
        items.add(item);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public Timestamp getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(Timestamp orderDate) {
        this.orderDate = orderDate;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public int getNumberAddress() {
        return numberAddress;
    }

    public void setNumberAddress(int numberAddress) {
        this.numberAddress = numberAddress;
    }

    public float getTotal() {
        return total;
    }

    public void setTotal(float total) {
        this.total = total;
    }

    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public List<OrderItemBean> getItems() {
        return items;
    }

    public void setItems(List<OrderItemBean> items) {
        this.items = items;
    }
}