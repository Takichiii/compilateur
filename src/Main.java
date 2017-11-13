import java.io.*;

public class Main {

	public static void main(String[] args) throws IOException {
		if (args.length==0)
		{
			System.err.println("Veuillez entrer le chemin du fichier � compiler !");
			return;
		}
		InputStream in = new FileInputStream(args[0]);
		Reader reader = new InputStreamReader(in);
		
		AnalyserLexical analyserLexical = new AnalyserLexical(reader);	
		//AnalyserSyntaxique analyserSyntaxique = new AnalyserSyntaxique();
	}
	

}
