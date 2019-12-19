package monkeys;

import javax.persistence.Entity;

public class Rum extends Element {
	
	private int visibility;
	private int energy;

	public Rum(int id, int x, int y, int visibility, int energy) {
		this.id = id;
		this.setVisibility(visibility);
		this.posX = x;
		this.posY = y;
		this.energy = energy;
		this.type = "Rum";
	}

	public int getVisibility() {
		return visibility;
	}

	public void setVisibility(int visibility) {
		this.visibility = visibility;
	}
	
	/**
	 * @return the energy
	 */
	public int getEnergy() {
		return energy;
	}

	/**
	 * @param energy the energy to set
	 */
	public void setEnergy(int energy) {
		this.energy = energy;
	}
}
