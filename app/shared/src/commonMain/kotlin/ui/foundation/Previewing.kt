/*
 * Ani
 * Copyright (C) 2022-2024 Him188
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

package me.him188.ani.app.ui.foundation

import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.material3.ColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.DpSize
import androidx.datastore.preferences.core.mutablePreferencesOf
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import kotlinx.io.files.Path
import me.him188.ani.app.data.repository.PreferencesRepositoryImpl
import me.him188.ani.app.data.repository.SettingsRepository
import me.him188.ani.app.data.source.media.resolver.HttpStreamingVideoSourceResolver
import me.him188.ani.app.data.source.media.resolver.LocalFileVideoSourceResolver
import me.him188.ani.app.data.source.media.resolver.TorrentVideoSourceResolver
import me.him188.ani.app.data.source.media.resolver.VideoSourceResolver
import me.him188.ani.app.data.source.session.PreviewSessionManager
import me.him188.ani.app.data.source.session.SessionManager
import me.him188.ani.app.navigation.AniNavigator
import me.him188.ani.app.navigation.BrowserNavigator
import me.him188.ani.app.navigation.LocalNavigator
import me.him188.ani.app.navigation.NoopBrowserNavigator
import me.him188.ani.app.platform.GrantedPermissionManager
import me.him188.ani.app.platform.LocalContext
import me.him188.ani.app.platform.PermissionManager
import me.him188.ani.app.platform.getCommonKoinModule
import me.him188.ani.app.platform.isInLandscapeMode
import me.him188.ani.app.platform.notification.NoopNotifManager
import me.him188.ani.app.platform.notification.NotifManager
import me.him188.ani.app.tools.caching.MemoryDataStore
import me.him188.ani.app.tools.torrent.DefaultTorrentManager
import me.him188.ani.app.tools.torrent.TorrentManager
import me.him188.ani.app.ui.foundation.layout.LayoutMode
import me.him188.ani.app.ui.foundation.layout.LocalLayoutMode
import me.him188.ani.app.ui.foundation.widgets.LocalToaster
import me.him188.ani.app.ui.foundation.widgets.Toaster
import me.him188.ani.app.ui.main.AniApp
import me.him188.ani.app.videoplayer.ui.state.DummyPlayerState
import me.him188.ani.app.videoplayer.ui.state.PlayerStateFactory
import me.him188.ani.utils.io.inSystem
import org.koin.core.context.startKoin
import org.koin.core.context.stopKoin
import org.koin.core.module.Module
import org.koin.dsl.module

val LocalIsPreviewing = staticCompositionLocalOf {
    false
}

@Composable
fun ProvideCompositionLocalsForPreview(
    module: Module.() -> Unit = {},
    colorScheme: ColorScheme? = null,
    content: @Composable () -> Unit,
) {
    PlatformPreviewCompositionLocalProvider {
        val coroutineScope = rememberCoroutineScope()
        val context = LocalContext.current
        runCatching { stopKoin() }
        startKoin {
            modules(getCommonKoinModule({ context }, coroutineScope))
            modules(
                module {
                    single<PlayerStateFactory> {
                        PlayerStateFactory { _, _ ->
                            DummyPlayerState(coroutineScope.coroutineContext)
                        }
                    }
                    single<SessionManager> { PreviewSessionManager }
                    factory<VideoSourceResolver> {
                        VideoSourceResolver.from(
                            get<TorrentManager>().engines
                                .map { TorrentVideoSourceResolver(it) }
                                .plus(LocalFileVideoSourceResolver())
                                .plus(HttpStreamingVideoSourceResolver()),
                        )
                    }
                    single<TorrentManager> {
                        DefaultTorrentManager.create(
                            coroutineScope.coroutineContext,
                            get(),
                            baseSaveDir = { Path("preview-cache").inSystem },
                        )
                    }
                    single<PermissionManager> { GrantedPermissionManager }
                    single<NotifManager> { NoopNotifManager }
                    single<BrowserNavigator> { NoopBrowserNavigator }
                    single<SettingsRepository> { PreferencesRepositoryImpl(MemoryDataStore(mutablePreferencesOf())) }
                    module()
                },
            )
        }
        val aniNavigator = remember { AniNavigator() }
        val showLandscapeUI = isInLandscapeMode()

        BoxWithConstraints {
            val size by rememberUpdatedState(
                with(LocalDensity.current) {
                    DpSize(constraints.maxWidth.toDp(), constraints.maxHeight.toDp())
                },
            )
            CompositionLocalProvider(
                LocalIsPreviewing provides true,
                LocalNavigator provides aniNavigator,
                LocalLayoutMode provides remember(size) { LayoutMode(showLandscapeUI, size) },
                LocalImageViewerHandler provides rememberImageViewerHandler(),
                LocalToaster provides remember {
                    object : Toaster {
                        override fun toast(text: String) {
                        }
                    }
                },
                LocalLifecycleOwner provides remember {
                    object : LifecycleOwner {
                        override val lifecycle: Lifecycle get() = GlobalLifecycle
                    }
                },
            ) {
                val navController = rememberNavController()
                SideEffect {
                    aniNavigator.setNavController(navController)
                }
                NavHost(navController, startDestination = "test") { // provide ViewModelStoreOwner
                    composable("test") {
                        AniApp(colorScheme = colorScheme) {
                            content()
                        }
                    }
                }
            }
        }
    }
}

@Composable
expect fun PlatformPreviewCompositionLocalProvider(content: @Composable () -> Unit)

private data object GlobalLifecycle : Lifecycle() {

    private val owner = object : LifecycleOwner {
        override val lifecycle get() = this@GlobalLifecycle
    }

    override val currentState get() = State.RESUMED

    override fun addObserver(observer: LifecycleObserver) {
//        require(observer is DefaultLifecycleObserver) {
//            "$observer must implement androidx.lifecycle.DefaultLifecycleObserver."
//        }
//
//        // Call the lifecycle methods in order and do not hold a reference to the observer.
//        observer.onCreate(owner)
//        observer.onStart(owner)
//        observer.onResume(owner)
    }

    override fun removeObserver(observer: LifecycleObserver) {}
}
