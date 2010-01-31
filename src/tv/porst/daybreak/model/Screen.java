package tv.porst.daybreak.model;

import net.sourceforge.jnhf.gui.Palette;

public final class Screen
{
	private final int[][] squareNumbers;
	private final TileInformation tiles;
	private final Palette palette;

	public Screen(final int[][] squareNumbers, final TileInformation tiles, final Palette palette)
	{
		this.squareNumbers = squareNumbers.clone();
		this.tiles = tiles;
		this.palette = palette;
	}

	public Palette getPalette()
	{
		return palette;
	}

	public int[][] getSquareNumbers()
	{
		return squareNumbers.clone();
	}

	public TileInformation getTiles()
	{
		return tiles;
	}
}
