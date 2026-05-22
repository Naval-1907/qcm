<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="fr">
<head>
    <meta charset="UTF-8" />
    <meta name="viewport" content="width=device-width, initial-scale=1.0" />
    <title>Gestion QCM - Accueil</title>
    <script src="https://cdn.tailwindcss.com"></script>
</head>
<body class="bg-slate-100 text-slate-800">
<div class="min-h-screen flex items-center justify-center p-6">
    <div class="max-w-3xl w-full bg-white rounded-3xl shadow-xl p-10">
        <h1 class="text-4xl font-bold mb-4">Gestion des Questionnaires</h1>
        <p class="text-slate-600 mb-8">Bienvenue dans votre application Java EE MVC avec JSP, Servlets et Tailwind CSS.</p>
        <div class="grid gap-4 md:grid-cols-2">
            <a href="main?action=dashboard" class="block rounded-3xl bg-blue-600 text-white text-center py-5 hover:bg-blue-700">Dashboard</a>
            <a href="main?action=gestion" class="block rounded-3xl bg-slate-800 text-white text-center py-5 hover:bg-slate-900">Gestion Étudiants & QCM</a>
            <a href="main?action=niveau" class="block rounded-3xl bg-emerald-600 text-white text-center py-5 hover:bg-emerald-700">Liste par Niveau</a>
            <a href="main?action=examen" class="block rounded-3xl bg-orange-500 text-white text-center py-5 hover:bg-orange-600">Examen</a>
            <a href="main?action=classement" class="block rounded-3xl bg-violet-600 text-white text-center py-5 hover:bg-violet-700">Classement</a>
        </div>
    </div>
</div>
</body>
</html>