package it.unisa.modelcargallery.filter;

import java.io.IOException;

import it.unisa.modelcargallery.model.UserBean;
import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@WebFilter(urlPatterns = { "/common/*", "/ProcessOrder" })
public class UserAccessFilter implements Filter {

	@Override
	public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain chain)
			throws IOException, ServletException {

		HttpServletRequest request = (HttpServletRequest) servletRequest;

		HttpServletResponse response = (HttpServletResponse) servletResponse;

		HttpSession session = request.getSession(false);

		UserBean user = null;
		String authToken = null;

		if (session != null) {
			user = (UserBean) session.getAttribute("user");

			authToken = (String) session.getAttribute("authToken");
		}

		if (user == null || authToken == null) {

			response.sendRedirect(request.getContextPath() + "/product?loginRequired=true");

			return;
		}

		if (!"user".equalsIgnoreCase(user.getRole())) {

			response.sendError(HttpServletResponse.SC_FORBIDDEN, "Accesso non autorizzato");

			return;
		}

		chain.doFilter(request, response);
	}
}