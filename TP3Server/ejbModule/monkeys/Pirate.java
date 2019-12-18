package monkeys;

import javax.persistence.Entity;

/**
 * @author Maxime Afonso
 */

@Entity
public class Pirate extends Element{
		
	/**
	 * The energy of the pirate
	 */
	public int energy;
	
	public Pirate (int id, int x, int y, int energy) {
		this.posX = x;
		this.posY = y;
		this.id = id;
		this.type = "Pirate";
		this.energy = energy;
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
	
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "Pirate [posX=" + posX + ", posY=" + posY + ", id=" + id + ", energy=" + energy + "]";
	}

}
