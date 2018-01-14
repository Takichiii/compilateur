import java.io.*;
import java.util.ArrayList;

public class Main {

	public static void main(String[] args) throws Exception {
		/*if (args.length==0)
		{
			System.err.println("Veuillez entrer le chemin du fichier � compiler !");
			return;
		}*/
		InputStream in = new FileInputStream(new File("src/ressource/test2.txt"));
		Reader reader = new InputStreamReader(in);
		
		AnalyserLexical analyserLexical = new AnalyserLexical(reader);
		ArrayList<Token> list = (ArrayList<Token>) analyserLexical.getAllTokens();
//		for (Token t : list)
//		{
//			if ( t.categorie== KeyWord.TOK_ID)
//				System.out.println(t.identifiant);
//			else if (t.categorie == KeyWord.TOK_VALEUR)
//				System.out.println(t.valeur);
//			else System.out.println(t.categorie.valeur);
//		}
		AnalyserSyntaxique analyserSyntaxique = new AnalyserSyntaxique(list);
		
		
		Noeud n;
		try {
			n = analyserSyntaxique.master(analyserSyntaxique.getNextToken());
			n.print();
			AnalyserSemantique analyserSemantique = new AnalyserSemantique(n);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	

}
