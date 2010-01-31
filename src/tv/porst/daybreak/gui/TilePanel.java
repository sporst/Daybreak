package tv.porst.daybreak.gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;

import javax.swing.JPanel;

import net.sourceforge.jnhf.gui.Palette;
import net.sourceforge.jnhf.romfile.TileData;
import tv.porst.daybreak.model.TileInformation;

public class TilePanel extends JPanel
{
	private TileInformation information;

	private Palette palette;

	private int tileIndex1 = -1;
	private int tileIndex2 = -1;
	private int tileIndex3 = -1;
	private int tileIndex4 = -1;

	private static final int TILE_WIDTH = 16;
	private static final int TILE_PER_LINE = 32;

	public TilePanel(final TileInformation information, final Palette palette)
	{
		this.information = information;
		this.palette = palette;

		setPreferredSize(new Dimension(TILE_PER_LINE * TILE_WIDTH, information.getTileData().size() / TILE_PER_LINE * TILE_WIDTH));
	}

	private boolean drawWithoutHighlighting()
	{
		return tileIndex1 == -1 ||tileIndex2 == -1 || tileIndex3 == -1 || tileIndex4 == -1 ;
	}

	private boolean isHighlighted(final int counter)
	{
		return counter == tileIndex1 ||counter == tileIndex2 ||counter == tileIndex3 ||counter == tileIndex4;
	}

	@Override
	public void paintComponent(final Graphics g)
	{
		super.paintComponent(g);

		final Graphics2D g2d = (Graphics2D) g;

		g.setColor(Color.WHITE);
		g.fillRect(0, 0, getWidth(), getHeight());

		int counter = 0;

		for (final TileData data : information.getTileData())
		{
			final TileBitmap bitmap = new TileBitmap(data, 0, palette);

			final int line = counter / TILE_PER_LINE;
			final int column = counter % TILE_PER_LINE;

			if (isHighlighted(counter) || drawWithoutHighlighting())
			{
				g2d.setComposite(Transparency.makeComposite(1.0f));
			}
			else
			{
				g2d.setComposite(Transparency.makeComposite(0.3f));
			}

			g.drawImage(bitmap, column * TILE_WIDTH, line * TILE_WIDTH, TILE_WIDTH, TILE_WIDTH, null);

			if (isHighlighted(counter))
			{
				g.setColor(Color.BLUE);
				g.drawRect(column * TILE_WIDTH, line * TILE_WIDTH, TILE_WIDTH - 1, TILE_WIDTH - 1);
			}

			counter++;
		}
	}

	public void setHighlightedTiles(final int tileIndex1, final int tileIndex2, final int tileIndex3, final int tileIndex4)
	{
		this.tileIndex1 = tileIndex1;
		this.tileIndex2 = tileIndex2;
		this.tileIndex3 = tileIndex3;
		this.tileIndex4 = tileIndex4;

		repaint();
	}

	public void setTiles(final TileInformation tiles, final Palette palette)
	{
		this.information = tiles;
		this.palette = palette;

		repaint();
	}
}
