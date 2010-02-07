package tv.porst.daybreak.model;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import net.sourceforge.jnhf.gui.Palette;
import net.sourceforge.jnhf.helpers.ArrayHelpers;
import net.sourceforge.jnhf.helpers.FileHelpers;
import net.sourceforge.jnhf.romfile.INesHeader;
import net.sourceforge.jnhf.romfile.InvalidRomException;
import tv.porst.daybreak.rom.GameDataReader;
import tv.porst.daybreak.rom.LevelDataReader;
import tv.porst.daybreak.rom.PaletteReader;
import tv.porst.daybreak.rom.SpriteReader;
import tv.porst.daybreak.rom.TilesReader;

public final class FaxanaduRom
{
	public static FaxanaduRom read(final String filename) throws IOException, InvalidRomException
	{
		final byte[] data = FileHelpers.readFile(new File(filename));

		final INesHeader header = new INesHeader(ArrayHelpers.copy(data, 0, INesHeader.NES_HEADER_SIZE));

		final GameData gameData = GameDataReader.read(data);

		final List<TileInformation> tileInformation = TilesReader.read(data);

		final List<Palette> palettes = PaletteReader.read(data);

		final List<Sprite> sprites = SpriteReader.read(data, palettes.get(0x1C));

		final List<Level> levels = LevelDataReader.readLevelData(data, tileInformation, palettes, gameData, sprites);

		return new FaxanaduRom(levels, tileInformation, palettes, sprites);
	}

	private final ArrayList<Level> levels;

	private final ArrayList<TileInformation> tileInformation;

	private final List<Palette> palettes;

	private final List<Sprite> sprites;

	private FaxanaduRom(final List<Level> levels, final List<TileInformation> tileInformation, final List<Palette> palettes, final List<Sprite> sprites)
	{
		this.levels = new ArrayList<Level>(levels);
		this.tileInformation = new ArrayList<TileInformation>(tileInformation);
		this.palettes = new ArrayList<Palette>(palettes);
		this.sprites = sprites;
	}

	public List<Level> getLevels()
	{
		return new ArrayList<Level>(levels);
	}

	public List<Palette> getPalettes()
	{
		return new ArrayList<Palette>(palettes);
	}

	public List<Sprite> getSprites()
	{
		return sprites;
	}

	public List<TileInformation> getTileInformation()
	{
		return tileInformation;
	}
}
