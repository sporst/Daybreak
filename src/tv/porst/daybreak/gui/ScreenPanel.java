package tv.porst.daybreak.gui;

import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.dnd.DnDConstants;
import java.awt.dnd.DropTarget;
import java.awt.dnd.DropTargetAdapter;
import java.awt.dnd.DropTargetDragEvent;
import java.awt.dnd.DropTargetDropEvent;
import java.awt.dnd.DropTargetEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.io.IOException;
import java.util.List;

import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.TransferHandler;

import net.sourceforge.jnhf.helpers.ImageHelpers;
import net.sourceforge.jnhf.helpers.ListenerProvider;
import tv.porst.daybreak.gui.sprites.IDragSpriteProvider;
import tv.porst.daybreak.gui.sprites.SpriteBitmap;
import tv.porst.daybreak.gui.sprites.SpriteTransferHandler;
import tv.porst.daybreak.gui.sprites.SpriteTransferable;
import tv.porst.daybreak.model.Block;
import tv.porst.daybreak.model.Level;
import tv.porst.daybreak.model.MetaData;
import tv.porst.daybreak.model.Screen;
import tv.porst.daybreak.model.Sprite;
import tv.porst.daybreak.model.SpriteLocation;

public class ScreenPanel extends JPanel
{
	private Screen screen;
	private MetaData metaData;

	private static int TILES_X = 16;
	private static int TILES_Y = 13;
	private static int BLOCK_WIDTH = 32;

	private final ListenerProvider<IScreenPanelListener> listeners = new ListenerProvider<IScreenPanelListener>();

	private int mouseRow = -1;

	private int mouseCol = -1;

	private final InternalMouseListener internalMouseListener = new InternalMouseListener();
	private Block highlightedBlock;
	private Block selectedBlock;
	private Level level;

	private Sprite draggedSprite = null;

	private final SpriteDragProvider spriteDragProvider = new SpriteDragProvider();

	private final TransferHandler spriteTransferHandler = new SpriteTransferHandler(spriteDragProvider);

	private final ScreenPanelOptions options = new ScreenPanelOptions();

	private final IScreenPanelOptionsListener internalScreenPanelOptionsListener = new InternalScreenPanelOptionsListener();

	public ScreenPanel(final Level level, final Screen screen)
	{
		this.level = level;
		this.screen = screen;
		this.metaData = level.getMetaData();

		setPreferredSize(new Dimension(TILES_X * BLOCK_WIDTH, TILES_Y * BLOCK_WIDTH));

		new DropTarget(this, new InternalDropTarget());

		setTransferHandler(spriteTransferHandler);

		addMouseListener(internalMouseListener);
		addMouseMotionListener(internalMouseListener);
		addMouseWheelListener(internalMouseListener);

		options.addListener(internalScreenPanelOptionsListener);
	}

	private Sprite getSprite(final int col, final int row)
	{
		final List<SpriteLocation> spriteData = screen.getSpriteData();

		for (int i=spriteData.size()-1;i>=0;i--)
		{
			final SpriteLocation sprite = spriteData.get(i);

			if (
					col >= sprite.getX() && col < sprite.getX() + sprite.getSprite().width() / 2 &&
					row >= sprite.getY() && row < sprite.getY() + sprite.getSprite().height() / 2)
			{
				screen.removeSprite(sprite);

				return sprite.getSprite();
			}
		}

		return null;
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

		final ScreenBitmap bitmap = new ScreenBitmap(screen, options, metaData.getBlocks(), mouseRow, mouseCol, highlightedBlock);

		g.drawImage(bitmap, 0, 0, 2 * bitmap.getWidth(), 2 * bitmap.getHeight(), null);

		if (draggedSprite != null)
		{
			final SpriteBitmap d = new SpriteBitmap(draggedSprite);

			g.drawImage(ImageHelpers.resize(d, d.getWidth() / 2, d.getHeight() / 2), mouseCol * BLOCK_WIDTH,  mouseRow * BLOCK_WIDTH, null);
		}
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
		this.level = level;
		this.screen = screen;
		this.metaData = level.getMetaData();

		repaint();

		for (final IScreenPanelListener listener : listeners)
		{
			try
			{
				listener.changedScreen(this, level, screen);
			}
			catch(final Exception exception)
			{
				exception.printStackTrace();
			}
		}
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

	private class InternalDropTarget extends DropTargetAdapter
	{
	    @Override
		public void dragEnter(final DropTargetDragEvent dtde)
	    {
			final Transferable t = dtde.getTransferable();

			if (t.isDataFlavorSupported(SpriteTransferable.SPRITE_FLAVOR))
			{
	            try
				{
	            	draggedSprite = (Sprite)t.getTransferData(SpriteTransferable.SPRITE_FLAVOR);

	            	repaint();
				}
				catch (final UnsupportedFlavorException e)
				{
					e.printStackTrace();
				}
				catch (final IOException e)
				{
					e.printStackTrace();
				}
			}
	    }

	    @Override
		public void dragExit(final DropTargetEvent dte)
	    {
	    	draggedSprite = null;

	    	repaint();
	    }

	    @Override
		public void dragOver(final DropTargetDragEvent dtde)
	    {
	    	mouseCol = dtde.getLocation().x / BLOCK_WIDTH;
	    	mouseRow = dtde.getLocation().y / BLOCK_WIDTH;

	    	repaint();
	    }

		@Override
		public void drop(final DropTargetDropEvent dtde)
		{
			final Transferable t = dtde.getTransferable();

			if (t.isDataFlavorSupported(SpriteTransferable.SPRITE_FLAVOR))
			{
				dtde.acceptDrop(DnDConstants.ACTION_COPY_OR_MOVE);

	            try
				{
					final Sprite sprite = (Sprite)t.getTransferData(SpriteTransferable.SPRITE_FLAVOR);

					final int row = dtde.getLocation().y / BLOCK_WIDTH;
					final int col = dtde.getLocation().x / BLOCK_WIDTH;

					screen.addSprite(new SpriteLocation(sprite, col, row, (byte) 0));

					dtde.getDropTargetContext().dropComplete(true);

					draggedSprite = null;
				}
				catch (final UnsupportedFlavorException e)
				{
					e.printStackTrace();
				}
				catch (final IOException e)
				{
					e.printStackTrace();
				}
			}
			else
			{
				dtde.rejectDrop();
			}
		}
	}

	private class InternalMouseListener extends MouseAdapter
	{
	    private void showPopupMenu(final MouseEvent event, final Block block)
		{
	    	final ScreenPanelMenu menu = new ScreenPanelMenu(ScreenPanel.this, options, block);

	    	menu.show(ScreenPanel.this, event.getX(), event.getY());
		}

	    @Override
	    public void mouseDragged(final MouseEvent e)
	    {
	    	mouseCol = e.getX() / BLOCK_WIDTH;
	    	mouseRow = e.getY() / BLOCK_WIDTH;

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
	    	mouseRow = e.getY() / BLOCK_WIDTH;
	    	mouseCol = e.getX() / BLOCK_WIDTH;

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
			mouseCol = event.getX() / BLOCK_WIDTH;
			mouseRow = event.getY() / BLOCK_WIDTH;

			final Block block = metaData.getBlocks()[screen.getSquareNumbers()[mouseRow][mouseCol]];

			if (event.isPopupTrigger())
			{
				showPopupMenu(event, block);
			}

			if (SwingUtilities.isLeftMouseButton(event))
			{
				draggedSprite = getSprite(mouseCol, mouseRow);

				if (draggedSprite != null)
				{
					final JComponent c = (JComponent) event.getSource();
					final TransferHandler handler = c.getTransferHandler();
					handler.exportAsDrag(c, event, TransferHandler.COPY);
				}
			}

			repaint();
		}

		@Override
		public void mouseReleased(final MouseEvent event)
	    {
	    	mouseCol = event.getX() / BLOCK_WIDTH;
	    	mouseRow = event.getY() / BLOCK_WIDTH;

	    	final Block block = metaData.getBlocks()[screen.getSquareNumbers()[mouseRow][mouseCol]];

			if (SwingUtilities.isLeftMouseButton(event) && selectedBlock != null)
			{
				screen.setBlock(mouseCol, mouseRow, selectedBlock);
			}
			else if (SwingUtilities.isRightMouseButton(event) && selectedBlock != null)
			{
				setSelectedBlock(null);
			}
			else if (event.isPopupTrigger())
			{
				showPopupMenu(event, block);
			}

	    	repaint();
	    }

		@Override
		public void mouseWheelMoved(final MouseWheelEvent e)
	    {
	    	final int currentIndex = level.getScreens().indexOf(screen);

	    	final int scrolledIndex = currentIndex + e.getWheelRotation();

	    	if (scrolledIndex < 0 || scrolledIndex >= level.getScreens().size())
			{
				return;
			}

	    	setScreen(level, level.getScreens().get(scrolledIndex));
	    }
	}

	private class InternalScreenPanelOptionsListener implements IScreenPanelOptionsListener
	{
		@Override
		public void changedDoorHighlighting(final boolean highlighted)
		{
			repaint();
		}

		@Override
		public void changedSolidBlockHighlighting(final boolean highlighted)
		{
			repaint();
		}
	}

	private class SpriteDragProvider implements IDragSpriteProvider
	{
		@Override
		public Sprite getSprite()
		{
			return draggedSprite;
		}
	}
}
