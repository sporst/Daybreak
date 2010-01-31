package tv.porst.daybreak.gui;

import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import net.sourceforge.jnhf.helpers.ListenerProvider;
import tv.porst.daybreak.model.Block;
import tv.porst.daybreak.model.Level;
import tv.porst.daybreak.model.MetaData;
import tv.porst.daybreak.model.Screen;

public class ScreenPanel extends JPanel
{
	private Screen screen;
	private MetaData metaData;

	private static int TILES_X = 16;
	private static int TILES_Y = 13;
	private static int TILE_WIDTH = 32;

	private final ListenerProvider<IScreenPanelListener> listeners = new ListenerProvider<IScreenPanelListener>();

	private int mouseRow = -1;

	private int mouseCol = -1;

	private final InternalMouseListener internalMouseListener = new InternalMouseListener();
	private Block highlightedBlock;
	private Block selectedBlock;

	public ScreenPanel(final Level level, final Screen screen)
	{
		this.screen = screen;
		this.metaData = level.getMetaData();

		setPreferredSize(new Dimension(TILES_X * TILE_WIDTH, TILES_Y * TILE_WIDTH));

		addMouseListener(internalMouseListener);
		addMouseMotionListener(internalMouseListener);
	}

	public void addListener(final IScreenPanelListener listener)
	{
		listeners.addListener(listener);
	}

	public Screen getScreen()
	{
		return screen;
	}

	@Override
	public void paintComponent(final Graphics g)
	{
		super.paintComponent(g);

		final ScreenBitmap bitmap = new ScreenBitmap(screen, metaData, mouseRow, mouseCol, highlightedBlock);

		g.drawImage(bitmap, 0, 0, 2 * bitmap.getWidth(), 2 * bitmap.getHeight(), null);
	}
	public void removeListener(final IScreenPanelListener listener)
	{
		listeners.removeListener(listener);
	}

	public void setHighlightedBlock(final Block block)
	{
		this.highlightedBlock = block;

		repaint();
	}

	public void setScreen(final Level level, final Screen screen)
	{
		this.screen = screen;
		this.metaData = level.getMetaData();

		repaint();
	}

	public void setSelectedBlock(final Block block)
	{
		selectedBlock = block;

		if (block == null)
		{
			setCursor(null);
		}
		else
		{
			final Toolkit toolkit = Toolkit.getDefaultToolkit();
			final Image image = new BlockBitmap(block, screen.getTiles(), screen.getPalette());
			final Cursor brokenCursor = toolkit.createCustomCursor(image , new Point(0,0), "img");
			setCursor(brokenCursor);
		}
	}

	private class InternalMouseListener extends MouseAdapter
	{
	    private void showPopupMenu(final MouseEvent event, final Block block)
		{
	    	final ScreenPanelMenu menu = new ScreenPanelMenu(ScreenPanel.this, block);

	    	menu.show(ScreenPanel.this, event.getX(), event.getY());
		}

	    @Override
	    public void mouseDragged(final MouseEvent e)
	    {
	    	mouseCol = e.getX() / TILE_WIDTH;
	    	mouseRow = e.getY() / TILE_WIDTH;

			if (selectedBlock != null)
			{
				screen.setBlock(mouseCol, mouseRow, selectedBlock);
			}

	    	repaint();
	    }

	    @Override
		public void mouseExited(final MouseEvent e)
	    {
	    	mouseRow = -1;
	    	mouseCol = -1;

	    	for (final IScreenPanelListener listener : listeners)
			{
	    		try
	    		{
	    			listener.hoveredBlock(null);
	    		}
	    		catch(final Exception exception)
	    		{
	    			exception.printStackTrace();
	    		}
			}

	    	repaint();
	    }

	    @Override
		public void mouseMoved(final MouseEvent e)
	    {
	    	mouseRow = e.getY() / TILE_WIDTH;
	    	mouseCol = e.getX() / TILE_WIDTH;

	    	final Block block = metaData.getBlocks()[screen.getSquareNumbers()[mouseRow][mouseCol]];

	    	for (final IScreenPanelListener listener : listeners)
			{
	    		try
	    		{
	    			listener.hoveredBlock(block);
	    		}
	    		catch(final Exception exception)
	    		{
	    			exception.printStackTrace();
	    		}
			}

	    	repaint();
	    }

		@Override
		public void mousePressed(final MouseEvent event)
		{
			mouseCol = event.getX() / TILE_WIDTH;
			mouseRow = event.getY() / TILE_WIDTH;

			final Block block = metaData.getBlocks()[screen.getSquareNumbers()[mouseRow][mouseCol]];

			if (event.isPopupTrigger())
			{
				showPopupMenu(event, block);
			}

			repaint();
		}

		@Override
		public void mouseReleased(final MouseEvent event)
	    {
	    	mouseCol = event.getX() / TILE_WIDTH;
	    	mouseRow = event.getY() / TILE_WIDTH;

	    	final Block block = metaData.getBlocks()[screen.getSquareNumbers()[mouseRow][mouseCol]];

			if (SwingUtilities.isLeftMouseButton(event) && selectedBlock != null)
			{
				screen.setBlock(mouseCol, mouseRow, selectedBlock);
			}
			else if (SwingUtilities.isRightMouseButton(event))
			{
				setSelectedBlock(null);
			}

			if (event.isPopupTrigger())
			{
				showPopupMenu(event, block);
			}

	    	repaint();
	    }
	}
}
