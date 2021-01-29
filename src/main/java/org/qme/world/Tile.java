package org.qme.world;

import org.qme.client.vis.RenderMaster;
import org.qme.client.vis.Renderable;
import org.qme.client.vis.WindowManager;

/**
 * Represents a single tile in the world. As with other objects, rendering and
 * bounds detection code will not be in this class, and rather with other
 * rendering / bounds detection code, etc.
 * @author adamhutchings
 * @since 0.1.0
 */
public class Tile implements Renderable {
	
	/**
	 * The x coordinate of the tile.
	 */
	public final int x;
	
	/**
	 * The y coordinate of the tile.
	 */
	public final int y;
	
	public final TileType type;
	
	/**
	 * Constructor - initialize with coordinates
	 * @param x the input x coordinate
	 * @param y the input y coordinate
	 */
	public Tile(int x, int y, TileType type) {
		this.x = x;
		this.y = y;
		this.type = type;
		WindowManager.addObject(this);
	}

	/**
	 * Wrapper from RenderMaster
	 */
	@Override
	public void draw() {
		RenderMaster.drawTile(this);
	}

}
