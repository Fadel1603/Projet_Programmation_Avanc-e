# Projet_Programmation_Avanc-e
Projet Java en équipe
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
 
public class ContactManager {
    private final String URL = "jdbc:mysql://localhost:3306/gestion_contacts";
    private final String USER = "root"; // adapte avec ton user
    private final String PASSWORD = ""; // adapte avec ton mot de passe
 
    private Connection connect() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }
 
    public void ajouter(Contact c) {
        String sql = "INSERT INTO contacts (nom, prenom, telephone, email, categorie, photo_path) VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection conn = connect();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
 
            stmt.setString(1, c.getNom());
            stmt.setString(2, c.getPrenom());
            stmt.setString(3, c.getTelephone());
            stmt.setString(4, c.getEmail());
            stmt.setString(5, c.getCategorie());
            stmt.setString(6, c.getPhotoPath());
            stmt.executeUpdate();
            System.out.println("✅ Contact ajouté avec succès.");
 
        } catch (SQLException e) {
            System.out.println("❌ Erreur MySQL : " + e.getMessage());
        }
    }
 
    public void afficherTous() {
        String sql = "SELECT * FROM contacts";
        try (Connection conn = connect();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
 
            int count = 0;
            while (rs.next()) {
                count++;
                System.out.println("Contact #" + rs.getInt("id"));
                System.out.println("Nom complet : " + rs.getString("prenom") + " " + rs.getString("nom"));
                System.out.println("Téléphone   : " + rs.getString("telephone"));
                System.out.println("Email       : " + rs.getString("email"));
                System.out.println("Catégorie   : " + rs.getString("categorie"));
                System.out.println("Photo       : " + rs.getString("photo_path"));
                System.out.println("----------------------------------");
            }
            if (count == 0) System.out.println("Aucun contact trouvé.");
        } catch (SQLException e) {
            System.out.println("❌ Erreur MySQL : " + e.getMessage());
        }
    }
 
    public void supprimer(int id) {
        String sql = "DELETE FROM contacts WHERE id = ?";
        try (Connection conn = connect();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
 
            stmt.setInt(1, id);
            int affected = stmt.executeUpdate();
            if (affected > 0) {
                System.out.println("✅ Contact supprimé.");
            } else {
                System.out.println("❌ Aucun contact avec cet ID.");
            }
 
        } catch (SQLException e) {
            System.out.println("❌ Erreur MySQL : " + e.getMessage());
        }
    }
 
    public void rechercher(String motCle) {
        String sql = "SELECT * FROM contacts WHERE nom LIKE ? OR prenom LIKE ?";
        try (Connection conn = connect();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
 
            stmt.setString(1, "%" + motCle + "%");
            stmt.setString(2, "%" + motCle + "%");
            ResultSet rs = stmt.executeQuery();
            boolean found = false;
            while (rs.next()) {
                found = true;
                System.out.println("Contact #" + rs.getInt("id"));
                System.out.println("Nom complet : " + rs.getString("prenom") + " " + rs.getString("nom"));
                System.out.println("Téléphone   : " + rs.getString("telephone"));
                System.out.println("Email       : " + rs.getString("email"));
                System.out.println("Catégorie   : " + rs.getString("categorie"));
                System.out.println("Photo       : " + rs.getString("photo_path"));
                System.out.println("----------------------------------");
            }
            if (!found) System.out.println("Aucun contact trouvé.");
 
        } catch (SQLException e) {
            System.out.println("❌ Erreur MySQL : " + e.getMessage());
        }
    }
}
