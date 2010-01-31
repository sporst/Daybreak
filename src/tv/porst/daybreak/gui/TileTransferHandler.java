package tv.porst.daybreak.gui;

import java.awt.datatransfer.Transferable;

import javax.swing.JComponent;
import javax.swing.TransferHandler;

public class TileTransferHandler extends TransferHandler
{
	private final IDragTileProvider provider;

	public TileTransferHandler(final IDragTileProvider provider)
	{
		super("Tile");

		this.provider = provider;
	}

    @Override
	protected Transferable createTransferable(final JComponent c)
    {
    	return new TileTransferable(provider.getTile());
    }

    @Override
	public int getSourceActions(final JComponent c)
    {
    	return COPY;
    }
}
