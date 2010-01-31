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

		for (int i=0;i<8;i++)
		{
			for (int j=0;j<8;j++)
			{
				final int color = Palettes.DEFAULT_PALETTE[palette.getData()[4 * attributeIndex + d[i * 8 + j]]];

				setRGB(j, i, color);
//
//				switch(d[i * 8 + j])
//				{
//				case 0: setRGB(i, j, 0xFF0000); break;
//				case 1: setRGB(i, j, 0x00FF00); break;
//				case 2: setRGB(i, j, 0x0000FF); break;
//				case 3: setRGB(i, j, 0xFFFF00); break;
//				}
			}
		}
	}
}
