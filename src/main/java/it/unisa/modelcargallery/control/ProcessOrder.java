package it.unisa.modelcargallery.control;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import it.unisa.modelcargallery.dao.OrderDao;
import it.unisa.modelcargallery.dao.OrderDaoImpl;
import it.unisa.modelcargallery.model.CartBean;
import it.unisa.modelcargallery.model.OrderBean;
import it.unisa.modelcargallery.model.OrderItemBean;
import it.unisa.modelcargallery.model.ProductBean;
import it.unisa.modelcargallery.model.UserBean;
import jakarta.servlet.RequestDispatcher;
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

        DataSource ds =
            (DataSource) getServletContext()
                .getAttribute("DataSource");

        if (ds == null) {
            throw new ServletException(
                "DataSource non disponibile"
            );
        }

        orderDao = new OrderDaoImpl(ds);
    }

    @Override
    protected void doPost(
            HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");

        HttpSession session =
            request.getSession(false);

        if (session == null) {
            response.sendRedirect(
                request.getContextPath() + "/product"
            );
            return;
        }

        UserBean user =
            (UserBean) session.getAttribute("user");

        String authToken =
            (String) session.getAttribute("authToken");

        if (user == null || authToken == null) {
            response.sendRedirect(
                request.getContextPath() +
                "/product?loginRequired=true"
            );
            return;
        }

        CartBean cart =
            (CartBean) session.getAttribute("cart");

        if (cart == null ||
                cart.getProducts() == null ||
                cart.getProducts().isEmpty()) {

            response.sendError(
                HttpServletResponse.SC_BAD_REQUEST,
                "Il carrello è vuoto"
            );

            return;
        }

        List<OrderItemBean> items =
            new ArrayList<OrderItemBean>();

        float total = 0;

        for (ProductBean product :
                cart.getProducts()) {

            total += product.getPrice();

            OrderItemBean itemTrovato = null;

            for (OrderItemBean item : items) {

                if (item.getProductCode() ==
                        product.getCode()) {

                    itemTrovato = item;
                    break;
                }
            }

            if (itemTrovato == null) {

                OrderItemBean nuovoItem =
                    new OrderItemBean();

                nuovoItem.setProductCode(
                    product.getCode()
                );

                nuovoItem.setProductName(
                    product.getName()
                );

                nuovoItem.setUnitPrice(
                    product.getPrice()
                );

                nuovoItem.setQuantity(1);

                items.add(nuovoItem);

            } else {

                itemTrovato.setQuantity(
                    itemTrovato.getQuantity() + 1
                );
            }
        }

        String name =
            request.getParameter("name");

        String surname =
            request.getParameter("surname");

        String address =
            request.getParameter("address");

        String number =
            request.getParameter("number");

        String paymentMethod =
            request.getParameter("paymentMethod");

        int numberAddress;

        try {
            numberAddress =
                Integer.parseInt(number);

        } catch (NumberFormatException e) {

            response.sendError(
                HttpServletResponse.SC_BAD_REQUEST,
                "Numero civico non valido"
            );

            return;
        }

        if (paymentMethod == null ||
                paymentMethod.isEmpty()) {

            paymentMethod = "NON_SPECIFICATO";
        }

        OrderBean order = new OrderBean();

        order.setUserId(
            user.getId()
        );

        order.setName(name);
        order.setSurname(surname);
        order.setAddress(address);
        order.setNumberAddress(numberAddress);

        order.setMail(
            user.getUsername()
        );

        order.setTotal(total);
        order.setPaymentMethod(paymentMethod);
        order.setItems(items);

        try {
            orderDao.doSave(order);

            // Il carrello viene svuotato solo
            // dopo il salvataggio dell'ordine
            cart.getProducts().clear();

            request.setAttribute(
                "order",
                order
            );

            RequestDispatcher dispatcher =
                request.getRequestDispatcher(
                    "/WEB-INF/view/Fattura.jsp"
                );

            dispatcher.forward(
                request,
                response
            );

        } catch (SQLException e) {

            throw new ServletException(
                "Errore durante il salvataggio dell'ordine",
                e
            );
        }
    }

    @Override
    protected void doGet(
            HttpServletRequest request,
            HttpServletResponse response)
            throws IOException {

        response.sendError(
            HttpServletResponse.SC_METHOD_NOT_ALLOWED
        );
    }
}