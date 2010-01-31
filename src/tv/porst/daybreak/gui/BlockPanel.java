package tv.porst.daybreak.gui;

import java.awt.Dimension;
import java.awt.Graphics;

import javax.swing.JPanel;

import net.sourceforge.jnhf.gui.Palette;
import tv.porst.daybreak.model.Block;
import tv.porst.daybreak.model.TileInformation;

public class BlockPanel extends JPanel
{
	private Block[] blocks;
	private TileInformation tileInformation;
	private Palette palette;

	private static final int BLOCK_SIZE = 32;
	private static final int BLOCK_PER_ROW = 16;

	public BlockPanel(final Block[] blocks, final TileInformation tileInformation, final Palette palette)
	{
		this.blocks = blocks.clone();
		this.tileInformation = tileInformation;
		this.palette = palette;

		setPreferredSize(new Dimension(BLOCK_PER_ROW * BLOCK_SIZE, blocks.length / BLOCK_PER_ROW * BLOCK_SIZE));
	}

	@Override
	public void paintComponent(final Graphics g)
	{
		for (int i=0;i<blocks.length;i++)
		{
			final Block block = blocks[i];

			final BlockBitmap bitmap = new BlockBitmap(block, tileInformation, palette);

			final int row = i / BLOCK_PER_ROW;
			final int col = i % BLOCK_PER_ROW;

			g.drawImage(bitmap, BLOCK_SIZE * col, BLOCK_SIZE * row, BLOCK_SIZE, BLOCK_SIZE, null);
		}
	}

	public void setBlocks(final Block[] blocks, final TileInformation tileInformation, final Palette palette)
	{
		this.blocks = blocks.clone();
		this.tileInformation = tileInformation;
		this.palette = palette;

		repaint();
	}
}
