package tv.porst.daybreak.gui;

import java.awt.BorderLayout;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.TitledBorder;

import tv.porst.daybreak.model.FaxanaduRom;
import tv.porst.daybreak.model.Level;
import tv.porst.daybreak.model.Screen;

public class MainWindow extends JFrame
{
	private final IScreenSelectionListener internalListener = new InternalScreenSelectionListener();
	private final ScreenPanel screenPanel;
	private final TilePanel tilePanel;
	private final BlockPanel blockPanel;

	public MainWindow(final FaxanaduRom rom)
	{
		super("Daybreak");

		final int screenIndex = 0;
		final int levelIndex = 0;

		final Level level = rom.getLevels().get(levelIndex);
		final Screen screen = level.getScreens().get(screenIndex);

		final JPanel leftPanel = new JPanel(new BorderLayout());

		final JPanel outerScreenPanel = new JPanel(new BorderLayout());
		screenPanel = new ScreenPanel(level, screen);

		outerScreenPanel.add(screenPanel);

		outerScreenPanel.setBorder(new TitledBorder("Screen"));

		leftPanel.add(outerScreenPanel);

		final JPanel lowerPanel = new JPanel(new BorderLayout());

		final JPanel outerBlockPanel = new JPanel(new BorderLayout());
		outerBlockPanel.setBorder(new TitledBorder("Blocks"));

		blockPanel = new BlockPanel(level.getMetaData().getBlocks(), screen.getTiles(), screen.getPalette());

		outerBlockPanel.add(blockPanel);

		lowerPanel.add(outerBlockPanel);

		final JPanel outerTilePanel = new JPanel(new BorderLayout());
		outerTilePanel.setBorder(new TitledBorder("Tiles"));
		tilePanel = new TilePanel(screen.getTiles(), screen.getPalette());

		outerTilePanel.add(tilePanel);

		lowerPanel.add(outerTilePanel, BorderLayout.SOUTH);

		leftPanel.add(lowerPanel, BorderLayout.SOUTH);

		add(leftPanel);

		final ScreenSelectionPanel screenSelectionPanel = new ScreenSelectionPanel(rom.getLevels());
		screenSelectionPanel.addListener(internalListener);

		add(screenSelectionPanel, BorderLayout.WEST);

		setDefaultCloseOperation(EXIT_ON_CLOSE);

		pack();
		setLocationRelativeTo(null);
	}

	private class InternalScreenSelectionListener implements IScreenSelectionListener
	{
		@Override
		public void selectedScreen(final Level level, final Screen screen)
		{
			screenPanel.setScreen(level, screen);
			tilePanel.setTiles(screen.getTiles(), screen.getPalette());
			blockPanel.setBlocks(level.getMetaData().getBlocks(), screen.getTiles(), screen.getPalette());
		}
	}
}
