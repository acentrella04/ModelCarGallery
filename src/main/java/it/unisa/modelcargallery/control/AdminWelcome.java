package it.unisa.modelcargallery.control;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Collection;

import javax.sql.DataSource;

import it.unisa.modelcargallery.dao.ProductDao;
import it.unisa.modelcargallery.dao.ProductDaoImpl;
import it.unisa.modelcargallery.model.ProductBean;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet("/admin/welcome")
public class AdminWelcome extends HttpServlet {

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

		String action = request.getParameter("action");

		try {
			if ("read".equalsIgnoreCase(action)) {
				readSingleProduct(request);
			}

			loadProductList(request);

			RequestDispatcher dispatcher = request.getRequestDispatcher("/WEB-INF/view/admin/welcomeAdmin.jsp");

			dispatcher.forward(request, response);

		} catch (SQLException | NumberFormatException e) {

			throw new ServletException("Errore nel recupero dei prodotti", e);
		}
	}

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		String action = request.getParameter("action");

		try {
			if ("insert".equalsIgnoreCase(action)) {

				insertProduct(request);

				response.sendRedirect(request.getContextPath() + "/admin/welcome");

			} else if ("update".equalsIgnoreCase(action)) {

				updateProduct(request);

				int code = Integer.parseInt(request.getParameter("code"));

				response.sendRedirect(request.getContextPath() + "/admin/welcome?action=read&code=" + code);

			} else if ("delete".equalsIgnoreCase(action)) {

				deleteProduct(request);

				response.sendRedirect(request.getContextPath() + "/admin/welcome");

			} else {

				response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Azione non valida");
			}

		} catch (SQLException | NumberFormatException e) {

			throw new ServletException("Errore durante la gestione del prodotto", e);
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

	private void updateProduct(HttpServletRequest request) throws SQLException {

		int code = Integer.parseInt(request.getParameter("code"));

		String name = request.getParameter("name");

		String description = request.getParameter("description");

		float price = Float.parseFloat(request.getParameter("price"));

		int quantity = Integer.parseInt(request.getParameter("quantity"));

		ProductBean product = new ProductBean();

		product.setCode(code);
		product.setName(name);
		product.setDescription(description);
		product.setPrice(price);
		product.setQuantity(quantity);

		productDao.doUpdate(product);
	}

	private void deleteProduct(HttpServletRequest request) throws SQLException {

		int code = Integer.parseInt(request.getParameter("code"));

		productDao.doDelete(code);
	}

	private void readSingleProduct(HttpServletRequest request) throws SQLException {

		int code = Integer.parseInt(request.getParameter("code"));

		ProductBean product = productDao.doRetrieveByKey(code);

		request.setAttribute("product", product);
	}

	private void loadProductList(HttpServletRequest request) throws SQLException {

		String sort = request.getParameter("sort");

		Collection<ProductBean> products = productDao.doRetrieveAll(sort);

		request.setAttribute("products", products);
	}
}