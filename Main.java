import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.Charset;

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
		AnalyserSyntaxique analyserSyntaxique = new AnalyserSyntaxique(analyserLexical);
	}
	

}
