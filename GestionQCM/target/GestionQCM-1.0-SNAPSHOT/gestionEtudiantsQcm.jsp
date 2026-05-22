<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html lang="fr">
<head>
    <meta charset="UTF-8" />
    <meta name="viewport" content="width=device-width, initial-scale=1.0" />
    <title>Gestion Étudiants & QCM - Gestion QCM</title>
    <script src="https://cdn.tailwindcss.com"></script>
</head>
<body class="bg-slate-100 text-slate-800">
<div class="min-h-screen">
    <div id="toast-container" class="fixed top-6 right-6 z-50 flex flex-col gap-4"></div>
    <div class="flex">
        <jsp:include page="sidebar.jsp" />
        <main class="flex-1 p-8 space-y-8">
            <div>
                <h1 class="text-3xl font-bold">Gestion Étudiants & QCM</h1>
                <p class="text-slate-600 mt-2">Gérez les étudiants, les questions et utilisez la recherche pour filtrer rapidement.</p>
            </div>

            <div class="grid gap-6 lg:grid-cols-2">
                <div class="rounded-3xl bg-white shadow p-6 border border-slate-200">
                    <h2 class="text-xl font-semibold mb-4">Recherche rapide</h2>
                    <form action="main" method="get" class="space-y-4">
                        <input type="hidden" name="action" value="gestion" />
                        <label class="block text-slate-700">Numéro ou nom :</label>
                        <input type="text" name="searchQuery" value="${searchQuery}" placeholder="Ex : E001 ou Dubois" class="w-full rounded-2xl border border-slate-300 px-4 py-3 focus:outline-none focus:ring-2 focus:ring-blue-500" />
                        <button type="submit" class="w-full rounded-2xl bg-blue-600 text-white py-3 hover:bg-blue-700">Rechercher</button>
                    </form>
                </div>
                <div class="rounded-3xl bg-white shadow p-6 border border-slate-200">
                    <h2 class="text-xl font-semibold mb-4">Ajouter / modifier un étudiant</h2>
                    <form action="main" method="post" class="space-y-4">
                        <input type="hidden" name="action" value="saveEtudiant" />
                        <input type="hidden" name="actionType" value="${editEtudiant != null ? 'update' : 'create'}" />
                        <input type="hidden" name="originalNumEtudiant" value="${editEtudiant.numEtudiant}" />
                        <div>
                            <label class="block text-slate-700">Numéro étudiant</label>
                            <input type="text" name="num_etudiant" value="${editEtudiant.numEtudiant}" required class="w-full rounded-2xl border border-slate-300 px-4 py-3" />
                        </div>
                        <div>
                            <label class="block text-slate-700">Nom</label>
                            <input type="text" name="nom" value="${editEtudiant.nom}" required class="w-full rounded-2xl border border-slate-300 px-4 py-3" />
                        </div>
                        <div>
                            <label class="block text-slate-700">Prénoms</label>
                            <input type="text" name="prenoms" value="${editEtudiant.prenoms}" required class="w-full rounded-2xl border border-slate-300 px-4 py-3" />
                        </div>
                        <div>
                            <label class="block text-slate-700">Niveau</label>
                            <select name="niveau" required class="w-full rounded-2xl border border-slate-300 px-4 py-3">
                                <option value="">Sélectionner</option>
                                <c:forEach items="${niveaux}" var="niv">
                                    <option value="${niv}" <c:if test="${editEtudiant.niveau == niv}">selected</c:if>>${niv}</option>
                                </c:forEach>
                            </select>
                        </div>
                        <div>
                            <label class="block text-slate-700">Email</label>
                            <input type="email" name="adr_email" value="${editEtudiant.adrEmail}" required class="w-full rounded-2xl border border-slate-300 px-4 py-3" />
                        </div>
                        <button type="submit" class="w-full rounded-2xl bg-emerald-600 text-white py-3 hover:bg-emerald-700">${editEtudiant == null ? "Ajouter" : "Modifier"}</button>
                    </form>
                </div>
            </div>

            <div class="grid gap-6 lg:grid-cols-2">
                <div class="rounded-3xl bg-white shadow p-6 border border-slate-200">
                    <h2 class="text-xl font-semibold mb-4">Liste des étudiants</h2>
                    <div id="toast-trigger" data-toast-message="${successMessage}" data-toast-type="success" class="hidden"></div>
                    <div class="overflow-x-auto">
                        <table class="min-w-full divide-y divide-slate-200">
                            <thead class="bg-slate-50">
                                <tr>
                                    <th class="px-4 py-3 text-left text-xs uppercase tracking-[0.18em]">#</th>
                                    <th class="px-4 py-3 text-left text-xs uppercase tracking-[0.18em]">Nom</th>
                                    <th class="px-4 py-3 text-left text-xs uppercase tracking-[0.18em]">Prénoms</th>
                                    <th class="px-4 py-3 text-left text-xs uppercase tracking-[0.18em]">Niveau</th>
                                    <th class="px-4 py-3 text-left text-xs uppercase tracking-[0.18em]">Email</th>
                                    <th class="px-4 py-3 text-left text-xs uppercase tracking-[0.18em]">Actions</th>
                                </tr>
                            </thead>
                            <tbody class="divide-y divide-slate-100">
                                <c:forEach items="${etudiants}" var="etu">
                                    <tr>
                                        <td class="px-4 py-3">${etu.numEtudiant}</td>
                                        <td class="px-4 py-3">${etu.nom}</td>
                                        <td class="px-4 py-3">${etu.prenoms}</td>
                                        <td class="px-4 py-3">${etu.niveau}</td>
                                        <td class="px-4 py-3">${etu.adrEmail}</td>
                                        <td class="px-4 py-3 space-x-2">
                                            <a href="main?action=editEtudiant&num_etudiant=${etu.numEtudiant}" class="inline-flex rounded-full bg-blue-600 text-white px-3 py-1 text-xs">Modifier</a>
                                            <a href="#" data-delete-action="deleteEtudiant" data-delete-param="num_etudiant" data-delete-value="${etu.numEtudiant}" data-delete-name="${etu.nom} ${etu.prenoms}" class="delete-button inline-flex rounded-full bg-red-600 text-white px-3 py-1 text-xs">Supprimer</a>
                                        </td>
                                    </tr>
                                </c:forEach>
                                <c:if test="${empty etudiants}">
                                    <tr><td colspan="6" class="px-4 py-6 text-center text-slate-500">Aucun étudiant trouvé.</td></tr>
                                </c:if>
                            </tbody>
                        </table>
                    </div>
                </div>
                <div class="rounded-3xl bg-white shadow p-6 border border-slate-200">
                    <h2 class="text-xl font-semibold mb-4">Liste des QCM</h2>
                    <form action="main" method="post" class="space-y-4 mb-6">
                        <input type="hidden" name="action" value="saveQcm" />
                        <input type="hidden" name="actionType" value="${editQcm != null ? 'update' : 'create'}" />
                        <input type="hidden" name="originalNumQuest" value="${editQcm.numQuest}" />
                        <div>
                            <label class="block text-slate-700">Numéro question</label>
                            <input type="text" name="num_quest" value="${editQcm.numQuest}" required class="w-full rounded-2xl border border-slate-300 px-4 py-3" />
                        </div>
                        <div>
                            <label class="block text-slate-700">Question</label>
                            <textarea name="question" required class="w-full rounded-2xl border border-slate-300 px-4 py-3" rows="3">${editQcm.question}</textarea>
                        </div>
                        <div class="grid gap-4 md:grid-cols-2">
                            <div>
                                <label class="block text-slate-700">Réponse 1</label>
                                <input type="text" name="reponse1" value="${editQcm.reponse1}" required class="w-full rounded-2xl border border-slate-300 px-4 py-3" />
                            </div>
                            <div>
                                <label class="block text-slate-700">Réponse 2</label>
                                <input type="text" name="reponse2" value="${editQcm.reponse2}" required class="w-full rounded-2xl border border-slate-300 px-4 py-3" />
                            </div>
                            <div>
                                <label class="block text-slate-700">Réponse 3</label>
                                <input type="text" name="reponse3" value="${editQcm.reponse3}" required class="w-full rounded-2xl border border-slate-300 px-4 py-3" />
                            </div>
                            <div>
                                <label class="block text-slate-700">Réponse 4</label>
                                <input type="text" name="reponse4" value="${editQcm.reponse4}" required class="w-full rounded-2xl border border-slate-300 px-4 py-3" />
                            </div>
                        </div>
                        <div>
                            <label class="block text-slate-700">Bonne réponse</label>
                            <select name="bonne_reponse" required class="w-full rounded-2xl border border-slate-300 px-4 py-3">
                                <option value="">Sélectionner</option>
                                <option value="${editQcm.reponse1}">Réponse 1</option>
                                <option value="${editQcm.reponse2}">Réponse 2</option>
                                <option value="${editQcm.reponse3}">Réponse 3</option>
                                <option value="${editQcm.reponse4}">Réponse 4</option>
                            </select>
                        </div>
                        <button type="submit" class="w-full rounded-2xl bg-slate-800 text-white py-3 hover:bg-slate-900">${editQcm == null ? "Ajouter" : "Modifier"}</button>
                    </form>
                    <div class="overflow-x-auto">
                        <table class="min-w-full divide-y divide-slate-200">
                            <thead class="bg-slate-50">
                                <tr>
                                    <th class="px-4 py-3 text-left text-xs uppercase tracking-[0.18em]">#</th>
                                    <th class="px-4 py-3 text-left text-xs uppercase tracking-[0.18em]">Question</th>
                                    <th class="px-4 py-3 text-left text-xs uppercase tracking-[0.18em]">Bonne réponse</th>
                                    <th class="px-4 py-3 text-left text-xs uppercase tracking-[0.18em]">Actions</th>
                                </tr>
                            </thead>
                            <tbody class="divide-y divide-slate-100">
                                <c:forEach items="${qcms}" var="q">
                                    <tr>
                                        <td class="px-4 py-3">${q.numQuest}</td>
                                        <td class="px-4 py-3">${q.question}</td>
                                        <td class="px-4 py-3">${q.bonneReponse}</td>
                                        <td class="px-4 py-3 space-x-2">
                                            <a href="main?action=editQcm&num_quest=${q.numQuest}" class="inline-flex rounded-full bg-blue-600 text-white px-3 py-1 text-xs">Modifier</a>
                                            <a href="#" data-delete-action="deleteQcm" data-delete-param="num_quest" data-delete-value="${q.numQuest}" data-delete-name="${q.question}" class="delete-button inline-flex rounded-full bg-red-600 text-white px-3 py-1 text-xs">Supprimer</a>
                                        </td>
                                    </tr>
                                </c:forEach>
                                <c:if test="${empty qcms}">
                                    <tr><td colspan="4" class="px-4 py-6 text-center text-slate-500">Aucun QCM disponible.</td></tr>
                                </c:if>
                            </tbody>
                        </table>
                    </div>
                </div>
            </div>
        </main>
    </div>
</div>
<div id="confirm-modal" class="fixed inset-0 z-50 hidden items-center justify-center bg-black/40 px-4">
    <div class="w-full max-w-md rounded-3xl bg-white p-6 shadow-2xl">
        <h3 class="text-xl font-semibold mb-3">Confirmer la suppression</h3>
        <p class="text-slate-600 mb-6">Voulez-vous vraiment supprimer <span id="delete-item-name" class="font-semibold"></span> ? Cette action est irréversible.</p>
        <form id="confirm-delete-form" action="main" method="post" class="space-y-4">
            <input type="hidden" name="action" value="" />
            <input type="hidden" name="deleteParam" value="" />
            <input type="hidden" name="deleteValue" value="" />
            <div class="flex justify-end gap-3">
                <button type="button" id="cancel-delete" class="rounded-2xl border border-slate-300 px-5 py-2 text-slate-700 hover:bg-slate-100">Annuler</button>
                <button type="submit" class="rounded-2xl bg-red-600 px-5 py-2 text-white hover:bg-red-700">Supprimer</button>
            </div>
        </form>
    </div>
</div>
<script>
    function showToast(message, type) {
        if (!message) return;
        const container = document.getElementById('toast-container');
        if (!container) return;
        const toast = document.createElement('div');
        const colors = {
            success: 'bg-emerald-600 text-white',
            error: 'bg-red-600 text-white',
            info: 'bg-slate-800 text-white'
        };
        toast.className = `max-w-sm rounded-3xl p-4 shadow-2xl ring-1 ring-black/10 transform transition-all duration-500 ease-out ${colors[type] || colors.info}`;
        toast.style.opacity = '0';
        toast.style.transform = 'translateY(-20px)';
        toast.innerHTML = `<p class="font-semibold">${type === 'error' ? 'Erreur' : type === 'success' ? 'Succès' : 'Info'}</p><p class="mt-2 text-sm">${message}</p>`;
        container.appendChild(toast);
        requestAnimationFrame(() => {
            toast.style.opacity = '1';
            toast.style.transform = 'translateY(0)';
        });
        setTimeout(() => {
            toast.style.opacity = '0';
            toast.style.transform = 'translateY(-20px)';
            toast.addEventListener('transitionend', () => toast.remove(), { once: true });
        }, 4000);
    }

    function openDeleteModal(action, param, value, name) {
        const modal = document.getElementById('confirm-modal');
        const itemName = document.getElementById('delete-item-name');
        const form = document.getElementById('confirm-delete-form');
        if (!modal || !itemName || !form) return;
        itemName.textContent = name;
        form.querySelector('input[name="action"]').value = action;
        form.querySelector('input[name="deleteParam"]').value = param;
        form.querySelector('input[name="deleteValue"]').value = value;
        modal.classList.remove('hidden');
    }

    function closeDeleteModal() {
        const modal = document.getElementById('confirm-modal');
        if (modal) modal.classList.add('hidden');
    }

    document.addEventListener('DOMContentLoaded', function () {
        const trigger = document.getElementById('toast-trigger');
        if (trigger && trigger.dataset.toastMessage) {
            showToast(trigger.dataset.toastMessage, trigger.dataset.toastType || 'success');
        }
        document.querySelectorAll('.delete-button').forEach(button => {
            button.addEventListener('click', function (event) {
                event.preventDefault();
                openDeleteModal(
                    event.currentTarget.dataset.deleteAction,
                    event.currentTarget.dataset.deleteParam,
                    event.currentTarget.dataset.deleteValue,
                    event.currentTarget.dataset.deleteName
                );
            });
        });
        const cancelButton = document.getElementById('cancel-delete');
        if (cancelButton) {
            cancelButton.addEventListener('click', closeDeleteModal);
        }
    });
</script>
</body>
</html>
