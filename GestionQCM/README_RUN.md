# Exécution du projet GestionQCM

## 1) Configuration Maven
Le projet est maintenant configuré avec un `pom.xml` minimal pour générer un WAR.

### Commandes Maven
```bash
cd "c:\Users\LENOVO X390\eclipse-workspace\GestionQCM"
mvn clean package
```

Le fichier généré apparaîtra dans `target/GestionQCM-1.0-SNAPSHOT.war`.

## 2) Ajout de Tomcat
Si Tomcat n'est pas installé, téléchargez Apache Tomcat 9 ou 10.
- https://tomcat.apache.org/
- Décompressez-le dans un dossier, par exemple `C:\apache-tomcat-9.0.XX`
- Définissez la variable d'environnement `CATALINA_HOME` sur ce dossier.

## 3) Lancement avec Tomcat
### Option Eclipse
- Ouvrez Eclipse.
- Ajoutez un serveur Apache Tomcat dans la vue "Servers".
- Déployez le projet `GestionQCM` sur ce serveur.
- Démarrez le serveur.

### Option terminal
```bash
cd /d "C:\apache-tomcat-9.0.XX"
bin\catalina.bat run
```

Puis ouvrez le navigateur :
```
http://localhost:8080/GestionQCM/
```

## 4) Chargement de la base PostgreSQL
- Créez la base `gestion_qcm` puis exécutez `db/create_schema_postgres.sql` :
	```powershell
	psql -U postgres -c "CREATE DATABASE gestion_qcm;"
	psql -U postgres -d gestion_qcm -f db/create_schema_postgres.sql
	```
- Vérifiez que `DBConnection` utilise les bons identifiants PostgreSQL (variables d'environnement `JDBC_DATABASE_*`).

## 5) Exécution des tests unitaires
```bash
mvn test
```

---

Si vous souhaitez, je peux également ajouter un `src/test/resources` de configuration de test et un profil Maven `test` séparé pour pointer vers une base de données dédiée.
