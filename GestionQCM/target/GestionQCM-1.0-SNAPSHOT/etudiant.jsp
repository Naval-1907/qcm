<%@ page language="java" contentType="text/html; charset=UTF-8"%>

<!DOCTYPE html>

<html>

<head>

<meta charset="UTF-8">

<title>Etudiant</title>

</head>

<body>

<h1>Ajouter Etudiant</h1>

<form action="addEtudiant" method="post">

Numero :
<input type="text" name="num">

<br><br>

Nom :
<input type="text" name="nom">

<br><br>

Prenoms :
<input type="text" name="prenoms">

<br><br>

Niveau :

<select name="niveau">

<option>L1</option>

<option>L2</option>

<option>L3</option>

<option>M1</option>

<option>M2</option>

</select>

<br><br>

Email :
<input type="email" name="email">

<br><br>

<button type="submit">

Ajouter

</button>

</form>

</body>

</html>