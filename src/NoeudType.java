
public enum NoeudType {
	ND_MUL("*"),
	ND_ADD("+"),
	ND_DIV("/"),
	ND_MOINS("-"),
	ND_MOINSU("-"),

	ND_ET("&&"),
	ND_OU("||"),
	
	//type de noeud pour un identifiant
	ND_REFVAR("id"),
	ND_AFFVAR("id"),
	ND_BLOCK("block"),
	ND_VARDECL("varDecl"),
	
	ND_CONST("valeur"),

	//types de
	ND_MOD("%"), ND_NOT("!"),

	//types pour noeuds conditionnels
	ND_IF("if"),
	ND_LOOP("while"),


	ND_BREAK("break"),
	ND_CONTINUE("continue"),
	ND_EQUAL("=="),
	ND_DIFF("!="),
	ND_INF("<"),
	ND_INFEQUAL("<="),
	ND_SUP(">"),
	ND_SUPEQUAL(">="),


	ND_FONCTION("fonction");

	public String valeur;
	private NoeudType(String valeur){
		this.valeur=valeur;
	}

}
