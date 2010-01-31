package tv.porst.daybreak.gui;

import java.awt.image.BufferedImage;

import net.sourceforge.jnhf.gui.Palette;
import net.sourceforge.jnhf.gui.Palettes;
import net.sourceforge.jnhf.romfile.TileData;

public class TileBitmap extends BufferedImage
{
	public TileBitmap(final TileData data, final int attributeIndex, final Palette palette)
	{
		super(8, 8, TYPE_3BYTE_BGR);

		final byte[] d = data.getData();
		final int[] bitmap = new int[3 * 8 * 8];

		final byte[] paletteData = palette.getData();

		for (int i=0;i<8;i++)
		{
			for (int j=0;j<8;j++)
			{
				final int color = Palettes.DEFAULT_PALETTE[paletteData[4 * attributeIndex + d[i * 8 + j]]];

				bitmap[3 * (i * 8 + j)] = (color >>> 16) & 0xFF;
				bitmap[3 * (i * 8 + j) + 1] = (color >>> 8)  & 0xFF;
				bitmap[3 * (i * 8 + j) + 2] = (color >>> 0)  & 0xFF;
			}
		}

		getRaster().setPixels(0, 0, 8, 8, bitmap);
	}
}
