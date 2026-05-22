<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html lang="fr">
<head>
    <meta charset="UTF-8" />
    <meta name="viewport" content="width=device-width, initial-scale=1.0" />
    <title>Dashboard - Gestion QCM</title>
    <script src="https://cdn.tailwindcss.com"></script>
</head>
<body class="bg-slate-100 text-slate-800">
<div class="min-h-screen">
    <div class="flex">
        <jsp:include page="sidebar.jsp" />
        <main class="flex-1 p-8">
            <div class="mb-8">
                <h1 class="text-3xl font-bold">Dashboard</h1>
                <p class="text-slate-600 mt-2">Vue synthétique des statistiques et des indicateurs clés de l'application.</p>
            </div>
            <div class="grid gap-6 md:grid-cols-3">
                <div class="rounded-3xl bg-white shadow p-6 border border-slate-200">
                    <h2 class="text-slate-500 uppercase tracking-[0.2em] text-xs">Total Étudiants</h2>
                    <p class="mt-4 text-5xl font-bold text-slate-900"><c:out value="${totalEtudiants}"/></p>
                </div>
                <div class="rounded-3xl bg-white shadow p-6 border border-slate-200">
                    <h2 class="text-slate-500 uppercase tracking-[0.2em] text-xs">Total QCM</h2>
                    <p class="mt-4 text-5xl font-bold text-slate-900"><c:out value="${totalQcm}"/></p>
                </div>
                <div class="rounded-3xl bg-white shadow p-6 border border-slate-200">
                    <h2 class="text-slate-500 uppercase tracking-[0.2em] text-xs">Moyenne des examens</h2>
                    <p class="mt-4 text-5xl font-bold text-slate-900"><c:out value="${moyenneExam}"/></p>
                </div>
            </div>
            <section class="mt-10 bg-white rounded-3xl shadow p-6 border border-slate-200">
                <h2 class="text-xl font-semibold mb-4">Guide rapide</h2>
                <p class="text-slate-600">Utilisez le menu de navigation pour gérer les étudiants, les QCM, les listes par niveau, les examens et le classement des résultats. Les données sont gérées en mémoire via un DataStore mock.</p>
            </section>
        </main>
    </div>
</div>
</body>
</html>
