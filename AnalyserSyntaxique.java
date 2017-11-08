import java.util.ArrayList;
import java.util.List;

public class AnalyserSyntaxique {
	
	public AnalyserLexical analyserLexical;
	public List<Token> L; //tokenList

	public AnalyserSyntaxique(AnalyserLexical analyserLexical) {
		this.analyserLexical = analyserLexical;
		L = new ArrayList<>();
		Token t1 = new Token("-",KeyWord.TOK_MOINS, 0, 0);
		Token t2 = new Token(3, 1, 0);
		L.add(t1);
		L.add(t2);
	}
	
	//crée noeud pour les types primaires
	public Noeud primaire(){
		for (int i=0; L.size(); i++){
			if (t.categorie == KeyWord.TOK_VALEUR){
				return new Noeud(NoeudType.ND_VALEUR, t.getValeur());
			}
			if (t.categorie == KeyWord.TOK_ID){
				return new Noeud(NoeudType.ND_REFVAR, t.getIdentifiant());
			}
			if (t.categorie == KeyWord.TOK_MOINS){
				Noeud N = primaire(L, L)
				return new Noeud(NoeudType.ND_REFVAR, t.getIdentifiant());
			}
			
		}
		return null;
	}
	
	
	public static void main(String[] args){
	}
	

}
