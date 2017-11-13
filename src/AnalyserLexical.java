import java.io.Reader;

public class AnalyserLexical {

	public Reader reader;
	public int ligne;
	public int colonne;
	
	public AnalyserLexical (Reader reader){
		this.reader=reader;
		ligne=0;
		colonne=0;
	}
	//test
	/*
	public Token getNextToken() throws IOException{
		boolean find = false;
		while (!find)
		{
			int r = reader.read();
			if (r==-1)
				return null;
			char c = (char)r;
			if (Character.isWhitespace(c) && c=='\n')
			{
				ligne++;
				colonne=0;
			}
			else if (Character.isWhitespace(c)){
				colonne++;
			}
			else if (Character.isLetter(c)){
				StringBuilder sb = new StringBuilder();
				do{
					sb.append(c);
					colonne++;
				}
				while ((r = reader.read())!=-1 && (Character.isLetter((char)r) || Character.isDigit((char)r)) );
				String id = sb.toString();
				for (KeyWord keyWord : KeyWord.values()) 
				{
					if (id.equals(keyWord.valeur))
					{
						return new Token(id,keyWord,ligne, colonne);
					}
				}
				return new Token(id,KeyWord.TOK_ID,ligne, colonne);
			}
			
			else if (Character.isDigit(c)){
				StringBuilder sb = new StringBuilder();
				do{
					sb.append(c);
					colonne++;
				}
				while ((r = reader.read())!=-1 && Character.isDigit((char)r));
				int valeur = Integer.valueOf(sb.toString());
				return new Token(valeur,ligne, colonne);
			}
			
			else{
				for (KeyWord keyWord : KeyWord.values()) 
				{
					BufferedReader buff = new BufferedReader(reader);
					
					if (String.valueOf(c).equals(keyWord.valeur))
					{
						return new Token("",keyWord,ligne, colonne);
					}
				}
			}
			colonne++;
		}
		return null;
	}
	*/
}
