import java.sql.*; // Bibliothèque JDBC pour interagir avec une base de données MySQL
import java.util.Scanner; // Pour lire les entrées utilisateur

public class GestionnaireContacts {
    // URL de connexion à la base de données locale
    private final String URL = "jdbc:mysql://localhost:3306/Gestion_de_contacts";
    private final String utilisateur; // Nom d'utilisateur pour la base
    private final String motDePasse;  // Mot de passe pour la base

    // Constructeur : initialise les identifiants de connexion
    public GestionnaireContacts(String utilisateur, String motDePasse) {
        this.utilisateur = utilisateur;
        this.motDePasse = motDePasse;
    }

    // Méthode pour établir une connexion avec la base de données
    private Connection connecter() throws SQLException {
        return DriverManager.getConnection(URL, utilisateur, motDePasse);
    }

    // Vérifie si un email existe déjà dans la base
    public boolean verifierEmailExistant(String email) {
        String sql = "SELECT COUNT(*) FROM contact WHERE email = ?";
        try (Connection conn = connecter();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, email);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) > 0; // Si le count est > 0, email existe
            }
        } catch (SQLException e) {
            System.out.println("Erreur MySQL (vérification email) : " + e.getMessage());
        }
        return false;
    }

    // Ajoute un contact si l'email n'existe pas déjà
    public void ajouterContact(Contact c) {
        if (verifierEmailExistant(c.getEmail())) {
            System.out.println("Cet email existe déjà. Contact non ajouté.");
            return;
        }

        String sql = "INSERT INTO contact (nom, prenom, telephone, email, categorie) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = connecter();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, c.getNom());
            stmt.setString(2, c.getPrenom());
            stmt.setInt(3, c.getTelephone());
            stmt.setString(4, c.getEmail());
            stmt.setString(5, c.getCategorie());

            stmt.executeUpdate();
            System.out.println("Contact ajouté avec succès.");

        } catch (SQLException e) {
            System.out.println("Erreur MySQL (ajout) : " + e.getMessage());
        }
    }

    // Affiche tous les contacts de la base
    public void afficherTousLesContacts() {
        String sql = "SELECT * FROM contact";
        try (Connection conn = connecter();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            int compteur = 0;
            while (rs.next()) {
                compteur++;
                System.out.println("Contact #" + rs.getInt("id_contact"));
                System.out.println("Nom complet : " + rs.getString("prenom") + " " + rs.getString("nom"));
                System.out.println("Téléphone   : " + rs.getInt("telephone"));
                System.out.println("Email       : " + rs.getString("email"));
                System.out.println("Catégorie   : " + rs.getString("categorie"));
                System.out.println("Créé le     : " + rs.getTimestamp("date_creation"));
                System.out.println("----------------------------------");
            }
            if (compteur == 0) System.out.println("⚠ Aucun contact trouvé.");

        } catch (SQLException e) {
            System.out.println("Erreur MySQL (affichage) : " + e.getMessage());
        }
    }

    // Supprime un contact selon son ID
    public void supprimer(int id_contact) {
        String sql = "DELETE FROM contact WHERE id_contact = ?";
        try (Connection conn = connecter();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id_contact);
            int affected = stmt.executeUpdate();

            if (affected > 0) {
                System.out.println("Contact supprimé.");
            } else {
                System.out.println("Aucun contact avec cet ID.");
            }

        } catch (SQLException e) {
            System.out.println("Erreur MySQL : " + e.getMessage());
        }
    }

    // Recherche les contacts avec un mot-clé (nom ou prénom)
    public void rechercherContact(String motCle) {
        String sql = "SELECT * FROM contact WHERE nom LIKE ? OR prenom LIKE ?";
        try (Connection conn = connecter();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, "%" + motCle + "%");
            stmt.setString(2, "%" + motCle + "%");

            ResultSet rs = stmt.executeQuery();
            boolean trouve = false;

            while (rs.next()) {
                trouve = true;
                System.out.println("Contact #" + rs.getInt("id_contact"));
                System.out.println("Nom complet : " + rs.getString("prenom") + " " + rs.getString("nom"));
                System.out.println("Téléphone   : " + rs.getInt("telephone"));
                System.out.println("Email       : " + rs.getString("email"));
                System.out.println("Catégorie   : " + rs.getString("categorie"));
                System.out.println("Créé le     : " + rs.getTimestamp("date_creation"));
                System.out.println("----------------------------------");
            }

            if (!trouve) {
                System.out.println("Aucun contact trouvé avec ce mot-clé.");
            }

        } catch (SQLException e) {
            System.out.println("Erreur MySQL (recherche) : " + e.getMessage());
        }
    }

    // Modifie les infos d'un contact en le recherchant par nom
    public void modifierContact(int id_contact) {
        String sqlCheck = "SELECT * FROM contact WHERE id_contact = ?";
        String sqlUpdate = "UPDATE contact SET nom = ?, prenom = ?, telephone = ?, email = ?, categorie = ? WHERE id_contact = ?";

        try (Connection conn = connecter();
             PreparedStatement checkStmt = conn.prepareStatement(sqlCheck);
             PreparedStatement updateStmt = conn.prepareStatement(sqlUpdate)) {

            checkStmt.setInt(1, id_contact);
            ResultSet rs = checkStmt.executeQuery();

            if (!rs.next()) {
                System.out.println("Aucun contact trouvé avec cet ID.");
                return;
            }

            Scanner scanner = new Scanner(System.in);
            System.out.println("Contact trouvé. Entrez les nouvelles informations :");

            System.out.print("Nouveau nom        : ");
            String nom = scanner.nextLine();

            System.out.print("Nouveau prénom     : ");
            String prenom = scanner.nextLine();

            int tel;
            while (true) {
                System.out.print("Nouveau téléphone  : ");
                try {
                    tel = Integer.parseInt(scanner.nextLine());
                    break;
                } catch (NumberFormatException e) {
                    System.out.println("Entrez uniquement des chiffres pour le téléphone.");
                }
            }

            String email;
            while (true) {
                System.out.print("Nouvel email       : ");
                email = scanner.nextLine();

                if (email.matches("^[\\w.-]+@[\\w.-]+\\.[a-zA-Z]{2,6}$")) break;

                System.out.println("Format d’email invalide. Réessayez.");
                System.out.println("️Exemple valide : exemple@domaine.com");
            }

            System.out.print("Nouvelle catégorie : ");
            String categorie = scanner.nextLine();

            updateStmt.setString(1, nom);
            updateStmt.setString(2, prenom);
            updateStmt.setInt(3, tel);
            updateStmt.setString(4, email);
            updateStmt.setString(5, categorie);
            updateStmt.setInt(6, id_contact);

            int updated = updateStmt.executeUpdate();
            if (updated > 0) {
                System.out.println("Contact mis à jour avec succès.");
            } else {
                System.out.println("Échec de la mise à jour.");
            }

        } catch (SQLException e) {
            System.out.println("Erreur MySQL : " + e.getMessage());
        }
    }
}
