package tv.porst.daybreak.rom;

import java.util.List;

import net.sourceforge.jnhf.gui.Palette;

public class PaletteAssigner
{
	public static Palette find(final List<Palette> palettes, final int levelId, final int screenId)
	{
		if ((levelId == 0 && screenId == 0) || true)
		{
			return palettes.get(6);
		}
		else
		{
			return null;
		}
	}
}
