package tv.porst.daybreak.gui.blocks;

import javax.swing.JCheckBoxMenuItem;
import javax.swing.JMenu;

import tv.porst.daybreak.gui.IBlockHighlighterOptions;
import tv.porst.daybreak.gui.actions.HighlightAirAction;
import tv.porst.daybreak.gui.actions.HighlightDoorsAction;
import tv.porst.daybreak.gui.actions.HighlightSolidBlocksAction;

public class HighlightBlocksMenu extends JMenu
{
	public HighlightBlocksMenu(final IBlockHighlighterOptions options)
	{
		super("Highlight");

		final JCheckBoxMenuItem highlightAirMenu = new JCheckBoxMenuItem(new HighlightAirAction(options));
		highlightAirMenu.setSelected(options.getHighlightAir());
		add(highlightAirMenu);

		final JCheckBoxMenuItem highlightSolidMenu = new JCheckBoxMenuItem(new HighlightSolidBlocksAction(options));
		highlightSolidMenu.setSelected(options.getHighlightSolidBlocks());
		add(highlightSolidMenu);

		final JCheckBoxMenuItem highlightDoors = new JCheckBoxMenuItem(new HighlightDoorsAction(options));
		highlightDoors.setSelected(options.getHighlightDoors());
		add(highlightDoors);
	}
}
