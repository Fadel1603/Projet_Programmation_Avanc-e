import java.io.Serializable;
import java.util.regex.Pattern;

public class Contact implements Serializable {
    private String nom;
    private String prenom;
    private String telephone;
    private String email;
    private String categorie;


    public Contact(String nom, String prenom, String telephone, String email, String categorie) {
        if (!EmailValide(email)) {
            throw new IllegalArgumentException("Format de l’e-mail invalide !");
        }
        this.nom = nom;
        this.prenom = prenom;
        this.telephone = telephone;
        this.email = email;
        this.categorie = categorie;
        // on ne définit pas dateCreation ici, car MySQL s’en charge
    }


    private boolean EmailValide(String email) {
        String regex = "^[\\w.-]+@[\\w.-]+\\.[a-zA-Z]{2,6}$";
        return Pattern.matches(regex, email);
    }

    // Getters
    public String getNom() { return nom; }
    public String getPrenom() { return prenom; }
    public String getTelephone() { return telephone; }
    public String getEmail() { return email; }
    public String getCategorie() { return categorie; }


    public String getNomComplet() {
        return prenom + " " + nom;
    }

    @Override
    public String toString() {
        return "Nom complet : " + getNomComplet() +
                "\nTéléphone   : " + telephone +
                "\nEmail       : " + email +
                "\nCatégorie   : " + categorie;
    }
}
