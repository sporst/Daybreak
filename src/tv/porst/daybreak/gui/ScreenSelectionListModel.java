package tv.porst.daybreak.gui;

import java.util.ArrayList;
import java.util.List;

import javax.swing.AbstractListModel;

import tv.porst.daybreak.model.Block;
import tv.porst.daybreak.model.IScreenListener;
import tv.porst.daybreak.model.Screen;
import tv.porst.daybreak.model.SpriteLocation;

public class ScreenSelectionListModel extends AbstractListModel
{
	private final InternalScreenListener internalScreenListener = new InternalScreenListener();

	private final List<Screen> screens;

	public ScreenSelectionListModel(final List<Screen> screens)
	{
		this.screens = new ArrayList<Screen>(screens);

		for (final Screen screen : screens)
		{
			screen.addListener(internalScreenListener);
		}
	}

	@Override
	public Object getElementAt(final int index)
	{
		return null;
	}

	public List<Screen> getScreens()
	{
		return new ArrayList<Screen>(screens);
	}

	@Override
	public int getSize()
	{
		return screens.size();
	}

	public void setScreens(final List<Screen> screens)
	{
		this.screens.clear();
		this.screens.addAll(screens);

		fireContentsChanged(this, 0, getSize());
	}

	private class InternalScreenListener implements IScreenListener
	{
		@Override
		public void addedSprite(final Screen screen, final SpriteLocation spriteLocation)
		{
			fireContentsChanged(this, 0, getSize());
		}

		@Override
		public void changedBlock(final Screen screen, final int x, final int y, final Block block)
		{
			fireContentsChanged(this, 0, getSize());
		}
	}
}