package rs.emulate.legacy.config;

import rs.emulate.legacy.config.varbit.BitVariableDefinition;

/**
 * A {@link BitVariableDefinition} with a name.
 *
 * @author Major
 */
public final class NamedVarbitDefinition extends BitVariableDefinition {

	/**
	 * The ConfigPropertyType for the name of a BitVariable.
	 */
	private static final ConfigPropertyType NAME_PROPERTY_TYPE = DynamicConfigPropertyType.valueOf("name", 2);

	/**
	 * Creates the NamedVarbitDefinition.
	 *
	 * @param id The id.
	 * @param properties The {@link ConfigPropertyMap}.
	 */
	public NamedVarbitDefinition(int id, ConfigPropertyMap properties) {
		super(id, properties);
	}

	/**
	 * Gets the {@link SerializableProperty} containing the name of this BitVariableDefinition.
	 *
	 * @return The name ConfigProperty.
	 */
	public SerializableProperty<String> getName() {
		return properties.get(NAME_PROPERTY_TYPE);
	}

}