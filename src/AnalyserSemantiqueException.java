
public class AnalyserSemantiqueException extends Exception{

	public AnalyserSemantiqueException(String msg, Token t)
	{
		super("Semantic Error : \"" + msg + "\", ligne : "  +  t.getLigne() + ", colonne : " + t.colonne);
	}
	
}
