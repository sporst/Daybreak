package tv.porst.daybreak.gui;

import javax.swing.JPopupMenu;

import net.sourceforge.jnhf.gui.Palette;
import tv.porst.daybreak.gui.actions.EditBlockAction;
import tv.porst.daybreak.model.Block;
import tv.porst.daybreak.model.TileInformation;

public class BlockPanelPopupMenu extends JPopupMenu
{
	public BlockPanelPopupMenu(final Block block, final TileInformation tileInformation, final Palette palette)
	{
		add(new EditBlockAction(block, tileInformation, palette));
	}
}
