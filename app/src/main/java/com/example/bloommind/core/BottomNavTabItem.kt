package com.example.bloommind.core

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import com.example.bloommind.ui.theme.BottomNavActiveColor
import com.example.bloommind.ui.theme.BottomNavIconSize
import com.example.bloommind.ui.theme.BottomNavInactiveColor
import com.example.bloommind.ui.theme.BottomNavTextStyle

@Composable
fun BottomNavTabItem(
    title: String,
    icon: ImageVector,
    selected: Boolean,
    onClick: () -> Unit
) {
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()
    val scale by animateFloatAsState(if (isPressed) 0.92f else 1f, label = "TabScale")

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .graphicsLayer(scaleX = scale, scaleY = scale)
            .clickable(
                interactionSource = interactionSource,
                indication = null,
                onClick = onClick
            )
            .padding(vertical = 8.dp)
    ) {
        Spacer(modifier = Modifier.size(5.dp))
        Icon(
            imageVector = icon,
            contentDescription = title,
            modifier = Modifier.size(BottomNavIconSize),
            tint = if (selected) BottomNavActiveColor else BottomNavInactiveColor
        )
        Spacer(modifier = Modifier.size(5.dp))
        Text(
            text = title,
            style = BottomNavTextStyle.copy(
                color = if (selected) BottomNavActiveColor else BottomNavInactiveColor
            )
        )
        if (selected) {
            Box(
                modifier = Modifier
                    .padding(top = 4.dp)
                    .height(2.dp)
                    .width(24.dp)
                    .background(BottomNavActiveColor, shape = MaterialTheme.shapes.small)
            )
        }
    }
}
