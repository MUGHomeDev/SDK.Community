/*
 * MUGHome Community SDK - MUGHome Community SDK for Kotlin/Java
 * https://github.com/MUGHomeDev/SDK.Community
 * Copyright (C) 2021-2022  MUGHome
 *
 * This Library is free software: you can redistribute it
 * and/or modify it under the terms of the GNU General Public
 * License as published by the Free Software Foundation,
 * either version 3 of the License, or (at your option)
 * any later version.
 *
 * This library is distributed in the hope that it will
 * be useful, but WITHOUT ANY WARRANTY; without even
 * the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU General Public
 * License for more details.
 *
 * You should have received a copy of the GNU General Public
 * License along with this program. If not,
 * see <https://www.gnu.org/licenses/>.
 *
 * Please contact us by email contact@mail.mughome.top
 * if you need additional information or have any questions
 */
package top.mughome.sdk.community.manager

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.Headers
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONException
import org.json.JSONObject
import top.mughome.sdk.community.Community
import top.mughome.sdk.community.model.ErrorCode
import top.mughome.sdk.community.util.Const
import top.mughome.sdk.community.util.Parser
import java.io.IOException
import java.net.MalformedURLException
import java.net.URL

/**
 * 账号登录、注册、登出操作类
 * @author Yang
 * @since 0.0.1
 * @see Community
 * @see ErrorCode
 * @see BaseManager
 */
internal class AccountManager : BaseManager() {
    /**
     * 实例内使用的OkHttpClient
     */
    private val client = OkHttpClient()

    /**
     * 登录返回的token
     */
    private lateinit var token: String

    /**
     * 登录
     * @author Yang
     * @since 0.0.1
     * @param userName 用户名
     * @param password 密码
     * @param remember 记住密码，默认为false
     * @return 登录结果
     * @see ErrorCode
     * @throws MalformedURLException
     * @throws IOException
     * @throws IllegalStateException
     * @throws JSONException
     */
    @Throws(MalformedURLException::class, IOException::class, IllegalStateException::class, JSONException::class)
    suspend fun login(
        userName: String,
        password: String,
        remember: Boolean = false
    ): ErrorCode {
        val url = URL(Const.BASE_URL + "account/login")
        val json = JSONObject()
            .put("user", userName)
            .put("password", password)
            .put("remember", remember)

        val response = post(url, json)

        if (response.code != 200) {
            response.close(); return ErrorCode.UNKNOWN_EXCEPTION
        }

        val responseJson = JSONObject(response.body?.string().toString())
        val code = Parser.parse(responseJson.getInt("code"))
        if (code != ErrorCode.SUCCESS) {
            response.close(); return code
        }
        // TODO: 获取该用户信息后解析
        return code
    }

    /**
     * 通过url和json数据进行post请求
     * @author Yang
     * @since 0.0.1
     * @param url 请求的url
     * @param outputData 请求的json数据
     * @return 返回的Response
     * @throws IOException
     * @throws IllegalStateException
     */
    @Throws(IOException::class, IllegalStateException::class)
    private suspend fun post(
        url: URL,
        outputData: JSONObject
    ) = withContext(Dispatchers.IO) {
        return@withContext client.newCall(
            Request.Builder()
                .url(url)
                .headers(
                    if (token.isNotEmpty() && token.isNotBlank())
                        Headers.headersOf(
                            "User-Agent", Community.userAgent,
                            "Content-Type", "application/json",
                            "Authorization", "Bearer $token"
                        )
                    else
                        Headers.headersOf(
                            "User-Agent", Community.userAgent,
                            "Content-Type", "application/json"
                        )
                )
                .post(
                    outputData.toString()
                        .toRequestBody("application/json".toMediaTypeOrNull())
                )
                .build()
        ).execute()
    }
}