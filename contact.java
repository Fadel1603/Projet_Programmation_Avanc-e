import java.io.Serializable;
import java.util.regex.Pattern;
public class Contact implements Serializable {
    // Attributs privés représentant les informations du contact
    private String nom;
    private String prenom;
    private String telephone;
    private String email;
    private String categorie;

    // Constructeur pour initialiser les attributs du contact
    public Contact(String nom, String prenom, String telephone, String email, String categorie) {
        this.nom = nom;
        this.prenom = prenom;
        this.telephone = telephone;
        this.email = email;
        this.categorie = categorie; }

    private boolean EmailValide(String email)
    {String regex = "^[\\w.-]+@[\\w.-]+\\.[a-zA-Z]{2,6}$";
        return Pattern.matches(regex, email);}

    // Méthodes getters pour accéder aux valeurs des attributs
    public String getNom() { return nom;}
    public String getPrenom() { return prenom;}
    public String getTelephone() { return telephone;}
    public String getEmail() { return email;}
    public String getCategorie() { return categorie;}

    // Méthode pour retourner le nom complet
    public String getNomComplet() {
        return prenom + " " + nom; }

    // Redéfinition de la méthode toString pour afficher les informations du contact
    @Override
    public String toString() {
        return "Nom complet : " + getNomComplet() +
                "\nTéléphone   : " + telephone +
                "\nEmail       : " + email +
                "\nCatégorie   : " + categorie;
    } }
