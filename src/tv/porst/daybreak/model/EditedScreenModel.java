package tv.porst.daybreak.model;

import java.util.ArrayList;
import java.util.List;

public class EditedScreenModel
{
	private final List<IEditedScreenModelListener> listeners = new ArrayList<IEditedScreenModelListener>();

	public void addListener(final IEditedScreenModelListener listener)
	{
		listeners.add(listener);
	}

	public void removeListener(final IEditedScreenModelListener listener)
	{
		listeners.remove(listener);
	}

	public void setSelectedScreen(final Level level, final Screen screen)
	{
		for (final IEditedScreenModelListener listener : listeners)
		{
			try
			{
				listener.changedScreen(level, screen);
			}
			catch(final Exception exception)
			{
				exception.printStackTrace();
			}
		}
	}
}
