import java.sql.*;              // Pour la connexion et les requêtes SQL (JDBC)
import java.util.Scanner;       // Pour lire les entrées utilisateur au clavier

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        // Demande les identifiants de connexion MySQL
        System.out.println("===== Connexion MySQL =====");
        System.out.print("Nom d'utilisateur : ");
        String utilisateur = scanner.nextLine();

        System.out.print("Mot de passe : ");
        String motDePasse = scanner.nextLine();

        // Création d’un gestionnaire de contacts avec les identifiants fournis
        GestionnaireContacts gestionnaire = new GestionnaireContacts(utilisateur, motDePasse);

        int choix;

        // Boucle principale du menu
        do {
            // Affiche le menu des options
            System.out.println("\n===== MENU CONTACTS =====");
            System.out.println("1. Afficher tous les contacts");
            System.out.println("2. Ajouter un contact");
            System.out.println("3. Supprimer un contact");
            System.out.println("4. Rechercher un contact");
            System.out.println("5. Modifier un contact");
            System.out.println("0. Quitter");
            System.out.print("Choix : ");

            // Vérifie que l’entrée est bien un chiffre
            while (!scanner.hasNextInt()) {
                System.out.println("Veuillez entrer un chiffre pour le choix.");
                scanner.next();
                System.out.print("Choix : ");
            }

            choix = scanner.nextInt();
            scanner.nextLine(); // Nettoie le buffer

            switch (choix) {
                case 1:
                    // Affiche tous les contacts
                    gestionnaire.afficherTousLesContacts();
                    break;

                case 2:
                    // Demande les infos du nouveau contact
                    System.out.print("Nom       : ");
                    String nom = scanner.nextLine();
                    System.out.print("Prénom    : ");
                    String prenom = scanner.nextLine();

                    // Validation : numéro de téléphone doit être uniquement des chiffres
                    String tel;
                    while (true) {
                        System.out.print("Téléphone : ");
                        tel = scanner.nextLine();
                        if (tel.matches("\\d+")) break;
                        System.out.println("Veuillez entrer uniquement des chiffres.");
                    }

                    // Validation du format d’email
                    String email;
                    while (true) {
                        System.out.print("Email     : ");
                        email = scanner.nextLine();

                        if (email.matches("^[\\w.-]+@[\\w.-]+\\.[a-zA-Z]{2,6}$")) break;

                        System.out.println("Format d’email invalide. Réessayez.");
                        System.out.println("️Exemple valide : exemple@domaine.com");
                    }

                    // Lecture de la catégorie
                    System.out.print("Catégorie : ");
                    String categorie = scanner.nextLine();

                    // Création du contact et ajout à la base
                    Contact c = new Contact(nom, prenom, tel, email, categorie);
                    gestionnaire.ajouterContact(c);
                    break;

                case 3:
                    // Suppression d’un contact (avec confirmation)
                    System.out.print("ID du contact à supprimer : ");
                    int id_contact = scanner.nextInt();
                    scanner.nextLine(); // Nettoie le buffer

                    // Requête SQL pour récupérer les infos du contact avant suppression
                    String sql = "SELECT * FROM contact WHERE id_contact = ?";
                    try (
                        Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/Gestion_de_contacts", utilisateur, motDePasse);
                        PreparedStatement stmt = conn.prepareStatement(sql)
                    ) {
                        stmt.setInt(1, id_contact);
                        ResultSet rs = stmt.executeQuery();

                        if (rs.next()) {
                            // Affiche les détails du contact sélectionné
                            System.out.println("-------- Contact sélectionné --------");
                            System.out.println("Nom complet : " + rs.getString("prenom") + " " + rs.getString("nom"));
                            System.out.println("Téléphone   : " + rs.getString("telephone"));
                            System.out.println("Email       : " + rs.getString("email"));
                            System.out.println("Catégorie   : " + rs.getString("categorie"));
                            System.out.println("Créé le     : " + rs.getTimestamp("date_creation"));
                            System.out.println("-------------------------------------");

                            // Demande confirmation
                            System.out.print("Êtes-vous sûr de vouloir supprimer ce contact ? (oui/non) : ");
                            String confirmation = scanner.nextLine().trim().toLowerCase();

                            if (confirmation.equals("oui") || confirmation.equals("o")) {
                                // Si l'utilisateur confirme → on supprime
                                gestionnaire.supprimer(id_contact);
                            } else {
                                System.out.println("Suppression annulée.");
                            }

                        } else {
                            System.out.println("Aucun contact trouvé avec cet ID.");
                        }

                    } catch (SQLException e) {
                        System.out.println("Erreur MySQL (recherche avant suppression) : " + e.getMessage());
                    }
                    break;

                case 4:
                    // Recherche d’un contact
                    System.out.print("Mot-clé de recherche : ");
                    String motCle = scanner.nextLine();
                    gestionnaire.rechercherContact(motCle);
                    break;

                case 5:
                    // Modification d’un contact (par ID)
                    System.out.print("id du contact à modifier : ");
                    int id_Contact = scanner.nextInt();
                    gestionnaire.modifierContact(id_Contact);
                    break;

                case 0:
                    // Sortie du programme
                    System.out.println("À bientôt !");
                    break;

                default:
                    // Choix invalide
                    System.out.println("Choix invalide.");
            }

        } while (choix != 0); // Boucle jusqu’à ce que l'utilisateur quitte

        scanner.close(); // Bonne pratique : fermeture du scanner
    }
}
