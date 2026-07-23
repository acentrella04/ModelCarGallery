<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"
	import="java.util.*,it.unisa.modelcargallery.model.*" %>

	<!DOCTYPE html>
	<html lang="it">

	<head>

		<meta charset="UTF-8">

		<meta name="viewport" content="width=device-width, initial-scale=1">

		<title>Dettagli ordine</title>

		<link rel="stylesheet" href="<%=request.getContextPath()%>/styles/forms.css">

		<link rel="stylesheet" href="<%=request.getContextPath()%>/styles/responsive.css">

		<script src="<%=request.getContextPath()%>/scripts/validation.js" defer>
		</script>

	</head>

	<body data-context-path="<%=request.getContextPath()%>">

		<% List<?> errors =
			(List
			<?>) request.getAttribute("errors");

if (errors != null && !errors.isEmpty()) {
%>

<div class="form-message form-message-error">

    <%
    for (Object error : errors) {
    %>

    <p><%=error%></p>

    <%
    }
    %>

</div>

<%
}

CartBean cart =
        (CartBean) session.getAttribute("cart");

if (cart == null) {
    cart = new CartBean();
    session.setAttribute("cart", cart);
}

boolean cartIsEmpty =
        cart.getProducts().isEmpty();

List<ProductBean> distinctProducts =
        cart.getDistinctProducts();
%>


<main>


    <section class="order-card">

        <h1>Riepilogo ordine</h1>

        <%
        if (!cartIsEmpty) {
        %>

        <div class="table-wrapper">

            <table>

                <thead>

                    <tr>
                        <th>Prodotto</th>
                        <th>Prezzo unitario</th>
                        <th>Quantità</th>
                        <th>Subtotale</th>
                    </tr>

                </thead>

                <tbody>

                <%
                for (ProductBean product : distinctProducts) {

                    int quantity =
                            cart.getQuantity(
                                    product.getCode()
                            );

                    float rowSubtotal =
                            product.getPrice() * quantity;
                %>

                    <tr>

                        <td>
                            <%=product.getName()%>
                        </td>

                        <td>
                            € <%=String.format(
                                    Locale.US,
                                    "%.2f",
                                    product.getPrice()
                            )%>
                        </td>

                        <td>
                            <%=quantity%>
                        </td>

                        <td>
                            € <%=String.format(
                                    Locale.US,
                                    "%.2f",
                                    rowSubtotal
                            )%>
                        </td>

                    </tr>

                <%
                }
                %>

                </tbody>

            </table>

        </div>

        <h2>
            Totale:
            € <%=String.format(
                    Locale.US,
                    "%.2f",
                    cart.getTotal()
            )%>
        </h2>

        <%
        } else {
        %>

        <p>Il carrello è vuoto.</p>

        <a href="<%=request.getContextPath()%>/common/welcome">
            Torna al catalogo
        </a>

        <%
        }
        %>

    </section>


    <%
    if (!cartIsEmpty) {
    %>

    <form id="orderForm"
          method="post"
          action="<%=request.getContextPath()%>/ProcessOrder"
          novalidate>

        <h2>Dati di spedizione</h2>


        <label for="nameSpace">
            Nome:
        </label>

        <input type="text"
               id="nameSpace"
               name="name"
               maxlength="30"
               autocomplete="given-name"
               placeholder="Mario"
               required>


        <label for="surnameSpace">
            Cognome:
        </label>

        <input type="text"
               id="surnameSpace"
               name="surname"
               maxlength="30"
               autocomplete="family-name"
               placeholder="Rossi"
               required>

        <label for="addressSpace">
            Indirizzo:
        </label>

        <input type="text"
               id="addressSpace"
               name="address"
               maxlength="80"
               autocomplete="street-address"
               placeholder="Via Roma"
               required>

        <label for="numberSpace">
            Numero civico:
        </label>

        <input type="number"
               id="numberSpace"
               name="number"
               min="1"
               max="99999"
               autocomplete="address-line2"
               placeholder="14"
               required>


     
        <fieldset id="paymentSection">

            <legend>Dati di pagamento</legend>

            <input type="hidden"
                   name="paymentMethod"
                   value="Carta">


            <div class="form-group">

                <label for="nameCardSpace">
                    Intestatario della carta:
                </label>

                <input type="text"
                       id="nameCardSpace"
                       name="nameCard"
                       maxlength="60"
                       autocomplete="cc-name"
                       placeholder="Mario Rossi"
                       required>

            </div>


            <div class="form-group">

                <label for="numberCardSpace">
                    Numero della carta:
                </label>

                <input type="text"
                       id="numberCardSpace"
                       name="numberCard"
                       maxlength="23"
                       inputmode="numeric"
                       autocomplete="cc-number"
                       placeholder="1234 5678 9012 3456"
                       required>

            </div>


            <div class="payment-row">


                <div class="form-group">

                    <label for="expireSpace">
                        Scadenza:
                    </label>

                    <input type="text"
                           id="expireSpace"
                           name="expire"
                           maxlength="5"
                           inputmode="numeric"
                           autocomplete="cc-exp"
                           placeholder="MM/AA"
                           required>

                </div>


                <div class="form-group">

                    <label for="cvvSpace">
                        CVV:
                    </label>

                    <input type="password"
                           id="cvvSpace"
                           name="CVV"
                           maxlength="4"
                           inputmode="numeric"
                           autocomplete="cc-csc"
                           placeholder="123"
                           required>

                </div>

            </div>

        </fieldset>


        <input type="submit"id="processOrder"value="Conferma ordine">

    </form>

    <p style="text-align: center;">

        <a href="<%=request.getContextPath()%>/common/welcome">
            Torna al carrello
        </a>

    </p>

    <%
    }
    %>

</main>

</body>

</html>