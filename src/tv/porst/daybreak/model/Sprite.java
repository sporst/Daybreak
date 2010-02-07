package tv.porst.daybreak.model;

import net.sourceforge.jnhf.gui.Palette;
import net.sourceforge.jnhf.romfile.TileData;

public class Sprite
{
	private final TileData[][] tiles;
	private final Palette palette;
	private final int id;

	public Sprite(final int id, final TileData[][] spriteData, final Palette palette)
	{
		this.id = id;
		this.tiles = spriteData;
		this.palette = palette;
	}

	public int getId()
	{
		return id;
	}

	public Palette getPalette()
	{
		return palette;
	}

	public TileData[][] getTiles()
	{
		return tiles;
	}

	public int height()
	{
		return tiles.length;
	}

	public int width()
	{
		return height() == 0 ? 0 : tiles[0].length;
	}
}
