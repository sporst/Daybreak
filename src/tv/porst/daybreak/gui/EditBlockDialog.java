package tv.porst.daybreak.gui;

import java.awt.BorderLayout;
import java.awt.Window;

import javax.swing.JDialog;

import net.sourceforge.jnhf.gui.Palette;
import tv.porst.daybreak.model.Block;
import tv.porst.daybreak.model.TileInformation;

public class EditBlockDialog extends JDialog
{
	public EditBlockDialog(final Block block, final TileInformation tileInformation, final Palette palette)
	{
		super((Window) null, "Edit Block");

		setLayout(new BorderLayout());

		setAlwaysOnTop(true);
		setResizable(false);

		final SingleBlockPanel panel = new SingleBlockPanel(block, tileInformation, palette);

		add(panel);

		setSize(200, 200);
	}
}
