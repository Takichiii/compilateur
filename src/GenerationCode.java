import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;

/**
 * Created on 09/11/17.
 */
public class GenerationCode {

    public GenerationCode() {
    }

    public String gencode(Noeud N){
        StringBuilder sb = new StringBuilder();
        if (N.getType() == NoeudType.ND_CONST){
            sb.append("push.i "+ N.getValeur());
            sb.append("\n");
        }
        else if (N.getType() == NoeudType.ND_ADD){
            sb.append(gencode(N.getEnfants().get(0)));
            sb.append(gencode(N.getEnfants().get(1)));
            sb.append("add.i");
            sb.append("\n");
        }
        else if (N.getType() == NoeudType.ND_MOINS){
            sb.append(gencode(N.getEnfants().get(0)));
            sb.append(gencode(N.getEnfants().get(1)));
            sb.append("sub.i");
            sb.append("\n");
        }
        else if (N.getType() == NoeudType.ND_MOINSU){
            sb.append("push.i 0");
            sb.append("\n");
            sb.append(gencode(N.getEnfants().get(0)));
            sb.append("sub.i");
            sb.append("\n");
        }
        else if (N.getType() == NoeudType.ND_MUL){
            sb.append(gencode(N.getEnfants().get(0)));
            sb.append(gencode(N.getEnfants().get(1)));
            sb.append("mul.i");
            sb.append("\n");
        }
        else if (N.getType() == NoeudType.ND_DIV){
            sb.append(gencode(N.getEnfants().get(0)));
            sb.append(gencode(N.getEnfants().get(1)));
            sb.append("div.i");
            sb.append("\n");
        }


        //TO DO : % AND NOT
        else if (N.getType() == NoeudType.ND_MOD){
            sb.append(gencode(N.getEnfants().get(0)));
            sb.append(gencode(N.getEnfants().get(1)));
            sb.append("div.i");
            sb.append("\n");
        }
        else if (N.getType() == NoeudType.ND_NOT){
            sb.append(gencode(N.getEnfants().get(0)));
            sb.append(gencode(N.getEnfants().get(1)));
            sb.append("div.i");
            sb.append("\n");
        }
        return sb.toString();
    }


    public static void main(String[] args) throws Exception {
        AnalyserLexical L = new AnalyserLexical(new Reader() {
            @Override
            public int read(char[] cbuf, int off, int len) throws IOException {
                return 0;
            }

            @Override
            public void close() throws IOException {

            }
        });

        List <Token> listToken = new ArrayList<Token>();
        listToken.add(new Token(KeyWord.TOK_MOINS, 0, 0));
        listToken.add(new Token(3, 1, 0));
        listToken.add(new Token(KeyWord.TOK_ADD, 1, 0));
        listToken.add(new Token(3, 1, 0));


        System.out.println(listToken);
        //AnalyserSyntaxique A = new AnalyserSyntaxique(L.getListToken());
        AnalyserSyntaxique A = new AnalyserSyntaxique(listToken);
        StringBuilder sb = new StringBuilder();
        sb.append(".start \n");
        GenerationCode G = new GenerationCode();
        Noeud N = A.terme(A.getNextToken());
        A.afficher(N);
        sb.append(G.gencode(N));
        sb.append("out.i \n");
        sb.append("halt \n");

        System.out.println(sb);

    }
}
