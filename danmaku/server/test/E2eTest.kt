package me.him188.ani.danmaku.server

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.request.bearerAuth
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.HttpStatusCode
import io.ktor.http.contentType
import io.ktor.serialization.kotlinx.json.json
import kotlinx.coroutines.test.runTest
import kotlinx.serialization.json.Json
import me.him188.ani.danmaku.protocol.DanmakuGetResponse
import me.him188.ani.danmaku.protocol.DanmakuInfo
import me.him188.ani.danmaku.protocol.DanmakuLocation
import me.him188.ani.danmaku.protocol.DanmakuPostRequest
import me.him188.ani.danmaku.server.ktor.getKtorServer
import org.junit.jupiter.api.AfterAll
import org.junit.jupiter.api.BeforeAll
import java.awt.Color
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class E2eTest {
    private val client = HttpClient {
        install(ContentNegotiation) {
            json(Json { ignoreUnknownKeys = true })
        }
    }
    private val serverEndpoint = "http://localhost:4394/api"

    @Test
    fun `test login, get username, post danmaku and get danmaku`() = runTest {
        val response = client.post("$serverEndpoint/login/bangumi") {
            contentType(ContentType.Application.Json)
            setBody("test_token_1")
        }
        assertEquals(HttpStatusCode.OK, response.status)
        val token = response.body<String>()

        val danmaku = DanmakuInfo(
            playTime = 1000,
            color = Color.BLACK.rgb,
            text = "This is a danmaku",
            location = DanmakuLocation.NORMAL
        )

//        val response2 = client.post("$serverEndpoint/danmaku/1") {
//            bearerAuth(token)
//            contentType(ContentType.Application.Json)
//            setBody<DanmakuPostRequest>(
//                DanmakuPostRequest(danmaku)
//            )
//        }
//        assertEquals(HttpStatusCode.OK, response2.status)

        val response3 = client.get("$serverEndpoint/danmaku/1") {
            bearerAuth(token)
        }
        assertEquals(HttpStatusCode.OK, response3.status)
        val danmakuList = response3.body<DanmakuGetResponse>().danmakuList
//        assertEquals(1, danmakuList.size)
//        assertEquals(danmaku, danmakuList[0].danmakuInfo)

        val response4 = client.get("$serverEndpoint/danmaku/1") {
            parameter("fromTime", 2000)
            bearerAuth(token)
        }
        assertEquals(HttpStatusCode.OK, response4.status)
        val danmakuList2 = response3.body<DanmakuGetResponse>().danmakuList
        assertTrue(danmakuList2.isEmpty())
    }

//    companion object {
//        private val server = getKtorServer(
//            EnvironmentVariables(
//                port = 4394,
//                testing = true,
//                jwtAudience = "test-audience",
//                jwtIssuer = "test-issuer",
//            )
//        )
//
//        @JvmStatic
//        @BeforeAll
//        fun setup() {
//            server.start(wait = false)
//        }
//
//        @JvmStatic
//        @AfterAll
//        fun teardown() {
//            server.stop(1000, 1000)
//        }
//    }

}