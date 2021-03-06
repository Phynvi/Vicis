package rs.emulate.legacy.version;

import rs.emulate.legacy.archive.Archive;
import rs.emulate.shared.util.DataBuffer;

import java.io.FileNotFoundException;

/**
 * Decoder for the version lists "map_index" entry.
 *
 * @author sfix
 * @author Major
 */
public final class MapIndexDecoder {

	/**
	 * The DataBuffer containing the data.
	 */
	private final DataBuffer data;

	/**
	 * Creates the MapIndexDecoder.
	 *
	 * @param archive The version list {@link Archive}.
	 * @throws FileNotFoundException If the {@code map_index} entry could not be found.
	 */
	public MapIndexDecoder(Archive archive) throws FileNotFoundException {
		data = archive.getEntry(MapIndex.ENTRY_NAME).getBuffer().asReadOnlyBuffer();
	}

	/**
	 * Decodes the contents of the {@code map_index} entry into a {@link MapIndex}.
	 *
	 * @return The MapIndex.
	 */
	public MapIndex decode() {
		int count = data.remaining() / (3 * Short.BYTES + Byte.BYTES);

		int[] areas = new int[count];
		int[] maps = new int[count];
		int[] objects = new int[count];
		boolean[] members = new boolean[count];

		for (int index = 0; index < count; index++) {
			areas[index] = data.getUnsignedShort();
			maps[index] = data.getUnsignedShort();
			objects[index] = data.getUnsignedShort();
			members[index] = data.getBoolean();
		}

		return new MapIndex(areas, objects, maps, members);
	}

}