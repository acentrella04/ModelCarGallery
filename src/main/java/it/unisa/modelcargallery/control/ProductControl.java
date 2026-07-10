package it.unisa.modelcargallery.control;

import java.io.IOException;
import java.sql.SQLException;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import javax.sql.DataSource;

import it.unisa.modelcargallery.dao.ProductDao;
import it.unisa.modelcargallery.dao.ProductDaoImpl;
import it.unisa.modelcargallery.model.CartBean;
import it.unisa.modelcargallery.model.ProductBean;

@WebServlet("/product")
public class ProductControl extends HttpServlet {

	private static final long serialVersionUID = 1L;

	private ProductDao productDao;

	@Override
	public void init(ServletConfig servletConfig) throws ServletException {
		super.init(servletConfig);
		DataSource ds = (DataSource) getServletContext().getAttribute("DataSource");
		if (ds == null) {
			throw new ServletException("DataSource non disponibile nel contesto");
		}
		productDao = new ProductDaoImpl(ds);
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		CartBean cart = (CartBean) request.getSession().getAttribute("cart");
		if (cart == null) {
			cart = new CartBean();
			request.getSession().setAttribute("cart", cart);
		}
		processAction(request, cart);
		// Salva il carrello aggiornato in sessione
		request.getSession().setAttribute("cart", cart);
		// Carica la lista di prodotti (eventualmente aggiornata) nella richiesta per la vista
		loadProducList(request);
		
		RequestDispatcher dispatcher = getServletContext().
				getRequestDispatcher("/WEB-INF/view/ProductView.jsp");
		dispatcher.forward(request, response);
	}

	private void processAction(HttpServletRequest request, CartBean cart) {
		String action = request.getParameter("action");
		try {
			if (action != null) {
				if (action.equalsIgnoreCase("addC")) {
					addProductToCart(request, cart);
				} else if (action.equalsIgnoreCase("deleteC")) {
					removeProductFromCart(request, cart);
				} else if (action.equalsIgnoreCase("read")) {
					readSingleProduct(request);
				} else if (action.equalsIgnoreCase("delete")) {
					deleteProduct(request);
				} else if (action.equalsIgnoreCase("insert")) {
					insertProduct(request);
				}
			}
		} catch (SQLException e) {
			System.err.println("Error:" + e.getMessage());
		}
	}

	private void insertProduct(HttpServletRequest request) throws SQLException {
		String name = request.getParameter("name");
		String description = request.getParameter("description");
		float price = Float.parseFloat(request.getParameter("price"));
		int quantity = Integer.parseInt(request.getParameter("quantity"));
		ProductBean product = new ProductBean();
		product.setName(name);
		product.setDescription(description);
		product.setPrice(price);
		product.setQuantity(quantity);
		productDao.doSave(product);
	}

	private void deleteProduct(HttpServletRequest request) throws SQLException {
		int code = Integer.parseInt(request.getParameter("code"));
		productDao.doDelete(code);
	}

	private void readSingleProduct(HttpServletRequest request) throws SQLException {
		int code = Integer.parseInt(request.getParameter("code"));
		request.setAttribute("product", productDao.doRetrieveByKey(code));
	}

	private void removeProductFromCart(HttpServletRequest request, CartBean cart) 
			throws SQLException {
		int code = Integer.parseInt(request.getParameter("code"));
		cart.deleteProduct(productDao.doRetrieveByKey(code));
	}

	private void addProductToCart(HttpServletRequest request, CartBean cart) 
			throws SQLException {
		int code = Integer.parseInt(request.getParameter("code"));
		cart.addProduct(productDao.doRetrieveByKey(code));
	}
	
	private void loadProducList(HttpServletRequest request) {
		String sort = request.getParameter("sort");
		try {
			request.setAttribute("products", productDao.doRetrieveAll(sort));
		} catch (SQLException e) {
			System.err.println("Error:" + e.getMessage());
		}
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doGet(request, response);
	}

}
