package me.him188.ani.app.torrent.api

import me.him188.ani.app.torrent.AniTorrentHandle

interface EventListener {
    val torrentName: String

    /**
     * 当有任何支持的事件时调用. 对于不支持的事件, 将会触发 [onUpdate].
     */
    fun onEvent(event: TorrentEvent)

    /**
     * 当有任何更新时, 在 BT 引擎的线程里调用. 对于支持的事件, 还会触发 [onEvent].
     */
    fun onUpdate(handle: AniTorrentHandle) {}
}