-- Script SQL pour créer la base de données et les tables du projet Gestion QCM

CREATE DATABASE IF NOT EXISTS gestion_qcm
  CHARACTER SET utf8mb4
  COLLATE utf8mb4_unicode_ci;

USE gestion_qcm;

CREATE TABLE IF NOT EXISTS ETUDIANT (
  num_etudiant VARCHAR(10) NOT NULL,
  nom VARCHAR(100) NOT NULL,
  prenoms VARCHAR(150) NOT NULL,
  niveau ENUM('L1','L2','L3','M1','M2') NOT NULL,
  adr_email VARCHAR(255) NOT NULL,
  PRIMARY KEY (num_etudiant),
  UNIQUE KEY uq_etudiant_email (adr_email)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE IF NOT EXISTS QCM (
  num_quest VARCHAR(10) NOT NULL,
  question TEXT NOT NULL,
  reponse1 VARCHAR(255) NOT NULL,
  reponse2 VARCHAR(255) NOT NULL,
  reponse3 VARCHAR(255) NOT NULL,
  reponse4 VARCHAR(255) NOT NULL,
  bonne_reponse VARCHAR(255) NOT NULL,
  PRIMARY KEY (num_quest)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE IF NOT EXISTS EXAMEN (
  num_exam VARCHAR(10) NOT NULL,
  num_etudiant VARCHAR(10) NOT NULL,
  annee_univ VARCHAR(9) NOT NULL,
  note DECIMAL(4,1) NOT NULL,
  PRIMARY KEY (num_exam),
  CONSTRAINT fk_examen_etudiant FOREIGN KEY (num_etudiant)
    REFERENCES ETUDIANT(num_etudiant)
    ON DELETE CASCADE
    ON UPDATE CASCADE,
  CONSTRAINT chk_annee_univ_format CHECK (annee_univ REGEXP '^[0-9]{4}-[0-9]{4}$'),
  CONSTRAINT chk_note_range CHECK (note >= 0 AND note <= 10)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- Exemples d'insertion pour démarrer l'application
INSERT IGNORE INTO ETUDIANT (num_etudiant, nom, prenoms, niveau, adr_email) VALUES
  ('E001', 'Dubois', 'Marie', 'L1', 'marie.dubois@ecole.fr'),
  ('E002', 'Nguyen', 'Thierry', 'L2', 'thierry.nguyen@ecole.fr'),
  ('E003', 'Martin', 'Emma', 'L3', 'emma.martin@ecole.fr');

INSERT IGNORE INTO QCM (num_quest, question, reponse1, reponse2, reponse3, reponse4, bonne_reponse) VALUES
  ('Q001', 'Quel est le langage utilisé pour les servlets ?', 'Python', 'Java', 'PHP', 'JavaScript', 'Java'),
  ('Q002', 'Quelle balise JSP permet l''inclusion dynamique ?', '<jsp:include>', '<jsp:directive>', '<c:forEach>', '<jsp:useBean>', '<jsp:include>');

INSERT IGNORE INTO EXAMEN (num_exam, num_etudiant, annee_univ, note) VALUES
  ('EX001', 'E002', '2022-2023', 7.5),
  ('EX002', 'E003', '2022-2023', 8.2),
  ('EX003', 'E001', '2022-2023', 6.0);

-- Table pour les administrateurs
CREATE TABLE IF NOT EXISTS ADMIN (
  id INT AUTO_INCREMENT PRIMARY KEY,
  username VARCHAR(50) NOT NULL UNIQUE,
  password VARCHAR(255) NOT NULL,
  nom VARCHAR(50)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- IMPORTANT: stocker les mots de passe hachés (ex: bcrypt). L'exemple ci-dessous insère
-- un administrateur par défaut avec mot de passe en clair pour démarrage rapide.
-- Remplacez la valeur par un hash bcrypt avant mise en production.
INSERT IGNORE INTO ADMIN (username, password, nom) VALUES
  ('admin', 'admin123', 'Administrateur');
