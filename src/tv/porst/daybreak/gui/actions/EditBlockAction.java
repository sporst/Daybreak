package tv.porst.daybreak.gui.actions;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;

import net.sourceforge.jnhf.gui.Palette;
import tv.porst.daybreak.gui.EditBlockDialog;
import tv.porst.daybreak.model.Block;
import tv.porst.daybreak.model.TileInformation;

public class EditBlockAction extends AbstractAction
{
	private final Block block;
	private final TileInformation tileInformation;
	private final Palette palette;

	public EditBlockAction(final Block block, final TileInformation tileInformation, final Palette palette)
	{
		super("Edit Block");

		this.block = block;
		this.tileInformation = tileInformation;
		this.palette = palette;
	}

	@Override
	public void actionPerformed(final ActionEvent e)
	{
		final EditBlockDialog dialog = new EditBlockDialog(block, tileInformation, palette);

		dialog.setVisible(true);
	}
}
