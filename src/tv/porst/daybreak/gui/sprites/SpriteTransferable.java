package tv.porst.daybreak.gui.sprites;

import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.IOException;

import tv.porst.daybreak.model.Sprite;

public class SpriteTransferable implements Transferable
{
	private static final DataFlavor[] SUPPORTED_FLAVORS = new DataFlavor[1];

	public static final DataFlavor SPRITE_FLAVOR;

	private final Sprite m_sprite;

	static
	{
		SPRITE_FLAVOR = new DataFlavor(Sprite.class, "Sprite");
		SUPPORTED_FLAVORS[0] = SPRITE_FLAVOR;
	}

	public SpriteTransferable(final Sprite sprite)
	{
		m_sprite = sprite;
	}

	public Sprite getSprite()
	{
		return m_sprite;
	}

	@Override
	public Object getTransferData(final DataFlavor flavor) throws UnsupportedFlavorException, IOException
	{
		if (flavor.equals(SPRITE_FLAVOR))
		{
			return m_sprite;
		}

		throw new UnsupportedFlavorException(flavor);
	}

	@Override
	public DataFlavor[] getTransferDataFlavors()
	{
		return SUPPORTED_FLAVORS.clone();
	}

	@Override
	public boolean isDataFlavorSupported(final DataFlavor flavor)
	{
		return flavor.equals(SPRITE_FLAVOR);
	}
}
