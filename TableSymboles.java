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
	
	public Symbole newSymbole(Token token){
		HashMap<String, Symbole> symboles = tableSymboles.peek(); 
		if (symboles.containsKey(token.getIdentifiant()))
		{
			System.out.println("La variable "+ token.getIdentifiant() + " a déjà été déclaré à la ligne "+ token.getLigne());
			return null;
		}
		Symbole symbole = new Symbole();
		symboles.put(token.getIdentifiant(), symbole);
		return symbole;
		
	}
	
	public Symbole recherche(Token token){
		 Symbole symbole = tableSymboles.peek().get(token.getIdentifiant());
		 if (symbole == null)
		 {
			 System.out.println("La variable " + token.getIdentifiant() + " n'a pas été déclarée !");
		 }
		 return symbole;
	}
	
}
