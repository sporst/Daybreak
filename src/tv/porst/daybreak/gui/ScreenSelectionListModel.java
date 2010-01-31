package tv.porst.daybreak.gui;

import javax.swing.AbstractListModel;

import tv.porst.daybreak.model.Block;
import tv.porst.daybreak.model.IScreenListener;
import tv.porst.daybreak.model.Level;
import tv.porst.daybreak.model.Screen;

public class ScreenSelectionListModel extends AbstractListModel
{
	private Level level;

	private final InternalScreenListener internalScreenListener = new InternalScreenListener();

	public ScreenSelectionListModel(final Level level)
	{
		this.level = level;

		for (final Screen screen : level.getScreens())
		{
			screen.addListener(internalScreenListener);
		}
	}

	@Override
	public Object getElementAt(final int index)
	{
		return null;
	}

	public Level getLevel()
	{
		return level;
	}

	@Override
	public int getSize()
	{
		return level.getScreens().size();
	}

	public void setLevel(final Level level)
	{
		this.level = level;

		fireContentsChanged(this, 0, getSize());
	}

	private class InternalScreenListener implements IScreenListener
	{
		@Override
		public void changedBlock(final Screen screen, final int x, final int y, final Block block)
		{
			fireContentsChanged(this, 0, getSize());
		}
	}
}