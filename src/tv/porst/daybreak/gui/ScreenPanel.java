package tv.porst.daybreak.gui;

import java.awt.Dimension;
import java.awt.Graphics;

import javax.swing.JPanel;

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

	public ScreenPanel(final Level level, final Screen screen)
	{
		this.screen = screen;
		this.metaData = level.getMetaData();

		setPreferredSize(new Dimension(TILES_X * TILE_WIDTH, TILES_Y * TILE_WIDTH));
	}

	@Override
	public void paintComponent(final Graphics g)
	{
		super.paintComponent(g);

		final ScreenBitmap bitmap = new ScreenBitmap(screen, metaData);

		g.drawImage(bitmap, 0, 0, 2 * bitmap.getWidth(), 2 * bitmap.getHeight(), null);
	}

	public void setScreen(final Level level, final Screen screen)
	{
		this.screen = screen;
		this.metaData = level.getMetaData();

		repaint();
	}
}
