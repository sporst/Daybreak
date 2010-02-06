package tv.porst.daybreak.gui;

import javax.swing.JFrame;
import javax.swing.JTabbedPane;

import tv.porst.daybreak.model.FaxanaduRom;

public class MainWindow extends JFrame
{
	public MainWindow(final FaxanaduRom rom)
	{
		super("Daybreak");

		setDefaultCloseOperation(EXIT_ON_CLOSE);

		final JTabbedPane pane = new JTabbedPane();

		pane.addTab("Levels", new ScreenTab(rom));
		pane.addTab("Sprites", new SpritesTab(rom));

		add(pane);

		pack();
		setLocationRelativeTo(null);
	}
}
