import java.util.List;
import java.util.Stack;

/**
 * Created on 09/11/17.
 */
public class GenerationCode {

    int compteur = 0;
    int L1 = 0;
    int L2 =0;
    Stack<Integer> stack= new Stack<Integer>();


    public int genLabel() {
        return compteur++;
    }

    public String gencode(Noeud N) {
        StringBuilder sb = new StringBuilder();
        if (N.getType() == NoeudType.ND_CONST) {
            sb.append("push.i " + N.getValeur());
        } else if (N.getType() == NoeudType.ND_ADD) {
            sb.append(gencode(N.getEnfants().get(0)));
            sb.append(gencode(N.getEnfants().get(1)));
            sb.append("add.i");
        } else if (N.getType() == NoeudType.ND_MOINS) {
            sb.append(gencode(N.getEnfants().get(0)));
            sb.append(gencode(N.getEnfants().get(1)));
            sb.append("sub.i");
        } else if (N.getType() == NoeudType.ND_MOINSU) {
            sb.append("push.i 0");
            sb.append("\n");
            sb.append(gencode(N.getEnfants().get(0)));
            sb.append("sub.i");
        } else if (N.getType() == NoeudType.ND_MUL) {
            sb.append(gencode(N.getEnfants().get(0)));
            sb.append(gencode(N.getEnfants().get(1)));
            sb.append("mul.i");
        } else if (N.getType() == NoeudType.ND_DIV) {
            sb.append(gencode(N.getEnfants().get(0)));
            sb.append(gencode(N.getEnfants().get(1)));
            sb.append("div.i");
        }//% AND NOT
        else if (N.getType() == NoeudType.ND_MOD) {
            sb.append(gencode(N.getEnfants().get(0)));
            sb.append(gencode(N.getEnfants().get(1)));
            sb.append("mod.i");
        } else if (N.getType() == NoeudType.ND_NOT) {
            sb.append(gencode(N.getEnfants().get(0)));
            sb.append("not.i");
        } else if (N.getType() == NoeudType.ND_AND) {
            sb.append(gencode(N.getEnfants().get(0)));
            sb.append(gencode(N.getEnfants().get(1)));
            sb.append("and.i");
        }

        //TODO IF fini?
        else if (N.getType() == NoeudType.ND_IF) {
            int L1 = genLabel();
            int L2 = genLabel();
            sb.append(gencode(N.getEnfants().get(0))); //gencode(E)
            sb.append("jumpf" + L1 +"\n");
            sb.append(gencode(N.getEnfants().get(1))); //gencode(S1)
            sb.append("jump" + L2 +"\n");
            sb.append("." + L1 +"\n");
            if (N.getEnfants().size() == 3) //s'il y a un else
                sb.append(gencode(N.getEnfants().get(2))); //gencode(S2)
            sb.append("." + L2);
        }

        //WHILE
        else if (N.getType() == NoeudType.ND_LOOP) {
            stack.push(L1);
            stack.push(L2);
            L1 = genLabel();
            L2 = genLabel();
            sb.append("." + L1 +"\n");
            sb.append(gencode(N.getEnfants().get(0)));
            sb.append("jump" + L1 +"\n");
            sb.append("." + L2 +"\n");
            L2 = stack.pop();
            L1 = stack.pop();
        }

        //BLOCK
        else if (N.getType() == NoeudType.ND_BLOCK) {
            List<Noeud> enfants = N.getEnfants();
            for (int i = 0; i < enfants.size(); i++) {
                sb.append(gencode(enfants.get(i)));
            }
        }
        //BREAK
        else if (N.getType() == NoeudType.ND_BREAK) {
            sb.append("jump" + L2);
        }

        //CONTINUE
        else if (N.getType() == NoeudType.ND_CONTINUE) {
            sb.append("jump" + L1);
        }

        //CALL
        else if (N.getType() == NoeudType.ND_CALL) {
            sb.append("prep" + N.getIdentifiant() + "\n");
            List<Noeud> enfants = N.getEnfants();
            for (int i = 0; i < enfants.size(); i++) {
                sb.append(gencode(enfants.get(i)));
            }
            sb.append("call" + enfants.size());
        }

        //DROP
        else if (N.getType() == NoeudType.ND_DROP) {
            sb.append(gencode(N.getEnfants().get(0)));
            sb.append("drop");
        }
        //OUT
        else if (N.getType() == NoeudType.ND_OUT) {
            sb.append(gencode(N.getEnfants().get(0)));
            sb.append("out.i");
        }

        //affectation variable
        else if (N.getType() == NoeudType.ND_AFFVAR) {
            sb.append(gencode(N.getEnfants().get(0)));
            sb.append("set" + N.getSlot());
        }//reference variable
        else if (N.getType() == NoeudType.ND_REFVAR) {
            sb.append("get" + N.getSlot());
        }

        //RETURN
        else if (N.getType() == NoeudType.ND_RETURN) {
            sb.append(gencode(N.getEnfants().get(0)));
            sb.append("ret");
        }

        //DEFFonction
        else if (N.getType() == NoeudType.ND_DEFFONCTION) {
            sb.append("." + N.getIdentifiant() + "\n");
            for (int i = 0; i < N.getNbVarLocale(); i++) {
                sb.append("push.i 9999 \n");
            }
            sb.append(gencode(N.getEnfants().get(0)));
            sb.append("push.i 0 \n");
            sb.append("ret");
        }


        //Master : arbre final
        else if (N.getType() == NoeudType.ND_MASTER) {
            sb.append(".start \n");
            sb.append("prep main \n");
            sb.append("call 0 \n");
            sb.append("halt \n");
            List<Noeud> enfants = N.getEnfants();
            for (int i = 0; i < enfants.size(); i++) {
                sb.append(gencode(enfants.get(i)));
            }
        }

        sb.append("\n");
        return sb.toString();
    }
}