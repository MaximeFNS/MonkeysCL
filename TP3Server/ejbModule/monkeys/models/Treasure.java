package monkeys.models;

public class Treasure extends Element {
	
	private int visibility;
	
	public Treasure(int x, int y, int visibility) {
		this.setVisibility(visibility);
		this.posX = x;
		this.posY = y;
		this.type = "Treasure";
	}
	
	public int getVisibility() {
		return visibility;
	}

	public void setVisibility(int visibility) {
		this.visibility = visibility;
	}

}
