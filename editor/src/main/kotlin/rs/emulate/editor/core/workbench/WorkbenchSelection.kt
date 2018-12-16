package rs.emulate.editor.core.workbench

import rs.emulate.editor.core.project.Project
import rs.emulate.editor.vfs.ResourceType
import rs.emulate.editor.vfs.VirtualFileId

sealed class WorkbenchSelection

data class VirtualFileSelection(
    val project: Project,
    val vfsId: VirtualFileId,
    val type: ResourceType
) : WorkbenchSelection()