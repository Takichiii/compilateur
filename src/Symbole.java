
public class Symbole {
	private int nbArgs;
	private int slot;
	

	public Symbole(int nbArgs){
		this.nbArgs = nbArgs;
	}

	public int getNbAgrs() {
		return nbArgs;
	}
	
	public int getSlot() {
		return slot;
	}

	public void setSlot(int slot) {
		this.slot = slot;
	}

}
