package tv.porst.daybreak.gui.actions;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;

import tv.porst.daybreak.gui.IBlockHighlighterOptions;

public class HighlightDoorsAction extends AbstractAction
{
	private final IBlockHighlighterOptions options;

	public HighlightDoorsAction(final IBlockHighlighterOptions options)
	{
		super("Doors");

		this.options = options;
	}

	@Override
	public void actionPerformed(final ActionEvent e)
	{
		options.setHighlightDoors(!options.getHighlightDoors());
	}
}
