package tv.porst.daybreak.gui;

import java.awt.BorderLayout;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.TitledBorder;

import tv.porst.daybreak.model.Block;
import tv.porst.daybreak.model.FaxanaduRom;
import tv.porst.daybreak.model.Level;
import tv.porst.daybreak.model.Screen;

public class MainWindow extends JFrame
{
	private final IScreenSelectionListener internalListener = new InternalScreenSelectionListener();
	private final ScreenPanel screenPanel;
	private final TilePanel tilePanel;
	private final BlockPanel blockPanel;
	private final IScreenPanelListener internalScreenPanelListener = new InternalScreenPanelListener();
	private final IBlockPanelListener internalBlockPanelListener = new InternalBlockPanelListener();

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
		screenPanel.addListener(internalScreenPanelListener );

		outerScreenPanel.add(screenPanel);

		outerScreenPanel.setBorder(new TitledBorder("Screen"));

		leftPanel.add(outerScreenPanel);

		final JPanel lowerPanel = new JPanel(new BorderLayout());

		final JPanel outerBlockPanel = new JPanel(new BorderLayout());
		outerBlockPanel.setBorder(new TitledBorder("Blocks"));

		blockPanel = new BlockPanel(level.getMetaData().getBlocks(), screen.getTiles(), screen.getPalette());
		blockPanel.addListener(internalBlockPanelListener );

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

	private void updateTileHighlighting(final Block block)
	{
		if (block == null)
		{
			tilePanel.setHighlightedTiles(-1, -1, -1, -1);
		}
		else
		{
			final int start = screenPanel.getScreen().getTiles().getStartLocation();

			final int tileIndex1 = block.getTile1() - start;
			final int tileIndex2 = block.getTile2() - start;
			final int tileIndex3 = block.getTile3() - start;
			final int tileIndex4 = block.getTile4() - start;

			tilePanel.setHighlightedTiles(tileIndex1, tileIndex2, tileIndex3, tileIndex4);
		}
	}

	private class InternalBlockPanelListener implements IBlockPanelListener
	{
		@Override
		public void clickedBlock(final Block block)
		{
			screenPanel.setSelectedBlock(block);
		}

		@Override
		public void hoveredBlock(final Block block)
		{
			screenPanel.setHighlightedBlock(block);

			updateTileHighlighting(block);
		}
	}

	private class InternalScreenPanelListener implements IScreenPanelListener
	{
		@Override
		public void hoveredBlock(final Block block)
		{
			blockPanel.setHighlightedBlock(block);

			updateTileHighlighting(block);
		}
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
