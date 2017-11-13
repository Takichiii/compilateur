import java.util.ArrayList;
import java.util.List;

public class Noeud {
	
	public NoeudType type;
	public int valeur;
	public String nom;
	public List<Noeud> enfants;

	public Noeud(NoeudType type, int valeur){
		this.type = type;
		this.valeur = valeur;
	}
	public Noeud(NoeudType type, String nom){
		this.type = type;
		this.nom = nom;
	}
	public Noeud(NoeudType type, List<Noeud> enfants){
		this.type = type;
		this.enfants = enfants; 
	}
	public NoeudType getType() {
		return type;
	}
	public void setType(NoeudType type) {
		this.type = type;
	}
	public int getValeur() {
		return valeur;
	}
	public void setValeur(int valeur) {
		this.valeur = valeur;
	}
	public String getNom() {
		return nom;
	}
	public void setNom(String nom) {
		this.nom = nom;
	}
	public List<Noeud> getEnfants() {
		return enfants;
	}
	public void setEnfants(List<Noeud> enfants) {
		this.enfants = enfants;
	}
	
	

}
