package tv.porst.daybreak.gui;

import java.util.ArrayList;
import java.util.List;

import tv.porst.daybreak.gui.blocks.IBlockHighlightingOptionsListener;

public class BlockHighlightingOptions implements IBlockHighlighterOptions
{
	private boolean highlightSolidBlocks = false;

	private boolean highlightDoors = false;

	private boolean highlightAir = false;

	private final List<IBlockHighlightingOptionsListener> listeners = new ArrayList<IBlockHighlightingOptionsListener>();

	public void addListener(final IBlockHighlightingOptionsListener listener)
	{
		listeners.add(listener);
	}

	@Override
	public boolean getHighlightAir()
	{
		return highlightAir;
	}

	public boolean getHighlightDoors()
	{
		return highlightDoors;
	}

	public boolean getHighlightSolidBlocks()
	{
		return highlightSolidBlocks;
	}

	public void removeListener(final IBlockHighlightingOptionsListener listener)
	{
		listeners.remove(listener);
	}

	@Override
	public void setHighlightAir(final boolean highlight)
	{
		highlightAir = highlight;

		for (final IBlockHighlightingOptionsListener listener : listeners)
		{
			try
			{
				listener.changedAirHighlighting(highlight);
			}
			catch(final Exception exception)
			{
				exception.printStackTrace();
			}
		}
	}

	public void setHighlightDoors(final boolean highlighted)
	{
		highlightDoors = highlighted;

		for (final IBlockHighlightingOptionsListener listener : listeners)
		{
			try
			{
				listener.changedDoorHighlighting(highlighted);
			}
			catch(final Exception exception)
			{
				exception.printStackTrace();
			}
		}
	}

	public void setHighlightSolidBlocks(final boolean highlighted)
	{
		highlightSolidBlocks = highlighted;

		for (final IBlockHighlightingOptionsListener listener : listeners)
		{
			try
			{
				listener.changedSolidBlockHighlighting(highlighted);
			}
			catch(final Exception exception)
			{
				exception.printStackTrace();
			}
		}
	}
}
