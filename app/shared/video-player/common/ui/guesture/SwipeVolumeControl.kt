package me.him188.ani.app.videoplayer.ui.guesture

import androidx.annotation.MainThread
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.platform.debugInspectorInfo
import androidx.compose.ui.unit.Dp
import me.him188.ani.app.platform.AudioManager
import me.him188.ani.app.platform.BrightnessManager
import me.him188.ani.app.platform.StreamType

interface LevelController {
    val level: Float
    
    @MainThread
    fun increaseLevel()

    @MainThread
    fun decreaseLevel()
}

fun AudioManager.asLevelController(
    streamType: StreamType,
): LevelController = object : LevelController {
    override val level: Float
        get() = getVolume(streamType)

    override fun increaseLevel() {
        val current = getVolume(streamType)
        setVolume(streamType, (current + 0.05f).coerceAtMost(1f))
    }

    override fun decreaseLevel() {
        val current = getVolume(streamType)
        setVolume(streamType, (current - 0.05f).coerceAtLeast(0f))
    }
}

fun BrightnessManager.asLevelController(): LevelController = object : LevelController {
    override val level: Float
        get() = getBrightness()

    override fun increaseLevel() {
        val current = getBrightness()
        setBrightness((current + 0.01f).coerceAtMost(1f))
    }

    override fun decreaseLevel() {
        val current = getBrightness()
        setBrightness((current - 0.01f).coerceAtLeast(0f))
    }
}

fun Modifier.swipeLevelControl(
    controller: LevelController,
    stepSize: Dp,
    orientation: Orientation,
    afterStep: (StepDirection) -> Unit = {},
): Modifier = composed(
    inspectorInfo = debugInspectorInfo {
        name = "swipeLevelControl"
        properties["controller"] = controller
        properties["stepSize"] = stepSize
        properties["orientation"] = orientation
    }
) {
    steppedDraggable(
        rememberSteppedDraggableState(
            stepSize = stepSize,
            onStep = { direction ->
                when (direction) {
                    StepDirection.FORWARD -> controller.increaseLevel()
                    StepDirection.BACKWARD -> controller.decreaseLevel()
                }
                afterStep(direction)
            },
        ),
        orientation = orientation,
    )

}