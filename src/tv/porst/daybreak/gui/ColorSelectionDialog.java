package tv.porst.daybreak.gui;

import java.awt.BorderLayout;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JDialog;

import net.sourceforge.jnhf.gui.ColorSelectionPanel;
import net.sourceforge.jnhf.gui.IColorSelectionListener;
import net.sourceforge.jnhf.gui.Palettes;

public abstract class ColorSelectionDialog extends JDialog
{
	private final IColorSelectionListener colorListener = new IColorSelectionListener()
	{
		@Override
		public void clickedColor(final int index)
		{
			selected(index);

			panel.removeListener(this);

			dispose();
		}
	};

	final ColorSelectionPanel panel = new ColorSelectionPanel(Palettes.FCEU_PAL_PALETTE);

	public ColorSelectionDialog()
	{
		setLayout(new BorderLayout());

		setUndecorated(true);

		panel.addListener(colorListener);

		add(panel);

		pack();

		addWindowFocusListener(new WindowAdapter()
		{
			@Override
			public void windowLostFocus(final WindowEvent e)
			{
				dispose();
			}
		});
	}

	protected abstract void selected(int index);
}
