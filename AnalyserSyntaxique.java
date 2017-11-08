import java.util.ArrayList;
import java.util.List;

import com.sun.java.accessibility.util.java.awt.ListTranslator;

public class AnalyserSyntaxique {
	
	public List<Token> tokenList;
	public int compteur;

	public AnalyserSyntaxique(List<Token> parTokenList) {
		tokenList = parTokenList;
		compteur =0;
	}
	
	//crée noeud pour les types primaires
	public Noeud primaire(){
		Token t = getNextToken();
		if (t==null)
		{
			return null;
		}
		if (t.categorie == KeyWord.TOK_VALEUR){
			return new Noeud(NoeudType.ND_VALEUR, t.getValeur());
		}
		if (t.categorie == KeyWord.TOK_ID){
			return new Noeud(NoeudType.ND_REFVAR, t.getIdentifiant());
		}
		if (t.categorie == KeyWord.TOK_MOINS){
			Noeud N = primaire();
			List<Noeud> enfants = new ArrayList<Noeud>();
			enfants.add(N);
			return new Noeud(NoeudType.ND_MOINSU, enfants);
		}
			
		return null;
	}
	
	
	public void afficheArbre(Noeud n)
	{
		if (n==null)
		{
			return;
		}
		System.out.println();
	}
	
	public Token getNextToken(){
		if (compteur+1>=tokenList.size())
		{
			return null;
		}
		return tokenList.get(compteur++);
	}
	
	public List<Token> getTokenList() {
		return tokenList;
	}

	public void setTokenList(List<Token> tokenList) {
		this.tokenList = tokenList;
	}

	public static void main(String[] args){
		List <Token> listToken = new ArrayList<Token>();
		listToken.add(new Token("-",KeyWord.TOK_MOINS, 0, 0));
		//listToken.add(new Token("+",KeyWord.TOK_ADD, 1, 0));
		listToken.add(new Token(3, 1, 0));
		AnalyserSyntaxique a = new AnalyserSyntaxique(listToken);
		Noeud n = a.primaire();
		a.afficheArbre(n);
		
		
		
	}
	

}
