package it.unisa.modelcargallery.control;

import java.io.IOException;
import java.sql.SQLException;

import javax.sql.DataSource;

import it.unisa.modelcargallery.dao.UserDaoImpl;
import it.unisa.modelcargallery.model.UserBean;
import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet("/check-email")
public class CheckEmailServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;

	private UserDaoImpl userDao;

	@Override
	public void init(ServletConfig config) throws ServletException {

		super.init(config);

		DataSource ds = (DataSource) getServletContext().getAttribute("DataSource");

		if (ds == null) {
			throw new ServletException("DataSource non disponibile");
		}

		userDao = new UserDaoImpl(ds);
	}

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		response.setContentType("text/plain");

		String email = request.getParameter("mail");

		if (email == null || email.trim().isEmpty()) {
			response.getWriter().write("invalid");
			return;
		}

		email = email.trim();

		String emailRegex = "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$";

		if (!email.matches(emailRegex)) {
			response.getWriter().write("invalid");
			return;
		}

		try {
			UserBean existingUser = userDao.doRetrieveByUsername(email);

			if (existingUser == null) {
				response.getWriter().write("available");
			} else {
				response.getWriter().write("taken");
			}

		} catch (SQLException e) {
			throw new ServletException("Errore durante il controllo dell'email", e);
		}
	}
}