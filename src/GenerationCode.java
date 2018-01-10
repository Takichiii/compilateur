import java.util.List;
import java.util.Stack;

/**
 * Created on 09/11/17.
 */
public class GenerationCode {

    int compteur = 0;


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

        //IF
        else if (N.getType() == NoeudType.ND_IF) {
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
        }

        //WHILE
        else if (N.getType() == NoeudType.ND_LOOP) {
            int L = genLabel();
            sb.append("." + L);
            sb.append(gencode(N.getEnfants().get(0)));
            sb.append("jump" + L);
        }

        //TODO BLOCK fini?
        else if (N.getType() == NoeudType.ND_BLOCK) {
            int L = genLabel();
            sb.append("." + L);
            sb.append(gencode(N.getEnfants().get(0)));
            sb.append(gencode(N.getEnfants().get(1)));
            sb.append("jump" + L);
        }
        //TODO BREAK CONTINUE
        else if (N.getType() == NoeudType.ND_BREAK || N.getType() == NoeudType.ND_CONTINUE) {
            Stack s = new Stack();
            sb.append("push");//TODO
            s.push(0);
            s.push(0);
            int L1 = genLabel();
            int L2 = genLabel();
            s.push(L1);
            s.push(L2);
            sb.append("." + L1);
            sb.append(gencode(N.getEnfants().get(0)));
            sb.append("." + L2);
            sb.append("pop"); // TODO
        }

        //CALL
        else if (N.getType() == NoeudType.ND_CALL) {
            sb.append("prep" + N.getIdentifiant());
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
            sb.append("." + N.getIdentifiant());
            List<String> varLocales = N.getArgs();//TODO var locales ?
            for (int i = 0; i < varLocales.size(); i++) {
                sb.append("push.i 9999" + varLocales.get(i) + "\n");
            }
            sb.append(gencode(N.getEnfants().get(0)));
            sb.append("push.i 0 \n");
            sb.append("ret\n");
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
            //TODO est-ce tout?
        }

        sb.append("\n");
        return sb.toString();
    }
}