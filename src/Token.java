
public class Token {

	public KeyWord categorie;
	public String identifiant;
	public int valeur;
	
	public int ligne;
	public int colonne;

	//constructeur pour les opérateurs
	public Token (KeyWord categorie, int ligne, int colonne)
	{
		this.categorie=categorie;
		this.ligne=ligne;
		this.colonne=colonne;
	}


	//constructeur pour les identifiants
	public Token (String identifiant, int ligne, int colonne)
	{
		this.identifiant=identifiant;
		this.categorie=KeyWord.TOK_ID;
		this.ligne=ligne;
		this.colonne=colonne;
	}


	//constructeur pour les valeurs
	public Token (int valeur, int ligne, int colonne)
	{
		this.valeur=valeur;
		this.ligne=ligne;
		this.colonne=colonne;
		this.categorie=KeyWord.TOK_VALEUR;
	}
	
	public KeyWord getCategorie() {
		return categorie;
	}

	public void setCategorie(KeyWord categorie) {
		this.categorie = categorie;
	}

	public String getIdentifiant() {
		return identifiant;
	}

	public void setIdentifiant(String identifiant) {
		this.identifiant = identifiant;
	}

	public int getValeur() {
		return valeur;
	}

	public void setValeur(int valeur) {
		this.valeur = valeur;
	}

	public int getLigne() {
		return ligne;
	}

	public void setLigne(int ligne) {
		this.ligne = ligne;
	}

	public int getColonne() {
		return colonne;
	}

	public void setColonne(int colonne) {
		this.colonne = colonne;
	}

	@Override
	public String toString() {

		if (valeur == 0) {
			return identifiant;
		}
		if (identifiant == null) {
			return Integer.toString(valeur);
		}
		return "autre";
	}
}
