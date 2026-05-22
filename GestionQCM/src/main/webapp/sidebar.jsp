<aside class="w-64 bg-white shadow-lg rounded-xl p-6 flex flex-col">
    <div class="text-2xl font-bold text-slate-900 mb-6">Gestion QCM</div>
    <nav class="space-y-3 text-sm flex-1">
        <a href="main?action=dashboard" class="block px-4 py-3 rounded-lg text-slate-700 hover:bg-slate-100">Dashboard</a>
        <a href="main?action=gestion" class="block px-4 py-3 rounded-lg text-slate-700 hover:bg-slate-100">Gestion Étudiants & QCM</a>
        <a href="main?action=niveau" class="block px-4 py-3 rounded-lg text-slate-700 hover:bg-slate-100">Liste par Niveau</a>
        <a href="main?action=examen" class="block px-4 py-3 rounded-lg text-slate-700 hover:bg-slate-100">Examen</a>
        <a href="main?action=classement" class="block px-4 py-3 rounded-lg text-slate-700 hover:bg-slate-100">Classement</a>
    </nav>

    <!-- Logout: utiliser POST pour des raisons de sécurité (évite CSRF via GET) -->
    <div class="mt-6">
        <form action="main?action=logout" method="post">
            <button type="submit" title="Se déconnecter" class="w-full flex items-center justify-center gap-2 rounded-2xl px-4 py-3 text-sm text-red-600 hover:bg-red-50 border border-transparent">
                <!-- simple logout icon -->
                <svg xmlns="http://www.w3.org/2000/svg" class="h-4 w-4" viewBox="0 0 20 20" fill="currentColor" aria-hidden="true">
                    <path fill-rule="evenodd" d="M3 4.5A1.5 1.5 0 014.5 3h6A1.5 1.5 0 0112 4.5V7a.75.75 0 01-1.5 0V4.5c0-.028.022-.5-.5-.5h-6c-.278 0-.5.222-.5.5v11c0 .278.222.5.5.5h6c.528 0 .5-.472.5-.5V12a.75.75 0 011.5 0v2.5A1.5 1.5 0 0110.5 16h-6A1.5 1.5 0 013 14.5v-10z" clip-rule="evenodd" />
                    <path d="M15.22 9.72a.75.75 0 010 1.06L13.56 12.44a.75.75 0 11-1.06-1.06l1.22-1.22H8.75a.75.75 0 010-1.5h5.0l-1.22-1.22a.75.75 0 011.06-1.06l1.66 1.66z" />
                </svg>
                Se déconnecter
            </button>
        </form>
    </div>
</aside>
