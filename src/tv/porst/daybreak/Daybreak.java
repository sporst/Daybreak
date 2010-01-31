package tv.porst.daybreak;

import java.io.IOException;

import net.sourceforge.jnhf.romfile.InvalidRomException;
import tv.porst.daybreak.gui.MainWindow;
import tv.porst.daybreak.model.FaxanaduRom;
import tv.porst.daybreak.model.Level;
import tv.porst.daybreak.model.Screen;

public class Daybreak
{
	public static void main(final String[] args)
	{
		try
		{
			final FaxanaduRom rom = FaxanaduRom.read("F:\\fce\\Faxanadu (U).nes");

			final MainWindow window = new MainWindow(rom);

			window.setVisible(true);

			for (final Level level : rom.getLevels())
			{
				for (final Screen screen : level.getScreens())
				{
//					LevelDataPrinter.print(screen.getSquareNumbers());
				}
			}
		}
		catch (final IOException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		catch (final InvalidRomException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
