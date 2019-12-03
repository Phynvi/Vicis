package rs.emulate.editor.legacy.npc

import rs.emulate.editor.core.workbench.properties.ResourceProperty

sealed class NpcProperty(
    override val name: String,
    override val category: String,
    override val description: String
) : ResourceProperty {

    object Models : NpcProperty(
        "Models",
        category = "model",
        description = "The model ids composed to create the npc."
    )

    object Name : NpcProperty(
        "Name",
        category = "text",
        description = "The name displayed on hover, right click, etc."
    )

    object Description : NpcProperty(
        "Description",
        category = "text",
        description = "The text shown when examining the npc."
    )

    object Size : NpcProperty(
        "Size",
        category = "model",
        description = "The size, in tiles, of the npc."
    )

    object StandingSequence : NpcProperty(
        "Standing animation",
        category = "animations",
        description = "The animation performed when the npc is standing idle."
    )

    object WalkingSequence : NpcProperty(
        "Walking animation",
        category = "animations",
        description = "The animation performed when the npc is walking."
    )

    object MovementSequences : NpcProperty(
        "Movement animations",
        category = "animations",
        description = "Set of animations performed when the npc performs different movements."
    )

    object Actions : NpcProperty(
        "Actions",
        category = "interaction",
        description = "The context menu actions."
    )

    object Colours : NpcProperty(
        "Colours",
        category = "model",
        description = "The model colours to replace for this npc."
    )

    object WidgetModels : NpcProperty(
        "Widget models",
        category = "model",
        description = "The model ids composed to display the npc on a widget."
    )

    object VisibleOnMinimap : NpcProperty(
        "Visible on minimap",
        category = "display",
        description = "Whether or not to display a yellow dot on the client minimap for this npc."
    )

    object CombatLevel : NpcProperty(
        "Combat level",
        category = "text",
        description = "The combat level displayed on right click (0 displays no level)."
    )

    object PlanarScale : NpcProperty(
        "Planar model scale",
        category = "display",
        description = "The scale factor to apply with respect to the plane (width/depth)."
    )

    object VerticalScale : NpcProperty(
        "Vertical model scale",
        category = "display",
        description = "The scale factor to apply vertically."
    )

    object PriorityRender : NpcProperty(
        "High-priority render",
        category = "display",
        description = "Whether or not this npc should be rendered before other npcs."
    )

    object Brightness : NpcProperty(
        "Brightness",
        category = "display",
        description = "The brightness offset to apply when lighting this npc's model."
    )

    object Diffusion : NpcProperty(
        "Diffusion",
        category = "display",
        description = "The diffusion offset to apply when lighting this npc's model."
    )

    object HeadIcon : NpcProperty(
        "Prayer head icon",
        category = "display",
        description = "The id of the prayer headicon to draw above this npc."
    )

    object DefaultOrientation : NpcProperty(
        "Default orientation",
        category = "display",
        description = "The default orientation of the npc model."
    )

    object Morphisms : NpcProperty(
        "Morphisms",
        category = "morphisms",
        description = "The npc ids this npc can morph into."
    )

    object Clickable : NpcProperty(
        "Clickable",
        category = "interaction",
        description = "Whether or not this npc can be clicked by a player."
    )

}