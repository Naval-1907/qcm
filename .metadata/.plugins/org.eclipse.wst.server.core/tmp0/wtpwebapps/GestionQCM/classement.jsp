<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html lang="fr">
<head>
    <meta charset="UTF-8" />
    <meta name="viewport" content="width=device-width, initial-scale=1.0" />
    <title>Classement - Gestion QCM</title>
    <script src="https://cdn.tailwindcss.com"></script>
</head>
<body class="bg-slate-100 text-slate-800">
<div class="min-h-screen">
    <div class="flex">
        <jsp:include page="sidebar.jsp" />
        <main class="flex-1 p-8">
            <div class="mb-6">
                <h1 class="text-3xl font-bold">Classement par mérite</h1>
                <p class="text-slate-600 mt-2">Affiche les résultats des examens triés par note décroissante.</p>
            </div>
            <div class="rounded-3xl bg-white shadow p-8 border border-slate-200">
                <div class="flex flex-col md:flex-row md:items-center md:justify-between mb-6">
                    <div>
                        <h2 class="text-xl font-semibold">Classement des examens</h2>
                        <p class="text-slate-500">Tri automatique par note décroissante.</p>
                    </div>
                    <a href="main?action=classement" class="mt-4 md:mt-0 inline-flex rounded-2xl bg-blue-600 text-white px-5 py-3 hover:bg-blue-700">Actualiser</a>
                </div>
                <div class="overflow-x-auto">
                    <table class="min-w-full divide-y divide-slate-200">
                        <thead class="bg-slate-50">
                            <tr>
                                <th class="px-4 py-3 text-left text-xs uppercase tracking-[0.18em]">Position</th>
                                <th class="px-4 py-3 text-left text-xs uppercase tracking-[0.18em]">Étudiant</th>
                                <th class="px-4 py-3 text-left text-xs uppercase tracking-[0.18em]">Niveau</th>
                                <th class="px-4 py-3 text-left text-xs uppercase tracking-[0.18em]">Note</th>
                                <th class="px-4 py-3 text-left text-xs uppercase tracking-[0.18em]">Année</th>
                            </tr>
                        </thead>
                        <tbody class="divide-y divide-slate-100">
                            <c:forEach items="${classementList}" var="result" varStatus="stat">
                                <tr>
                                    <td class="px-4 py-3 font-semibold">${stat.index + 1}</td>
                                    <td class="px-4 py-3">${result.nom} ${result.prenoms}</td>
                                    <td class="px-4 py-3">${result.niveau}</td>
                                    <td class="px-4 py-3">${result.note}</td>
                                    <td class="px-4 py-3">${result.anneeUniv}</td>
                                </tr>
                            </c:forEach>
                            <c:if test="${empty classementList}">
                                <tr><td colspan="5" class="px-4 py-6 text-center text-slate-500">Aucun examen enregistré pour le moment.</td></tr>
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
