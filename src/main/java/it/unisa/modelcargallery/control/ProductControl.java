package it.unisa.modelcargallery.control;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Collection;

import javax.sql.DataSource;

import it.unisa.modelcargallery.dao.ProductDao;
import it.unisa.modelcargallery.dao.ProductDaoImpl;
import it.unisa.modelcargallery.model.CartBean;
import it.unisa.modelcargallery.model.ProductBean;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

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

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		CartBean cart = (CartBean) request.getSession().getAttribute("cart");

		if (cart == null) {

			cart = new CartBean();

			request.getSession().setAttribute("cart", cart);
		}

		String action = request.getParameter("action");
		processAction(request, cart);

		request.getSession().setAttribute("cart", cart);

		if ("addC".equalsIgnoreCase(action) || "deleteC".equalsIgnoreCase(action)
				|| "removeAllC".equalsIgnoreCase(action) || "clearC".equalsIgnoreCase(action)) {

			response.sendRedirect(request.getContextPath() + "/product");

			return;
		}
		loadProductList(request);

		RequestDispatcher dispatcher = request.getRequestDispatcher("/WEB-INF/view/ProductView.jsp");

		dispatcher.forward(request, response);
	}

	private void processAction(HttpServletRequest request, CartBean cart) throws ServletException {

		String action = request.getParameter("action");

		if (action == null || action.trim().isEmpty()) {

			return;
		}

		try {

			if ("addC".equalsIgnoreCase(action)) {

				int code = Integer.parseInt(request.getParameter("code"));

				ProductBean product = productDao.doRetrieveByKey(code);

				cart.addProduct(product);

			} else if ("deleteC".equalsIgnoreCase(action)) {

				int code = Integer.parseInt(request.getParameter("code"));

				cart.deleteOneProduct(code);

			} else if ("removeAllC".equalsIgnoreCase(action)) {

				int code = Integer.parseInt(request.getParameter("code"));

				cart.deleteAllProducts(code);

			} else if ("clearC".equalsIgnoreCase(action)) {

				cart.clearCart();

			} else if ("read".equalsIgnoreCase(action)) {

				int code = Integer.parseInt(request.getParameter("code"));

				ProductBean product = productDao.doRetrieveByKey(code);

				request.setAttribute("product", product);
			}

		} catch (SQLException e) {

			throw new ServletException("Errore durante il recupero del prodotto", e);

		} catch (NumberFormatException e) {

			throw new ServletException("Il codice del prodotto non è valido", e);
		}
	}

	private void loadProductList(HttpServletRequest request) throws ServletException {

		String description = request.getParameter("description");

		try {

			Collection<ProductBean> products;
			if (description == null || description.trim().isEmpty()) {

				products = productDao.doRetrieveRandom();

				description = "";

			} else {

				description = description.trim();

				products = productDao.doRetrieveByDescription(description);
			}

			request.setAttribute("products", products);
			request.setAttribute("selectedDescription", description);

		} catch (SQLException e) {

			throw new ServletException("Errore nel recupero dei prodotti", e);
		}
	}

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		doGet(request, response);
	}
}