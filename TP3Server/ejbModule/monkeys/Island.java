package monkeys;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
/**
 * @author Mickael Clavreul
 */
@Entity
public class Island {
	
	private int id;
	private int[][] map;

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
