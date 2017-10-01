package rs.emulate.legacy.model

import com.google.common.base.Preconditions
import rs.emulate.legacy.model.ModelFeature.*
import rs.emulate.shared.util.DataBuffer
import java.util.EnumSet
import java.util.HashSet

/**
 * A decoder for [Model]s.
 *
 * @param buffer The uncompressed [DataBuffer] containing the encoded `Model`.
 */
class ModelDecoder(private val buffer: DataBuffer) {

    /**
     * An indexed array of [Vertex]es in the [Model] being decoded.
     */
    private lateinit var vertices: Array<Vertex?>

    /**
     * An indexed array of texture coordinates projected into viewspace.
     */
    private lateinit var texCoords: Array<TexCoord?>

    /**
     * The faces of the geometry in the [Model] being decoded.
     */
    private lateinit var faces: Array<Face?>

    /**
     * A set of model rendering features enabled for the [Model] being decoded.
     */
    private var features: Set<ModelFeature> = HashSet()

    /**
     * Decode the [ModelFeature]s present given the attributes in the `header` block.
     */
    private fun decodeFeatures(header: DataBuffer): Set<ModelFeature> {
        val features = EnumSet.noneOf(ModelFeature::class.java)

        FACE_TEXTURES.isEnabled(features, header::getBoolean)
        FACE_RENDER_PRIORITY.isEnabled(features, { header.getUnsignedByte() == 255 })
        FACE_TRANSPARENCY.isEnabled(features, header::getBoolean)
        FACE_SKINNING.isEnabled(features, header::getBoolean)
        VERTEX_SKINNING.isEnabled(features, header::getBoolean)

        return features
    }

    fun decode(): Model {
        val header = buffer.asReadOnlyBuffer()
        header.position(buffer.capacity() - HEADER_SIZE)

        vertices = arrayOfNulls(header.getUnsignedShort())
        faces = arrayOfNulls(header.getUnsignedShort())
        texCoords = arrayOfNulls(header.getUnsignedByte())
        features = decodeFeatures(header)

        val xDataLength = header.getUnsignedShort()
        val yDataLength = header.getUnsignedShort()
        val zDataLength = header.getUnsignedShort()
        val faceDataLength = header.getUnsignedShort()

        var offset = 0
        offset += vertices.size

        val faceTypesOffset = offset
        offset += faces.size

        val faceRenderPrioritiesOffset = offset
        if (features.contains(FACE_RENDER_PRIORITY)) {
            offset += faces.size
        }

        val faceBonesOffset = offset
        if (features.contains(FACE_SKINNING)) {
            offset += faces.size
        }

        val faceTexturePointersOffset = offset
        if (features.contains(FACE_TEXTURES)) {
            offset += faces.size
        }

        val vertexBonesOffset = offset
        if (features.contains(VERTEX_SKINNING)) {
            offset += vertices.size
        }

        val faceAlphasOffset = offset
        if (features.contains(FACE_TRANSPARENCY)) {
            offset += faces.size
        }

        val faceDataOffset = offset
        offset += faceDataLength

        val faceColoursOffset = offset
        offset += faces.size * 2

        val texCoordsOffset = offset
        offset += texCoords.size * 6

        val xDataOffset = offset
        offset += xDataLength

        val yDataOffset = offset
        offset += yDataLength

        val zDataOffset = offset
        offset += zDataLength

        decodeVertices(xDataOffset, yDataOffset, zDataOffset, vertexBonesOffset)
        decodeFaces(faceDataOffset, faceTypesOffset, faceColoursOffset, faceRenderPrioritiesOffset,
            faceAlphasOffset, faceBonesOffset, faceTexturePointersOffset)
        decodeTexCoords(texCoordsOffset)

        return Model(features, faces.requireNoNulls(), vertices.requireNoNulls(), texCoords.requireNoNulls())
    }

    /**
     * Decode the projected texture coordinates for the [Model] being decoded.
     *
     * @param offset The offset into `buffer` of the texture coordinate data.
     */
    private fun decodeTexCoords(offset: Int) {
        val texCoordsBuffer = buffer.asReadOnlyBuffer()
        texCoordsBuffer.position(offset)

        for (index in texCoords.indices) {
            val origin = texCoordsBuffer.getUnsignedByte()
            val u = texCoordsBuffer.getUnsignedByte()
            val v = texCoordsBuffer.getUnsignedByte()

            texCoords[index] = TexCoord(origin, u, v)
        }
    }

    /**
     * Decode the triangle [Face]s that make up the geometry of the [Model] being decoded.
     */
    private fun decodeFaces(
        faceDataOffset: Int,
        typesOffset: Int,
        coloursOffset: Int,
        renderPrioritiesOffset: Int,
        alphasOffset: Int,
        bonesOffset: Int,
        texturePointersOffset: Int
    ) {
        Preconditions.checkState(faces.isNotEmpty(), "Must be 1 or more faces present")

        val faceData = buffer.asReadOnlyBuffer()
        faceData.position(faceDataOffset)

        val types = buffer.asReadOnlyBuffer()
        types.position(typesOffset)

        val colours = buffer.asReadOnlyBuffer()
        colours.position(coloursOffset)

        val renderPriorities = buffer.asReadOnlyBuffer()
        renderPriorities.position(renderPrioritiesOffset)

        val alphas = buffer.asReadOnlyBuffer()
        alphas.position(alphasOffset)

        val bones = buffer.asReadOnlyBuffer()
        bones.position(bonesOffset)

        val texturePointers = buffer.asReadOnlyBuffer()
        texturePointers.position(texturePointersOffset)

        var faceA = 0
        var faceB = 0
        var faceC = 0
        var offset = 0

        for (index in faces.indices) {
            val type = types.getUnsignedByte()
            val colour = colours.getUnsignedShort()
            val renderPriority = if (features.contains(FACE_RENDER_PRIORITY)) renderPriorities.getUnsignedByte() else -1
            val alpha = if (features.contains(FACE_TRANSPARENCY)) alphas.getUnsignedByte() else -1
            val bone = if (features.contains(FACE_SKINNING)) bones.getUnsignedByte() else -1
            val texturePointer = if (features.contains(FACE_TEXTURES)) bones.getUnsignedByte() else -1

            if (type == 1) {
                faceA = faceData.getSignedSmart() + offset
                offset = faceA

                faceB = faceData.getSignedSmart() + offset
                offset = faceB

                faceC = faceData.getSignedSmart() + offset
                offset = faceC
            } else if (type == 2) {
                faceB = faceC
                faceC = faceData.getSignedSmart() + offset
                offset = faceC
            } else if (type == 3) {
                faceA = faceC
                faceC = faceData.getSignedSmart() + offset
                offset = faceC
            } else if (type == 4) {
                val temp = faceA
                faceA = faceB
                faceB = temp

                faceC = faceData.getSignedSmart() + offset
                offset = faceC
            }

            faces[index] = Face(faceA, faceB, faceC, colour, renderPriority, alpha, bone, texturePointer)
        }
    }

    /**
     * Decode a list of [Vertex] objects from the input [Model] file.
     */
    private fun decodeVertices(xDataOffset: Int, yDataOffset: Int, zDataOffset: Int, vertexBonesOffset: Int) {
        Preconditions.checkState(vertices.isNotEmpty(), "Vertex count must be greater than 0")

        val directionBuffer = buffer.asReadOnlyBuffer()
        directionBuffer.position(0)
        val verticesX = buffer.asReadOnlyBuffer()
        verticesX.position(xDataOffset)

        val verticesY = buffer.asReadOnlyBuffer()
        verticesY.position(yDataOffset)

        val verticesZ = buffer.asReadOnlyBuffer()
        verticesZ.position(zDataOffset)

        val bones = buffer.asReadOnlyBuffer()
        bones.position(vertexBonesOffset)

        var baseX = 0
        var baseY = 0
        var baseZ = 0

        for (index in vertices.indices) {
            val mask = directionBuffer.getUnsignedByte()
            var x = 0
            if (mask and VERTEX_X_POSITION != 0) {
                x = verticesX.getSignedSmart()
            }

            var y = 0
            if (mask and VERTEX_Y_POSITION != 0) {
                y = verticesY.getSignedSmart()
            }

            var z = 0
            if (mask and VERTEX_Z_POSITION != 0) {
                z = verticesZ.getSignedSmart()
            }

            x += baseX
            y += baseY
            z += baseZ
            baseX = x
            baseY = y
            baseZ = z

            vertices[index] = Vertex(x, y, z)

            // TODO bone decoding
            val bone = if (features.contains(VERTEX_SKINNING)) bones.getUnsignedByte() else -1
        }
    }

    companion object {

        /**
         * The size in `byte`s of the model header block.
         */
        private const val HEADER_SIZE = 18

        /**
         * The mask for vertex data blocks that contain an X coordinate.
         */
        private const val VERTEX_X_POSITION = 0x1

        /**
         * The mask for vertex data blocks that contain a Y coordinate.
         */
        private const val VERTEX_Y_POSITION = 0x2

        /**
         * The mask for vertex data blocks that contain a Z coordinate.
         */
        private const val VERTEX_Z_POSITION = 0x4

        /**
         * Decode a feature flag value and adds it to [features] if the flag was set.
         */
        private fun ModelFeature.isEnabled(features: MutableSet<ModelFeature>, decoder: () -> Boolean) {
            if (decoder()) {
                features.add(this)
            }
        }
    }
}
