package tv.porst.daybreak.gui.blocks;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.dnd.DnDConstants;
import java.awt.dnd.DropTarget;
import java.awt.dnd.DropTargetAdapter;
import java.awt.dnd.DropTargetDropEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;

import javax.swing.JPanel;

import net.sourceforge.jnhf.gui.Palette;
import net.sourceforge.jnhf.helpers.ListenerProvider;
import net.sourceforge.jnhf.romfile.TileData;
import tv.porst.daybreak.gui.BlockBitmap;
import tv.porst.daybreak.gui.BlockHighlighter;
import tv.porst.daybreak.gui.TileTransferable;
import tv.porst.daybreak.gui.Transparency;
import tv.porst.daybreak.model.Block;
import tv.porst.daybreak.model.TileInformation;

public class BlockPanel extends JPanel
{
	private Block[] blocks;
	private TileInformation tileInformation;
	private Palette palette;
	private Block highlightedBlock;

	private static final int BLOCK_SIZE = 32;
	private static final int BLOCK_PER_ROW = 16;

	private final InternalMouseListener internalMouseListener = new InternalMouseListener();

	private final ListenerProvider<IBlockPanelListener> listeners = new ListenerProvider<IBlockPanelListener>();

	private final BlockPanelOptions options = new BlockPanelOptions();

	private final IBlockHighlightingOptionsListener internalOptionsListener = new InternalOptionsListener();

	public BlockPanel(final Block[] blocks, final TileInformation tileInformation, final Palette palette)
	{
		this.blocks = blocks.clone();
		this.tileInformation = tileInformation;
		this.palette = palette;

		new DropTarget(this, new InternalDropTarget());

		addMouseListener(internalMouseListener);
		addMouseMotionListener(internalMouseListener);

		options.getHighlightingOptions().addListener(internalOptionsListener);

		setPreferredSize(new Dimension(BLOCK_PER_ROW * BLOCK_SIZE, blocks.length / BLOCK_PER_ROW * BLOCK_SIZE));
	}

	public void addListener(final IBlockPanelListener listener)
	{
		listeners.addListener(listener);
	}

	@Override
	public void paintComponent(final Graphics g)
	{
		final Graphics2D g2d = (Graphics2D) g;

		g.setColor(Color.WHITE);
		g.fillRect(0, 0, getWidth(), getHeight());

		for (int i=0;i<blocks.length;i++)
		{
			final Block block = blocks[i];

			final BlockBitmap bitmap = new BlockBitmap(block, tileInformation, palette);

			BlockHighlighter.highlight(options.getHighlightingOptions(), block, bitmap);

			final int row = i / BLOCK_PER_ROW;
			final int col = i % BLOCK_PER_ROW;

			if (highlightedBlock == block || highlightedBlock == null)
			{
				g2d.setComposite(Transparency.makeComposite(1.0f));
			}
			else
			{
				g2d.setComposite(Transparency.makeComposite(0.3f));
			}

			g.drawImage(bitmap, BLOCK_SIZE * col, BLOCK_SIZE * row, BLOCK_SIZE, BLOCK_SIZE, null);

			if (highlightedBlock == block)
			{
				g.setColor(Color.RED);
				g2d.setStroke(new BasicStroke(2));
				g.drawRect(BLOCK_SIZE * col, BLOCK_SIZE * row, BLOCK_SIZE - 1, BLOCK_SIZE - 1);
			}
		}
	}

	public void removeListener(final IBlockPanelListener listener)
	{
		listeners.removeListener(listener);
	}

	public void setBlocks(final Block[] blocks, final TileInformation tileInformation, final Palette palette)
	{
		this.blocks = blocks.clone();
		this.tileInformation = tileInformation;
		this.palette = palette;

		repaint();
	}

	public void setHighlightedBlock(final Block highlightedBlock)
	{
		this.highlightedBlock = highlightedBlock;

		repaint();
	}

	private class InternalDropTarget extends DropTargetAdapter
	{
		@Override
		public void drop(final DropTargetDropEvent dtde)
		{
			final Transferable t = dtde.getTransferable();

			if (t.isDataFlavorSupported(TileTransferable.TILE_FLAVOR))
			{
				dtde.acceptDrop(DnDConstants.ACTION_COPY_OR_MOVE);

	            try
				{
					final TileData s = (TileData)t.getTransferData(TileTransferable.TILE_FLAVOR);

					final int row = dtde.getLocation().y / BLOCK_SIZE;
					final int col = dtde.getLocation().x / BLOCK_SIZE;

					final int subrow = dtde.getLocation().y % BLOCK_SIZE / (BLOCK_SIZE / 2);
					final int subcol = dtde.getLocation().x % BLOCK_SIZE / (BLOCK_SIZE / 2);

					if (subrow == 0 && subcol == 0)
					{
						blocks[row * BLOCK_PER_ROW + col].setTile1(s.getIndex() + tileInformation.getStartLocation());
					}
					else if (subrow == 0 && subcol == 1)
					{
						blocks[row * BLOCK_PER_ROW + col].setTile2(s.getIndex() + tileInformation.getStartLocation());
					}
					else if (subrow == 1 && subcol == 0)
					{
						blocks[row * BLOCK_PER_ROW + col].setTile3(s.getIndex() + tileInformation.getStartLocation());
					}
					else if (subrow == 1 && subcol == 1)
					{
						blocks[row * BLOCK_PER_ROW + col].setTile4(s.getIndex() + tileInformation.getStartLocation());
					}


					dtde.getDropTargetContext().dropComplete(true);
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
	    	final BlockPanelPopupMenu menu = new BlockPanelPopupMenu(block, options, tileInformation, palette);

	    	menu.show(BlockPanel.this, event.getX(), event.getY());
		}

	    @Override
		public void mouseExited(final MouseEvent e)
	    {
	    	for (final IBlockPanelListener listener : listeners)
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
	    	final int mouseRow = e.getY() / BLOCK_SIZE;
	    	final int mouseCol = e.getX() / BLOCK_SIZE;

	    	final Block block = blocks[mouseRow * BLOCK_PER_ROW + mouseCol];

	    	for (final IBlockPanelListener listener : listeners)
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
	    public void mousePressed(final MouseEvent e)
	    {
	    	if (e.isPopupTrigger())
	    	{
		    	final int mouseRow = e.getY() / BLOCK_SIZE;
		    	final int mouseCol = e.getX() / BLOCK_SIZE;

		    	final Block block = blocks[mouseRow * BLOCK_PER_ROW + mouseCol];

	    		showPopupMenu(e, block);
	    	}
	    }

		@Override
    	public void mouseReleased(final MouseEvent e)
    	{
	    	final int mouseRow = e.getY() / BLOCK_SIZE;
	    	final int mouseCol = e.getX() / BLOCK_SIZE;

	    	final Block block = blocks[mouseRow * BLOCK_PER_ROW + mouseCol];

	    	if (e.isPopupTrigger())
	    	{
	    		showPopupMenu(e, block);
	    	}

	    	for (final IBlockPanelListener listener : listeners)
	    	{
	    		try
	    		{
	    			listener.clickedBlock(e, block);
	    		}
	    		catch(final Exception exception)
	    		{
	    			exception.printStackTrace();
	    		}
	    	}

	    	repaint();
	    }
	}

	private class InternalOptionsListener implements IBlockHighlightingOptionsListener
	{
		@Override
		public void changedAirHighlighting(final boolean highlighted)
		{
			repaint();
		}

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
}
