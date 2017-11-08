public enum KeyWord{
	TOK_IF("if"), 
	TOK_ELSE("else"),
	TOK_FOR("for"),
	TOK_WHILE("while"),
	TOK_DO("do"),
	TOK_BREAK("break"),
	TOK_CONTINUE("continue"),
	TOK_RETURN("return"),
	TOK_INT("int"),
	TOK_VOID("Void"),
	
	TOK_PARENTHESE_O("("),
	TOK_PARENTHESE_F(")"),
	TOK_ACCOLADE_O("{"),
	TOK_ACCOLADE_F("}"),
	TOK_CROCHET_O("{"),
	TOK_CROCHET_F("]"),
	

	TOK_MUL("*"),
	TOK_MOINS("-"),
	TOK_ADD("+"),
	TOK_DIV("/"),
	
	TOK_ID("id"),
	TOK_VALEUR("valeur")
	
	
	;
	public String valeur;
	private KeyWord(String valeur){
		this.valeur=valeur;
	}
}