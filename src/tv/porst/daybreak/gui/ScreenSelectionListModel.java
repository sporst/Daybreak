package tv.porst.daybreak.gui;

import javax.swing.AbstractListModel;

import tv.porst.daybreak.model.Level;

public class ScreenSelectionListModel extends AbstractListModel
{
	private Level level;

	public ScreenSelectionListModel(final Level level)
	{
		this.level = level;
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
}