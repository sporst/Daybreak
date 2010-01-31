package tv.porst.daybreak.gui;

import java.awt.Dimension;
import java.awt.Graphics;

import javax.swing.JPanel;

import net.sourceforge.jnhf.gui.Palette;
import tv.porst.daybreak.model.Block;
import tv.porst.daybreak.model.TileInformation;

public class SingleBlockPanel extends JPanel
{
	private static final int BLOCK_WIDTH = 64;

	private final Block block;
	private final TileInformation tileInformation;
	private final Palette palette;

	public SingleBlockPanel(final Block block, final TileInformation tileInformation, final Palette palette)
	{
		this.block = block;
		this.tileInformation = tileInformation;
		this.palette = palette;

		setPreferredSize(new Dimension(BLOCK_WIDTH, BLOCK_WIDTH));
	}

	@Override
	public void paintComponent(final Graphics g)
	{
		super.paintComponent(g);

		final BlockBitmap bitmap = new BlockBitmap(block, tileInformation, palette);

		g.drawImage(bitmap, 0, 0, BLOCK_WIDTH, BLOCK_WIDTH, null);
	}
}
