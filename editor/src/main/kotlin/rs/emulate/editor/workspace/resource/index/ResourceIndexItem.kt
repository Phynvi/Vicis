package rs.emulate.editor.workspace.resource.index

import rs.emulate.editor.workspace.resource.ResourceId

sealed class ResourceIndexEntry

data class ResourceIndexItem(val id: ResourceId, val label: String) : ResourceIndexEntry()

data class ResourceIndexCategory(val name: String, val children: List<ResourceIndexEntry>) : ResourceIndexEntry()
