package tv.porst.daybreak.gui.sprites;

import java.awt.datatransfer.Transferable;

import javax.swing.JComponent;
import javax.swing.TransferHandler;

public class SpriteTransferHandler extends TransferHandler
{
	private final IDragSpriteProvider provider;

	public SpriteTransferHandler(final IDragSpriteProvider provider)
	{
		super("Sprite");

		this.provider = provider;
	}

    @Override
	protected Transferable createTransferable(final JComponent c)
    {
    	return new SpriteTransferable(provider.getSprite());
    }

    @Override
	public int getSourceActions(final JComponent c)
    {
    	return COPY;
    }
}
