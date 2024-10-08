package components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Surface
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Preview
@Composable
fun myLoader(loadingStatus: LoadingStatus) {
    var animationStatus by remember { mutableStateOf(false) }

    LaunchedEffect(loadingStatus.getLoadingState()) {
        animationStatus = loadingStatus.getLoadingState() == LoadingStatus.LoadingState.Loading
    }
    AnimatedVisibility(
        visible = animationStatus,
        enter = fadeIn(animationSpec = tween(500)),
        exit = fadeOut(animationSpec = tween(300)),
    ) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            when (loadingStatus.getLoadingState()) {
                LoadingStatus.LoadingState.BeforeLoading,
                LoadingStatus.LoadingState.Success,
                LoadingStatus.LoadingState.Failed,
                LoadingStatus.LoadingState.Loading -> {
                    Surface(
                        modifier = Modifier.fillMaxSize(), color = Color(0x10ffffff)
                    ) {
                        Box(
                            contentAlignment = Alignment.Center,
                        ) {
                            when (loadingStatus.getLoaderType()) {
                                LoadingStatus.LoaderType.Circular -> CircularProgressIndicator(
                                    modifier = Modifier.size(50.dp)
                                )

                                LoadingStatus.LoaderType.Linear -> LinearProgressIndicator(
                                    progress = { loadingStatus.getProgress() / 100f },
                                    modifier = Modifier.size(200.dp, 4.dp),
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

class LoadingStatus {
    private var loadingState by mutableStateOf(LoadingState.BeforeLoading)
    private var loaderType by mutableStateOf(LoaderType.Circular)

    //进度值，仅在Linear模式下有效，取值范围0-100
    private var progress by mutableStateOf(0)
    private var message by mutableStateOf("")

    fun startLoading(): LoadingStatus {
        loadingState = LoadingState.Loading
        return this
    }

    fun stopLoading(): LoadingStatus {
        loadingState = LoadingState.Success
        return this
    }

    fun failLoading(): LoadingStatus {
        loadingState = LoadingState.Failed
        return this
    }

    fun reset(): LoadingStatus {
        loadingState = LoadingState.BeforeLoading
        progress = 0
        return this
    }

    fun circular(): LoadingStatus {
        loaderType = LoaderType.Circular
        return this
    }

    fun linear(): LoadingStatus {
        loaderType = LoaderType.Linear
        return this
    }

    fun progress(progress: Int): LoadingStatus {
        this.progress = progress
        return this
    }

    fun message(message: String): LoadingStatus {
        this.message = message
        return this
    }

    @JvmName("getProgress1")
    fun getProgress(): Int {
        return progress
    }

    @JvmName("getMessage1")
    fun getMessage(): String {
        return message
    }

    @JvmName("getLoadingState1")
    fun getLoadingState(): LoadingState {
        return loadingState
    }

    @JvmName("getLoaderType1")
    fun getLoaderType(): LoaderType {
        return loaderType
    }

    enum class LoadingState {
        BeforeLoading, Loading, Success, Failed
    }

    enum class LoaderType {
        Circular, Linear
    }
}