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

import com.alibaba.fastjson.JSONException
import com.alibaba.fastjson.JSONObject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.Headers
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import top.mughome.sdk.community.Community
import top.mughome.sdk.community.model.ErrorCode
import top.mughome.sdk.community.model.IToken
import top.mughome.sdk.community.util.Const
import top.mughome.sdk.community.util.Getter
import top.mughome.sdk.community.util.Parser
import java.io.IOException
import java.net.MalformedURLException
import java.net.URL

/**
 * 账号登录、注册、登出操作类
 * @author Yang
 * @since 0.0.1
 * @see UserManager
 * @see IToken
 */
class AccountManager : UserManager(), IToken {
    // region 初始化

    /**
     * 实例内使用的OkHttpClient
     */
    private val client = OkHttpClient()

    /**
     * 初始化IToken字段
     */
    override var exp = -1L
    override var token = ""

    // endregion

    // region 账号操作

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
    @Throws(
        MalformedURLException::class,
        IOException::class,
        IllegalStateException::class,
        JSONException::class
    )
    suspend fun login(
        userName: String,
        password: String,
        remember: Boolean = false
    ): ErrorCode {
        val url = URL(Const.BASE_URL + "account/login")
        val json = JSONObject().apply {
            put("name", userName)
            put("password", password)
            put("isRemember", remember)
        }
        val response = post(url, json)

        if (response.code != 200) {
            response.close(); return ErrorCode.UNKNOWN_EXCEPTION
        }

        val responseJson = JSONObject.parseObject(response.body?.string().toString())
        val code = Parser.parse(responseJson.getInteger("code"))
        if (code != ErrorCode.SUCCESS) {
            response.close(); return code
        }

        Parser.parseLogin(responseJson, this)

        Community.loginCallback(this.token)
        response.close()
        return code
    }

    /**
     * 注册
     * @author Yang
     * @since 0.0.1
     * @param userName 用户名
     * @param password 密码
     * @param email 邮箱
     * @param term 是否同意服务条款
     * @return 注册结果
     * @see ErrorCode
     * @throws MalformedURLException
     * @throws IOException
     * @throws IllegalStateException
     * @throws JSONException
     */
    @Throws(
        MalformedURLException::class,
        IOException::class,
        IllegalStateException::class,
        JSONException::class
    )
    suspend fun register(
        userName: String,
        password: String,
        email: String,
        term: Boolean,
    ): ErrorCode {
        val url = URL(Const.BASE_URL + "account/register")
        val json = JSONObject().apply {
            put("name", userName)
            put("password", password)
            put("email", email)
            put("isAcceptTerm", term)
        }
        val response = post(url, json)

        if (response.code != 200) {
            response.close(); return ErrorCode.UNKNOWN_EXCEPTION
        }

        val responseJson = JSONObject.parseObject(response.body?.string().toString())
        val code = Parser.parse(responseJson.getInteger("code"))
        if (code != ErrorCode.SUCCESS) {
            response.close(); return code
        }

        Parser.parseLogin(responseJson, this)
        Community.loginCallback(this.token)
        response.close()
        return code
    }

    /**
     * 登出
     * @author Yang
     * @since 0.0.1
     */
    fun logout() {
        clear()
        token = ""
        exp = -1L
    }

    /**
     * token更新
     * @author Yang
     * @since 0.0.1
     * @param token token
     * @return 更新结果
     * @see ErrorCode
     * @throws MalformedURLException
     * @throws IOException
     * @throws IllegalStateException
     * @throws JSONException
     */
    @Throws(
        MalformedURLException::class,
        IOException::class,
        IllegalStateException::class,
        JSONException::class
    )
    suspend fun tokenRenew(token: String = this.token): ErrorCode {
        val url = URL(Const.BASE_URL + "token/renew?token=$token&isRemember=true")

        val response = Getter.get(url)

        if (response.code != 200) {
            response.close(); return ErrorCode.UNKNOWN_EXCEPTION
        }

        val responseJson = JSONObject.parseObject(response.body?.string().toString())
        val code = Parser.parse(responseJson.getInteger("code"))
        if (code != ErrorCode.SUCCESS) {
            response.close(); return code
        }

        Parser.parseToken(responseJson.getJSONObject("data").getJSONObject("token"), this)
        Community.loginCallback(this.token)
        response.close()
        return code
    }

    // endregion

    // region 其他操作

    /**
     * 重新发送邮件
     * @author Yang
     * @since 0.0.1
     * @param id 用户id
     * @return 操作结果
     * @see ErrorCode
     * @throws MalformedURLException
     * @throws IOException
     * @throws IllegalStateException
     * @throws JSONException
     */
    @Throws(
        MalformedURLException::class,
        IOException::class,
        IllegalStateException::class,
        JSONException::class
    )
    suspend fun resendVerifyEmail(id: Int = this.id): ErrorCode {
        val url = URL(Const.BASE_URL + "email/verify/send?userId=$id")

        val response = Getter.get(url)

        if (response.code != 200) {
            response.close(); return ErrorCode.UNKNOWN_EXCEPTION
        }
        val responseJson = JSONObject.parseObject(response.body?.string().toString())
        val code = Parser.parse(responseJson.getInteger("code"))
        response.close()
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
    @Throws(
        IOException::class,
        IllegalStateException::class
    )
    private suspend fun post(
        url: URL,
        outputData: JSONObject
    ) = withContext(Dispatchers.IO) {
        return@withContext client.newCall(
            Request.Builder()
                .url(url)
                .headers(
                    Headers.headersOf(
                        "User-Agent", Community.userAgent,
                        "Content-Type", "application/json; charset=utf-8"
                    )
                )
                .post(
                    outputData.toString()
                        .toRequestBody("application/json".toMediaTypeOrNull())
                )
                .build()
        ).execute()
    }

    /**
     * 重写toString方法
     * @author Yang
     * @since 0.0.1
     * @return toString
     */
    override fun toString(): String {
        return "AccountManager(id=$id, userName='$userName', userDisplayName='$userDisplayName', userRole=$userRole, userAvatar='$userAvatar', userCreatedDate='$userCreatedDate', exp=$exp, token='$token')"
    }

    /**
     * 清除字段数据
     * @author Yang
     * @since 0.0.1
     * @return 清空的AccountManager
     */
    override fun clear(): AccountManager {
        super.clear()
        exp = -1L
        token = ""

        return this
    }

    // endregion
}