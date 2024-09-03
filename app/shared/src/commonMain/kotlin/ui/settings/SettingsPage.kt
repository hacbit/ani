package me.him188.ani.app.ui.settings

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Scaffold
import androidx.compose.material3.ScaffoldDefaults
import androidx.compose.material3.SecondaryScrollableTabRow
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.CoroutineStart
import kotlinx.coroutines.launch
import me.him188.ani.app.navigation.AniNavigator
import me.him188.ani.app.navigation.OverrideNavigation
import me.him188.ani.app.platform.Platform
import me.him188.ani.app.platform.isMobile
import me.him188.ani.app.ui.foundation.layout.isShowLandscapeUI
import me.him188.ani.app.ui.foundation.pagerTabIndicatorOffset
import me.him188.ani.app.ui.foundation.widgets.LocalToaster
import me.him188.ani.app.ui.foundation.widgets.TopAppBarGoBackButton
import me.him188.ani.app.ui.profile.SettingsViewModel
import me.him188.ani.app.ui.settings.framework.components.SettingsScope
import me.him188.ani.app.ui.settings.tabs.AboutTab
import me.him188.ani.app.ui.settings.tabs.DebugTab
import me.him188.ani.app.ui.settings.tabs.app.AppSettingsTab
import me.him188.ani.app.ui.settings.tabs.media.AutoCacheGroup
import me.him188.ani.app.ui.settings.tabs.media.CacheDirectoryGroup
import me.him188.ani.app.ui.settings.tabs.media.MediaSelectionGroup
import me.him188.ani.app.ui.settings.tabs.media.TorrentEngineGroup
import me.him188.ani.app.ui.settings.tabs.media.VideoResolverGroup
import me.him188.ani.app.ui.settings.tabs.network.DanmakuGroup
import me.him188.ani.app.ui.settings.tabs.network.GlobalProxyGroup
import me.him188.ani.app.ui.settings.tabs.media.source.MediaSourceGroup
import me.him188.ani.app.ui.settings.tabs.network.OtherTestGroup

/**
 * @see renderPreferenceTab 查看名称
 */
@Immutable
enum class SettingsTab {
    APP,
    MEDIA,
    NETWORK,
    ABOUT,
    DEBUG
    ;

    companion object {
        @Stable
        val Default = MEDIA
    }
}

@Composable
fun SettingsPage(
    vm: SettingsViewModel,
    modifier: Modifier = Modifier,
    initialTab: SettingsTab = SettingsTab.Default,
    contentPadding: PaddingValues = PaddingValues(0.dp),
    allowBack: Boolean = !isShowLandscapeUI(),
    contentWindowInsets: WindowInsets = ScaffoldDefaults.contentWindowInsets,
) {
    Scaffold(
        modifier,
        topBar = {
            TopAppBar(
                title = { Text("设置") },
                navigationIcon = {
                    if (allowBack) {
                        TopAppBarGoBackButton()
                    }
                },
            )
        },
        contentWindowInsets = contentWindowInsets,
    ) { topBarPaddings ->
        val pageCount by remember {
            derivedStateOf {
                SettingsTab.entries.run { if (vm.isInDebugMode) size else (size - 1) }
            }
        }
        val pagerState = rememberPagerState(
            initialPage = initialTab.ordinal,
            pageCount = { pageCount },
        )

        val scope = rememberCoroutineScope()

        // Pager with TabRow
        Column(Modifier.padding(topBarPaddings).fillMaxSize()) {
            SecondaryScrollableTabRow(
                selectedTabIndex = pagerState.currentPage,
                indicator = @Composable { tabPositions ->
                    TabRowDefaults.SecondaryIndicator(
                        Modifier.pagerTabIndicatorOffset(pagerState, tabPositions),
                    )
                },
                modifier = Modifier.fillMaxWidth(),
            ) {
                val tabs by remember {
                    derivedStateOf {
                        SettingsTab.entries
                            .filter { if (vm.isInDebugMode) true else it != SettingsTab.DEBUG }
                    }
                }
                tabs.forEachIndexed { index, tabId ->
                    Tab(
                        selected = pagerState.currentPage == index,
                        onClick = { scope.launch { pagerState.animateScrollToPage(index) } },
                        text = {
                            Text(text = renderPreferenceTab(tabId))
                        },
                    )
                }
            }

            OverrideNavigation(
                remember(scope, pagerState) {
                    { old ->
                        object : AniNavigator by old {
                            override fun navigateSettings(tab: SettingsTab) {
                                scope.launch(start = CoroutineStart.UNDISPATCHED) {
                                    pagerState.animateScrollToPage(tab.ordinal)
                                }
                            }
                        }
                    }
                },
            ) {
                HorizontalPager(
                    state = pagerState,
                    Modifier.fillMaxSize(),
                    userScrollEnabled = Platform.currentPlatform.isMobile(),
                ) { index ->
                    val type = SettingsTab.entries[index]
                    Column(Modifier.fillMaxSize().padding(contentPadding)) {
                        when (type) {
                            SettingsTab.MEDIA -> {
                                SettingsTab(modifier) {
                                    VideoResolverGroup(vm.videoResolverSettingsState)
                                    AutoCacheGroup(vm.mediaCacheSettingsState)

                                    TorrentEngineGroup(vm.torrentSettingsState)
                                    CacheDirectoryGroup(vm.cacheDirectoryGroupState)
                                    MediaSelectionGroup(vm.mediaSelectionGroupState)
                                }
                            }

                            SettingsTab.NETWORK -> {
                                SettingsTab(Modifier.fillMaxSize()) {
                                    GlobalProxyGroup(vm.proxySettingsState)
                                    MediaSourceGroup(vm.mediaSourceGroupState, vm.editMediaSourceState)
                                    OtherTestGroup(vm.otherTesters)
                                    DanmakuGroup(vm.danmakuSettingsState, vm.danmakuServerTesters)
                                }
                            }

                            SettingsTab.ABOUT -> {
                                val toaster = LocalToaster.current
                                AboutTab(
                                    modifier = Modifier.fillMaxSize(),
                                    onTriggerDebugMode = {
                                        if (vm.debugTriggerState.triggerDebugMode()) {
                                            toaster.toast("已开启调试模式")
                                        }
                                    },
                                )
                            }

                            SettingsTab.APP -> AppSettingsTab(
                                vm.softwareUpdateGroupState,
                                vm.uiSettings,
                                vm.videoScaffoldConfig,
                                Modifier.fillMaxSize(),
                            )

                            SettingsTab.DEBUG -> DebugTab(
                                vm.debugSettingsState,
                                Modifier.fillMaxSize(),
                                onDisableDebugMode = { scope.launch { pagerState.animateScrollToPage(0) } },
                            )
                        }
                    }
                }
            }
        }
    }
}


@Composable
private fun renderPreferenceTab(
    tab: SettingsTab,
): String {
    return when (tab) {
        SettingsTab.APP -> "应用与界面"
        SettingsTab.NETWORK -> "数据源与网络"
        SettingsTab.MEDIA -> "播放与缓存"
        SettingsTab.ABOUT -> "关于"
        SettingsTab.DEBUG -> "调试"
    }
}

@Composable
internal fun SettingsTab(
    modifier: Modifier = Modifier,
    content: @Composable SettingsScope.() -> Unit,
) {
    Column(modifier.verticalScroll(rememberScrollState()), verticalArrangement = Arrangement.spacedBy(16.dp)) {
        val scope = remember(this) {
            object : SettingsScope(), ColumnScope by this {}
        }
        scope.content()
    }
}
