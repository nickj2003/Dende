package com.main.dende.utils

import android.content.pm.ActivityInfo
import androidx.activity.compose.LocalActivity
import androidx.compose.runtime.*
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat

@Composable
fun LockOrientation(
    orientation: Int,
    immersive: Boolean = false
) {
    val activity = LocalActivity.current
    val window = activity?.window
    val insetsController = remember(window) {
        window?.decorView?.let { decorView ->
            WindowCompat.getInsetsController(window, decorView)
        }
    }

    LaunchedEffect(Unit) {
        activity?.requestedOrientation = orientation

        window?.let {
            WindowCompat.setDecorFitsSystemWindows(it, !immersive)
            if (immersive) {
                insetsController?.systemBarsBehavior =
                    WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
                insetsController?.hide(WindowInsetsCompat.Type.systemBars())
            } else {
                insetsController?.show(WindowInsetsCompat.Type.systemBars())
            }
        }
    }

    DisposableEffect(Unit) {
        onDispose {
            activity?.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED
            window?.let {
                WindowCompat.setDecorFitsSystemWindows(it, true)
                insetsController?.show(WindowInsetsCompat.Type.systemBars())
            }
        }
    }
}
