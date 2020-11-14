package org.qme.main;

import java.util.ArrayList;
import org.qme.vis.QInputScreen;
import org.qme.vis.QRenderScreen;
import org.qme.vis.ui.UIComponent;
import org.qme.world.Tile;
import org.qme.world.World;

/**
 * This class represents an instance of QME.
 * @author adamhutchings
 * @since pre0
 */
public class QApplication {
	
	/**
	 * What state the app is in. Used
	 * for toggling menus.
	 * @since pre1
	 */
	private GlobalState state;
	
	/**
	 * Represents the component of the screen that
	 * responds to key and mouse press events.
	 * @since pre0
	 */
	public QInputScreen qiscreen;
	
	/**
	 * Represents the component of the screen that
	 * renders graphical objects.
	 * @see org.qme.vis.QRenderable
	 */
	public QRenderScreen qrscreen;
	
	/**
	 * Every object that needs to be updated in the reload
	 * method. The QObject constructor automatically adds
	 * the object to this list.
	 */
	public ArrayList<QObject> objects;
	
	/**
	 * Actually a variable for the world lol.
	 */
	public World world;
	
	/**
	 * The game that gets looped through and stuff.
	 */
	public GameState game;
	
	/**
	 * Initializes the elements appropriately.
	 * @author adamhutchings
	 * @since pre0
	 */
	public QApplication() {
		
		qiscreen = new QInputScreen(this);
		qrscreen = new QRenderScreen(this);
		
		qiscreen.add(qrscreen);
		
		objects = new ArrayList<>();
		
		setState(GlobalState.MAIN_MENU);
		
	}
	
	/**
	 * Updates every object appropriately and
	 * calls the repaint method.
	 * @author adamhutchings
	 * @since pre0
	 * @see org.qme.main.QObject
	 * @see org.qme.vis.QRenderScreen
	 */
	public void reload() {
		
		// Check for mouse positions (hovering)
		qiscreen.updateHovers(qrscreen.getMousePosition());
		
		// Update everything
		for (QObject qo : objects) {
			qo.update(this);
		}
		
		// Render everything
		qiscreen.repaint();
		
	}
	
	public GlobalState getState() {
		return state;
	}
	
	/**
	 * Makes things visible if need be.
	 * @author adamhutchings
	 * @since pre1
	 * @param s the state to make
	 */
	public void setState(GlobalState s) {
		
		state = s;
		
		for (QObject object : objects) {
			
			if (object instanceof UIComponent) {
				
				if (((UIComponent) object).getActiveState() == state) {
					object.active = true;
				} else {
					object.active = false;
				}
				
			}
			
		}
		
	}
	
	/**
	 * Get the tooltipped tile.
	 */
	public Tile getHighlightedTile() {
		for (Tile[] row : world.tiles) {
			for (Tile tile : row) {
				if (tile.tooltip) {
					return tile;
				}
			}
		}
		return null;
	}
	
}
