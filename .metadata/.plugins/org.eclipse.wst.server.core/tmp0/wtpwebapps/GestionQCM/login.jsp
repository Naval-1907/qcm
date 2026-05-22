<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html lang="fr">
<head>
    <meta charset="UTF-8" />
    <meta name="viewport" content="width=device-width, initial-scale=1.0" />
    <title>Connexion - Gestion QCM</title>
    <script src="https://cdn.tailwindcss.com"></script>
</head>
<body class="bg-slate-100">
<div class="min-h-screen flex items-center justify-center">
    <!-- Toast container: utilisé pour afficher une notification après déconnexion -->
    <div id="toast-container" class="fixed top-6 right-6 z-50 flex flex-col gap-4"></div>
    <div class="w-full max-w-md bg-white rounded-3xl shadow p-8">
        <h1 class="text-2xl font-bold mb-4 text-center">Espace d'administration</h1>
        <p class="text-sm text-slate-600 mb-6 text-center">Connectez-vous pour accéder à la gestion des étudiants et QCM.</p>

        <c:if test="${not empty loginError}">
            <div class="mb-4 rounded-2xl bg-red-50 border border-red-200 text-red-700 p-3">${loginError}</div>
        </c:if>
        <!-- Le message de déconnexion est affiché via un toast JS si ?logout=1 dans l'URL -->

        <form action="main?action=login" method="post" class="space-y-4">
            <div>
                <label class="block text-slate-700">Nom d'utilisateur</label>
                <input name="username" type="text" required class="w-full rounded-2xl border border-slate-300 px-4 py-3" placeholder="admin" />
            </div>
            <div>
                <label class="block text-slate-700">Mot de passe</label>
                <input name="password" type="password" required class="w-full rounded-2xl border border-slate-300 px-4 py-3" placeholder="••••••••" />
            </div>
            <div class="flex items-center justify-between">
                <div class="text-sm text-slate-500">Accès administrateur requis</div>
                <button type="submit" class="rounded-2xl bg-slate-800 text-white px-6 py-2 hover:bg-slate-900">Se connecter</button>
            </div>
        </form>
    </div>
</div>
    <script>
        // Simple toast implementation pour la page de login.
        function showToast(message, type) {
            const container = document.getElementById('toast-container');
            if (!container) return;
            const colors = {
                success: 'bg-emerald-600 text-white',
                info: 'bg-slate-800 text-white',
                error: 'bg-red-600 text-white'
            };
            const toast = document.createElement('div');
            toast.className = `max-w-sm rounded-3xl p-4 shadow-2xl ring-1 ring-black/10 transform transition-all duration-500 ease-out ${colors[type] || colors.info}`;
            toast.innerHTML = `<p class="font-semibold">${message}</p>`;
            toast.style.opacity = '0';
            toast.style.transform = 'translateY(-20px)';
            container.appendChild(toast);
            requestAnimationFrame(() => {
                toast.style.opacity = '1';
                toast.style.transform = 'translateY(0)';
            });
            setTimeout(() => {
                toast.style.opacity = '0';
                toast.style.transform = 'translateY(-20px)';
                toast.addEventListener('transitionend', () => toast.remove(), { once: true });
            }, 3500);
        }

        document.addEventListener('DOMContentLoaded', function () {
            // Si l'URL contient logout=1, afficher un toast de succès
            try {
                const params = new URLSearchParams(window.location.search);
                if (params.get('logout') === '1') {
                    showToast('Déconnexion réussie. À bientôt !', 'success');
                }
            } catch (e) {
                // ignore
            }
        });
    </script>
    </body>
    </html>
