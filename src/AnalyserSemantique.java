
public class AnalyserSemantique {

	private int nbVar;
	private TableSymboles sym;
	
	public AnalyserSemantique()
	{
		nbVar=0;
		sym = new TableSymboles();
	}
	
	public void sem(Noeud n)
	{
		if (n.getType()==NoeudType.ND_BLOCK)
		{
			sym.debutBloc();
			for(Noeud enfant : n.getEnfants())
			{
				sem(enfant);
			}
			sym.finBloc();
		}
		else
		{
			for (Noeud enfant : n.getEnfants())
			{
				sem(enfant);
			}
		}
		if (n.getType()==NoeudType.ND_VARDECL)
		{
			Symbole s = sym.newSymbole(n.getToken());
			s.slot=nbVar++;
		}
		if (n.getType()==NoeudType.ND_REFVAR || n.getType()==NoeudType.ND_AFFVAR)
		{
			Symbole s = sym.recherche(n.getToken());
			n.setSlot(s.slot);
			
		}
	}
}
