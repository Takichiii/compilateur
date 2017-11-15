
public class Symbole {
	String type;
	String emplacement;
	int slot;
	

	public Symbole(String type, String emplacement){
		this.type=type;
		this.emplacement=emplacement;
	}
	
	public Symbole(){
	}

	public int getSlot() {
		return slot;
	}

	public void setSlot(int slot) {
		this.slot = slot;
	}

}
