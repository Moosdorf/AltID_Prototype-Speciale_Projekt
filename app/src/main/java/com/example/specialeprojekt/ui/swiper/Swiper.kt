package com.example.specialeprojekt.ui.swiper

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.draggable
import androidx.compose.foundation.gestures.rememberDraggableState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PaintingStyle.Companion.Stroke
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import kotlin.math.roundToInt

@Composable
fun Swiper(onConfirmed: () -> Unit) {
    val width = 300.dp
    val thumbSize = 56.dp
    val maxPx = with(LocalDensity.current) { (width - thumbSize).toPx() }

    var offsetX by remember { mutableFloatStateOf(0f) }
    val confirmed = offsetX >= maxPx * 0.9f

    Box(
        modifier = Modifier
            .width(width)
            .height(thumbSize)
            .background(
                Color(33, 150, 243, 255), RoundedCornerShape(50))
    ) {
        Text(
            text = if (confirmed) "Godkendt!" else "GODKEND",
            modifier = Modifier.align(Alignment.Center),
            color = Color.White
        )

        Box(
            modifier = Modifier
                .offset { IntOffset(offsetX.roundToInt(), 0) }
                .size(thumbSize)
                .background(Color.White, RoundedCornerShape(50))
                .draggable(
                    orientation = Orientation.Horizontal,
                    state = rememberDraggableState { delta ->
                        offsetX = (offsetX + delta).coerceIn(0f, maxPx)
                    },
                    onDragStopped = {
                        if (offsetX >= maxPx * 0.9f) {
                            onConfirmed()
                        } else {
                            offsetX = 0f // snap back
                        }
                    }
                ),
                contentAlignment = Alignment.Center
        )   {
            Canvas(modifier = Modifier.size(24.dp)) {
                    val w = size.width
                    val h = size.height
                    val stroke = Stroke(
                        width = 3.dp.toPx(),
                        cap = StrokeCap.Round,
                        join = StrokeJoin.Round
                    )
                    // Horizontal line
                    drawLine(
                        color = Color(33, 150, 243, 255),
                        start = Offset(0f, h / 2),
                        end = Offset(w, h / 2),
                        strokeWidth = stroke.width,
                        cap = StrokeCap.Round
                    )
                    // Arrow head top
                    drawLine(
                        color = Color(33, 150, 243, 255),
                        start = Offset(w * 0.5f, 0f),
                        end = Offset(w, h / 2),
                        strokeWidth = stroke.width,
                        cap = StrokeCap.Round
                    )
                    // Arrow head bottom
                    drawLine(
                        color = Color(33, 150, 243, 255),
                        start = Offset(w * 0.5f, h),
                        end = Offset(w, h / 2),
                        strokeWidth = stroke.width,
                        cap = StrokeCap.Round
                    )
            }
        }
    }
}


