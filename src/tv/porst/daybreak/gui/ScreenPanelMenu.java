package tv.porst.daybreak.gui;

import javax.swing.JPopupMenu;

import tv.porst.daybreak.gui.actions.SelectBlockAction;
import tv.porst.daybreak.model.Block;

public class ScreenPanelMenu extends JPopupMenu
{
	public ScreenPanelMenu(final ScreenPanel panel, final Block block)
	{
		add(new SelectBlockAction(panel, block));
	}
}
