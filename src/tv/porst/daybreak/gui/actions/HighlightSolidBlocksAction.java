package tv.porst.daybreak.gui.actions;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;

import tv.porst.daybreak.gui.IBlockHighlighterOptions;

public class HighlightSolidBlocksAction extends AbstractAction
{
	private final IBlockHighlighterOptions options;

	public HighlightSolidBlocksAction(final IBlockHighlighterOptions options)
	{
		super("Solid Blocks");

		this.options = options;
	}

	@Override
	public void actionPerformed(final ActionEvent e)
	{
		options.setHighlightSolidBlocks(!options.getHighlightSolidBlocks());
	}
}
