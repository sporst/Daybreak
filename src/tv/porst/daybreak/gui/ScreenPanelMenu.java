package tv.porst.daybreak.gui;

import javax.swing.JCheckBoxMenuItem;
import javax.swing.JMenu;
import javax.swing.JPopupMenu;
import javax.swing.JSeparator;

import tv.porst.daybreak.gui.actions.HighlightDoorsAction;
import tv.porst.daybreak.gui.actions.HighlightSolidBlocksAction;
import tv.porst.daybreak.gui.actions.SelectBlockAction;
import tv.porst.daybreak.model.Block;

public class ScreenPanelMenu extends JPopupMenu
{
	public ScreenPanelMenu(final ScreenPanel panel, final ScreenPanelOptions options, final Block block)
	{
		add(new SelectBlockAction(panel, block));

		add(new JSeparator());

		final JMenu optionsMenu = new JMenu("Highlight");

		final JCheckBoxMenuItem highlightSolidMenu = new JCheckBoxMenuItem(new HighlightSolidBlocksAction(options));
		highlightSolidMenu.setSelected(options.getHighlightSolidBlocks());
		optionsMenu.add(highlightSolidMenu);

		final JCheckBoxMenuItem highlightDoors = new JCheckBoxMenuItem(new HighlightDoorsAction(options));
		highlightDoors.setSelected(options.getHighlightDoors());
		optionsMenu.add(highlightDoors);

		add(optionsMenu);
	}
}
