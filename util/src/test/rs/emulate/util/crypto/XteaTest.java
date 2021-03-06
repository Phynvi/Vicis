package rs.emulate.util.crypto;

import org.junit.Test;
import rs.emulate.shared.util.DataBuffer;
import rs.emulate.shared.util.crypto.Xtea;

import static org.junit.Assert.assertArrayEquals;

/**
 * A unit test to validate the XTEA code.
 *
 * @author Graham
 */
public final class XteaTest {

	/**
	 * The vectors used in the XTEA enciphering/deciphering tests.
	 */
	private static final String[][] TEST_VECTORS = {
			{ "000102030405060708090a0b0c0d0e0f", "4142434445464748", "497df3d072612cb5" },
			{ "000102030405060708090a0b0c0d0e0f", "4141414141414141", "e78f2d13744341d8" },
			{ "000102030405060708090a0b0c0d0e0f", "5a5b6e278948d77f", "4141414141414141" },
			{ "00000000000000000000000000000000", "4142434445464748", "a0390589f8b8efa5" },
			{ "00000000000000000000000000000000", "4141414141414141", "ed23375a821a8c2d" },
			{ "00000000000000000000000000000000", "70e1225d6e4e7655", "4141414141414141" } };

	/**
	 * Executes the decipher test.
	 */
	@Test
	public void testDecipher() {
		for (String[] vector : TEST_VECTORS) {
			int[] key = new int[4];
			for (int index = 0; index < key.length; index++) {
				String hex = vector[0].substring(index * 8, (index + 1) * 8);
				key[index] = Integer.parseInt(hex, 16);
			}

			byte[] plaintext = new byte[8];
			for (int index = 0; index < plaintext.length; index++) {
				String hex = vector[1].substring(index * 2, (index + 1) * 2);
				plaintext[index] = (byte) Integer.parseInt(hex, 16);
			}

			byte[] ciphertext = new byte[8];
			for (int index = 0; index < plaintext.length; index++) {
				String hex = vector[2].substring(index * 2, (index + 1) * 2);
				ciphertext[index] = (byte) Integer.parseInt(hex, 16);
			}

			DataBuffer buffer = DataBuffer.wrap(ciphertext);
			Xtea.decipher(buffer, 0, 8, key);
			assertArrayEquals(plaintext, buffer.array());
		}
	}

	/**
	 * Executes the encipher test.
	 */
	@Test
	public void testEncipher() {
		for (String[] vector : TEST_VECTORS) {
			int[] key = new int[4];
			for (int index = 0; index < key.length; index++) {
				String hex = vector[0].substring(index * 8, (index + 1) * 8);
				key[index] = Integer.parseInt(hex, 16);
			}

			byte[] plaintext = new byte[8];
			for (int index = 0; index < plaintext.length; index++) {
				String hex = vector[1].substring(index * 2, (index + 1) * 2);
				plaintext[index] = (byte) Integer.parseInt(hex, 16);
			}

			byte[] ciphertext = new byte[8];
			for (int index = 0; index < plaintext.length; index++) {
				String hex = vector[2].substring(index * 2, (index + 1) * 2);
				ciphertext[index] = (byte) Integer.parseInt(hex, 16);
			}

			DataBuffer buffer = DataBuffer.wrap(plaintext);
			Xtea.encipher(buffer, 0, 8, key);
			assertArrayEquals(ciphertext, buffer.array());
		}
	}

}