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
			Token t1 = getNextToken();
			//appel de fonctions
			if (t1.getCategorie() == KeyWord.TOK_PARENTHESE_O){
				//TODO
				List<Noeud> enfants = new ArrayList<Noeud>();
				Noeud N;
				t=getNextToken();
				while((N = expression(t))!=null)
				{
					enfants.add(N);
					t=getNextToken();
				}
				return new Noeud(NoeudType.ND_FONCTION, t);
			}
			//identifiant
			else {
				undo();
				return new Noeud(NoeudType.ND_REFVAR, t);
			}
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
			if (N2 == null)
				throw new Exception("Y a rien après le * wesh");
			List<Noeud> enfants = new ArrayList<Noeud>();
			enfants.add(N);
			enfants.add(N2);
			return new Noeud(NoeudType.ND_MUL, enfants);
		}
		else if(t1.getCategorie() == KeyWord.TOK_DIV){
			Noeud N2 = facteur(getNextToken());
			if (N2 == null)
				throw new Exception("Y a rien après le / wesh");
			List<Noeud> enfants = new ArrayList<Noeud>();
			enfants.add(N);
			enfants.add(N2);
			return new Noeud(NoeudType.ND_DIV, enfants);
		}
		else if(t1.getCategorie() == KeyWord.TOK_MOD){
			Noeud N2 = terme(getNextToken());
			if (N2 == null)
				throw new Exception("Y a rien après le % wesh");
			List<Noeud> enfants = new ArrayList<Noeud>();
			enfants.add(N);
			enfants.add(N2);
			return new Noeud(NoeudType.ND_MOD, enfants);
		}
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
			Noeud N2 = terme(getNextToken());
			if (N2 == null)
				throw new Exception("Y a rien après le + wesh");
			List<Noeud> enfants = new ArrayList<Noeud>();
			enfants.add(N);
			enfants.add(N2);
			return new Noeud(NoeudType.ND_ADD, enfants);
		}
		else if(t1.getCategorie() == KeyWord.TOK_MOINS){
			Noeud N2 = terme(getNextToken());
			if (N2 == null)
				throw new Exception("Y a rien après le - wesh");
			List<Noeud> enfants = new ArrayList<Noeud>();
			enfants.add(N);
			enfants.add(N2);
			return new Noeud(NoeudType.ND_MOINS, enfants);
		}
		undo();
		return N;

	}

	public Noeud comparaison(Token t) throws Exception {
		Noeud N = terme(t);
		if (N == null) {
			return null;
		}
		Token t1 = getNextToken();
		if (t1 == null)
			return N;
		if (t1.getCategorie() == KeyWord.TOK_EQUAL) {
			Noeud N2 = comparaison(getNextToken());
			if (N2 == null)
				throw new Exception("Y a rien après le == wesh");
			List<Noeud> enfants = new ArrayList<Noeud>();
			enfants.add(N);
			enfants.add(N2);
			return new Noeud(NoeudType.ND_EQUAL, enfants);
		}else if (t1.getCategorie() == KeyWord.TOK_DIFF) {
			Noeud N2 = comparaison(getNextToken());
			if (N2 == null)
				throw new Exception("Y a rien après le != wesh");
			List<Noeud> enfants = new ArrayList<Noeud>();
			enfants.add(N);
			enfants.add(N2);
			return new Noeud(NoeudType.ND_DIFF, enfants);
		}else if (t1.getCategorie() == KeyWord.TOK_SUP) {
			Noeud N2 = comparaison(getNextToken());
			if (N2 == null)
				throw new Exception("Y a rien après le > wesh");
			List<Noeud> enfants = new ArrayList<Noeud>();
			enfants.add(N);
			enfants.add(N2);
			return new Noeud(NoeudType.ND_SUP, enfants);
		}else if (t1.getCategorie() == KeyWord.TOK_SUPEQUAL) {
			Noeud N2 = comparaison(getNextToken());
			if (N2 == null)
				throw new Exception("Y a rien après le >= wesh");
			List<Noeud> enfants = new ArrayList<Noeud>();
			enfants.add(N);
			enfants.add(N2);
			return new Noeud(NoeudType.ND_SUPEQUAL, enfants);
		}else if (t1.getCategorie() == KeyWord.TOK_INF) {
			Noeud N2 = comparaison(getNextToken());
			if (N2 == null)
				throw new Exception("Y a rien après le < wesh");
			List<Noeud> enfants = new ArrayList<Noeud>();
			enfants.add(N);
			enfants.add(N2);
			return new Noeud(NoeudType.ND_INF, enfants);
		}else if (t1.getCategorie() == KeyWord.TOK_INFEQUAL) {
			Noeud N2 = comparaison(getNextToken());
			if (N2 == null)
				throw new Exception("Y a rien après le <= wesh");
			List<Noeud> enfants = new ArrayList<Noeud>();
			enfants.add(N);
			enfants.add(N2);
			return new Noeud(NoeudType.ND_INFEQUAL, enfants);
		}
		undo();
		return N;
	}

	//L -> &&
	public Noeud logique(Token t) throws Exception {
		Noeud N = comparaison(t);
		if (N == null) {
			return null;
		}
		Token t1 = getNextToken();
		if (t1 == null)
			return N;
		if (t1.getCategorie() == KeyWord.TOK_ET) {
			Noeud N2 = logique(getNextToken());
			if (N2 == null)
				throw new Exception("Y a rien après le et wesh");
			List<Noeud> enfants = new ArrayList<Noeud>();
			enfants.add(N);
			enfants.add(N2);
			return new Noeud(NoeudType.ND_ET, enfants);
		}
		undo();
		return N;
	}

	public Noeud expression(Token t) throws Exception {
		Noeud N = logique(t);
		if (N == null) {
			return null;
		}
		Token t1 = getNextToken();
		if (t1 == null)
			return N;
		if (t1.getCategorie() == KeyWord.TOK_OU) {
			Noeud N2 = expression(getNextToken());
			if (N2 == null)
				throw new Exception("Y a rien après le ou wesh");
			List<Noeud> enfants = new ArrayList<Noeud>();
			enfants.add(N);
			enfants.add(N2);
			return new Noeud(NoeudType.ND_OU, enfants);
		}
		undo();
		return N;
	}

	public Noeud affectation(Token t) throws Exception {
		if (t.categorie == KeyWord.TOK_ID){
			t = getNextToken();
			if (t.categorie == KeyWord.TOK_AFF){

				Noeud N = expression(getNextToken());
				if (N == null) {
					throw  new Exception("Problème : affectation sans valeur!");
				}
				List<Noeud> enfants = new ArrayList<Noeud>();
				enfants.add(N);
				return new Noeud(NoeudType.ND_AFFVAR, enfants);

			}
			else {
				undo();
			}
		}
		return null;
	}


	public Noeud statement(Token t) throws Exception {
		//'{'S*'}'
		if (t.categorie == KeyWord.TOK_ACCOLADE_O){
			List<Noeud> enfants = new ArrayList<Noeud>();
			Noeud N;
			t=getNextToken();
			while((N = statement(t))!=null)
			{
				enfants.add(N);
				t=getNextToken();
			}
			if ( ! (t.categorie == KeyWord.TOK_ACCOLADE_F)){
				throw new Exception("Problème : pas de '}'");
			}
			else
			{
				return new Noeud(NoeudType.ND_BLOCK, enfants);
			}
		}
		//A';' | E ';'
		Noeud N = affectation(t);
		if (N != null || (N = expression(t)) != null){
			t = getNextToken();
			if (t.categorie == KeyWord.TOK_PV){
				return N;
			}else {
				throw new Exception("Problème : omission de ';' ");
			}
		}

		//if '('E')' S ('else' S)?
		if (t.categorie == KeyWord.TOK_IF){
			t = getNextToken();
			if (t.categorie == KeyWord.TOK_PARENTHESE_O){
				List<Noeud> enfants = new ArrayList<Noeud>();
				Noeud E = expression(getNextToken());
				if (E == null){
					throw new Exception("Problème : omission de la condition du if");
				}
				t = getNextToken();
				if (t.categorie == KeyWord.TOK_PARENTHESE_F){
					Noeud S1 = statement(getNextToken());
					if (S1 == null){
						throw new Exception("Problème : omission du statement du if");
					}
					enfants.add(E);
					enfants.add(S1);
					if (t.categorie == KeyWord.TOK_ELSE){
						Noeud S2 = statement(getNextToken());
						if (S2 == null){
							throw new Exception("Problème : omission du statement du else");
						}
						enfants.add(S2);
					}
					return new Noeud(NoeudType.ND_IF, enfants);
				}else {
					throw new Exception("Problème : omission parenthèse fermante");
				}
			}
			else {
				throw new Exception("Problème : omission parenthèse ouvrante");

			}

		}
		//while '(' E ')' S
		if (t.categorie == KeyWord.TOK_WHILE){
			t = getNextToken();
			if (t.categorie == KeyWord.TOK_PARENTHESE_O){
				List<Noeud> enfantsLoop = new ArrayList<Noeud>();
				List<Noeud> enfantsIf = new ArrayList<Noeud>();
				Noeud E = expression(getNextToken());
				if (E == null){
					throw new Exception("Problème : omission de la condition du while");
				}
				t = getNextToken();
				if (t.categorie == KeyWord.TOK_PARENTHESE_F){
					Noeud S = statement(getNextToken());
					if (S == null){
						throw new Exception("Problème : omission du statement du while");
					}
					enfantsIf.add(E);
					enfantsIf.add(S);
					enfantsIf.add(new Noeud(NoeudType.ND_BREAK));
					Noeud If = new Noeud(NoeudType.ND_IF, enfantsIf);//noeud if
					enfantsLoop.add(If);
					return new Noeud(NoeudType.ND_LOOP, enfantsLoop); //noeud loop
				}else {
					throw new Exception("Problème : omission parenthèse fermante");
				}
			}
			else {
				throw new Exception("Problème : omission parenthèse ouvrante");

			}

		}

		//do S while '(' E ')'
		//possibilité } mal placée
		if (t.categorie == KeyWord.TOK_DO) {
			Noeud S = statement(getNextToken());
			if (S == null) {
				throw new Exception("Problème : omission du statement du do");
			}
			t = getNextToken();
			if (t.categorie == KeyWord.TOK_WHILE) {
				t = getNextToken();
				if (t.categorie == KeyWord.TOK_PARENTHESE_O) {
					List<Noeud> enfantsLoop = new ArrayList<Noeud>();
					List<Noeud> enfantsBlock = new ArrayList<Noeud>();
					List<Noeud> enfantsIf = new ArrayList<Noeud>();
					Noeud E = expression(getNextToken());
					if (E == null) {
						throw new Exception("Problème : omission de la condition du while");
					}
					t = getNextToken();
					if (t.categorie == KeyWord.TOK_PARENTHESE_F) {
						//enfants du if
						enfantsIf.add(E);
						enfantsIf.add(new Noeud(NoeudType.ND_CONTINUE));
						enfantsIf.add(new Noeud(NoeudType.ND_BREAK));
						Noeud If = new Noeud(NoeudType.ND_IF, enfantsIf);//crée noeud if

						//enfants du block
						enfantsBlock.add(S);
						enfantsBlock.add(If);
						Noeud block = new Noeud(NoeudType.ND_BLOCK, enfantsIf);//crée noeud block

						//enfants du loop
						enfantsLoop.add(block);

						return new Noeud(NoeudType.ND_LOOP, enfantsLoop); //noeud loop
					} else {
						throw new Exception("Problème : omission parenthèse fermante");
					}
				} else {
					throw new Exception("Problème : omission parenthèse ouvrante");

				}

			}
		}

		//for(A1, E, A2)S
		if (t.categorie == KeyWord.TOK_FOR){
			t = getNextToken();
			if (t.categorie == KeyWord.TOK_PARENTHESE_O) {
				Noeud A1 = statement(getNextToken());
				if (A1 == null) {
					throw new Exception("Problème : omission de l'initialisation dans le for");
				}
				t = getNextToken();
				if (t.categorie == KeyWord.TOK_VIRGULE) {
					Noeud E = expression(getNextToken());
					if (E == null) {
						throw new Exception("Problème : omission de la condition d'arrêt dans le for");
					}
					t = getNextToken();
					if (t.categorie == KeyWord.TOK_VIRGULE) {
						Noeud A2 = statement(getNextToken());
						if (A2 == null) {
							throw new Exception("Problème : omission de l'incrémentation dans le for");
						}
						t = getNextToken();
						if (t.categorie == KeyWord.TOK_PARENTHESE_O) {
							Noeud S = statement(getNextToken());
							if (S == null) {
								throw new Exception("Problème : omission du statement du for");
							}
							List<Noeud> enfantsBlock = new ArrayList<Noeud>();
							List<Noeud> enfantsLoop = new ArrayList<Noeud>();
							List<Noeud> enfantsIf = new ArrayList<Noeud>();

							//enfants if
							enfantsIf.add(E);
							enfantsIf.add(S);
							enfantsIf.add(new Noeud(NoeudType.ND_BREAK));
							Noeud If = new Noeud(NoeudType.ND_IF, enfantsIf);//crée noeud if

							//enfants loop
							enfantsLoop.add(A2);
							enfantsLoop.add(If);
							Noeud loop = new Noeud(NoeudType.ND_LOOP, enfantsLoop);//crée noeud loop

							//enfants block
							enfantsBlock.add(A1);
							Noeud block = new Noeud(NoeudType.ND_BLOCK, enfantsBlock);//crée noeud block

							enfantsLoop.add(block);

							return new Noeud(NoeudType.ND_BLOCK, enfantsLoop); //noeud block
						}else {
							throw new Exception("Problème : omission ')' après l'incrémentation dans le for");

						}

					}else {
						throw new Exception("Problème : omission ',' après la condition d'arrêt dans le for");

					}

				}else {
					throw new Exception("Problème : omission ',' après initialisation dans le for");

				}
			}else {
				throw new Exception("Problème : omission parenthèse ouvrante");

			}
		}

		//break ;
		if (t.categorie == KeyWord.TOK_BREAK){
			t = getNextToken();
			if (t.categorie == KeyWord.TOK_PV){
				return new Noeud(NoeudType.ND_BREAK);
			}else {
				throw new Exception("Problème : omission de ';' après break");
			}
		}

		//continue;
		if (t.categorie == KeyWord.TOK_CONTINUE){
			t = getNextToken();
			if (t.categorie == KeyWord.TOK_PV){
				return new Noeud(NoeudType.ND_CONTINUE);
			}else {
				throw new Exception("Problème : omission de ';' après continue");
			}
		}

		//out E;
		if (t.categorie == KeyWord.TOK_OUT){
			Noeud E = expression(getNextToken());
			if (E == null){
				throw new Exception("Problème : omission de l'expression après le out");
			}
			t = getNextToken();
			if (t.categorie == KeyWord.TOK_PV){
				List<Noeud> enfants = new ArrayList<Noeud>();
				enfants.add(E);
				return new Noeud(NoeudType.ND_OUT, enfants);
			}else {
				throw new Exception("Problème : omission de ';' du out");
			}
		}


		return null;
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

		listToken.add(new Token(KeyWord.TOK_OU, 1, 0));
		listToken.add(new Token(5, 1, 0));


		AnalyserSyntaxique a = new AnalyserSyntaxique(listToken);
		Noeud n = a.expression(a.getNextToken());
		n.print();
		a.afficher(n);
		
	}
	

}
