
public class AnalyserSemantique {

	private int nbVar;
	private TableSymboles sym;
	
	public AnalyserSemantique(Noeud n) throws AnalyserSemantiqueException
	{
		nbVar=0;
		sym = new TableSymboles();
		semLoop(n);
		sem(n);
	}
	
	public void sem(Noeud n) throws AnalyserSemantiqueException
	{
		if (n.getType()==NoeudType.ND_BLOCK || n.getType()==NoeudType.ND_MASTER)
		{
			sym.debutBloc();
			for(Noeud enfant : n.getEnfants())
			{
				sem(enfant);
			}
			sym.finBloc();
		}
		else if (n.getType()==NoeudType.ND_VARDECL)
		{
			Symbole s = sym.newSymbole(n.getIdentifiant(), 0);
			if (s==null)
				throw new AnalyserSemantiqueException("Variable déja existante",n.getToken());
			s.setSlot(nbVar++);
		}
		else if (n.getType()==NoeudType.ND_REFVAR || n.getType()==NoeudType.ND_AFFVAR)
		{
			Symbole s = sym.recherche(n.getToken());
			n.setSlot(s.getSlot());
		}
		else if (n.getType()==NoeudType.ND_DEFFONCTION)
		{
			Symbole symbole = sym.newSymbole(n.getIdentifiant(),n.getArgs().size());
			if (symbole==null)
				throw new AnalyserSemantiqueException("La fonction "+n.getIdentifiant()+" est déja existante",n.getToken());
			
			Noeud block = n.getEnfants().get(0);
			boolean present=false;
			for (Noeud enfant : block.getEnfants())
			{
				if (enfant.getType() == NoeudType.ND_RETURN)
				{
					present=true;
					break;
				}
			}
			if (!present)
			{
				throw new AnalyserSemantiqueException("Il faut un return dans la fonction "+n.getIdentifiant(), n.getToken());
			}
			
			nbVar=0;
			sym.debutBloc();
			for (String id : n.getArgs())
			{
				Symbole s = sym.newSymbole(id,0);
				if (s==null)
					throw new AnalyserSemantiqueException("Variable déja existante",n.getToken());
				s.setSlot(nbVar++);						
			}
			
			int nbpar=nbVar;
			sem(n.getEnfants().get(0));
			n.setNbVarLocale(nbVar-nbpar);
			sym.finBloc();
		}
		else if (n.getType()==NoeudType.ND_CALL)
		{
			Symbole symbole = sym.rechercheMethode(n.getToken());
			if (symbole == null)
			{
				throw new AnalyserSemantiqueException("La fonction "+n.getIdentifiant()+" n'existe pas!", n.getToken());
			}
			if (n.getEnfants().size()!=symbole.getNbAgrs())
			{
				throw new AnalyserSemantiqueException("Le nombre d'arguments pour l'appel de la fonction "+n.getIdentifiant()+ "n'est pas correct !", n.getToken());
			}
			for (Noeud enfant : n.getEnfants())
			{
				sem(enfant);
			}
		}
		else
		{
			for (Noeud enfant : n.getEnfants())
			{
				sem(enfant);
			}
		}
		
	}
	
	public void semLoop(Noeud n) throws AnalyserSemantiqueException
	{
		if (n.getType()==NoeudType.ND_LOOP)
		{
			return;
		}
		if (n.getType()==NoeudType.ND_BREAK || n.getType()==NoeudType.ND_CONTINUE)
		{
			throw new AnalyserSemantiqueException("Break et continue doivent être dans une loop!",n.getToken());
		}
		for (Noeud enfant : n.getEnfants())
		{
			semLoop(enfant);
		}

	}
}
