package tv.porst.daybreak.gui;

import java.awt.BorderLayout;

import javax.swing.JPanel;
import javax.swing.JScrollPane;

import tv.porst.daybreak.model.FaxanaduRom;

public class SpritesTab extends JPanel
{
	public SpritesTab(final FaxanaduRom rom)
	{
		super(new BorderLayout());

		final SpriteSelectionList list = new SpriteSelectionList(rom);

		final JPanel leftPanel = new JPanel(new BorderLayout());

		leftPanel.add(new JScrollPane(list));

		add(leftPanel, BorderLayout.WEST);
	}
}
