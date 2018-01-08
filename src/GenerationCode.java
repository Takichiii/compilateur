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


        //% AND NOT
        else if (N.getType() == NoeudType.ND_MOD){
            sb.append(gencode(N.getEnfants().get(0)));
            sb.append(gencode(N.getEnfants().get(1)));
            sb.append("mod.i");
            sb.append("\n");
        }
        else if (N.getType() == NoeudType.ND_NOT){
            sb.append(gencode(N.getEnfants().get(0)));
            sb.append("not.i");
            sb.append("\n");
        }
        else if (N.getType() == NoeudType.ND_AND){
            sb.append(gencode(N.getEnfants().get(0)));
            sb.append(gencode(N.getEnfants().get(1)));
            sb.append("and.i");
            sb.append("\n");
        }

        //IF
        else if (N.getType() == NoeudType.ND_IF){
            int L1 = genLabel();
            int L2 = genLabel();
            sb.append(gencode(N.getEnfants().get(0))); //gencode(E)
            sb.append("jumpf" + L1);
            sb.append(gencode(N.getEnfants().get(1))); //gencode(S1)
            sb.append("jump" + L2);
            sb.append("." + L1);
            if (N.getEnfants().size() == 3) //s'il y a un else
                sb.append(gencode(N.getEnfants().get(2))); //gencode(S2)
            sb.append("." + L2);
            sb.append("\n");
        }


        //WHILE
        else if (N.getType() == NoeudType.ND_LOOP){
            int L = genLabel();
            sb.append("." + L);
            sb.append(gencode(N.getEnfants().get(0)));
            sb.append("jump" + L);
        }

        //TODO BLOCK fini?
        else if (N.getType() == NoeudType.ND_BLOCK){
            int L = genLabel();
            sb.append("." + L);
            sb.append(gencode(N.getEnfants().get(0)));
            sb.append(gencode(N.getEnfants().get(1)));
            sb.append("jump" + L);
        }
        //TODO BREAK CONTINUE
        else if (N.getType() == NoeudType.ND_BREAK || N.getType() == NoeudType.ND_CONTINUE){
            sb.append("push");//TODO
            int L1 = genLabel();
            int L2 = genLabel();
            sb.append("." + L1);
            sb.append(gencode(N.getEnfants().get(0)));
            sb.append("." + L2);
            sb.append("pop"); // TODO
        }

        return sb.toString();
    }

    int compteur = 0;
    public int genLabel(){
        return compteur++;
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
        sb.append(G.gencode(N));
        sb.append("out.i \n");
        sb.append("halt \n");

        System.out.println(sb);

    }
}
