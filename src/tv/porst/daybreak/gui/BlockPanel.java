package tv.porst.daybreak.gui;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JPanel;

import net.sourceforge.jnhf.gui.Palette;
import net.sourceforge.jnhf.helpers.ListenerProvider;
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

	public BlockPanel(final Block[] blocks, final TileInformation tileInformation, final Palette palette)
	{
		this.blocks = blocks.clone();
		this.tileInformation = tileInformation;
		this.palette = palette;

		addMouseListener(internalMouseListener);
		addMouseMotionListener(internalMouseListener);

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

	private class InternalMouseListener extends MouseAdapter
	{
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
	    public void mouseReleased(final MouseEvent e)
	    {
	    	final int mouseRow = e.getY() / BLOCK_SIZE;
	    	final int mouseCol = e.getX() / BLOCK_SIZE;

	    	final Block block = blocks[mouseRow * BLOCK_PER_ROW + mouseCol];

	    	for (final IBlockPanelListener listener : listeners)
	    	{
	    		try
	    		{
	    			listener.clickedBlock(block);
	    		}
	    		catch(final Exception exception)
	    		{
	    			exception.printStackTrace();
	    		}
	    	}

	    	repaint();
	    }
	}
}
