package it.unisa.modelcargallery.control;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

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
    @Override
    protected void doGet(
            HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {

        request.getRequestDispatcher(
                "/WEB-INF/view/Setup.jsp"
        ).forward(request, response);
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

    	@Override
    	protected void doPost(
    	        HttpServletRequest request,
    	        HttpServletResponse response)
    	        throws ServletException, IOException {

    	    request.setCharacterEncoding("UTF-8");

    	    List<String> errors = new ArrayList<String>();

    	    String email = request.getParameter("mail");
    	    String password = request.getParameter("pwd");

    	    email = validateField(email, "email", errors);
    	    password = validateField(password, "password", errors);

    	    /*
    	     * Se i campi non sono validi, torno alla pagina
    	     * di registrazione e mostro gli errori.
    	     */
    	    if (!errors.isEmpty()) {

    	        request.setAttribute("errors", errors);
    	        request.setAttribute("mailValue", email);

    	        RequestDispatcher dispatcher =
    	                request.getRequestDispatcher(
    	                        "/WEB-INF/view/Setup.jsp"
    	                );

    	        dispatcher.forward(request, response);
    	        return;
    	    }

    	    try {
    	        DataSource ds =
    	                (DataSource) getServletContext()
    	                        .getAttribute("DataSource");

    	        UserDaoImpl userDao =
    	                new UserDaoImpl(ds);

    	        UserBean existingUser =
    	                userDao.doRetrieveByUsername(email);

    	        if (existingUser != null) {

    	            errors.add(
    	                    "Esiste già un account registrato con questa email."
    	            );

    	            request.setAttribute("errors", errors);
    	            request.setAttribute("mailValue", email);

    	            request.getRequestDispatcher(
    	                    "/WEB-INF/view/Setup.jsp"
    	            ).forward(request, response);

    	            return;
    	        }

    	        UserBean user = new UserBean();

    	        user.setUsername(email);

    	        user.setPasswordHash(
    	                loginServlet.toDigiset(password)
    	        );

    	        /*
    	         * Il ruolo non deve essere letto dal form.
    	         */
    	        user.setRole("user");

    	        userDao.doSave(user);
    	        
    	        /*
    	         * Recupero nuovamente l'utente perché il database
    	         * ha generato il suo ID.
    	         */
    	        UserBean registeredUser =
    	                userDao.doRetrieveByUsername(email);

    	        if (registeredUser == null) {
    	            throw new ServletException(
    	                    "Registrazione completata, ma impossibile recuperare l'utente"
    	            );
    	        }

    	        HttpSession session = request.getSession();

    	        session.setAttribute("user", registeredUser);
    	        session.setAttribute("userMail", registeredUser.getUsername());
    	        session.setAttribute("role", registeredUser.getRole());

    	        session.setAttribute(
    	                "authToken",
    	                UUID.randomUUID().toString()
    	        );

    	        /*
    	         * Mantiene l'eventuale carrello già creato
    	         * mentre l'utente non era registrato.
    	         */
    	        CartBean cart =
    	                (CartBean) session.getAttribute("cart");

    	        if (cart == null) {
    	            cart = new CartBean();
    	            session.setAttribute("cart", cart);
    	        }

    	        response.sendRedirect(
    	                request.getContextPath() + "/common/welcome"
    	        );

    	        return;
    	    } catch (SQLException e) {

    	        throw new ServletException(
    	                "Errore durante la registrazione",
    	                e
    	        );
    	    }
    	}
}
