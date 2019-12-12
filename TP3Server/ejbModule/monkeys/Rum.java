package monkeys;

public class Rum extends Element {
	
	private int visibility;
	
	public Rum(int id, int x, int y, int visibility) {
		this.setVisibility(visibility);
		this.id = id;
		this.posX = x;
		this.posY = y;
	}

	public int getVisibility() {
		return visibility;
	}

	public void setVisibility(int visibility) {
		this.visibility = visibility;
	}
}
