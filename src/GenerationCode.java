/**
 * Created on 09/11/17.
 */
public class GenerationCode {

    public GenerationCode() {
    }

    public void genCode(Noeud N){
        StringBuilder sb = new StringBuilder();
        if (N.getType() == NoeudType.ND_CONST){
            System.out.println("push.i "+ N.getValeur());
        }
        else if (N.getType() == NoeudType.ND_ADD){
            genCode(N.getEnfants().get(0));
            genCode(N.getEnfants().get(1));
            System.out.println("add.i");
        }
        else if (N.getType() == NoeudType.ND_MOINSU){
            genCode(N.getEnfants().get(0));
            genCode(N.getEnfants().get(1));
            System.out.println("add.i");
        }
    }
}
