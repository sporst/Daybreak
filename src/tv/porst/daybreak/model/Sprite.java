package tv.porst.daybreak.model;

import net.sourceforge.jnhf.gui.Palette;
import net.sourceforge.jnhf.romfile.TileData;

public class Sprite
{
	private final TileData[][] tiles;
	private final Palette palette;

	public Sprite(final TileData[][] spriteData, final Palette palette)
	{
		this.tiles = spriteData;
		this.palette = palette;
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
		return tiles[0].length;
	}
}
