package tv.porst.daybreak.gui.sprites;

import javax.swing.AbstractListModel;

import tv.porst.daybreak.model.Block;
import tv.porst.daybreak.model.FaxanaduRom;
import tv.porst.daybreak.model.IScreenListener;
import tv.porst.daybreak.model.Screen;
import tv.porst.daybreak.model.Sprite;
import tv.porst.daybreak.model.SpriteLocation;

public class SpriteSelectionListModel extends AbstractListModel
{
	private final FaxanaduRom rom;

	private final InternalScreenListener internalScreenListener = new InternalScreenListener();

	public SpriteSelectionListModel(final FaxanaduRom rom)
	{
		this.rom = rom;

		for (final Sprite sprite : rom.getSprites())
		{
//			sprite.addListener(internalScreenListener);
		}
	}

	@Override
	public Object getElementAt(final int index)
	{
		return null;
	}

	@Override
	public int getSize()
	{
		return rom.getSprites().size();
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

		@Override
		public void removeSprite(final Screen screen, final SpriteLocation sprite)
		{
			fireContentsChanged(this, 0, getSize());
		}
	}
}