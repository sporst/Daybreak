package tv.porst.daybreak.gui;

import java.awt.Color;
import java.awt.image.BufferedImage;

import net.sourceforge.jnhf.gui.Palette;
import net.sourceforge.jnhf.gui.Palettes;
import net.sourceforge.jnhf.romfile.TileData;

public class TileBitmap extends BufferedImage
{
	public TileBitmap(final TileData data, final int attributeIndex, final Palette palette, final boolean transparent)
	{
		super(8, 8, TYPE_4BYTE_ABGR);

		final byte[] d = data == null ? new byte[64] : data.getData();
		final int[] bitmap = new int[4 * 8 * 8];

		final byte[] paletteData = palette.getData();

		for (int i=0;i<8;i++)
		{
			for (int j=0;j<8;j++)
			{
				if (transparent && d[i * 8 + j] == 0)
				{
					bitmap[4 * (i * 8 + j) + 3] = 0;

					continue;
				}

				final Color color = Palettes.FCEU_PAL_PALETTE[paletteData[4 * attributeIndex + d[i * 8 + j]]];

				bitmap[4 * (i * 8 + j) + 0] = color.getRed();
				bitmap[4 * (i * 8 + j) + 1] = color.getGreen();
				bitmap[4 * (i * 8 + j) + 2] = color.getBlue();
				bitmap[4 * (i * 8 + j) + 3] = 0xFF;
			}
		}

		getRaster().setPixels(0, 0, 8, 8, bitmap);
	}
}
