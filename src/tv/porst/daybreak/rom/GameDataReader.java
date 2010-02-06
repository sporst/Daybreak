package tv.porst.daybreak.rom;

import net.sourceforge.jnhf.helpers.ArrayHelpers;
import tv.porst.daybreak.model.GameData;

public class GameDataReader
{
	public static GameData read(final byte[] data)
	{
		final byte[] nlvl_table = ArrayHelpers.copy(data, 0x3E5F7, 15);

		return new GameData(nlvl_table);
	}
}
