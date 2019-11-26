package monkeys;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import javax.ejb.LocalBean;
import javax.ejb.Stateless;

/**
 * Session Bean implementation class Configuration
 */
@Stateless
@LocalBean
public class Configuration implements ConfigurationLocal {

    /**
     * Default constructor. 
     */
    public Configuration() {
        // TODO Auto-generated constructor stub
    }

	@Override
	public int[][] getMap() {
		
		InputStream is = Thread.currentThread().getContextClassLoader()
				.getResourceAsStream("monkeys.properties");

		Properties properties = new Properties();
		try {
			properties.load(is);
		} catch (IOException e) {
			e.printStackTrace();
		}
		String map = properties.getProperty("MONKEYS_MAP");
		map = map.substring(1, map.length()-1);
		System.out.println(map);
		int[][] matrice = new int[10][10];
		String[] lignes = map.split(";");
		for (int i=0; i<lignes.length;i++) {
			String ligne = lignes[i];
			String[] nombres = ligne.split(",");
			for(int j=0;j<nombres.length;j++) {
				matrice[i][j] = Integer.parseInt(nombres[j]);
			}
		}
		return matrice;
	}

	@Override
	public int getNbHunterMonkeys() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int getNbErraticMonkeys() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int getHunterSpeed() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int getErraticSpeed() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int getRhumEnergy() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int getRhumTime() {
		// TODO Auto-generated method stub
		return 0;
	}

}
