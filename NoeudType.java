
public enum NoeudType {
	ND_MUL("*"),
	ND_ADD("+"),
	ND_DIV("/"),
	ND_MOINS("-"),
	ND_MOINSU("-"),
	
	//type de noeud pour un identifiant
	ND_REFVAR("id"),
	ND_CREERVAR("id"),
	//?
	//?
	
	ND_VALEUR("valeur")
	
	
	;
	public String valeur;
	private NoeudType(String valeur){
		this.valeur=valeur;
	}

}
