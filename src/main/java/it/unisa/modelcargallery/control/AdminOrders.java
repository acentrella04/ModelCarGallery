package it.unisa.modelcargallery.control;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Collection;

import javax.sql.DataSource;

import it.unisa.modelcargallery.dao.OrderDao;
import it.unisa.modelcargallery.dao.OrderDaoImpl;
import it.unisa.modelcargallery.model.OrderBean;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet("/admin/orders")
public class AdminOrders extends HttpServlet {

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
    protected void doGet(
            HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {

        String from =
            request.getParameter("from");

        String to =
            request.getParameter("to");

        String mail =
            request.getParameter("mail");

        try {
            Collection<OrderBean> orders =
                orderDao.doRetrieveAll(
                    from,
                    to,
                    mail
                );

            request.setAttribute(
                "orders",
                orders
            );

            RequestDispatcher dispatcher =
                request.getRequestDispatcher(
                    "/WEB-INF/view/admin/orders.jsp"
                );

            dispatcher.forward(
                request,
                response
            );

        } catch (SQLException e) {

            throw new ServletException(
                "Errore nel recupero degli ordini",
                e
            );
        }
    }
}