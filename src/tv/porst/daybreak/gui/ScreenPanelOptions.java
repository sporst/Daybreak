package tv.porst.daybreak.gui;

import java.util.ArrayList;
import java.util.List;

public class ScreenPanelOptions
{
	private boolean highlightSolidBlocks = false;

	private boolean highlightDoors = false;

	private final List<IScreenPanelOptionsListener> listeners = new ArrayList<IScreenPanelOptionsListener>();

	public void addListener(final IScreenPanelOptionsListener listener)
	{
		listeners.add(listener);
	}

	public boolean getHighlightDoors()
	{
		return highlightDoors;
	}

	public boolean getHighlightSolidBlocks()
	{
		return highlightSolidBlocks;
	}

	public void removeListener(final IScreenPanelOptionsListener listener)
	{
		listeners.remove(listener);
	}

	public void setHighlightDoors(final boolean highlighted)
	{
		highlightDoors = highlighted;

		for (final IScreenPanelOptionsListener listener : listeners)
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

		for (final IScreenPanelOptionsListener listener : listeners)
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
