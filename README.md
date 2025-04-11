# Projet_Programmation_Avanc-e
Projet Java en équipe
import java.io.Serializable;
 
public class Contact implements Serializable {
    private String nom;
    private String prenom;
    private String telephone;
    private String email;
    private String categorie;
    private String photoPath;
 
   public Contact(String nom, String prenom, String telephone, String email, String categorie, String photoPath) {
        this.nom = nom;
        this.prenom = prenom;
        this.telephone = telephone;
        this.email = email;
        this.categorie = categorie;
        this.photoPath = photoPath;
    }
 
   public String getNomComplet() {
        return prenom + " " + nom;
    }
 
   public String toString() {
        return "Nom complet : " + getNomComplet() +
               "\nTéléphone   : " + telephone +
               "\nEmail       : " + email +
               "\nCatégorie   : " + categorie +
               "\nPhoto       : " + (photoPath.isEmpty() ? "Aucune" : photoPath) + "\n";
    }
 
  // Getters & Setters ici si nécessaire
}
