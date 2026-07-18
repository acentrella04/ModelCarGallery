package it.unisa.modelcargallery.control;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import it.unisa.modelcargallery.dao.UserDaoImpl;
import it.unisa.modelcargallery.model.CartBean;
import it.unisa.modelcargallery.model.UserBean;

/**
 * Servlet implementation class regServlet
 */
@WebServlet("/regServlet")
public class regServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public regServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		List<String> errors = new ArrayList<>();
        String email = request.getParameter("mail");
        String pwd = request.getParameter("pwd");
        String role= request.getParameter("role");
        email = validateField(email, "email", errors);
        pwd = validateField(pwd, "password", errors);
        role= validateField(role,"role",errors);
        RequestDispatcher dispatcher = request.getRequestDispatcher("/product");
        if (!errors.isEmpty()) {
            request.setAttribute("errors", errors);
            dispatcher.forward(request, response);
            return;
        }
        try {
            DataSource ds = (DataSource) getServletContext().getAttribute("DataSource");
            UserDaoImpl userDao = new UserDaoImpl(ds);
            UserBean existingUser = userDao.doRetrieveByUsername(email);

            if (existingUser != null) {
                errors.add("Email già registrata");
                request.setAttribute("errors", errors);

                dispatcher = request.getRequestDispatcher("/WEB-INF/view/Setup.jsp");
                dispatcher.forward(request, response);
                return;
            }
            UserBean user=new UserBean();
            user.setUsername(email);
            String digiset = regServlet.toDigiset(pwd);
            user.setPasswordHash(digiset);
            user.setRole(role);
            userDao.doSave(user);
            // Utente normale: qui eventualmente serve il carrello
            CartBean cart = (CartBean) request.getSession().getAttribute("cart");
            if (cart == null) {
               cart = new CartBean();
               request.getSession().setAttribute("cart", cart);
              }
            response.sendRedirect(request.getContextPath() + "/common/welcome");
        }catch (Exception e) {
            throw new ServletException("Errore durante la registrazione", e);
        }
	}
       
        
        public static String toDigiset(String password) {
    		try {
    			MessageDigest md=MessageDigest.getInstance("SHA-512");
    			byte[] digisetBytes=md.digest(password.getBytes(StandardCharsets.UTF_8));
    			StringBuilder sb= new StringBuilder();
    			
    			for(byte b: digisetBytes) {
    				sb.append(String.format("%02x", b));
    			}
    			return sb.toString();
    		}catch(NoSuchAlgorithmException e) {
    			throw new RuntimeException("Algoritmo SHA-512 non disponibile",e);
    		}
    	}
    	
    	private String validateField(String value, String fieldName, List<String> errors) {
            if (value == null || value.trim().isEmpty()) {
                errors.add("Il campo " + fieldName + " non può essere vuoto");
                return "";
            }
            return value.trim();
        }

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
