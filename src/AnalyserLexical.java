import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;

public class AnalyserLexical {

	public Reader reader;
	public int ligne;
	public int colonne;
	public int compteur;
	public List<Character> charList;
	
	public AnalyserLexical (Reader reader){
		this.reader=reader;
		ligne=1;
		colonne=1;
		compteur=0;
		try {
			init();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void init() throws IOException{
		int r;
		charList = new ArrayList<Character>();
		while((r=reader.read())!=-1)
		{
			charList.add((char)r);
		}
		//for (char c : charList)
			//System.out.println(c);
	}

	public List<Token> getAllTokens() throws Exception
	{
		List<Token> list = new ArrayList<Token>();
		Token token = getNextToken();
		while (token!=null)
		{
			list.add(token);
			token = getNextToken();
		}
		return list;
	}
	
	public Token getNextToken() throws Exception{
		if (compteur>=charList.size())
		{
			return null;
		}
		
		char c = charList.get(compteur);
		//System.out.println("char : "+ c);
		
		if (Character.isWhitespace(c) && c!=' ')
		{ 
			if (compteur+1<charList.size() && (charList.get(compteur+1)=='\n' || charList.get(compteur+1)=='\r'))
			{
				compteur++;
			}
			ligne++;
			colonne=1;
			compteur++;
			return getNextToken();
		}
		else if (Character.isWhitespace(c) && c==' '){
			colonne++;
			compteur++;
			//System.out.println("whitespace");
			return getNextToken();
		}
		else if (Character.isLetter(c)){
			StringBuilder sb = new StringBuilder();
			sb.append(c);
			colonne++;
			
			while (compteur+1<charList.size() && (Character.isLetter(charList.get(compteur+1)) || Character.isDigit(charList.get(compteur+1))))
			{
				compteur++;
				sb.append(charList.get(compteur));
				colonne++;
			}
			
			compteur++;
			String id = sb.toString();
			for (KeyWord keyWord : KeyWord.values()) 
			{
				if (id.equals(keyWord.valeur))
				{
					return new Token(keyWord,ligne, colonne);
				}
			}
			return new Token(id,ligne, colonne);
		}
		
		else if (Character.isDigit(c)){
			StringBuilder sb = new StringBuilder();
			sb.append(c);
			colonne++;
			while (compteur+1<charList.size() && Character.isDigit(charList.get(compteur+1)))
			{
				compteur++;
				sb.append(charList.get(compteur));
				colonne++;
			}
			compteur++;
			int valeur = Integer.parseInt(sb.toString());
			return new Token(valeur,ligne, colonne);
		}
		
		else{
			StringBuilder sb = new StringBuilder();
			sb.append(c);
			
			if (compteur+1<charList.size() && (Character.getType(charList.get(compteur+1))== Character.MATH_SYMBOL || Character.getType(charList.get(compteur+1))==24))
			{
				sb.append(charList.get(compteur+1));
				compteur++;
			}
				
			for (KeyWord keyWord : KeyWord.values()) 
			{				
				if (sb.toString().equals(keyWord.valeur))
				{
					colonne++;
					compteur++;
					return new Token(keyWord,ligne, colonne);
				}
			}
		}

		throw new Exception("Unknown Char : " + String.valueOf(c) );//+ Character.getType(c));
	}
	
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
						return new Token(id,keyWord,ligne, colonne);
					}
				}
			}
			colonne++;
		}
	}*/
}
