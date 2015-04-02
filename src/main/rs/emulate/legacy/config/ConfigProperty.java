package rs.emulate.legacy.config;

import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.function.BiConsumer;
import java.util.function.Function;

import rs.emulate.shared.prop.Property;
import rs.emulate.shared.util.DataBuffer;
import rs.emulate.util.Assertions;

/**
 * A property belonging to a definition in the config archive..
 * 
 * @author Major
 * 
 * @param <T> The property value type.
 */
public final class ConfigProperty<T> extends Property<T, ConfigPropertyType> {

	/**
	 * A Function that decodes a value from a Buffer.
	 */
	private final Function<DataBuffer, T> decoder;

	/**
	 * A BiConsumer that encodes the value into a Buffer.
	 */
	private final BiConsumer<DataBuffer, T> encoder;

	/**
	 * A Function that returns the size of this DefinitionProperty, in bytes.
	 */
	private final Function<T, Integer> size;

	/**
	 * Creates the DefinitionProperty.
	 * 
	 * @param type The type of the property.
	 * @param defaultValue The default value. May be {@code null}.
	 * @param encoder A {@link BiConsumer} that encodes the value into a {@link DataBuffer}.
	 * @param decoder A {@link Function} that decodes the value from a Buffer.
	 * @param size A Function that returns the size of the value, in bytes.
	 */
	public ConfigProperty(ConfigPropertyType type, T defaultValue, BiConsumer<DataBuffer, T> encoder,
			Function<DataBuffer, T> decoder, Function<T, Integer> size) {
		this(type, null, defaultValue, encoder, decoder, size);
	}

	/**
	 * Creates the DefinitionProperty.
	 * 
	 * @param type The type of the property.
	 * @param defaultValue The default value. May be {@code null}.
	 * @param encoder A {@link BiConsumer} that encodes the value into a {@link DataBuffer}.
	 * @param decoder A {@link Function} that decodes the value from a Buffer.
	 * @param size The size of the value, in bytes.
	 */
	public ConfigProperty(ConfigPropertyType type, T defaultValue, BiConsumer<DataBuffer, T> encoder,
			Function<DataBuffer, T> decoder, int size) {
		this(type, defaultValue, encoder, decoder, value -> size);
	}

	/**
	 * Creates the DefinitionProperty.
	 * 
	 * @param type The name of the property.
	 * @param value The value. May be {@code null}.
	 * @param defaultValue The default value. May be {@code null}.
	 * @param encoder A {@link BiConsumer} that encodes the value into a {@link DataBuffer}.
	 * @param decoder A {@link Function} that decodes the value from a Buffer.
	 * @param size A Function that returns the size of the value, in bytes.
	 */
	public ConfigProperty(ConfigPropertyType type, T value, T defaultValue, BiConsumer<DataBuffer, T> encoder,
			Function<DataBuffer, T> decoder, Function<T, Integer> size) {
		super(type, value, defaultValue);
		Assertions.checkNonNull("Decoder, encoder, and size lambdas must not be null.", decoder, encoder, size);

		this.encoder = encoder;
		this.decoder = decoder;
		this.size = size;
	}

	/**
	 * Decodes a value from the specified {@link DataBuffer}, and sets the value of this DefinitionProperty to it.
	 *
	 * @param buffer The buffer.
	 */
	public void decode(DataBuffer buffer) {
		T value = decoder.apply(buffer);
		this.value = Optional.of(value);
	}

	/**
	 * Creates a duplicate of this DefinitionProperty.
	 * 
	 * @return The definition property.
	 */
	public ConfigProperty<T> duplicate() {
		return new ConfigProperty<>(type, value.orElse(null), defaultValue, encoder, decoder, size);
	}

	/**
	 * Encodes this DefinitionProperty into a {@link DataBuffer}.
	 *
	 * @return The buffer.
	 * @throws NoSuchElementException If this method is called on a property without a value set.
	 */
	public DataBuffer encode() {
		T value = this.value.get();
		DataBuffer buffer = DataBuffer.allocate(size.apply(value));

		encoder.accept(buffer, value);
		return buffer.flip();
	}

}