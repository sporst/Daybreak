package tv.porst.daybreak.rom;

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.jnhf.gui.Palette;
import net.sourceforge.jnhf.helpers.ArrayHelpers;

public class PaletteReader
{
	public static List<Palette> read(final byte[] data)
	{
		final int offset = 0x2C010;

		final List<Palette> palettes = new ArrayList<Palette>();

		for (int i=0;i<31;i++)
		{
			palettes.add(new Palette(ArrayHelpers.copy(data, offset + i * Palette.SIZE_IN_BYTES, Palette.SIZE_IN_BYTES)));
		}

		return palettes;
	}

}
