import java.util.HashMap;
import java.util.Stack;

public class TableSymboles {
	public Stack<HashMap<String, Symbole>> tableSymboles;
	
	public TableSymboles(){
		tableSymboles = new Stack<HashMap<String, Symbole>>();
	}
	
	public void debutBloc(){
		tableSymboles.push(new HashMap<String, Symbole>());
	}
	
	public void finBloc(){
		tableSymboles.pop();
	}
	
	public Symbole newSymbole(String id, int nbArgs ){
		HashMap<String, Symbole> symboles = tableSymboles.peek(); 
		if (symboles.containsKey(id))
		{
			System.out.println("La variable "+ id + " a d�j� �t� d�clar�");
			return null;
		}
		Symbole symbole = new Symbole(nbArgs);
		symboles.put(id, symbole);
		return symbole;
	}
	
	public Symbole recherche(Token token){
		Symbole symbole=null;
		for (int i=0 ; i<tableSymboles.size();i++)
		{
			if (tableSymboles.get(i).containsKey(token.getIdentifiant()))
				symbole = tableSymboles.get(i).get(token.getIdentifiant());
		}
		if (symbole == null)
		{
			System.out.println("La variable " + token.getIdentifiant() + " n'a pas �t� d�clar�e !");
			return null;
		}
		return symbole;
	}
	
	public Symbole rechercheMethode(Token token){
		 Symbole symbole = tableSymboles.firstElement().get(token.getIdentifiant());
		 if (symbole == null)
		 {
			 System.out.println("La variable " + token.getIdentifiant() + " n'a pas �t� d�clar�e !");
			 return null;
		 }
		 return symbole;
	}
	
}
