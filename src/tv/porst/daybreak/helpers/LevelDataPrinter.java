package tv.porst.daybreak.helpers;

public class LevelDataPrinter
{
	public static void print(final int[][] data)
	{
		for (final int[] element : data)
		{
			for (final int element2 : element)
			{
				System.out.printf("%02X ", element2);
			}

			System.out.println();
		}
	}
}
