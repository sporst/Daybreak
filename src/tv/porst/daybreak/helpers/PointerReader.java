package tv.porst.daybreak.helpers;

import net.sourceforge.jnhf.helpers.ByteHelpers;

public class PointerReader
{
	public static int readPointer(final byte[] data, final int start)
	{
		return ByteHelpers.readWordLittleEndian(data, start);
	}

	public static int[] readPointers(final byte[] data, final int start, final int count)
	{
		final int[] pointers = new int[count];

		for (int i=0;i<pointers.length;i++)
		{
			pointers[i] = ByteHelpers.readWordLittleEndian(data, start + i * 2);
		}

		return pointers;
	}

	public static int[] readTable(final byte[] data, final int start, final int correction)
	{
		final int end = ByteHelpers.readWordLittleEndian(data, start + correction);

		return readUntil(data, start + correction, end + correction);
	}

	public static int[] readUntil(final byte[] data, final int start, final int end)
	{
		final int[] pointers = new int[(end - start) / 2];

		for (int i=0;i<pointers.length;i++)
		{
			pointers[i] = ByteHelpers.readWordLittleEndian(data, start + i * 2);
		}

		return pointers;
	}
}
