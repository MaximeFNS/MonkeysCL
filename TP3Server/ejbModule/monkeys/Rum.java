package monkeys;

import javax.persistence.Entity;

public class Rum extends Element {
	
	private int visibility;
	
	public Rum(int id, int x, int y, int visibility) {
		this.id = id;
		this.setVisibility(visibility);
		this.posX = x;
		this.posY = y;
		this.type = "Rum";
	}

	public int getVisibility() {
		return visibility;
	}

	public void setVisibility(int visibility) {
		this.visibility = visibility;
	}
}
