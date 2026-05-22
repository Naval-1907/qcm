<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html lang="fr">
<head>
    <meta charset="UTF-8" />
    <meta name="viewport" content="width=device-width, initial-scale=1.0" />
    <title>Liste par Niveau - Gestion QCM</title>
    <script src="https://cdn.tailwindcss.com"></script>
</head>
<body class="bg-slate-100 text-slate-800">
<div class="min-h-screen">
    <div class="flex">
        <jsp:include page="sidebar.jsp" />
        <main class="flex-1 p-8">
            <div class="mb-6">
                <h1 class="text-3xl font-bold">Liste par Niveau</h1>
                <p class="text-slate-600 mt-2">Filtrez les étudiants par niveau et consultez l'effectif total par niveau.</p>
            </div>
            <div class="grid gap-6 lg:grid-cols-2">
                <div class="rounded-3xl bg-white shadow p-6 border border-slate-200">
                    <form action="main" method="get" class="space-y-4">
                        <input type="hidden" name="action" value="niveau" />
                        <label class="block text-slate-700">Sélectionnez un niveau</label>
                        <select name="niveau" class="w-full rounded-2xl border border-slate-300 px-4 py-3" onchange="this.form.submit()">
                            <c:forEach items="${niveaux}" var="niv">
                                <option value="${niv}" <c:if test="${selectedLevel == niv}">selected</c:if>>${niv}</option>
                            </c:forEach>
                        </select>
                    </form>
                </div>
                <div class="rounded-3xl bg-white shadow p-6 border border-slate-200">
                    <h2 class="text-xl font-semibold">Effectif total</h2>
                    <p class="mt-4 text-5xl font-bold text-slate-900"><c:out value="${effectif}"/></p>
                    <p class="text-slate-500 mt-2">Étudiants inscrits en <strong>${selectedLevel}</strong>.</p>
                </div>
            </div>
            <div class="mt-8 rounded-3xl bg-white shadow p-6 border border-slate-200">
                <h2 class="text-xl font-semibold mb-4">Étudiants du niveau ${selectedLevel}</h2>
                <div class="overflow-x-auto">
                    <table class="min-w-full divide-y divide-slate-200">
                        <thead class="bg-slate-50">
                            <tr>
                                <th class="px-4 py-3 text-left text-xs uppercase tracking-[0.18em]">#</th>
                                <th class="px-4 py-3 text-left text-xs uppercase tracking-[0.18em]">Nom</th>
                                <th class="px-4 py-3 text-left text-xs uppercase tracking-[0.18em]">Prénoms</th>
                                <th class="px-4 py-3 text-left text-xs uppercase tracking-[0.18em]">Email</th>
                            </tr>
                        </thead>
                        <tbody class="divide-y divide-slate-100">
                            <c:forEach items="${filteredEtudiants}" var="etu">
                                <tr>
                                    <td class="px-4 py-3">${etu.numEtudiant}</td>
                                    <td class="px-4 py-3">${etu.nom}</td>
                                    <td class="px-4 py-3">${etu.prenoms}</td>
                                    <td class="px-4 py-3">${etu.adrEmail}</td>
                                </tr>
                            </c:forEach>
                            <c:if test="${empty filteredEtudiants}">
                                <tr><td colspan="4" class="px-4 py-6 text-center text-slate-500">Aucun étudiant pour ce niveau.</td></tr>
                            </c:if>
                        </tbody>
                    </table>
                </div>
            </div>
        </main>
    </div>
</div>
</body>
</html>
