package tv.porst.daybreak.model;

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.jnhf.gui.Palette;
import net.sourceforge.jnhf.helpers.ListenerProvider;

public final class Screen
{
	private final int[][] squareNumbers;
	private final TileInformation tiles;

	private Palette palette;

	private final ListenerProvider<IScreenListener> listeners = new ListenerProvider<IScreenListener>();
	private final List<SpriteLocation> spriteData;

	public Screen(final int[][] squareNumbers, final List<SpriteLocation> spriteData, final TileInformation tiles)
	{
		this.squareNumbers = squareNumbers.clone();
		this.spriteData = new ArrayList<SpriteLocation>(spriteData);
		this.tiles = tiles;
	}

	public void addListener(final IScreenListener listener)
	{
		listeners.addListener(listener);
	}

	public void addSprite(final SpriteLocation spriteLocation)
	{
		spriteData.add(spriteLocation);

		for (final IScreenListener listener : listeners)
		{
			try
			{
				listener.addedSprite(this, spriteLocation);
			}
			catch(final Exception exception)
			{
				exception.printStackTrace();
			}
		}
	}

	public Palette getPalette()
	{
		return palette;
	}

	public List<SpriteLocation> getSpriteData()
	{
		return new ArrayList<SpriteLocation>(spriteData);
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

	public void removeSprite(final SpriteLocation sprite)
	{
		spriteData.remove(sprite);

		for (final IScreenListener listener : listeners)
		{
			try
			{
				listener.removeSprite(this, sprite);
			}
			catch(final Exception exception)
			{
				exception.printStackTrace();
			}
		}
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

	public void setPalette(final Palette palette)
	{
		this.palette = palette;
	}
}
