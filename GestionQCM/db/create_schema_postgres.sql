-- Schema creation for GestionQCM (PostgreSQL)
-- Tables: ADMIN, ETUDIANT, QCM, EXAMEN

CREATE SCHEMA IF NOT EXISTS public;

-- Admins table (store BCrypt hashed passwords)
CREATE TABLE IF NOT EXISTS admin (
    username VARCHAR(50) PRIMARY KEY,
    password VARCHAR(100) NOT NULL
);

-- Students
CREATE TABLE IF NOT EXISTS etudiant (
    num_etudiant VARCHAR(20) PRIMARY KEY,
    nom VARCHAR(100),
    prenoms VARCHAR(200),
    niveau VARCHAR(10),
    adr_email VARCHAR(200)
);

-- QCM questions
CREATE TABLE IF NOT EXISTS qcm (
    num_quest VARCHAR(50) PRIMARY KEY,
    question TEXT,
    reponse1 TEXT,
    reponse2 TEXT,
    reponse3 TEXT,
    reponse4 TEXT,
    bonne_reponse VARCHAR(50)
);

-- Exams
CREATE TABLE IF NOT EXISTS examen (
    num_exam VARCHAR(20) PRIMARY KEY,
    num_etudiant VARCHAR(20) REFERENCES etudiant(num_etudiant) ON DELETE CASCADE,
    annee_univ VARCHAR(20),
    note NUMERIC(5,2)
);

-- Example indexes
CREATE INDEX IF NOT EXISTS idx_examen_num_etudiant ON examen(num_etudiant);
CREATE INDEX IF NOT EXISTS idx_qcm_num_quest ON qcm(num_quest);

-- Sample insert (admin): insert a BCrypt hash for password 'admin123' after generating the hash (see PowerShell commands)
-- INSERT INTO admin(username, password) VALUES('admin', '$2a$...');

-- End of schema
