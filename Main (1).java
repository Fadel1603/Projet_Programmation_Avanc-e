import java.sql.*;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.println("===== Connexion MySQL =====");
        System.out.print("Nom d'utilisateur : ");
        String utilisateur = scanner.nextLine();

        System.out.print("Mot de passe : ");
        String motDePasse = scanner.nextLine();

        GestionnaireContacts gestionnaire = new GestionnaireContacts(utilisateur, motDePasse);

        int choix;

        do {
            System.out.println("\n===== MENU CONTACTS =====");
            System.out.println("1. Afficher tous les contacts");
            System.out.println("2. Ajouter un contact");
            System.out.println("3. Supprimer un contact");
            System.out.println("4. Rechercher un contact");
            System.out.println("5. Modifier un contact");
            System.out.println("0. Quitter");
            System.out.print("Choix : ");

            while (!scanner.hasNextInt()) {
                System.out.println("Veuillez entrer un chiffre pour le choix.");
                scanner.next();
                System.out.print("Choix : ");
            }

            choix = scanner.nextInt();
            scanner.nextLine();

            switch (choix) {
                case 1:
                    gestionnaire.afficherTousLesContacts();
                    break;
                case 2:
                    System.out.print("Nom       : ");
                    String nom = scanner.nextLine();
                    System.out.print("Prénom    : ");
                    String prenom = scanner.nextLine();

                    String tel;
                    while (true) {
                        System.out.print("Téléphone : ");
                        tel = scanner.nextLine();
                        if (tel.matches("\\d+")) {
                            break;
                        } else {
                            System.out.println("Veuillez entrer uniquement des chiffres.");
                        }
                    }


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

                    Contact c = new Contact(nom, prenom, tel, email, categorie);
                    gestionnaire.ajouterContact(c);
                    break;

                case 3:
                    System.out.print("ID du contact à supprimer : ");
                    int id_contact = scanner.nextInt();
                    scanner.nextLine(); // Pour éviter les bugs avec le nextLine suivant

                    // 1. On récupère les infos du contact
                    String sql = "SELECT * FROM contact WHERE id_contact = ?";
                    try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/Gestion_de_contacts", utilisateur, motDePasse);
                         PreparedStatement stmt = conn.prepareStatement(sql)) {

                        stmt.setInt(1, id_contact);
                        ResultSet rs = stmt.executeQuery();

                        if (rs.next()) {
                            // 2. On affiche les infos du contact
                            System.out.println("-------- Contact sélectionné --------");
                            System.out.println("Nom complet : " + rs.getString("prenom") + " " + rs.getString("nom"));
                            System.out.println("Téléphone   : " + rs.getInt("telephone"));
                            System.out.println("Email       : " + rs.getString("email"));
                            System.out.println("Catégorie   : " + rs.getString("categorie"));
                            System.out.println("Créé le     : " + rs.getTimestamp("date_creation"));
                            System.out.println("-------------------------------------");

                            // 3. Demande de confirmation
                            System.out.print("Êtes-vous sûr de vouloir supprimer ce contact ? (oui/non) : ");
                            String confirmation = scanner.nextLine().trim().toLowerCase();

                            if (confirmation.equals("oui") || confirmation.equals("o")) {
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
                    System.out.print("Mot-clé de recherche : ");
                    String motCle = scanner.nextLine();
                    gestionnaire.rechercherContact(motCle);
                    break;

                case 5:
                    System.out.print("id du contact à modifier : ");
                    int id_Contact = scanner.nextInt();
                    gestionnaire.modifierContact(id_Contact);
                    break;

                case 0:
                    System.out.println("À bientôt !");
                    break;

                default:
                    System.out.println("Choix invalide.");
            }

        } while (choix != 0);

        scanner.close();
    }
}