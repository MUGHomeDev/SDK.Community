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
package top.mughome.sdk.community.util

import com.alibaba.fastjson.JSONException
import com.alibaba.fastjson.JSONObject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.Headers
import okhttp3.OkHttpClient
import okhttp3.Request
import top.mughome.sdk.community.Community
import java.io.IOException
import java.net.MalformedURLException
import java.net.URL

/**
 * 该类用来获取api返回的原始JSON数据
 * @author Yang
 * @since 0.0.1
 * @throws IOException
 * @throws IllegalStateException
 * @throws JSONException
 * @throws MalformedURLException
 */
internal object Getter {
    // region 初始化

    /**
     * 发送请求用的OkHttpClient
     */
    private val client = OkHttpClient()

    // endregion

    // region 获取数据

    /**
     * 获取所有用户
     * @author Yang
     * @since 0.0.1
     * @return JSONObject 用户列表
     */
    suspend fun getUsers(): JSONObject {
        val url = URL(Const.BASE_URL + "user")
        return JSONObject.parseObject(get(url).body?.string().toString())
    }

    /**
     * 通过uid获取用户信息
     * @author Yang
     * @since 0.0.1
     * @param id 用户id
     * @return JSONObject 用户信息
     */
    suspend fun getUser(id: Int): JSONObject {
        val url = URL(Const.BASE_URL + "user/$id")
        return JSONObject.parseObject(get(url).body?.string().toString())
    }

    /**
     * 通过用户名获取用户信息
     * @author Yang
     * @since 0.0.1
     * @param name 用户名
     * @return JSONObject 用户信息
     */
    suspend fun getUser(name: String): JSONObject {
        val url = URL(Const.BASE_URL + "user?name=$name")
        return JSONObject.parseObject(get(url).body?.string().toString())
    }

    /**
     * 获取所有帖子
     * @author Yang
     * @since 0.0.1
     * @return JSONObject 帖子列表
     */
    suspend fun getPosts(): JSONObject {
        val url = URL(Const.BASE_URL + "post")
        return JSONObject.parseObject(get(url).body?.string().toString())
    }

    /**
     * 通过帖子id获取帖子信息
     * @author Yang
     * @since 0.0.1
     * @param id 帖子id
     * @return JSONObject 帖子信息
     */
    suspend fun getPost(id: Int): JSONObject {
        val url = URL(Const.BASE_URL + "post/$id")
        return JSONObject.parseObject(get(url).body?.string().toString())
    }

    // endregion

    // region 发送请求

    /**
     * 通过url发送请求
     * @author Yang
     * @since 0.0.1
     * @param url 请求url
     * @return JSONObject 请求返回的json数据
     * @throws IOException
     * @throws IllegalStateException
     */
    @Throws(
        IOException::class,
        IllegalStateException::class
    )
    suspend fun get(
        url: URL,
    ) = withContext(Dispatchers.IO) {
        return@withContext client.newCall(
            Request.Builder()
                .url(url)
                .headers(
                    if (Community.token != "")
                        Headers.headersOf(
                            "User-Agent", Community.userAgent,
                            "Authorization", "Bearer ${Community.token}"
                        )
                    else
                        Headers.headersOf(
                            "User-Agent", Community.userAgent
                        )
                )
                .get()
                .build()
        ).execute()
    }

    // endregion
}