package me.him188.ani.app.videoplayer.ui

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import org.jetbrains.skia.Data
import org.jetbrains.skia.Image
import org.jetbrains.skia.ImageInfo
import org.jetbrains.skia.Pixmap
import sun.misc.Unsafe
import uk.co.caprica.vlcj.player.base.MediaPlayer
import uk.co.caprica.vlcj.player.embedded.videosurface.CallbackVideoSurface
import uk.co.caprica.vlcj.player.embedded.videosurface.VideoSurface
import uk.co.caprica.vlcj.player.embedded.videosurface.VideoSurfaceAdapters
import uk.co.caprica.vlcj.player.embedded.videosurface.callback.BufferFormat
import uk.co.caprica.vlcj.player.embedded.videosurface.callback.BufferFormatCallback
import uk.co.caprica.vlcj.player.embedded.videosurface.callback.RenderCallback
import java.nio.Buffer
import java.nio.ByteBuffer

/**
 * Implementation of a video surface that uses native callbacks to receive video frame data for rendering.
 *
 * From `https://github.com/caprica/vlcj/issues/1234#issuecomment-2143293403`
 */
class SkiaImageVideoSurface : VideoSurface(VideoSurfaceAdapters.getVideoSurfaceAdapter()) {
    private val videoSurface = SkiaImageCallbackVideoSurface()
    private lateinit var pixmap: Pixmap
    private val skiaImage = mutableStateOf<Image?>(null)

    val image: State<Image?> = skiaImage

    private inner class SkiaImageBufferFormatCallback : BufferFormatCallback {
        private var sourceWidth: Int = 0
        private var sourceHeight: Int = 0

        override fun getBufferFormat(sourceWidth: Int, sourceHeight: Int): BufferFormat {
            this.sourceWidth = sourceWidth
            this.sourceHeight = sourceHeight
            return BufferFormat(
                "I422",
                sourceWidth,
                sourceHeight,
                intArrayOf(sourceWidth, sourceWidth / 2, sourceWidth / 2),
                intArrayOf(sourceHeight, sourceHeight / 2, sourceHeight / 2)
            )
        }

        override fun allocatedBuffers(buffers: Array<ByteBuffer>) {
//            val buffer = buffers[0]
//            val pointer = UNSAFE.getLong(buffer, addressOffset)
//            val imageInfo = ImageInfo.makeN32Premul(sourceWidth, sourceHeight, ColorSpace.sRGB)
//            pixmap = Pixmap.make(imageInfo, pointer, sourceWidth * 4)
        }
    }

    private inner class SkiaImageRenderCallback : RenderCallback {
        override fun display(
            mediaPlayer: MediaPlayer,
            nativeBuffers: Array<ByteBuffer>,
            bufferFormat: BufferFormat,
        ) {
            val yPlane = nativeBuffers[0]
            val uPlane = nativeBuffers[1]
            val vPlane = nativeBuffers[2]
            val width = bufferFormat.width
            val height = bufferFormat.height

            // Convert I422 to BGR
            val bgrData = i422ToBgr(yPlane, uPlane, vPlane, width, height)

            // Create a Pixmap from BGR data
            val imageInfo = ImageInfo.makeN32Premul(width, height)
            pixmap = Pixmap.make(imageInfo, Data.makeFromBytes(bgrData), width * 4)

            // Create an Image from the Pixmap
            skiaImage.value = Image.makeFromPixmap(pixmap)
        }
    }

    private inner class SkiaImageCallbackVideoSurface : CallbackVideoSurface(
        SkiaImageBufferFormatCallback(),
        SkiaImageRenderCallback(),
        true,
        videoSurfaceAdapter,
    )

    override fun attach(mediaPlayer: MediaPlayer) {
        videoSurface.attach(mediaPlayer)
    }

    private companion object {
        @Suppress("DiscouragedPrivateApi")
        private val UNSAFE: Unsafe = kotlin.run {
            val field = Unsafe::class.java.getDeclaredField("theUnsafe")
            field.isAccessible = true
            field.get(null) as Unsafe
        }

        val addressOffset: Long = UNSAFE.objectFieldOffset(Buffer::class.java.getDeclaredField("address"))
    }
}

fun i422ToBgr(yPlane: ByteBuffer, uPlane: ByteBuffer, vPlane: ByteBuffer, width: Int, height: Int): ByteArray {
    val bgrData = ByteArray(width * height * 3)

    for (i in 0 until height) {
        for (j in 0 until width step 2) {
            val yIndex = i * width + j
            val uvIndex = (i / 2) * (width / 2) + (j / 2)

            val y1 = yPlane.get(yIndex).toInt() and 0xFF
            val y2 = yPlane.get(yIndex + 1).toInt() and 0xFF
            val u = uPlane.get(uvIndex).toInt() and 0xFF - 128
            val v = vPlane.get(uvIndex).toInt() and 0xFF - 128

            val bgr1 = yuvToBgr(y1, u, v)
            val bgr2 = yuvToBgr(y2, u, v)

            val pos1 = (i * width + j) * 3
            val pos2 = pos1 + 3

            bgrData[pos1] = bgr1[2]
            bgrData[pos1 + 1] = bgr1[1]
            bgrData[pos1 + 2] = bgr1[0]

            bgrData[pos2] = bgr2[2]
            bgrData[pos2 + 1] = bgr2[1]
            bgrData[pos2 + 2] = bgr2[0]
        }
    }

    return bgrData
}

fun yuvToBgr(y: Int, u: Int, v: Int): ByteArray {
    val c = y - 16
    val d = u
    val e = v

    var r = (298 * c + 409 * e + 128) shr 8
    var g = (298 * c - 100 * d - 208 * e + 128) shr 8
    var b = (298 * c + 516 * d + 128) shr 8

    r = r.coerceIn(0, 255)
    g = g.coerceIn(0, 255)
    b = b.coerceIn(0, 255)

    return byteArrayOf(b.toByte(), g.toByte(), r.toByte())
}