package tv.porst.daybreak.gui;

import java.awt.Color;
import java.awt.Graphics;

import tv.porst.daybreak.model.Block;

public class BlockHighlighter
{
	public static void highlight(final IBlockHighlighterOptions options, final Block block, final BlockBitmap bitmap)
	{
		Color color = null;

		final Graphics g = bitmap.getGraphics();

		if (block.getProperty().isSolid() && options.getHighlightSolidBlocks())
		{
			color = Color.GREEN;
		}
		else if (block.getProperty().isDoor() && options.getHighlightDoors())
		{
			color = Color.RED;
		}
		else if (block.getProperty().isAir() && options.getHighlightAir())
		{
			color = Color.BLUE;
		}

		if (color != null)
		{
			g.setColor(color);

			for (int i=0;i<2 * bitmap.getWidth();i+=2)
			{
				g.drawLine(0, i, i, 0);
			}
		}
	}
}
