<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ page contentType="text/html; charset=UTF-8"
	import="java.util.*,it.unisa.modelcargallery.model.*"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Registrazione</title>
</head>
<body>
	<form method="post" action="regServlet">
		<fieldset>
			<div>
				<h1>Inserisci i tuoi dati</h1>
				Email:<input type="email" id="mailSpace" name="mail"><br>
				Password:<input type="password" id="pwdSpace" name="pwd"><br>
				Role:<br>
				User<input type="radio" name="role" value="user" checked="checked"><br>
				<input type="submit" value="Registrati">
			</div>
		</fieldset>
	</form>

</body>
</html>