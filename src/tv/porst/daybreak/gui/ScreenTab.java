package tv.porst.daybreak.gui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.SwingUtilities;
import javax.swing.border.TitledBorder;

import net.sourceforge.jnhf.gui.PalettePanel;
import tv.porst.daybreak.gui.actions.ShowScreensWithSpriteAction;
import tv.porst.daybreak.gui.blocks.BlockPanel;
import tv.porst.daybreak.gui.blocks.IBlockPanelListener;
import tv.porst.daybreak.gui.sprites.SpriteSelectionList;
import tv.porst.daybreak.model.Block;
import tv.porst.daybreak.model.EditedScreenModel;
import tv.porst.daybreak.model.FaxanaduRom;
import tv.porst.daybreak.model.IEditedScreenModelListener;
import tv.porst.daybreak.model.Level;
import tv.porst.daybreak.model.Screen;

public class ScreenTab extends JPanel
{
	private final IScreenSelectionListener internalListener = new InternalScreenSelectionListener();
	private final ScreenPanel screenPanel;
	private final TilePanel tilePanel;
	private final BlockPanel blockPanel;
	private final IScreenPanelListener internalScreenPanelListener = new InternalScreenPanelListener();
	private final IBlockPanelListener internalBlockPanelListener = new InternalBlockPanelListener();
	private final PalettePanel palettePanel;
	private final MouseListener internalSpriteListMouseListener = new InternalSpriteListMouseListener();
	private final SpriteSelectionList spriteSelectionList;
	private final FaxanaduRom rom;

	private final EditedScreenModel model = new EditedScreenModel();
	private final IEditedScreenModelListener internalSelectionListener = new InternalEditedScreenModelListener();
	private final ScreenSelectionPanel screenSelectionPanel;
	private final MouseListener internalPaletteMouseListener = new InternalPaletteMouseListener();

	public ScreenTab(final FaxanaduRom rom)
	{
		super(new BorderLayout());

		final int screenIndex = 0;
		final int levelIndex = 0;

		this.rom = rom;

		final Level level = rom.getLevels().get(levelIndex);
		final Screen screen = level.getScreens().get(screenIndex);

		final JPanel leftPanel = new JPanel(new BorderLayout());

		final JPanel outerScreenPanel = new JPanel(new BorderLayout());
		screenPanel = new ScreenPanel(level, screen);
		screenPanel.addListener(internalScreenPanelListener );

		palettePanel = new PalettePanel(screen.getPalette());
		palettePanel.addMouseListener(internalPaletteMouseListener);

		final JPanel outerPalettePanel = new JPanel(new BorderLayout());
		outerPalettePanel.setBorder(new TitledBorder("Screen Palette"));
		outerPalettePanel.setPreferredSize(new Dimension(palettePanel.getPreferredSize().width + 10, palettePanel.getPreferredSize().height + 30));

		outerPalettePanel.add(palettePanel, BorderLayout.NORTH);

		outerScreenPanel.add(outerPalettePanel, BorderLayout.NORTH);

		final JPanel innerScreenPanel = new JPanel(new BorderLayout());

		innerScreenPanel.add(screenPanel);
		innerScreenPanel.setBorder(new TitledBorder("Screen"));

		outerScreenPanel.add(innerScreenPanel);

		leftPanel.add(outerScreenPanel);

		final JPanel lowerPanel = new JPanel(new BorderLayout());

		final JPanel outerBlockPanel = new JPanel(new BorderLayout());
		outerBlockPanel.setBorder(new TitledBorder("Blocks"));

		blockPanel = new BlockPanel(level.getMetaData().getBlocks(), screen.getTiles(), screen.getPalette());
		blockPanel.addListener(internalBlockPanelListener);

		outerBlockPanel.add(blockPanel);

		lowerPanel.add(outerBlockPanel);

		final JPanel outerTilePanel = new JPanel(new BorderLayout());
		outerTilePanel.setBorder(new TitledBorder("Tiles"));
		tilePanel = new TilePanel(screen.getTiles(), screen.getPalette());

		outerTilePanel.add(tilePanel);

		lowerPanel.add(outerTilePanel, BorderLayout.SOUTH);

		leftPanel.add(lowerPanel, BorderLayout.SOUTH);

		add(leftPanel);

		final JPanel spritePanel = new JPanel(new BorderLayout());
		spritePanel.setBorder(new TitledBorder("Sprites"));

		spriteSelectionList = new SpriteSelectionList(rom);
		spriteSelectionList.addMouseListener(internalSpriteListMouseListener );

		spritePanel.add(new JScrollPane(spriteSelectionList));

		add(spritePanel, BorderLayout.EAST);

		screenSelectionPanel = new ScreenSelectionPanel(rom.getLevels());
		screenSelectionPanel.addListener(internalListener);

		add(screenSelectionPanel, BorderLayout.WEST);

		model.addListener(internalSelectionListener);
	}

	private void selectScreen(final Level level, final Screen screen)
	{
		screenSelectionPanel.setScreen(level, screen);
		screenPanel.setScreen(level, screen);
		tilePanel.setTiles(screen.getTiles(), screen.getPalette());
		blockPanel.setBlocks(level.getMetaData().getBlocks(), screen.getTiles(), screen.getPalette());
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
		public void clickedBlock(final MouseEvent event, final Block block)
		{
			if (SwingUtilities.isLeftMouseButton(event))
			{
				screenPanel.setSelectedBlock(block);
			}
		}

		@Override
		public void hoveredBlock(final Block block)
		{
			screenPanel.setHighlightedBlock(block);

			updateTileHighlighting(block);
		}
	}

	private class InternalEditedScreenModelListener implements IEditedScreenModelListener
	{
		@Override
		public void changedScreen(final Level level, final Screen screen)
		{
			selectScreen(level, screen);
		}
	}

	private class InternalPaletteMouseListener extends MouseAdapter
	{
		@Override
		public void mouseReleased(final MouseEvent e)
	    {
			final int clickedColumn = e.getX() / 32;

//			final Color clickedColor = Palettes.FCEU_PAL_PALETTE[palettePanel.getPalette().getData()[clickedColumn]];

			if (SwingUtilities.isRightMouseButton(e))
			{
				final ColorSelectionDialog dialog = new ColorSelectionDialog()
				{
					@Override
					protected void selected(final int index)
					{
						palettePanel.getPalette().setColor(clickedColumn, (byte) index);
					}
				};

				dialog.setLocation(e.getXOnScreen(), e.getYOnScreen());
				dialog.setVisible(true);
			}
	    }
	}

	private class InternalScreenPanelListener implements IScreenPanelListener
	{
		@Override
		public void changedScreen(final ScreenPanel screenPanel, final Level level, final Screen screen)
		{
			palettePanel.setPalette(screen.getPalette());
		}

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
			selectScreen(level, screen);
		}
	}

	private class InternalSpriteListMouseListener extends MouseAdapter
	{

		private void showPopupMenu(final MouseEvent e)
		{
	    	final int index = spriteSelectionList.locationToIndex(e.getPoint());

	    	final JPopupMenu menu = new JPopupMenu();

	    	menu.add(new JMenuItem(new ShowScreensWithSpriteAction(rom, rom.getSprites().get(index), model)));

	    	menu.show(e.getComponent(), e.getX(), e.getY());
		}

	    @Override
		public void mousePressed(final MouseEvent e)
	    {
	    	if (e.isPopupTrigger())
	    	{
	    		showPopupMenu(e);
	    	}
	    }

		@Override
	    public void mouseReleased(final MouseEvent e)
	    {
	    	if (e.isPopupTrigger())
	    	{
	    		showPopupMenu(e);
	    	}
	    }
	}
}
