package monkeys;

public class Monkey extends Element {
	private int speed;
	
	public Monkey(int id, int x, int y, int speed) {
		this.id = id;
		this.posX = x;
		this.posY = y;
		this.speed = speed;
	}

	public int getSpeed() {
		return speed;
	}

	public void setSpeed(int speed) {
		this.speed = speed;
	}
	
}
