package tv.porst.daybreak.gui.actions;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;

import tv.porst.daybreak.gui.ScreenPanelOptions;

public class HighlightSolidBlocksAction extends AbstractAction
{
	private final ScreenPanelOptions options;

	public HighlightSolidBlocksAction(final ScreenPanelOptions options)
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
