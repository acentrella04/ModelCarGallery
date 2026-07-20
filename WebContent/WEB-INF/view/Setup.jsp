<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" import="java.util.*" %>

	<!DOCTYPE html>
	<html lang="it">

	<head>

		<meta charset="UTF-8">

		<meta name="viewport" content="width=device-width, initial-scale=1">

		<title>Registrazione</title>

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

Object mailValue =
        request.getAttribute("mailValue");
%>


<main>

    <form id="registrationForm"
          action="<%=request.getContextPath()%>/regServlet"
          method="post"
          novalidate>

        <fieldset>

            <legend>
                Registrazione
            </legend>

            <h1>Crea il tuo account</h1>


            <!-- EMAIL -->

            <label for="mailSpace">
                Email:
            </label>

            <input type="email"
                   id="mailSpace"
                   name="mail"
                   maxlength="100"
                   autocomplete="email"
                   placeholder="nome@email.it"
                   value="<%=mailValue != null ? mailValue : ""%>"
                   required>

            <span id="emailAvailabilityMessage"
                  class="availability-message"
                  aria-live="polite">
            </span>


            <!-- PASSWORD -->

            <label for="pwdSpace">
                Password:
            </label>

            <input type="password"
                   id="pwdSpace"
                   name="pwd"
                   minlength="8"
                   maxlength="100"
                   autocomplete="new-password"
                   placeholder="Inserisci una password"
                   required>

            <p>
                La password deve contenere almeno 8 caratteri,
                una lettera maiuscola, una minuscola e un numero.
            </p>


            <!-- PULSANTI -->

            <div class="login-actions">

                <input type="submit"
                       value="Registrati">

                <a href="<%=request.getContextPath()%>/product"
                   id="regbutton">
                    Torna al catalogo
                </a>

            </div>

        </fieldset>

    </form>

</main>

</body>

</html>