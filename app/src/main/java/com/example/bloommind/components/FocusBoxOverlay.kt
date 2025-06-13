package com.example.bloommind.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Composable
fun FocusBoxOverlay(
    boxSize: Dp = 220.dp,
    cornerRadius: Dp = 16.dp,
    borderColor: Color = Color.White,
    borderWidth: Dp = 2.dp
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .drawWithContent {
                drawContent()
                val canvasWidth = size.width
                val canvasHeight = size.height

                val boxPx = boxSize.toPx()
                val left = (canvasWidth - boxPx) / 2f
                val top = (canvasHeight - boxPx) / 2f

                drawRect(
                    color = Color.Black.copy(alpha = 0.5f),
                    size = Size(canvasWidth, canvasHeight),
                    blendMode = BlendMode.SrcOver
                )

                drawRect(
                    color = Color.Transparent,
                    topLeft = Offset(left, top),
                    size = Size(boxPx, boxPx),
                    blendMode = BlendMode.Clear
                )

                drawRoundRect(
                    color = borderColor,
                    topLeft = Offset(left, top),
                    size = Size(boxPx, boxPx),
                    cornerRadius = CornerRadius(cornerRadius.toPx(), cornerRadius.toPx()),
                    style = Stroke(width = borderWidth.toPx())
                )
            }
    )
}
