package tv.porst.daybreak.model;

import net.sourceforge.jnhf.gui.Palette;
import net.sourceforge.jnhf.helpers.ListenerProvider;

public final class Screen
{
	private final int[][] squareNumbers;
	private final TileInformation tiles;
	private final Palette palette;

	private final ListenerProvider<IScreenListener> listeners = new ListenerProvider<IScreenListener>();

	public Screen(final int[][] squareNumbers, final TileInformation tiles, final Palette palette)
	{
		this.squareNumbers = squareNumbers.clone();
		this.tiles = tiles;
		this.palette = palette;
	}

	public void addListener(final IScreenListener listener)
	{
		listeners.addListener(listener);
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

	public void removeListener(final IScreenListener listener)
	{
		listeners.removeListener(listener);
	}

	public void setBlock(final int x, final int y, final Block block)
	{
		if (squareNumbers[y][x] == block.getIndex())
		{
			return;
		}

		squareNumbers[y][x] = block.getIndex();

		for (final IScreenListener listener : listeners)
		{
			try
			{
				listener.changedBlock(this, x, y, block);
			}
			catch(final Exception exception)
			{
				exception.printStackTrace();
			}
		}
	}
}
