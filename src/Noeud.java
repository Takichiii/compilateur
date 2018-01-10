import java.util.ArrayList;
import java.util.List;

public class Noeud {
	
	private NoeudType type;
	private int slot;
	private int valeur;
	private Token token;
	private List<Noeud> enfants;
	private List<String> args;

	public Noeud(NoeudType type, int valeur){
		this.type = type;
		this.valeur = valeur;
		this.enfants = new ArrayList<>();
		this.args = new ArrayList<>();
	}
	public Noeud(NoeudType type, Token token){
		this.type = type;
		this.token = token;
		this.enfants = new ArrayList<>();
		this.args = new ArrayList<>();
	}
	public Noeud(NoeudType type, List<Noeud> enfants){
		this.type = type;
		this.enfants = enfants;
		this.args = new ArrayList<>();
	}

	public Noeud(NoeudType type){ //pour noeuds sans enfants ex break
		this.type = type;
		this.enfants = new ArrayList<>();
		this.args = new ArrayList<>();
	}

	public void print() {
        print("", true);
    }
	
    private void print(String prefix, boolean isTail) {
    	String str  = "";
    	if (type == NoeudType.ND_CONST)
    		str=Integer.toString(valeur);
    	else if (type == NoeudType.ND_REFVAR)
    		str=getIdentifiant();
    	else
    		str=type.valeur;
        System.out.println(prefix + (isTail ? "└── " : "├── ") + str);
        for (int i = 0; i < enfants.size() - 1; i++) {
            enfants.get(i).print(prefix + (isTail ? "    " : "│   "), false);
        }
        if (enfants.size() > 0) {
        	enfants.get(enfants.size() - 1)
                    .print(prefix + (isTail ?"    " : "│   "), true);
        }
    }
	
	public String getIdentifiant() {
		return token.getIdentifiant();
	}
    
	public NoeudType getType() {
		return type;
	}
	public void setType(NoeudType type) {
		this.type = type;
	}
	public int getValeur() {
		return valeur;
	}
	public void setValeur(int valeur) {
		this.valeur = valeur;
	}
	public Token getToken() {
		return token;
	}
	public void setNom(Token token) {
		this.token = token;
	}
	public List<Noeud> getEnfants() {
		return enfants;
	}
	public void setEnfants(List<Noeud> enfants) {
		this.enfants = enfants;
	}
	public int getSlot() {
		return slot;
	}
	public void setSlot(int slot) {
		this.slot = slot;
	}
	public List<String> getArgs() {
		return args;
	}
	public void setArgs(List<String> args) {
		this.args = args;
	}

}
