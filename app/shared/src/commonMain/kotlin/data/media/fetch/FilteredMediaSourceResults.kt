package me.him188.ani.app.data.media.fetch

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.shareIn
import me.him188.ani.app.data.models.MediaSelectorSettings
import kotlin.coroutines.CoroutineContext

/**
 * 正在进行中的数据源查询的结果. 根据用户设置隐藏禁用的数据源
 */
class FilteredMediaSourceResults(
    results: Flow<List<MediaSourceFetchResult>>,
    settings: Flow<MediaSelectorSettings>, // 不可以为 empty flow
    private val flowDispatcher: CoroutineContext = Dispatchers.Default,
    private val enableCaching: Boolean = true,
) {
    private fun <T> Flow<T>.cached() = if (enableCaching) {
        shareIn(CoroutineScope(flowDispatcher), started = SharingStarted.WhileSubscribed(5000))
    } else {
        this
    }

    /**
     * 根据设置, 过滤掉禁用的数据源, 并按照查询到的数量降序排序.
     */
    // Not shared
    val filteredSourceResults: Flow<List<MediaSourceFetchResult>> = results.flatMapLatest { results ->
        settings.flatMapLatest { settings ->
            // 过滤掉禁用的
            val candidates = results.filterTo(mutableListOf()) {
                if (!settings.showDisabled && it.state.value.isDisabled) return@filterTo false
                true
            }

            // 收集它们的 size
            combine(candidates.map { sizes -> sizes.resultsIfEnabled.map { it.size } }) { sizes ->
                // 按照结果数量从大到小, 把禁用的放在最后.
                candidates.sortedWith(
                    compareByDescending<MediaSourceFetchResult> { result ->
                        if (result.state.value.isDisabled) {
                            Int.MIN_VALUE
                        } else {
                            sizes[candidates.indexOf(result)]
                        }
                    }.thenComparing<String> { it.mediaSourceId } // 大小相同的按 ID 排序, 保证稳定
                )
            }
        }
    }.cached()

//    /**
//     * 有至少一个数据源仍然处于 [MediaSourceFetchState.Working] 状态.
//     */
//    private val anyLoading = results.flatMapLatest { results ->
//        results
//            .map { it.state }
//            .combinedAny { it.isWorking }
//    }.flowOn(flowDispatcher)
//
//    /**
//     * 当前的状态.
//     *
//     * 统一计算到一个对象里以保持状态一致. 如果在 UI 中分别 collect 每个属性则有可能会出现状态不一致的情况.
//     */
//    val state: Flow<FilteredMediaSourceResultsState> =
//        combine(anyLoading, filteredSourceResults) { anyLoading, filteredSourceResults ->
//            FilteredMediaSourceResultsState(filteredSourceResults, anyLoading)
//        }.cached()
}

//@Stable
//class FilteredMediaSourceResultsState(
//    val filteredSourceResults: List<MediaSourceFetchResult>,
//    val anyLoading: Boolean,
//    val webSources: List<MediaSourceFetchResult> = filteredSourceResults.filter { it.kind == MediaSourceKind.WEB },
//    val btSources: List<MediaSourceFetchResult> = filteredSourceResults.filter { it.kind == MediaSourceKind.BitTorrent },
//    val enabledSourceCount: Int = filteredSourceResults.count { !it.state.value.isDisabled && it.kind != MediaSourceKind.LocalCache },
//    val totalSourceCount: Int = filteredSourceResults.count { it.kind != MediaSourceKind.LocalCache }, // 缓存数据源属于内部的, 用户应当无感
//) {
//    companion object {
//        val Initial = FilteredMediaSourceResultsState(emptyList(), true)
//    }
//}

private val EmptyFilteredMediaSourceResults by lazy(LazyThreadSafetyMode.NONE) {
    FilteredMediaSourceResults(flowOf(emptyList()), flowOf(MediaSelectorSettings.Default))
}

fun emptyMediaSourceResults() = EmptyFilteredMediaSourceResults