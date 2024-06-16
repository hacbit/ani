package me.him188.ani.app.videoplayer.ui

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import org.jetbrains.skia.ColorSpace
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
import uk.co.caprica.vlcj.player.embedded.videosurface.callback.format.RV32BufferFormat
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
            return RV32BufferFormat(sourceWidth, sourceHeight)
        }

        override fun allocatedBuffers(buffers: Array<ByteBuffer>) {
            val buffer = buffers[0]
            val pointer = UNSAFE.getLong(buffer, addressOffset)
            val imageInfo = ImageInfo.makeN32Premul(sourceWidth, sourceHeight, ColorSpace.sRGB)
            pixmap = Pixmap.make(imageInfo, pointer, sourceWidth * 4)
        }
    }

    private inner class SkiaImageRenderCallback : RenderCallback {
        override fun display(
            mediaPlayer: MediaPlayer,
            nativeBuffers: Array<ByteBuffer>,
            bufferFormat: BufferFormat,
        ) {
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