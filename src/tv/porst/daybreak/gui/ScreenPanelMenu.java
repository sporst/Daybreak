package tv.porst.daybreak.gui;

import javax.swing.JPopupMenu;
import javax.swing.JSeparator;

import tv.porst.daybreak.gui.actions.SelectBlockAction;
import tv.porst.daybreak.gui.blocks.HighlightBlocksMenu;
import tv.porst.daybreak.model.Block;

public class ScreenPanelMenu extends JPopupMenu
{
	public ScreenPanelMenu(final ScreenPanel panel, final ScreenPanelOptions options, final Block block)
	{
		add(new SelectBlockAction(panel, block));

		add(new JSeparator());

		add(new HighlightBlocksMenu(options.getHighlightingOptions()));
	}
}
