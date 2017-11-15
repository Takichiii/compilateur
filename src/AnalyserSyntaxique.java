import java.util.ArrayList;
import java.util.List;

public class AnalyserSyntaxique {
	
	public List<Token> tokenList;
	public int compteur;

	public AnalyserSyntaxique(List<Token> parTokenList) {
		tokenList = parTokenList;
		compteur =0;
	}
	
	//crée noeud pour les types primaires
	public Noeud primaire(Token t) throws Exception {
		if (t==null)
		{
			return null;
		}
		if (t.categorie == KeyWord.TOK_VALEUR){
			return new Noeud(NoeudType.ND_CONST, t.getValeur());
		}
		if (t.categorie == KeyWord.TOK_ID){
			return new Noeud(NoeudType.ND_REFVAR, t);
		}
		if (t.categorie == KeyWord.TOK_MOINS){
			Noeud N = primaire(getNextToken());
			if (N == null)
				throw  new Exception("Problème : pas de valeur après le moins unitaire!");
			List<Noeud> enfants = new ArrayList<Noeud>();
			enfants.add(N);
			return new Noeud(NoeudType.ND_MOINSU, enfants);
		}
			
		return null;
	}

	public Noeud facteur(Token t) throws Exception {
		Noeud N = primaire(t);
		if (N == null) {
			return null;
		}
		Token t1 = getNextToken();
		if (t1 == null)
			return N;
		if (t1.getCategorie() == KeyWord.TOK_MUL) {
			Noeud N2 = facteur(getNextToken());
			List<Noeud> enfants = new ArrayList<Noeud>();
			enfants.add(N);
			enfants.add(N2);
			return new Noeud(NoeudType.ND_MUL, enfants);
		}
		else if(t1.getCategorie() == KeyWord.TOK_DIV){
			Noeud N2 = facteur(getNextToken());
			List<Noeud> enfants = new ArrayList<Noeud>();
			enfants.add(N);
			enfants.add(N2);
			return new Noeud(NoeudType.ND_DIV, enfants);
		}
		//to do %
		undo();
		return N;

	}

	public Noeud terme(Token t) throws Exception {
		Noeud N = facteur(t);
		if (N == null) {
			return null;
		}
		Token t1 = getNextToken();
		if (t1 == null)
			return N;
		if (t1.getCategorie() == KeyWord.TOK_ADD) {
			Noeud N2 = facteur(getNextToken());
			List<Noeud> enfants = new ArrayList<Noeud>();
			enfants.add(N);
			enfants.add(N2);
			return new Noeud(NoeudType.ND_ADD, enfants);
		}
		else if(t1.getCategorie() == KeyWord.TOK_MOINS){
			Noeud N2 = facteur(getNextToken());
			List<Noeud> enfants = new ArrayList<Noeud>();
			enfants.add(N);
			enfants.add(N2);
			return new Noeud(NoeudType.ND_MOINS, enfants);
		}
		//to do %
		undo();
		return N;

	}
	
	
	public void afficher(Noeud n)
	{
		if (n==null)
		{
			return;
		}
		if (n.getType() == NoeudType.ND_CONST)
			System.out.print(n.getValeur());
		if (n.getType() == NoeudType.ND_REFVAR)
			System.out.print(n.getIdentifiant());
		if (n.getType() == NoeudType.ND_MOINSU) {
			System.out.print("-");
			afficher(n.getEnfants().get(0)); //la liste de tokens ne contient qu'une seule valeur
		}
		if (n.getType() == NoeudType.ND_MUL) {
			afficher(n.getEnfants().get(0));
			System.out.print("*");
			afficher(n.getEnfants().get(1));
		}
		if (n.getType() == NoeudType.ND_DIV) {
			afficher(n.getEnfants().get(0));
			System.out.print("/");
			afficher(n.getEnfants().get(1));
		}
		if (n.getType() == NoeudType.ND_ADD) {
			afficher(n.getEnfants().get(0));
			System.out.print("+");
			afficher(n.getEnfants().get(1));
		}
		if (n.getType() == NoeudType.ND_MOINS) {
			afficher(n.getEnfants().get(0));
			System.out.print("-");
			afficher(n.getEnfants().get(1));
		}
	}

	public Token getNextToken(){
		if (compteur<tokenList.size())
		{
			return tokenList.get(compteur++);

		}
		return null;

	}

	public void undo(){
		compteur--;

	}
	
	public List<Token> getTokenList() {
		return tokenList;
	}

	public void setTokenList(List<Token> tokenList) {
		this.tokenList = tokenList;
	}

	public static void main(String[] args) throws Exception {
		List <Token> listToken = new ArrayList<Token>();
		listToken.add(new Token(KeyWord.TOK_MOINS, 0, 0));
		listToken.add(new Token(3, 1, 0));
		listToken.add(new Token(KeyWord.TOK_ADD, 1, 0));
		listToken.add(new Token(3, 1, 0));
		listToken.add(new Token(KeyWord.TOK_DIV, 1, 0));
		listToken.add(new Token(3, 1, 0));
		AnalyserSyntaxique a = new AnalyserSyntaxique(listToken);
		Noeud n = a.terme(a.getNextToken());
		a.afficher(n);
		
	}
	

}
