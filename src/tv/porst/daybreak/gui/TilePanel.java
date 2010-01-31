package tv.porst.daybreak.gui;

import java.awt.Dimension;
import java.awt.Graphics;

import javax.swing.JPanel;

import net.sourceforge.jnhf.gui.Palette;
import net.sourceforge.jnhf.romfile.TileData;
import tv.porst.daybreak.model.TileInformation;

public class TilePanel extends JPanel
{
	private TileInformation information;

	private Palette palette;

	private static final int TILE_WIDTH = 16;
	private static final int TILE_PER_LINE = 32;

	public TilePanel(final TileInformation information, final Palette palette)
	{
		this.information = information;
		this.palette = palette;

		setPreferredSize(new Dimension(TILE_PER_LINE * TILE_WIDTH, information.getTileData().size() / TILE_PER_LINE * TILE_WIDTH));
	}

	@Override
	public void paintComponent(final Graphics g)
	{
		super.paintComponent(g);

		int counter = 0;

		for (final TileData data : information.getTileData())
		{
			final TileBitmap bitmap = new TileBitmap(data, 0, palette);

			final int line = counter / TILE_PER_LINE;
			final int column = counter % TILE_PER_LINE;

			g.drawImage(bitmap, column * TILE_WIDTH, line * TILE_WIDTH, TILE_WIDTH, TILE_WIDTH, null);

			counter++;
		}
	}

	public void setTiles(final TileInformation tiles, final Palette palette)
	{
		this.information = tiles;
		this.palette = palette;

		repaint();
	}
}
