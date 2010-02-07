package tv.porst.daybreak.gui.actions;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;

import tv.porst.daybreak.gui.IBlockHighlighterOptions;

public class HighlightAirAction extends AbstractAction
{
	private final IBlockHighlighterOptions options;

	public HighlightAirAction(final IBlockHighlighterOptions options)
	{
		super("Air");

		this.options = options;
	}

	@Override
	public void actionPerformed(final ActionEvent e)
	{
		options.setHighlightAir(!options.getHighlightAir());
	}
}
