package it.unisa.modelcargallery.control;

import java.io.IOException;
import java.sql.SQLException;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import it.unisa.modelcargallery.dao.OrderDao;
import it.unisa.modelcargallery.dao.OrderDaoImpl;
import it.unisa.modelcargallery.model.CartBean;
import it.unisa.modelcargallery.model.ProductBean;
import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@WebServlet("/ProcessOrder")
public class ProcessOrder extends HttpServlet {

    private static final long serialVersionUID = 1L;

    private OrderDao orderDao;

    @Override
    public void init(ServletConfig config)
            throws ServletException {

        super.init(config);

        DataSource ds = (DataSource) getServletContext().getAttribute("DataSource");

        if (ds == null) {
            throw new ServletException("DataSource non disponibile");
        }

        orderDao = new OrderDaoImpl(ds);
    }

    @Override
    protected void doGet(HttpServletRequest request,HttpServletResponse response) throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");

        HttpSession session = request.getSession(false);

        if (session == null) {
            response.sendRedirect(request.getContextPath() + "/");
            return;
        }

        CartBean cart = (CartBean) session.getAttribute("cart");

        if (cart == null|| cart.getProducts() == null|| cart.getProducts().isEmpty()) {

            response.sendRedirect(request.getContextPath() + "/common/welcome");
            return;
        }

        String name = request.getParameter("name");

        String surname = request.getParameter("surname");

        String address = request.getParameter("address");

        String numberParameter = request.getParameter("number");

        /*
         * Recuperiamo la mail dalla sessione,
         * non dal form modificabile nel browser.
         */
        String mail = (String) session.getAttribute("userMail");

        if (isBlank(name)|| isBlank(surname)|| isBlank(address)|| isBlank(numberParameter)|| isBlank(mail)) {

            request.setAttribute("errors",List.of("Compila correttamente tutti i dati"));

            request.getRequestDispatcher("/WEB-INF/view/common/InformationOrder.jsp").forward(request, response);

            return;
        }

        int numberAddress;

        try {
            numberAddress = Integer.parseInt(numberParameter);

        } catch (NumberFormatException e) {

            request.setAttribute("errors",List.of("Il numero civico non è valido"));

            request.getRequestDispatcher("/WEB-INF/view/common/InformationOrder.jsp").forward(request, response);

            return;
        }

        List<ProductBean> cartProducts = cart.getProducts();

        Map<Integer, ProductBean> uniqueProducts = new LinkedHashMap<>();

        Map<Integer, Integer> quantities = new LinkedHashMap<>();

        for (ProductBean product : cartProducts) {

            uniqueProducts.put(product.getCode(),product);

            quantities.merge(product.getCode(),1,Integer::sum);
        }

        try {

            int orderId = orderDao.saveOrder(
                    name.trim(),
                    surname.trim(),
                    address.trim(),
                    numberAddress,
                    mail,
                    uniqueProducts,
                    quantities
            );

            /*
             * Svuota il carrello solamente dopo
             * il salvataggio riuscito.
             */
            cart.getProducts().clear();

            session.setAttribute("cart", cart);

            response.sendRedirect(request.getContextPath() + "/common/welcome?orderSuccess="+ orderId);

        } catch (SQLException e) {

            e.printStackTrace();

            request.setAttribute("errors",List.of("Errore durante il salvataggio dell'ordine"));
            
            request.getRequestDispatcher("/WEB-INF/view/common/InformationOrder.jsp").forward(request, response);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    	doGet(request, response);
    }

    private boolean isBlank(String value) {
        return value == null || value.isBlank();
    }
}