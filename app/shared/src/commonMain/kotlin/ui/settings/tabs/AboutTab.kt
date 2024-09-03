package me.him188.ani.app.ui.settings.tabs

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import me.him188.ani.app.data.source.media.cache.MediaAutoCacheService
import me.him188.ani.app.data.source.media.cache.MediaCacheManager
import me.him188.ani.app.data.source.session.OpaqueSession
import me.him188.ani.app.data.source.session.SessionManager
import me.him188.ani.app.data.source.session.isSessionVerified
import me.him188.ani.app.data.source.session.unverifiedAccessTokenOrNull
import me.him188.ani.app.navigation.BrowserNavigator
import me.him188.ani.app.platform.LocalContext
import me.him188.ani.app.platform.currentAniBuildConfig
import me.him188.ani.app.ui.foundation.AbstractViewModel
import me.him188.ani.app.ui.foundation.isInDebugMode
import me.him188.ani.app.ui.foundation.widgets.LocalToaster
import me.him188.ani.app.ui.profile.AniHelpSection
import me.him188.ani.app.ui.profile.DebugInfo
import me.him188.ani.app.ui.settings.SettingsTab
import me.him188.ani.utils.platform.annotations.TestOnly
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import org.koin.mp.KoinPlatform

class AboutTabViewModel : AbstractViewModel(), KoinComponent {
    val browserNavigator: BrowserNavigator by inject()
    private val sessionManager: SessionManager by inject()
    private val cacheManager: MediaCacheManager by inject()

    private val validMediaCacheTasks = kotlin.run {
        val mediaCacheListFromStorages = cacheManager.storages.map { storageFlow ->
            storageFlow.flatMapLatest { storage ->
                if (storage == null) {
                    return@flatMapLatest emptyFlow()
                }
                storage.listFlow
            }.onStart { emit(emptyList()) }
        }
        combine(mediaCacheListFromStorages) { lists ->
            lists.asSequence().flatten().count { it.isValid() }
        }
    }

    val debugInfo = debugInfoFlow().shareInBackground(started = SharingStarted.Eagerly)

    @OptIn(OpaqueSession::class)
    private fun debugInfoFlow() = combine(
        sessionManager.state,
        sessionManager.processingRequest.flatMapLatest { it?.state ?: flowOf(null) },
        sessionManager.isSessionVerified,
        validMediaCacheTasks,
    ) { session, processingRequest, isSessionValid, activeTasks ->
        DebugInfo(
            properties = buildMap {
                val buildConfig = currentAniBuildConfig
                put("isDebug", buildConfig.isDebug.toString())
                if (buildConfig.isDebug) {
                    put("accessToken", session.unverifiedAccessTokenOrNull)
                    put("data/source/session", session.toString())
                }
                put("processingRequest.state", processingRequest.toString())
                put("sessionManager.isSessionValid", isSessionValid.toString())
                put("mediaCacheManager.validTasks", activeTasks.toString())
            },
        )
    }
}

@Stable
private const val GITHUB_REPO = "https://github.com/him188/ani"

@Stable
private const val BANGUMI = "https://bangumi.tv"

@Stable
private const val DANDANPLAY = "https://www.dandanplay.com/"

@Stable
private const val DMHY = "https://dmhy.b168.net/"

@Stable
private const val ACG_RIP = "https://acg.rip/"

@Stable
private const val MIKAN = "https://mikanime.tv/"

@OptIn(DelicateCoroutinesApi::class, TestOnly::class)
@Composable
fun AboutTab(
    vm: AboutTabViewModel = viewModel { AboutTabViewModel() },
    modifier: Modifier = Modifier,
    onTriggerDebugMode: () -> Unit = { },
) {
    val context by rememberUpdatedState(LocalContext.current)
    val toaster = LocalToaster.current

    SettingsTab(modifier) {
        Group(
            title = { Text("关于 Ani") },
        ) {
            Column(Modifier.padding(horizontal = 16.dp)) {
                val style by rememberUpdatedState(
                    MaterialTheme.typography.bodyMedium.toSpanStyle()
                        .copy(color = MaterialTheme.colorScheme.onSurface),
                )
                val primaryColor by rememberUpdatedState(MaterialTheme.colorScheme.primary)
                val text by remember {
                    derivedStateOf {
                        buildAnnotatedString {
                            pushStyle(style)
                            append("Ani 完全免费无广告且开源, 源代码可在 ")
                            pushStyle(
                                SpanStyle(
                                    color = primaryColor,
                                    textDecoration = TextDecoration.Underline,
                                ),
                            )
                            append("GitHub")
                            pop()
                            append(" 找到")
                            pop()
                        }

                    }
                }
                ClickableText(
                    text,
                    style = MaterialTheme.typography.bodyMedium,
                ) {
                    vm.browserNavigator.openBrowser(context, GITHUB_REPO)
                }
            }

            Group(title = { Text("感谢你的支持") }) {
                AniHelpSection(Modifier.fillMaxWidth().padding(horizontal = 16.dp))
            }
        }

        Group(
            title = { Text("鸣谢") },
        ) {
            Column(Modifier.padding(horizontal = 16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text("Ani 使用了许多爱好者用爱发电维护的免费服务.", style = MaterialTheme.typography.bodyMedium)

                Text("特别感谢:", style = MaterialTheme.typography.bodyMedium)

                val listStyle = MaterialTheme.typography.bodyMedium.copy(MaterialTheme.colorScheme.primary)
                ClickableText(
                    AnnotatedString("· Bangumi 番组计划"),
                    style = listStyle,
                ) {
                    vm.browserNavigator.openBrowser(context, BANGUMI)
                }

                ClickableText(
                    AnnotatedString("· Mikan 蜜柑计划"),
                    style = listStyle,
                ) {
                    vm.browserNavigator.openBrowser(context, MIKAN)
                }

                ClickableText(
                    AnnotatedString("· 動漫花園資源網"),
                    style = listStyle,
                ) {
                    vm.browserNavigator.openBrowser(context, DMHY)
                }

                ClickableText(
                    AnnotatedString("· acg.rip"),
                    style = listStyle,
                ) {
                    vm.browserNavigator.openBrowser(context, ACG_RIP)
                }

                ClickableText(
                    AnnotatedString("· 弹弹play"),
                    style = listStyle,
                ) {
                    vm.browserNavigator.openBrowser(context, DANDANPLAY)
                }
            }
        }

        Group(
            title = {
                Text(
                    text = "调试信息",
                    modifier = Modifier.clickable(
                        interactionSource = remember { MutableInteractionSource() },
                        indication = null,
                        onClick = onTriggerDebugMode,
                    ),
                )
            },
            description = { Text("在反馈问题时附上日志可能有用") },
        ) {
            Column(Modifier.padding(horizontal = 16.dp), verticalArrangement = Arrangement.spacedBy(4.dp)) {
                if (isInDebugMode()) {
                    val debugInfo by vm.debugInfo.collectAsStateWithLifecycle(null)
                    val clipboard = LocalClipboardManager.current
                    for ((name, value) in debugInfo?.properties.orEmpty()) {
                        Text(
                            "$name: $value",
                            Modifier.fillMaxWidth().clickable {
                                value?.let { clipboard.setText(AnnotatedString(it)) }
                            },
                            style = MaterialTheme.typography.labelSmall,
                        )
                    }

                    FilledTonalButton(
                        {
                            GlobalScope.launch {
                                KoinPlatform.getKoin().get<MediaAutoCacheService>().checkCache()
                            }
                        },
                    ) {
                        Text("执行自动缓存")
                    }

                    FilledTonalButton(
                        {
                            GlobalScope.launch {
                                KoinPlatform.getKoin().get<MediaCacheManager>().closeAllCaches()
                                withContext(Dispatchers.Main) {
                                    toaster.toast("已关闭所有缓存任务")
                                }
                            }
                        },
                    ) {
                        Text("关闭所有缓存任务")
                    }

                    FilledTonalButton(
                        {
                            GlobalScope.launch {
                                KoinPlatform.getKoin().get<SessionManager>().clearSession()
                            }
                        },
                    ) {
                        Text("清除游客模式记录")
                    }

                    FilledTonalButton(
                        {
                            GlobalScope.launch { KoinPlatform.getKoin().get<SessionManager>().invalidateSession() }
                        },
                    ) {
                        Text("Invalidate Session")
                    }
                }

                FilledTonalButton(
                    {
                        GlobalScope.launch {
                            KoinPlatform.getKoin().get<SessionManager>().clearSession()
                        }
                    },
                ) {
                    Text("退出登录")
                }

                PlatformDebugInfoItems()
            }
        }
    }
}


@Composable
internal expect fun ColumnScope.PlatformDebugInfoItems()
