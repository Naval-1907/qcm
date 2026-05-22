<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html lang="fr">
<head>
    <meta charset="UTF-8" />
    <meta name="viewport" content="width=device-width, initial-scale=1.0" />
    <title>Examen - Gestion QCM</title>
    <script src="https://cdn.tailwindcss.com"></script>
</head>
<body class="bg-slate-100 text-slate-800">
<div class="min-h-screen">
    <div id="toast-container" class="fixed top-6 right-6 z-50 flex flex-col gap-4"></div>
    <div class="flex">
        <jsp:include page="sidebar.jsp" />
        <main class="flex-1 p-8">
            <div class="mb-6">
                <h1 class="text-3xl font-bold">Examen</h1>
                <p class="text-slate-600 mt-2">Saisissez le numéro d'étudiant et l'année universitaire pour démarrer un examen de 10 questions.</p>
            </div>
            <div id="exam-toast-trigger" data-toast-message="${examMessage}" data-toast-type="info" class="hidden"></div>
            <c:if test="${not empty examMessage}">
                <div class="mb-6 rounded-3xl bg-amber-50 border border-amber-200 p-5 text-amber-900">${examMessage}</div>
            </c:if>
            <c:choose>
                <c:when test="${examStep == 'question'}">
                    <div class="rounded-3xl bg-white shadow p-8 border border-slate-200">
                        <div class="mb-6">
                            <div class="flex items-center justify-between text-sm font-medium text-slate-600 mb-2">
                                <span>Progression de l'examen</span>
                                <span>${questionNumber} / ${totalQuestions}</span>
                            </div>
                            <div class="h-3 rounded-full bg-slate-200 overflow-hidden">
                                <div id="exam-progress-bar" class="h-full rounded-full bg-gradient-to-r from-blue-500 to-cyan-500 transition-all duration-500"></div>
                            </div>
                        </div>
                        <h2 class="text-xl font-semibold mb-4">Question ${questionNumber} sur ${totalQuestions}</h2>
                        <p class="text-slate-700 mb-6"><strong><c:out value="${currentQuestion.question}"/></strong></p>
                        <form action="main" method="post" class="space-y-4">
                            <input type="hidden" name="action" value="answerQuestion" />
                            <div class="space-y-3">
                                <label class="block rounded-2xl border border-slate-200 p-4 bg-slate-50 cursor-pointer">
                                    <input type="radio" name="selectedAnswer" value="${currentQuestion.reponse1}" class="mr-3" required />
                                    ${currentQuestion.reponse1}
                                </label>
                                <label class="block rounded-2xl border border-slate-200 p-4 bg-slate-50 cursor-pointer">
                                    <input type="radio" name="selectedAnswer" value="${currentQuestion.reponse2}" class="mr-3" />
                                    ${currentQuestion.reponse2}
                                </label>
                                <label class="block rounded-2xl border border-slate-200 p-4 bg-slate-50 cursor-pointer">
                                    <input type="radio" name="selectedAnswer" value="${currentQuestion.reponse3}" class="mr-3" />
                                    ${currentQuestion.reponse3}
                                </label>
                                <label class="block rounded-2xl border border-slate-200 p-4 bg-slate-50 cursor-pointer">
                                    <input type="radio" name="selectedAnswer" value="${currentQuestion.reponse4}" class="mr-3" />
                                    ${currentQuestion.reponse4}
                                </label>
                            </div>
                            <button type="submit" class="rounded-2xl bg-blue-600 text-white px-6 py-3 hover:bg-blue-700">Suivant</button>
                        </form>
                    </div>
                </c:when>
                <c:when test="${examStep == 'finished'}">
                    <div class="rounded-3xl bg-white shadow p-8 border border-slate-200">
                        <h2 class="text-2xl font-semibold mb-4">Résultat de l'examen</h2>
                        <p class="text-slate-700 mb-6">${examResult}</p>
                        <a href="main?action=examen" class="inline-flex rounded-2xl bg-emerald-600 text-white px-6 py-3 hover:bg-emerald-700">Recommencer un examen</a>
                    </div>
                </c:when>
                <c:otherwise>
                    <div class="rounded-3xl bg-white shadow p-8 border border-slate-200 max-w-xl">
                        <form action="main" method="post" class="space-y-6">
                            <input type="hidden" name="action" value="startExam" />
                            <div>
                                <label class="block text-slate-700 mb-2">Numéro étudiant</label>
                                <input type="text" name="num_etudiant" required class="w-full rounded-2xl border border-slate-300 px-4 py-3" placeholder="Ex : E001" />
                            </div>
                            <div>
                                <label class="block text-slate-700 mb-2">Année universitaire</label>
                                <input type="text" name="annee_univ" required class="w-full rounded-2xl border border-slate-300 px-4 py-3" placeholder="2022-2023" />
                            </div>
                            <button type="submit" class="rounded-2xl bg-blue-600 text-white px-6 py-3 hover:bg-blue-700">Démarrer l'examen</button>
                        </form>
                    </div>
                </c:otherwise>
            </c:choose>
        </main>
    </div>
</div>

<script>
    function showToast(message, type) {
        if (!message) return;
        const container = document.getElementById('toast-container');
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

    document.addEventListener('DOMContentLoaded', function () {
        const trigger = document.getElementById('exam-toast-trigger');
        if (trigger && trigger.dataset.toastMessage) {
            showToast(trigger.dataset.toastMessage, trigger.dataset.toastType || 'info');
        }
        const progressBar = document.getElementById('exam-progress-bar');
        if (progressBar) {
            const questionNumber = parseInt('${questionNumber}', 10) || 0;
            const totalQuestions = parseInt('${totalQuestions}', 10) || 10;
            const percent = Math.min(100, Math.max(0, Math.round((questionNumber / totalQuestions) * 100)));
            progressBar.style.width = percent + '%';
        }
    });
</script>
</body>
</html>
