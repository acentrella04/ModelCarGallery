package it.unisa.modelcargallery.control;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import java.util.Locale;

import javax.sql.DataSource;

import it.unisa.modelcargallery.dao.ProductDao;
import it.unisa.modelcargallery.dao.ProductDaoImpl;
import it.unisa.modelcargallery.model.CartBean;
import it.unisa.modelcargallery.model.ProductBean;
import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@WebServlet("/cart-ajax")
public class CartAjaxServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    private ProductDao productDao;

    @Override
    public void init(ServletConfig config)
            throws ServletException {

        super.init(config);

        DataSource ds =
                (DataSource) getServletContext()
                        .getAttribute("DataSource");

        if (ds == null) {
            throw new ServletException(
                    "DataSource non disponibile nel contesto"
            );
        }

        productDao = new ProductDaoImpl(ds);
    }

    @Override
    protected void doPost(
            HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");

        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        HttpSession session =
                request.getSession();

        CartBean cart =
                (CartBean) session.getAttribute("cart");

        if (cart == null) {
            cart = new CartBean();
            session.setAttribute("cart", cart);
        }

        String action =
                request.getParameter("action");

        if (action == null ||
                action.trim().isEmpty()) {

            sendError(
                    response,
                    HttpServletResponse.SC_BAD_REQUEST,
                    "Azione del carrello non specificata"
            );

            return;
        }

        try {

            if ("addC".equalsIgnoreCase(action)) {

                int code = readProductCode(request);

                ProductBean product =
                        productDao.doRetrieveByKey(code);

                /*
                 * Nel tuo ProductDaoImpl, se il prodotto non esiste,
                 * viene restituito un ProductBean con code uguale a 0.
                 */
                if (product == null ||
                        product.getCode() == 0) {

                    sendError(
                            response,
                            HttpServletResponse.SC_NOT_FOUND,
                            "Prodotto non trovato"
                    );

                    return;
                }

                cart.addProduct(product);

            } else if ("deleteC".equalsIgnoreCase(action)) {

                int code = readProductCode(request);

                cart.deleteOneProduct(code);

            } else if ("removeAllC".equalsIgnoreCase(action)) {

                int code = readProductCode(request);

                cart.deleteAllProducts(code);

            } else if ("clearC".equalsIgnoreCase(action)) {

                cart.clearCart();

            } else {

                sendError(
                        response,
                        HttpServletResponse.SC_BAD_REQUEST,
                        "Azione del carrello non valida"
                );

                return;
            }

            session.setAttribute("cart", cart);

            writeCartJson(response, cart);

        } catch (NumberFormatException e) {

            sendError(
                    response,
                    HttpServletResponse.SC_BAD_REQUEST,
                    "Codice prodotto non valido"
            );

        } catch (SQLException e) {

            throw new ServletException(
                    "Errore durante la gestione AJAX del carrello",
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
                HttpServletResponse.SC_METHOD_NOT_ALLOWED,
                "Usare il metodo POST"
        );
    }

    private int readProductCode(
            HttpServletRequest request) {

        String code =
                request.getParameter("code");

        if (code == null ||
                code.trim().isEmpty()) {

            throw new NumberFormatException(
                    "Codice prodotto assente"
            );
        }

        return Integer.parseInt(code);
    }

    private void writeCartJson(
            HttpServletResponse response,
            CartBean cart)
            throws IOException {

        List<ProductBean> distinctProducts =
                cart.getDistinctProducts();

        StringBuilder json =
                new StringBuilder();

        json.append("{");

        json.append("\"success\":true,");

        json.append("\"count\":");
        json.append(cart.getProducts().size());
        json.append(",");

        json.append("\"total\":");
        json.append(formatNumber(cart.getTotal()));
        json.append(",");

        json.append("\"items\":[");

        for (int i = 0;
             i < distinctProducts.size();
             i++) {

            ProductBean product =
                    distinctProducts.get(i);

            int quantity =
                    cart.getQuantity(
                            product.getCode()
                    );

            float subtotal =
                    product.getPrice() * quantity;

            json.append("{");

            json.append("\"code\":");
            json.append(product.getCode());
            json.append(",");

            json.append("\"name\":\"");
            json.append(
                    escapeJson(product.getName())
            );
            json.append("\",");

            json.append("\"price\":");
            json.append(
                    formatNumber(product.getPrice())
            );
            json.append(",");

            json.append("\"quantity\":");
            json.append(quantity);
            json.append(",");

            json.append("\"subtotal\":");
            json.append(formatNumber(subtotal));

            json.append("}");

            if (i < distinctProducts.size() - 1) {
                json.append(",");
            }
        }

        json.append("]");

        json.append("}");

        response.getWriter().write(
                json.toString()
        );
    }

    private void sendError(
            HttpServletResponse response,
            int status,
            String message)
            throws IOException {

        response.setStatus(status);

        String json =
                "{"
                + "\"success\":false,"
                + "\"message\":\""
                + escapeJson(message)
                + "\""
                + "}";

        response.getWriter().write(json);
    }

    private String formatNumber(float number) {

        return String.format(
                Locale.US,
                "%.2f",
                number
        );
    }

    private String escapeJson(String value) {

        if (value == null) {
            return "";
        }

        return value
                .replace("\\", "\\\\")
                .replace("\"", "\\\"")
                .replace("\b", "\\b")
                .replace("\f", "\\f")
                .replace("\n", "\\n")
                .replace("\r", "\\r")
                .replace("\t", "\\t");
    }
}
