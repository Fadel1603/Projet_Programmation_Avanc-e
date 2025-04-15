
Les étapes de notre projet 

Dans ce projet, on nous demande de développer une application console en Java permettant de gérer une base de contacts personnels ou professionnels via une base de données MySQL.
1	Les objectifs du projet 
Les principales fonctionnalités de ce programme sont : 
•	Connexion à la base de données MySQL (via saisie utilisateur/mot de passe)
•	Affichage de tous les contacts enregistrés ;
•	Ajout d'un nouveau contact ;
•	Suppression d’un contact ;
•	Recherche de contacts par mot-clé (nom, prénom, etc.) ;
•	Modification d’un contact existant;
•	Menu interactif simple dans la console ;

2	La structure du projet
•	Main.java : C’est la classe principale qui contient le menu principal, l’interaction utilisateur et les appels aux fonctions du gestionnaire.
•	GestionnaireContacts.java : est la classe qui contient les méthodes liées aux opérations sur la base de données .
•	Contact.java : est la classe représentant un contact avec ses attributs (nom, prénom, téléphone, email, catégorie).

3	Connexion a la base de donnees
 Pour se connecter à la BDD, l'utilisateur doit entrer son nom d’utilisateur et mot de passe MySQL lors de l'exécution. La connexion est faite avec JDBC.
Ci-dessous le script utiliser pour la création de notre base de données
drop database if exists Gestion_de_contacts;

create database Gestion_de_contacts;
use Gestion_de_contacts ;

-- Création de la table contact
CREATE TABLE Contact (
id_contact int primary key auto_increment,
Nom varchar(100),
prenom varchar(120),
categorie varchar(60),
telephone varchar(50),
email varchar(50) unique,
adresse text,
date_creation timestamp default current_timestamp
   );
Exécution du programme 
Pour exécuter notre programme nous devons :
1.	Compiler : java Main.java GestionnaireContacts.java Contact.java
2.	Executer: java Main
3.	Suivre les instructions dans la console.
=======
Classe GestionnaireContacts :
Cette classe représente la partie centrale de la gestion des contacts dans notre projet. Elle contient toutes les fonctionnalités nécessaires pour se connecter à une base de données MySQL, ajouter, modifier, afficher, rechercher ou supprimer des contacts.
Elle intervient après la création de la classe Contact. C’est ici que l’on va manipuler les objets Contact pour effectuer des opérations réelles sur les données, via JDBC.


Étape 1 : Importation des bibliothèques nécessaires
-	 java.sql. * : ce package contient toutes les classes nécessaires pour se connecter à une base de données SQL (comme Connection, Statement, ResultSet, PreparedStatement, etc.)
-	java.util.Scanner : utilisé pour la lecture des entrées clavier lors de la modification d’un contact.

Étape 2 : Définition des attributs et du constructeur
-	URL : contient l’adresse de connexion à la base de données.
-	utilisateur et motDePasse : les identifiants pour se connecter à sa base locale.
-	Le constructeur :  permet de créer un objet GestionnaireContacts avec des identifiants spécifiques du l’utilisateur.

Étape 3 : Connexion à la base de données
Dans cette étape, nous avons mis en place une méthode de connexion à la base de données MySQL. Cette connexion est essentielle pour pouvoir interagir avec la base : ajouter, supprimer, modifier ou consulter les contacts.

Étape 4 : Vérification de l’unicité d’un email
Dans cette étape nous avons défini une méthode qui nous permet de vérifier si un contact avec le même email existe déjà. Elle est utilisée avant l’ajout d’un nouveau contact pour éviter les doublons.

Étape 5 : Ajout d’un contact
Cette méthode :
-	Vérifie d’abord si l’email est déjà utilisé (verifierEmailExistant).
-	Si ce n’est pas le cas, elle insère le contact dans la table SQL.

Étape 6 : Affichage de tous les contacts
Cette méthode récupère et affiche tous les contacts de l’utilisateur. Elle affiche aussi la date de création (champ date_creation) et s'assure d'indiquer quand aucun contact n’est trouvé quand la table est vide.

Étape 7 : Suppression d’un contact par ID
Permet de supprimer un contact à partir de son identifiant (clé primaire). Elle utilise un PreparedStatement avec un paramètre pour éviter les injections SQL.
Elle prend un identifiant.
Elle tente de supprimer le contact avec cet identifiant dans la base.
Elle affiche un message en fonction du résultat (supprimé ou introuvable).
Elle gère les erreurs SQL proprement.

Étape 8 : Recherche de contacts (par mot-clé)
Permet de rechercher un contact en fonction d’un mot-clé dans le nom ou le prénom. Affiche les résultats trouvés ou un message s’il n’y a rien.

Étape 9 : Modification d’un contact (par nom)
Le rôle principal de la méthode  modifierContact est de permettre à l’utilisateur de corriger ou actualiser les données d’un contact tout en lui laissant une contrainte comme au début au niveau du courriel. Elle garantit aussi la fiabilité des données grâce à des vérifications (existence du contact, format email) et à une confirmation finale.









