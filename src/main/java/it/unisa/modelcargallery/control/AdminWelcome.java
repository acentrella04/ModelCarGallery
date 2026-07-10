package it.unisa.modelcargallery.control;


import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collection;

import javax.sql.DataSource;

import it.unisa.modelcargallery.dao.ProductDao;
import it.unisa.modelcargallery.dao.ProductDaoImpl;
import it.unisa.modelcargallery.model.ProductBean;

/**
 * Servlet implementation class AdminWelcome
 */
@WebServlet("/admin/welcome")
public class AdminWelcome extends HttpServlet {
	
	private static final long serialVersionUID = 1L;
	
	private ProductDao productDao;
	
	@Override
	public void init(ServletConfig config) throws ServletException {
		super.init(config);

		DataSource ds = (DataSource) getServletContext().getAttribute("DataSource");
		if (ds == null) {
			throw new ServletException("DataSource non disponibile nel contesto applicativo.");
		}
		productDao = new ProductDaoImpl(ds);
	}
       
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
		try {
		Collection<ProductBean> products = productDao.doRetrieveAll("code");

		request.setAttribute("products", products);

		RequestDispatcher dispatcher =
		request.getRequestDispatcher("/WEB-INF/view/admin/welcomeAdmin.jsp");

		dispatcher.forward(request, response);
    }catch(Exception e){
    	throw new ServletException("Errore nel recupero dei prodotti", e);
    }
		
}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}
}
