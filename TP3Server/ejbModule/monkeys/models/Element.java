package monkeys.models;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
/**
 * @author Mickael Clavreul
 */
@Entity
@Table(name="ELEMENTS")
public class Element implements Serializable{
	protected int posX;
	protected int posY;
	
	@Id
	@Column(name="ELEMENT_ID")
	protected int id;
	protected String type;
	protected String state;
	protected Island island;
	protected int energy;
	protected int visibility;

	@ManyToOne
	@JoinColumn(name="ELEMENT_ISLAND_ID")
	public Island getIsland() {
		return island;
	}
	
	public void setIsland(Island island) {
		this.island = island;
	}
	
	public Element() {}

	/**
	 * @return posX
	 */
	public int getPosX() {
		return posX;
	}

	public void setPosX(int posX) {
		this.posX = posX;
	}

	/**
	 * @return posY
	 */
	public int getPosY() {
		return posY;
	}

	public void setPosY(int posY) {
		this.posY = posY;
	}

	/**
	 * @return the id
	 */
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}
	
	/**
	 * @return the type
	 */
	public String getType() {
		return type;
	}

	/**
	 * @param type the type to set
	 */
	public void setType(String type) {
		this.type = type;
	}
	
	public String getState() {
		return state;
	}

	public void setState(String etat) {
		state = etat;
	}
	
	public int getEnergy() {
		return energy;
	}
	
	public void setEnergy(int energy) {
		this.energy = energy;
		
	}
	
	public int getVisibility() {
		return visibility;
	}

	public void setVisibility(int visibility) {
		this.visibility = visibility;
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "Element [posX=" + posX + ", posY=" + posY + ", id=" + id + ", type=" + type + ", state=" + state + "]";
	}
}
