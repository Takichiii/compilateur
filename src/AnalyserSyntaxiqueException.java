
public class AnalyserSyntaxiqueException extends Exception{

	public AnalyserSyntaxiqueException(String msg, Token t)
	{
		super("Syntax Error : \"" + msg + "\", ligne : "  +  t.getLigne() + ", colonne : " + t.colonne);
	}
	
}
