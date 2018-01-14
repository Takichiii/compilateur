import java.io.*;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Main {

	public static void main(String[] args) throws Exception {
		/*if (args.length==0)
		{
			System.err.println("Veuillez entrer le chemin du fichier � compiler !");
			return;
		}*/
		File folder = new File("src/ressource/test prof/tests/");
		for (final File fileEntry : folder.listFiles()) {
			{
				if (fileEntry.getName().contains("pass") && !fileEntry.getName().contains("out"))
					launchCompil(fileEntry);
	        }
	    }
		
		

	}
	
	private static void launchCompil(File file) throws Exception
	{
		System.out.println("File : "+file.getName());
		InputStream in = new FileInputStream(file);
		Reader reader = new InputStreamReader(in);
		
		AnalyserLexical analyserLexical = new AnalyserLexical(reader);
		ArrayList<Token> list = (ArrayList<Token>) analyserLexical.getAllTokens();

		AnalyserSyntaxique analyserSyntaxique = new AnalyserSyntaxique(list);
		
		
		Noeud n;
		try {
			n = analyserSyntaxique.master(analyserSyntaxique.getNextToken());
			n.print();
			AnalyserSemantique analyserSemantique = new AnalyserSemantique(n);
			GenerationCode generationCode = new GenerationCode();
			String code = generationCode.gencode(n);
			System.out.println(code);
			List<String> lines = Arrays.asList(code);
			Path out = Paths.get("out.txt");
			Files.write(out, lines, Charset.forName("UTF-8"));
			Process process = new ProcessBuilder("C:/Users/Antoine/git/compilateur/src/ressource/test prof/MSM/msm.exe","out.txt").start();
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			baos.writeTo(process.getOutputStream());
			System.out.println(baos.toString());

			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	

}
