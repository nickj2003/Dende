package com.main.dende.ui.component

import androidx.compose.foundation.layout.RowScope
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.main.dende.ui.theme.*

@Composable
fun DefaultButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    content: @Composable RowScope.() -> Unit
) {
    val colors = ButtonDefaults.buttonColors(
        containerColor = Black,
        contentColor = White,
        disabledContainerColor = White,
        disabledContentColor = Black,
    )

    Button(
        onClick = onClick,
        colors = colors,
        enabled = enabled,
        content = content
    )
}
