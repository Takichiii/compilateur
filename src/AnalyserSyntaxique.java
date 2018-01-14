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
	public Noeud primaire(Token t) throws AnalyserSyntaxiqueException {
		if (t==null)
		{
			return null;
		}
		if (t.categorie == KeyWord.TOK_VALEUR){
			return new Noeud(NoeudType.ND_CONST, t);
		}
		if (t.categorie == KeyWord.TOK_ID) {
			Token t1 = getNextToken();
			//appel de fonction
			if (t1.getCategorie() == KeyWord.TOK_PARENTHESE_O) {
				t1 = getNextToken();
				if (t1.categorie == KeyWord.TOK_VALEUR || t1.categorie == KeyWord.TOK_ID) {//si fonction avec paramètres
					List<Noeud> enfants = new ArrayList<Noeud>();
					enfants.add(new Noeud(NoeudType.ND_REFVAR, t1)); //1er argument
					t1 = getNextToken();
					while (t1.categorie == KeyWord.TOK_VIRGULE) { //plus d'1 argument
						t1 = getNextToken();
						if (t1.categorie == KeyWord.TOK_VALEUR || t1.categorie == KeyWord.TOK_ID) {
							enfants.add(new Noeud(NoeudType.ND_REFVAR, t1));
							t1 = getNextToken();
						} else
							throw new AnalyserSyntaxiqueException("Deuxième argument manquant ou virgule en trop!", t);
					}
				}
				if (t1.categorie == KeyWord.TOK_PARENTHESE_F) { //si fonction sans paramètres
					return new Noeud(NoeudType.ND_CALL, t1);
				} else throw new AnalyserSyntaxiqueException("Oublie de la parenthèse fermante", t);
			}
			//simple identifiant
			else {
				undo();
				return new Noeud(NoeudType.ND_REFVAR, t);
			}
		}

		if (t.categorie == KeyWord.TOK_MOINS){
			Noeud N = primaire(getNextToken());
			if (N == null)
				throw  new AnalyserSyntaxiqueException("Problème : pas de valeur après le moins unitaire!", t);
			List<Noeud> enfants = new ArrayList<Noeud>();
			enfants.add(N);
			return new Noeud(NoeudType.ND_MOINSU, enfants);
		}
		return null;
	}

	public Noeud facteur(Token t) throws AnalyserSyntaxiqueException {
		if (t==null)
			return null;
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
				throw new AnalyserSyntaxiqueException("Y a rien après le * wesh", t1);
			List<Noeud> enfants = new ArrayList<Noeud>();
			enfants.add(N);
			enfants.add(N2);
			return new Noeud(NoeudType.ND_MUL, enfants);
		}
		else if(t1.getCategorie() == KeyWord.TOK_DIV){
			Noeud N2 = facteur(getNextToken());
			if (N2 == null)
				throw new AnalyserSyntaxiqueException("Y a rien après le / wesh", t1);
			List<Noeud> enfants = new ArrayList<Noeud>();
			enfants.add(N);
			enfants.add(N2);
			return new Noeud(NoeudType.ND_DIV, enfants);
		}
		else if(t1.getCategorie() == KeyWord.TOK_MOD){
			Noeud N2 = terme(getNextToken());
			if (N2 == null)
				throw new AnalyserSyntaxiqueException("Y a rien après le % wesh", t1);
			List<Noeud> enfants = new ArrayList<Noeud>();
			enfants.add(N);
			enfants.add(N2);
			return new Noeud(NoeudType.ND_MOD, enfants);
		}
		undo();
		return N;

	}

	public Noeud terme(Token t) throws AnalyserSyntaxiqueException {
		if (t==null)
			return null;
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
				throw new AnalyserSyntaxiqueException("Y a rien après le + wesh", t1);
			List<Noeud> enfants = new ArrayList<Noeud>();
			enfants.add(N);
			enfants.add(N2);
			return new Noeud(NoeudType.ND_ADD, enfants);
		}
		else if(t1.getCategorie() == KeyWord.TOK_MOINS){
			Noeud N2 = terme(getNextToken());
			if (N2 == null)
				throw new AnalyserSyntaxiqueException("Y a rien après le - wesh", t1);
			List<Noeud> enfants = new ArrayList<Noeud>();
			enfants.add(N);
			enfants.add(N2);
			return new Noeud(NoeudType.ND_MOINS, enfants);
		}
		undo();
		return N;

	}

	public Noeud comparaison(Token t) throws AnalyserSyntaxiqueException {
		if (t==null)
			return null;
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
				throw new AnalyserSyntaxiqueException("Y a rien après le == wesh", t1);
			List<Noeud> enfants = new ArrayList<Noeud>();
			enfants.add(N);
			enfants.add(N2);
			return new Noeud(NoeudType.ND_EQUAL, enfants);
		}else if (t1.getCategorie() == KeyWord.TOK_DIFF) {
			Noeud N2 = comparaison(getNextToken());
			if (N2 == null)
				throw new AnalyserSyntaxiqueException("Y a rien après le != wesh", t1);
			List<Noeud> enfants = new ArrayList<Noeud>();
			enfants.add(N);
			enfants.add(N2);
			return new Noeud(NoeudType.ND_DIFF, enfants);
		}else if (t1.getCategorie() == KeyWord.TOK_SUP) {
			Noeud N2 = comparaison(getNextToken());
			if (N2 == null)
				throw new AnalyserSyntaxiqueException("Y a rien après le > wesh", t1);
			List<Noeud> enfants = new ArrayList<Noeud>();
			enfants.add(N);
			enfants.add(N2);
			return new Noeud(NoeudType.ND_SUP, enfants);
		}else if (t1.getCategorie() == KeyWord.TOK_SUPEQUAL) {
			Noeud N2 = comparaison(getNextToken());
			if (N2 == null)
				throw new AnalyserSyntaxiqueException("Y a rien après le >= wesh", t1);
			List<Noeud> enfants = new ArrayList<Noeud>();
			enfants.add(N);
			enfants.add(N2);
			return new Noeud(NoeudType.ND_SUPEQUAL, enfants);
		}else if (t1.getCategorie() == KeyWord.TOK_INF) {
			Noeud N2 = comparaison(getNextToken());
			if (N2 == null)
				throw new AnalyserSyntaxiqueException("Y a rien après le < wesh", t1);
			List<Noeud> enfants = new ArrayList<Noeud>();
			enfants.add(N);
			enfants.add(N2);
			return new Noeud(NoeudType.ND_INF, enfants);
		}else if (t1.getCategorie() == KeyWord.TOK_INFEQUAL) {
			Noeud N2 = comparaison(getNextToken());
			if (N2 == null)
				throw new AnalyserSyntaxiqueException("Y a rien après le <= wesh", t1);
			List<Noeud> enfants = new ArrayList<Noeud>();
			enfants.add(N);
			enfants.add(N2);
			return new Noeud(NoeudType.ND_INFEQUAL, enfants);
		}
		undo();
		return N;
	}

	//L -> &&
	public Noeud logique(Token t) throws AnalyserSyntaxiqueException {
		if (t==null)
			return null;
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
				throw new AnalyserSyntaxiqueException("Y a rien après le et wesh", t1);
			List<Noeud> enfants = new ArrayList<Noeud>();
			enfants.add(N);
			enfants.add(N2);
			return new Noeud(NoeudType.ND_AND, enfants);
		}
		undo();
		return N;
	}

	public Noeud expression(Token t) throws AnalyserSyntaxiqueException {
		if (t==null)
			return null;
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
				throw new AnalyserSyntaxiqueException("Y a rien après le ou wesh", t1);
			List<Noeud> enfants = new ArrayList<Noeud>();
			enfants.add(N);
			enfants.add(N2);
			return new Noeud(NoeudType.ND_OU, enfants);
		}
		undo();
		return N;
	}

	public Noeud affectation(Token t) throws AnalyserSyntaxiqueException {
		if (t==null)
			return null;
		if (t.categorie == KeyWord.TOK_ID){
			Token t1 = getNextToken();
			if (t1!=null && t1.categorie == KeyWord.TOK_AFF){

				Noeud N = expression(getNextToken());
				if (N == null) {
					throw  new AnalyserSyntaxiqueException("Problème : affectation sans valeur!", t);
				}
				List<Noeud> enfants = new ArrayList<Noeud>();
				enfants.add(N);
				return new Noeud(NoeudType.ND_AFFVAR, enfants, t);

			}
			else {
				undo();
			}
		}
		return null;
	}


	public Noeud statement(Token t) throws AnalyserSyntaxiqueException {
		//'{'S*'}'
		if (t==null)
		{
			return null;
		}
		if (t.categorie == KeyWord.TOK_ACCOLADE_O){
			List<Noeud> enfants = new ArrayList<Noeud>();
			Noeud N;
			Token t1=getNextToken();
			while((N = statement(t1))!=null)
			{
				enfants.add(N);
				t1=getNextToken();
			}
			if (t1==null || !(t1.categorie == KeyWord.TOK_ACCOLADE_F)){
				throw new AnalyserSyntaxiqueException("Problème : pas de '}'", t);
			}
			else
			{
				return new Noeud(NoeudType.ND_BLOCK, enfants);
			}
		}
		//A';' | E ';'
		Noeud N = affectation(t);
		if (N != null || (N = expression(t)) != null){
			Token t1 = getNextToken();
			if (t1!=null && t1.categorie == KeyWord.TOK_PV){
				return N;
			}else {
				throw new AnalyserSyntaxiqueException("Problème : omission de ';' ", t);
			}
		}else {
			N = expression(t);
			if (N != null) {
				Token t1 = getNextToken();
				if (t1 != null && t1.categorie == KeyWord.TOK_PV) {
					List<Noeud> enfants = new ArrayList<Noeud>();
					enfants.add(N);
					return new Noeud(NoeudType.ND_DROP, enfants);
				} else {
					throw new AnalyserSyntaxiqueException("Problème : omission de ';' ", t);
				}
			}
		}



		//if '('E')' S ('else' S)?
		if (t.categorie == KeyWord.TOK_IF){
			Token t1 = getNextToken();
			if (t1!=null && t1.categorie == KeyWord.TOK_PARENTHESE_O){
				List<Noeud> enfants = new ArrayList<Noeud>();
				Noeud E = expression(getNextToken());
				if (E == null){
					throw new AnalyserSyntaxiqueException("Problème : omission de la condition du if", t);
				}
				t1 = getNextToken();
				if (t1!=null && t1.categorie == KeyWord.TOK_PARENTHESE_F){
					Noeud S1 = statement(getNextToken());
					if (S1 == null){
						throw new AnalyserSyntaxiqueException("Problème : omission du statement du if", t);
					}
					enfants.add(E);
					enfants.add(S1);
					t1 = getNextToken();
					if (t1!=null && t1.categorie == KeyWord.TOK_ELSE){
						Noeud S2 = statement(getNextToken());
						if (S2 == null){
							throw new AnalyserSyntaxiqueException("Problème : omission du statement du else", t);
						}
						enfants.add(S2);
					}
					else 
						undo();
					return new Noeud(NoeudType.ND_IF, enfants);
				}else {
					throw new AnalyserSyntaxiqueException("Problème : omission parenthèse fermante", t);
				}
			}
			else {
				throw new AnalyserSyntaxiqueException("Problème : omission parenthèse ouvrante", t);

			}

		}
		//while '(' E ')' S
		if (t.categorie == KeyWord.TOK_WHILE){
			Token t1 = getNextToken();
			if (t1!=null && t1.categorie == KeyWord.TOK_PARENTHESE_O){
				List<Noeud> enfantsLoop = new ArrayList<Noeud>();
				List<Noeud> enfantsIf = new ArrayList<Noeud>();
				Noeud E = expression(getNextToken());
				if (E == null){
					throw new AnalyserSyntaxiqueException("Problème : omission de la condition du while", t);
				}
				t1 = getNextToken();
				if (t1!=null && t1.categorie == KeyWord.TOK_PARENTHESE_F){
					Noeud S = statement(getNextToken());
					if (S == null){
						throw new AnalyserSyntaxiqueException("Problème : omission du statement du while", t);
					}
					enfantsIf.add(E);
					enfantsIf.add(S);
					enfantsIf.add(new Noeud(NoeudType.ND_BREAK, t1));
					Noeud If = new Noeud(NoeudType.ND_IF, enfantsIf);//noeud if
					enfantsLoop.add(If);
					return new Noeud(NoeudType.ND_LOOP, enfantsLoop); //noeud loop
				}else {
					throw new AnalyserSyntaxiqueException("Problème : omission parenthèse fermante", t);
				}
			}
			else {
				throw new AnalyserSyntaxiqueException("Problème : omission parenthèse ouvrante", t);

			}

		}

		//do S while '(' E ')'
		//possibilité } mal placée
		if (t.categorie == KeyWord.TOK_DO) {
			Noeud S = statement(getNextToken());
			if (S == null) {
				throw new AnalyserSyntaxiqueException("Problème : omission du statement du do", t);
			}
			Token t1 = getNextToken();
			if (t1!=null && t1.categorie == KeyWord.TOK_WHILE) {
				t1 = getNextToken();
				if (t1!=null && t1.categorie == KeyWord.TOK_PARENTHESE_O) {
					List<Noeud> enfantsLoop = new ArrayList<Noeud>();
					List<Noeud> enfantsBlock = new ArrayList<Noeud>();
					List<Noeud> enfantsIf = new ArrayList<Noeud>();
					Noeud E = expression(getNextToken());
					if (E == null) {
						throw new AnalyserSyntaxiqueException("Problème : omission de la condition du while", t);
					}
					t1 = getNextToken();
					if (t1!=null && t1.categorie == KeyWord.TOK_PARENTHESE_F) {
						//enfants du if
						enfantsIf.add(E);
						enfantsIf.add(new Noeud(NoeudType.ND_CONTINUE,t1));
						enfantsIf.add(new Noeud(NoeudType.ND_BREAK,t1));
						Noeud If = new Noeud(NoeudType.ND_IF, enfantsIf);//crée noeud if

						//enfants du block
						enfantsBlock.add(S);
						enfantsBlock.add(If);
						Noeud block = new Noeud(NoeudType.ND_BLOCK, enfantsIf);//crée noeud block

						//enfants du loop
						enfantsLoop.add(block);

						return new Noeud(NoeudType.ND_LOOP, enfantsLoop); //noeud loop
					} else {
						throw new AnalyserSyntaxiqueException("Problème : omission parenthèse fermante", t);
					}
				} else {
					throw new AnalyserSyntaxiqueException("Problème : omission parenthèse ouvrante", t);

				}

			}
		}

		//for(A1, E, A2)S
		if (t.categorie == KeyWord.TOK_FOR){
			Token t1 = getNextToken();
			if (t1!=null && t1.categorie == KeyWord.TOK_PARENTHESE_O) {
				Noeud A1 = statement(getNextToken());
				if (A1 == null) {
					throw new AnalyserSyntaxiqueException("Problème : omission de l'initialisation dans le for", t);
				}
				t1 = getNextToken();
				if (t1!=null && t1.categorie == KeyWord.TOK_VIRGULE) {
					Noeud E = expression(getNextToken());
					if (E == null) {
						throw new AnalyserSyntaxiqueException("Problème : omission de la condition d'arrêt dans le for", t);
					}
					t1 = getNextToken();
					if (t1!=null && t1.categorie == KeyWord.TOK_VIRGULE) {
						Noeud A2 = statement(getNextToken());
						if (A2 == null) {
							throw new AnalyserSyntaxiqueException("Problème : omission de l'incrémentation dans le for", t);
						}
						t1 = getNextToken();
						if (t1!=null && t1.categorie == KeyWord.TOK_PARENTHESE_F) {
							Noeud S = statement(getNextToken());
							if (S == null) {
								throw new AnalyserSyntaxiqueException("Problème : omission du statement du for", t);
							}
							List<Noeud> enfantsBlock = new ArrayList<Noeud>();
							List<Noeud> enfantsLoop = new ArrayList<Noeud>();
							List<Noeud> enfantsIf = new ArrayList<Noeud>();

							//enfants if
							enfantsIf.add(E);
							enfantsIf.add(S);
							enfantsIf.add(new Noeud(NoeudType.ND_BREAK,t1));
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
							throw new AnalyserSyntaxiqueException("Problème : omission ')' après l'incrémentation dans le for", t);

						}

					}else {
						throw new AnalyserSyntaxiqueException("Problème : omission ',' après la condition d'arrêt dans le for", t);

					}

				}else {
					throw new AnalyserSyntaxiqueException("Problème : omission ',' après initialisation dans le for", t);

				}
			}else {
				throw new AnalyserSyntaxiqueException("Problème : omission parenthèse ouvrante", t);

			}
		}

		//break ;
		if (t.categorie == KeyWord.TOK_BREAK){
			Token t1 = getNextToken();
			if (t1!=null &&  t1.categorie == KeyWord.TOK_PV){
				return new Noeud(NoeudType.ND_BREAK, t1);
			}else {
				throw new AnalyserSyntaxiqueException("Problème : omission de ';' après break", t);
			}
		}

		//continue;
		if (t.categorie == KeyWord.TOK_CONTINUE){
			Token t1 = getNextToken();
			if (t1!=null && t1.categorie == KeyWord.TOK_PV){
				return new Noeud(NoeudType.ND_CONTINUE,t1);
			}else {
				throw new AnalyserSyntaxiqueException("Problème : omission de ';' après continue", t);
			}
		}

		//out E;
		if (t.categorie == KeyWord.TOK_OUT){
			Noeud E = expression(getNextToken());
			if (E == null){
				throw new AnalyserSyntaxiqueException("Problème : omission de l'expression après le out", t);
			}
			Token t1 = getNextToken();
			if (t1!=null && t1.categorie == KeyWord.TOK_PV){
				List<Noeud> enfants = new ArrayList<Noeud>();
				enfants.add(E);
				return new Noeud(NoeudType.ND_OUT, enfants);
			}else {
				throw new AnalyserSyntaxiqueException("Problème : omission de ';' du out", t);
			}
		}

		//return E;
		if (t.categorie == KeyWord.TOK_RETURN){
			Noeud E = expression(getNextToken());
			if (E == null){
				throw new AnalyserSyntaxiqueException("Problème : omission de l'expression après le return", t);
			}
			Token t1 = getNextToken();
			if (t1!=null && t1.categorie == KeyWord.TOK_PV){
				List<Noeud> enfants = new ArrayList<Noeud>();
				enfants.add(E);
				return new Noeud(NoeudType.ND_RETURN, enfants);
			}else {
				throw new AnalyserSyntaxiqueException("Problème : omission de ';' du return", t);
			}
		}

		//
		if (t.categorie == KeyWord.TOK_INT) {
			Token idToken = getNextToken();
			if (idToken != null && idToken.categorie == KeyWord.TOK_ID) {
				Token t1 = getNextToken();
				if (t1 != null && t1.categorie == KeyWord.TOK_PV) {
					return new Noeud(NoeudType.ND_VARDECL, idToken);
				}
			}
		}

		return null;
	}
	
	public Noeud definition(Token t) throws Exception {
		//int ident'('int ident*')' S
		if (t==null)
		{
			return null;
		}
		if (t.categorie == KeyWord.TOK_INT) {
			Token idToken = getNextToken();
			if (idToken != null && idToken.categorie == KeyWord.TOK_ID) {
				List<String> args = new ArrayList<>();
				Token t1 = getNextToken();
				if (t1 != null && t1.categorie == KeyWord.TOK_PARENTHESE_O) { //si fonction avec paramètres
					t1 = getNextToken();
					if (t1 != null && t1.categorie == KeyWord.TOK_INT) {
						t1 = getNextToken();
						if (t1 != null && t1.categorie == KeyWord.TOK_ID) {
							args.add(t1.getIdentifiant());
							t1 = getNextToken();
							while (t1 != null && t1.categorie == KeyWord.TOK_VIRGULE) {
								t1 = getNextToken();
								if (t1 != null && t1.categorie == KeyWord.TOK_INT) {
									t1 = getNextToken();
									if (t1 != null && t1.categorie == KeyWord.TOK_ID) {
										args.add(t1.getIdentifiant());
										t1 = getNextToken();
									}
									else throw new AnalyserSyntaxiqueException("id manquant", t);
								}
								else throw new AnalyserSyntaxiqueException("int manquant", t);
							}
						}
						else throw new AnalyserSyntaxiqueException("id manquant", t);
					}
					if (t1 != null && t1.categorie == KeyWord.TOK_PARENTHESE_F) {
						Noeud S = statement(getNextToken());
						if (S == null)
							throw new AnalyserSyntaxiqueException("Y a rien après la signature wesh' ", t1);
						List<Noeud> enfants = new ArrayList<Noeud>();
						enfants.add(S);
						Noeud N = new Noeud(NoeudType.ND_DEFFONCTION, enfants, idToken);
						N.setArgs(args);
						return N;
					}
					else throw new AnalyserSyntaxiqueException("Oublie de la parenthèse fermante", t);
				}
				else throw new AnalyserSyntaxiqueException("Définition fonction sans '(' " , t);
			}
			else throw new AnalyserSyntaxiqueException("Définition fonction incomplète : int tout seul " , t);
		}

		return null;
	}

	public Noeud master(Token t) throws Exception {
		if (t == null){
			throw new AnalyserSyntaxiqueException("Aucun token !", t);
		}
		List<Noeud> enfants = new ArrayList<Noeud>();
		Noeud D;
		while((D = definition(t))!=null)
		{
			enfants.add(D);
			t=getNextToken();
		}
		if (getNextToken()!= null){
			throw new AnalyserSyntaxiqueException("Token tout seul ou fonction mal définie", t);
		}

		return new Noeud(NoeudType.ND_MASTER, enfants);
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

//	public static void main(String[] args) throws AnalyserSyntaxiqueException {
//		List <Token> listToken = new ArrayList<Token>();
//		listToken.add(new Token(KeyWord.TOK_MOINS, 0, 0));
//		listToken.add(new Token(3, 1, 0));
//		listToken.add(new Token(KeyWord.TOK_ADD, 1, 0));
//		listToken.add(new Token(3, 1, 0));
//
//		listToken.add(new Token(KeyWord.TOK_DIV, 1, 0));
//		listToken.add(new Token(3, 1, 0));
//
//		listToken.add(new Token(KeyWord.TOK_OU, 1, 0));
//		listToken.add(new Token(5, 1, 0));
//
//
//		AnalyserSyntaxique a = new AnalyserSyntaxique(listToken);
//		Noeud n = a.expression(a.getNextToken());
//		n.print();
//		//a.afficher(n);
//		
//	}
	

}
