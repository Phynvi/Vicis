package rs.emulate.legacy.config.varp

import rs.emulate.legacy.config.ConfigPropertyMap
import rs.emulate.legacy.config.MutableConfigDefinition
import rs.emulate.legacy.config.SerializableProperty

/**
 * A definition for a parameter variable (a 'varp').
 *
 * @param id The id.
 * @param properties The [ConfigPropertyMap].
 */
open class ParameterVariableDefinition(
    id: Int,
    properties: ConfigPropertyMap
) : MutableConfigDefinition(id, properties) {

    /**
     * Gets the [SerializableProperty] containing the parameter.
     */
    val parameter: SerializableProperty<Int>
        get() = getProperty(ParameterVariableProperty.PARAMETER)

    companion object {

        /**
         * The name of the ArchiveEntry containing the ParameterVariableDefinitions, without the extension.
         */
        const val ENTRY_NAME = "varp"
    }

}