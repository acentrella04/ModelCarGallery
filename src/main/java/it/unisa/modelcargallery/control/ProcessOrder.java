package it.unisa.modelcargallery.control;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

import javax.sql.DataSource;

import it.unisa.modelcargallery.dao.OrderDao;
import it.unisa.modelcargallery.dao.OrderDaoImpl;
import it.unisa.modelcargallery.model.CartBean;
import it.unisa.modelcargallery.model.OrderBean;
import it.unisa.modelcargallery.model.ProductBean;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet("/ProcessOrder")
public class ProcessOrder extends HttpServlet {

    private static final long serialVersionUID = 1L;

    private OrderDao orderDao;
    private int count=0;
    private float subtotal=0;

    @Override
	public void init(ServletConfig servletConfig) throws ServletException {
		super.init(servletConfig);
		DataSource ds = (DataSource) getServletContext().getAttribute("DataSource");
		if (ds == null) {
			throw new ServletException("DataSource non disponibile nel contesto");
		}
		orderDao = new OrderDaoImpl(ds);
	}

    protected void doGet(HttpServletRequest request,HttpServletResponse response) throws ServletException, IOException {
    	CartBean cart = (CartBean) request.getSession().getAttribute("cart");
    	if (cart == null) {
			throw new ServletException("Carrello vuoto, impossibile procedere all'ordine");
		}
    	count = cart.getProducts().size();
    	List<ProductBean> prodcart = cart.getProducts();
		for (ProductBean beancart : prodcart) {
			subtotal=subtotal+beancart.getPrice();
		}
    	try {
			insertProduct(request);
		} catch (SQLException e) {
			System.err.println("Error:" + e.getMessage());
		}
    	RequestDispatcher dispatcher = getServletContext().
				getRequestDispatcher("/WEB-INF/view/Fattura.jsp");
		dispatcher.forward(request, response);
    	
    }
    private void insertProduct(HttpServletRequest request) throws SQLException {
    	String name=request.getParameter("name");
    	String surname=request.getParameter("surname");
    	String address=request.getParameter("address");
    	String number=request.getParameter("number");
    	String mail=request.getParameter("mail");
    	float unit_price=subtotal;
    	int quantity=count;
    	
    	OrderBean order=new OrderBean();
    	order.setName(name);
    	order.setSurname(surname);
    	order.setAddress(address);
    	order.setNumberAddress(number);
    	order.setMail(mail);
    	order.setUnitPrice(unit_price);
    	order.setQuantity(quantity);
    	orderDao.doSave(order);
    	request.setAttribute("order", order);
    }
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    	doGet(request, response);
    }
}
