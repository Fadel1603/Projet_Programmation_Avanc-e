import java.sql.*;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        // Scanner pour lire les entrées de l'utilisateur
        Scanner scanner = new Scanner(System.in);

        // Connexion à la base de données MySQL
        System.out.println("===== Connexion MySQL =====");
        System.out.print("Nom d'utilisateur : ");
        String utilisateur = scanner.nextLine();

        System.out.print("Mot de passe : ");
        String motDePasse = scanner.nextLine();

        // Création du gestionnaire avec les identifiants
        GestionnaireContact gestionnaire = new GestionnaireContact(utilisateur, motDePasse);

        int choix;

        // Boucle du menu principal
        do {
            afficherMenu();

            // Vérifie que l'utilisateur entre un chiffre
            while (!scanner.hasNextInt()) {
                System.out.println("Veuillez entrer un chiffre pour le choix.");
                scanner.next();
                System.out.print("Choix : ");
            }

            choix = scanner.nextInt();
            scanner.nextLine(); // Consomme le retour à la ligne

            // Traitement du choix utilisateur
            switch (choix) {
                case 1:
                    // Affiche tous les contacts
                    gestionnaire.afficherTousLesContacts();
                    break;

                case 2:
                    // Ajoute un nouveau contact
                    ajouterContact(scanner, gestionnaire);
                    break;

                case 3:
                    // Supprime un contact après confirmation
                    supprimerContact(scanner, utilisateur, motDePasse, gestionnaire);
                    break;

                case 4:
                    // Recherche d’un contact par mot-clé
                    System.out.print("Mot-clé de recherche : ");
                    String motCle = scanner.nextLine();
                    gestionnaire.rechercherContact(motCle);
                    break;

                case 5:
                    // Modification d’un contact par son ID
                    System.out.print("ID du contact à modifier : ");
                    int idContact = scanner.nextInt();
                    scanner.nextLine(); // éviter les bugs de nextLine
                    gestionnaire.modifierContact(idContact);
                    break;

                case 0:
                    // Quitte le programme
                    System.out.println("À bientôt !");
                    break;

                default:
                    // Gestion des choix non valides
                    System.out.println("Choix invalide.");
                    break;
            }

        } while (choix != 0); // Continue tant que l'utilisateur ne choisit pas 0

        scanner.close(); // Ferme le scanner
    }

    // Méthode pour afficher le menu principal
    private static void afficherMenu() {
        System.out.println("\n===== MENU CONTACTS =====");
        System.out.println("1. Afficher tous les contacts");
        System.out.println("2. Ajouter un contact");
        System.out.println("3. Supprimer un contact");
        System.out.println("4. Rechercher un contact");
        System.out.println("5. Modifier un contact");
        System.out.println("0. Quitter");
        System.out.print("Choix : ");
    }

    // Méthode pour ajouter un contact avec validation des champs
    private static void ajouterContact(Scanner scanner, GestionnaireContact gestionnaire) {
        System.out.print("Nom       : ");
        String nom = scanner.nextLine();

        System.out.print("Prénom    : ");
        String prenom = scanner.nextLine();

        // Saisie du téléphone
        String telephone;
        while (true) {
            System.out.print("Téléphone : ");
            try {
                telephone = (scanner.nextLine());
                break;
            } catch (NumberFormatException e) {
                System.out.println("Veuillez entrer uniquement des chiffres.");
            }
        }

        // Saisie de l'email avec vérification du format
        String email;
        while (true) {
            System.out.print("Email     : ");
            email = scanner.nextLine();
            if (email.matches("^[\\w.-]+@[\\w.-]+\\.[a-zA-Z]{2,6}$")) break;
            System.out.println("Format d’email invalide. Réessayez.");
            System.out.println("️Exemple valide : exemple@domaine.com");
        }

        System.out.print("Catégorie : ");
        String categorie = scanner.nextLine();

        // Création et ajout du contact
        Contact contact = new Contact(nom, prenom, telephone, email, categorie);
        gestionnaire.ajouterContact(contact);
    }

    // Méthode pour supprimer un contact avec confirmation
    private static void supprimerContact(Scanner scanner, String utilisateur, String motDePasse, GestionnaireContact gestionnaire) {
        System.out.print("ID du contact à supprimer : ");
        int idContact = scanner.nextInt();
        scanner.nextLine(); // éviter les bugs avec nextLine

        // Préparation de la requête SQL
        String sql = "SELECT * FROM contact WHERE id_contact = ?";

        try (
                Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/Gestion_de_contacts", utilisateur, motDePasse);
                PreparedStatement stmt = conn.prepareStatement(sql)
        ) {
            stmt.setInt(1, idContact);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                // Affiche les détails du contact avant suppression
                System.out.println("-------- Contact sélectionné --------");
                System.out.println("Nom complet : " + rs.getString("prenom") + " " + rs.getString("nom"));
                System.out.println("Téléphone   : " + rs.getInt("telephone"));
                System.out.println("Email       : " + rs.getString("email"));
                System.out.println("Catégorie   : " + rs.getString("categorie"));
                System.out.println("Créé le     : " + rs.getTimestamp("date_creation"));
                System.out.println("-------------------------------------");

                // Demande de confirmation
                System.out.print("Êtes-vous sûr de vouloir supprimer ce contact ? (oui/non) : ");
                String confirmation = scanner.nextLine().trim().toLowerCase();

                if (confirmation.equals("oui") || confirmation.equals("o")) {
                    gestionnaire.supprimer(idContact);
                } else {
                    System.out.println("Suppression annulée.");
                }

            } else {
                System.out.println("Aucun contact trouvé avec cet ID.");
            }

        } catch (SQLException e) {
            // Gestion des erreurs SQL
            System.out.println("Erreur MySQL (recherche avant suppression) : " + e.getMessage());
        }
    }
}

