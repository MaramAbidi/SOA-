# 📋 Projet REST - Gestion des Personnes

# Maram Abidi TD3 TP5
# BILEL SOUDENI TD3 TP5


## 📝 Description du Projet

Application web full-stack de gestion des personnes développée avec une architecture REST moderne. Ce projet démontre l'implémentation complète d'un système CRUD (Create, Read, Update, Delete) avec une séparation claire entre le backend et le frontend.

### Fonctionnalités Principales

- ✅ **Affichage** de toutes les personnes enregistrées
- 🔍 **Recherche** de personnes par nom (recherche dynamique)
- ➕ **Ajout** de nouvelles personnes
- ✏️ **Modification** des informations existantes
- 🗑️ **Suppression** de personnes
- 🎨 Interface utilisateur moderne et responsive
- 🔄 Opérations en temps réel sans rechargement de page

---

## 🛠️ Technologies Utilisées

### Backend
- **Java 8** - Langage de programmation
- **JAX-RS (Jersey 2.35)** - Framework REST pour les services web
- **JPA/Hibernate 5.2.6** - ORM pour la persistance des données
- **MySQL 5.x** - Système de gestion de base de données
- **Maven** - Gestion des dépendances et build

### Frontend
- **HTML5** - Structure de la page
- **CSS3** - Stylisation moderne avec gradients et animations
- **JavaScript (ES6+)** - Logique cliente et appels API
- **Fetch API** - Communication avec le backend REST

### Serveur d'Application
- **Apache Tomcat 8.x/9.x** - Serveur d'application Java EE

---

## 📂 Structure du Projet

```
projet-rest-fullstack/
│
├── src/
│   ├── main/
│   │   ├── java/com/rest/
│   │   │   ├── model/
│   │   │   │   └── Person.java              # Entité JPA Person
│   │   │   ├── service/
│   │   │   │   └── PersonService.java       # Endpoints REST
│   │   │   └── util/
│   │   │       ├── EMUtil.java              # Gestionnaire EntityManager
│   │   │       └── CorsFilter.java          # Filtre CORS
│   │   ├── resources/
│   │   │   └── META-INF/
│   │   │       └── persistence.xml          # Configuration JPA
│   │   └── webapp/
│   │       ├── index.html                   # Page principale
│   │       ├── css/
│   │       │   └── style.css                # Styles
│   │       ├── js/
│   │       │   └── app.js                   # Logique JavaScript
│   │       └── WEB-INF/
│   │           └── web.xml                  # Configuration web
│   └── test/
│
├── pom.xml                                   # Configuration Maven
└── README.md                                 # Documentation
```

---

## 🚀 Instructions d'Installation et d'Exécution

### Prérequis

Avant de commencer, assurez-vous d'avoir installé :

- ☕ **JDK 8** ou supérieur ([Télécharger](https://www.oracle.com/java/technologies/downloads/))
- 📦 **Maven 3.x** ([Télécharger](https://maven.apache.org/download.cgi))
- 🗄️ **MySQL 5.x** ou supérieur ([Télécharger](https://dev.mysql.com/downloads/))
- 🖥️ **Apache Tomcat 8.x/9.x** ([Télécharger](https://tomcat.apache.org/download-90.cgi))

### Étape 1 : Configuration de la Base de Données

1. Démarrez votre serveur MySQL

2. Créez un utilisateur root (si ce n'est pas déjà fait) :
   ```sql
   -- La base de données sera créée automatiquement par Hibernate
   -- Pas besoin de créer manuellement la base 'persondb'
   ```

3. **Important** : La configuration dans `persistence.xml` utilise :
   - **URL** : `jdbc:mysql://localhost:3306/persondb`
   - **Utilisateur** : `root`
   - **Mot de passe** : *(vide par défaut)*
   
   Si vos identifiants sont différents, modifiez le fichier `src/main/resources/META-INF/persistence.xml`

### Étape 2 : Compilation du Projet

Ouvrez un terminal dans le répertoire du projet et exécutez :

```bash
mvn clean package
```

Cette commande va :
- Télécharger toutes les dépendances
- Compiler le code Java
- Créer le fichier WAR dans le dossier `target/`

### Étape 3 : Déploiement sur Tomcat

#### Méthode 1 : Déploiement Manuel

1. Copiez le fichier `target/ProjetREST.war`
2. Collez-le dans le dossier `webapps/` de votre installation Tomcat
3. Démarrez Tomcat :
   - **Windows** : `bin\startup.bat`
   - **Linux/Mac** : `bin/startup.sh`

#### Méthode 2 : Via l'IDE (Eclipse/IntelliJ)

1. Configurez Tomcat comme serveur dans votre IDE
2. Faites un clic droit sur le projet → **Run As** → **Run on Server**
3. Sélectionnez votre serveur Tomcat

### Étape 4 : Accès à l'Application

Une fois Tomcat démarré, accédez à l'application via votre navigateur :

```
http://localhost:8080/ProjetREST/
```

---

## 🔌 Endpoints REST API

L'API REST est accessible à l'URL de base : `http://localhost:8080/ProjetREST/api/persons`

| Méthode | Endpoint | Description |
|---------|----------|-------------|
| `GET` | `/persons` | Récupérer toutes les personnes |
| `GET` | `/persons/{id}` | Récupérer une personne par ID |
| `GET` | `/persons/search?nom=Dupont` | Rechercher par nom |
| `POST` | `/persons` | Ajouter une nouvelle personne |
| `PUT` | `/persons/{id}` | Modifier une personne existante |
| `DELETE` | `/persons/{id}` | Supprimer une personne |

### Exemple de Requête POST

```json
{
  "nom": "Dupont",
  "prenom": "Jean",
  "email": "jean.dupont@email.com",
  "telephone": "0612345678",
  "adresse": "123 Rue de Paris, 75001 Paris"
}
```

---

## 🎥 Vidéo de Démonstration

📹 **Lien vers la démonstration vidéo** : ([Voir la démonstration sur Google Drive](https://drive.google.com/drive/folders/1Tm9fmZIaJNQbiuIJcWQyivgKwgCTSUkz?usp=sharing))

La vidéo présente :
- Installation et configuration du projet
- Démonstration de toutes les fonctionnalités CRUD
- Navigation dans l'interface utilisateur
- Tests des endpoints REST

---

## 🐛 Dépannage

### Problème : La base de données n'est pas créée

**Solution** : Vérifiez que l'URL JDBC contient `createDatabaseIfNotExist=true`

### Problème : Erreur de connexion MySQL

**Solution** : Vérifiez vos identifiants dans `persistence.xml` et assurez-vous que MySQL est démarré

### Problème : Port 8080 déjà utilisé

**Solution** : Modifiez le port dans `server.xml` de Tomcat ou arrêtez l'application utilisant le port 8080

### Problème : Erreur CORS

**Solution** : Le filtre CORS est déjà configuré dans `CorsFilter.java`. Vérifiez qu'il est bien enregistré dans `web.xml`

---


---


**Bonne exploration du projet ! 🚀**
