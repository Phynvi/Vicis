package rs.emulate.legacy.config.npc

import rs.emulate.legacy.config.ConfigDecoder
import rs.emulate.legacy.config.ConfigDecoder.Companion.DEFINITION_TERMINATOR
import rs.emulate.shared.util.DataBuffer

object NpcDefinitionDecoder : ConfigDecoder<NpcDefinition> {

    override val entryName: String = "npc"

    override fun decode(id: Int, buffer: DataBuffer): NpcDefinition {
        val definition = NpcDefinition(id)
        var opcode = buffer.getUnsignedByte()

        while (opcode != DEFINITION_TERMINATOR) {
            definition.decode(buffer, opcode)
            opcode = buffer.getUnsignedByte()
        }

        return definition
    }

    private fun NpcDefinition.decode(buffer: DataBuffer, opcode: Int) {
        when (opcode) {
            1 -> {
                val count = buffer.getUnsignedByte()
                models = IntArray(count) { buffer.getUnsignedShort() }
            }
            2 -> name = buffer.getString()
            3 -> description = buffer.getString()
            12 -> size = buffer.getByte()
            13 -> standingSequence = buffer.getUnsignedShort()
            14 -> walkingSequence = buffer.getUnsignedShort()
            17 -> movementSequences = MovementAnimationSet.decode(buffer)
            in 30..34 -> actions[opcode - 30] = buffer.getString()
            40 -> {
                val count = buffer.getUnsignedByte()

                repeat(count) {
                    val original = buffer.getUnsignedShort()
                    val replacement = buffer.getUnsignedShort()

                    replacementColours[original] = replacement
                }
            }
            60 -> {
                val count = buffer.getUnsignedByte()
                widgetModels = IntArray(count) { buffer.getUnsignedShort() }
            }
            93 -> visibleOnMinimap = false
            95 -> combatLevel = buffer.getUnsignedShort()
            97 -> planarScale = buffer.getUnsignedShort()
            98 -> verticalScale = buffer.getUnsignedShort()
            99 -> priorityRender = true
            100 -> brightness = buffer.getByte()
            101 -> diffusion = buffer.getByte()
            102 -> headIconId = buffer.getUnsignedShort()
            103 -> defaultOrientation = buffer.getUnsignedShort()
            106 -> morphisms = MorphismSet.decode(buffer)
            107 -> clickable = false
        }
    }

}
