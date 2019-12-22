package monkeys;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

/**
 * @author Mickael Clavreul
 */
@Entity
@Table(name = "ISLANDS")
public class Island implements Serializable {

	@Id
	@Column(name = "ISLAND_ID")
	private int id;

	private int[][] map;
	protected Collection<Element> elements = new ArrayList<Element>();

	@OneToMany(mappedBy = "island", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
	public Collection<Element> getElements() {
		return elements;
	}

	public void setElements(Collection<Element> elements) {
		this.elements = elements;
	}

	public Island() {
		this.map = null;
	}

	/**
	 * @return the id
	 */
	@Id
	@GeneratedValue
	public int getId() {
		return id;
	}

	/**
	 * @param id the id to set
	 */
	public void setId(int id) {
		this.id = id;
	}

	/**
	 * @return the map
	 */
	@Column(length = 100000)
	public int[][] getMap() {
		return map;
	}

	/**
	 * @param map the map to set
	 */
	public void setMap(int[][] map) {
		this.map = map;
	}
}
