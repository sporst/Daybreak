package tv.porst.daybreak.gui;

import java.awt.BorderLayout;
import java.awt.Dimension;

import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JPanel;

import tv.porst.daybreak.model.FaxanaduRom;

public class MainWindow extends JFrame
{
	public MainWindow(final FaxanaduRom rom)
	{
		super("Daybreak");

		setDefaultCloseOperation(EXIT_ON_CLOSE);

		add(new ScreenTab(rom));

		final JMenuBar mainMenu = new JMenuBar();

		final JMenu fileMenu = new JMenu("File");

		mainMenu.add(fileMenu);

		setJMenuBar(mainMenu);

		final JPanel panel = new JPanel();
		panel.setPreferredSize(new Dimension(0, 20));

		add(panel, BorderLayout.SOUTH);

		pack();
		setLocationRelativeTo(null);
		setResizable(false);
	}
}
