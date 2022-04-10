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
import top.mughome.sdk.community.Community.token
import top.mughome.sdk.community.model.*
import top.mughome.sdk.community.util.Const
import top.mughome.sdk.community.util.Getter
import top.mughome.sdk.community.util.Parser
import java.io.IOException
import java.net.MalformedURLException
import java.net.URL

/**
 * 帖子信息操作类
 * @author Yang
 * @since 0.0.1
 * @see IManager
 */
class PostManager : IManager, Post, Included() {
    // region 初始化

    /**
     * 实例内使用OkHttpClient
     */
    private val client = OkHttpClient()

    /**
     * 初始化BaseModel
     */
    override val type = ModelType.POST
    override var id = -1

    /**
     * 初始化Post
     */
    override var title = ""
    override var createdUserId = -1
    override var content = ""
    override var createdDate = ""
    override var lastCommentId: Int? = null
    override var lastCommentUserId: Int? = null
    override var lastCommentDate: String? = null
    override var editedDate: String? = null
    override var editedUserId: Int? = null
    override var viewCount = -1
    override var commentCount = -1
    override var likeCount = -1

    /**
     * 初始化Included
     */
    override var included: MutableList<BaseModel> = mutableListOf()

    var allPosts = mutableMapOf<Int, Post>()

    // endregion

    //TODO: 实现帖子信息的更新、删除操作

    // region 帖子相关操作

    /**
     * 获取帖子
     * @param id 帖子id
     * @throws IOException
     * @throws JSONException
     * @throws MalformedURLException
     * @throws IllegalStateException
     * @author Yang
     * @since 0.0.1
     */
    @Throws(
        IOException::class,
        JSONException::class,
        MalformedURLException::class,
        IllegalStateException::class
    )
    override suspend fun get(id: Int) {
        val json = Getter.getPost(id)
        Parser.parsePost(json.getJSONObject("data"), this)
    }

    suspend fun getAll() {
        val json = Getter.getPosts()
        Parser.parsePosts(json.getJSONObject("data"), this)
    }

    /**
     * 创建帖子
     * @param title 帖子标题
     * @param content 帖子内容
     * @return 创建结果
     * @throws IOException
     * @throws JSONException
     * @throws MalformedURLException
     * @throws IllegalStateException
     * @see ErrorCode
     * @author Yang
     * @since 0.0.1
     */
    @Throws(
        IOException::class,
        JSONException::class,
        MalformedURLException::class,
        IllegalStateException::class
    )
    suspend fun create(title: String, content: String): ErrorCode {
        val url = URL(Const.BASE_URL + "post")
        val json = JSONObject().apply {
            put("title", title)
            put("content", content)
        }


        val response = post(url, json)
        val responseJson = JSONObject.parseObject(response.body?.string().toString())
        val code = Parser.parse(responseJson.getInteger("code"))
        if (code != ErrorCode.SUCCESS) {
            response.close(); return code
        }

        val id = responseJson.getJSONObject("data").getInteger("postId")

        get(id)

        response.close()
        return code
    }

    suspend fun update() {
        throw NotImplementedError()
    }

    suspend fun delete() {
        throw NotImplementedError()
    }

    /**
     * 帖子点赞
     * @param id 帖子id
     * @return 点赞结果
     * @throws IOException
     * @throws JSONException
     * @throws MalformedURLException
     * @throws IllegalStateException
     * @see ErrorCode
     * @author Yang
     * @since 0.0.1
     */
    suspend fun like(id: Int = this.id): ErrorCode {
        val url = URL(Const.BASE_URL + "like/post?id=$id")
        val response = get(url)
        val responseJson = JSONObject.parseObject(response.body?.string().toString())
        val code = Parser.parse(responseJson.getInteger("code"))
        response.close()
        return code
    }

    /**
     * 帖子取消点赞
     * @param id 帖子id
     * @return 点赞结果
     * @throws IOException
     * @throws JSONException
     * @throws MalformedURLException
     * @throws IllegalStateException
     * @see ErrorCode
     * @author Yang
     * @since 0.0.1
     */
    suspend fun disLike(id: Int = this.id): ErrorCode {
        val url = URL(Const.BASE_URL + "like/post?id=$id&cancel=true")
        val response = get(url)
        val responseJson = JSONObject.parseObject(response.body?.string().toString())
        val code = Parser.parse(responseJson.getInteger("code"))
        response.close()
        return code
    }

    // endregion

    // region 帖子评论相关操作

    /**
     * 评论
     * @param content 评论内容
     * @return 评论结果
     * @see ErrorCode
     * @author Yang
     * @since 0.0.1
     * @throws IOException
     * @throws JSONException
     * @throws MalformedURLException
     * @throws IllegalStateException
     */
    @Throws(
        IOException::class,
        JSONException::class,
        MalformedURLException::class,
        IllegalStateException::class
    )
    suspend fun createComment(content: String): ErrorCode {
        val url = URL(Const.BASE_URL + "comment")
        val json = JSONObject().apply {
            put("content", content)
            put("postId", id)
        }
        val response = post(url, json)
        val responseJson = JSONObject.parseObject(response.body?.string().toString())

        val code = Parser.parse(responseJson.getInteger("code"))
        if (code != ErrorCode.SUCCESS) {
            response.close(); return code
        }

        get(id)

        response.close()
        return code
    }

    /**
     * 评论点赞
     * @param id 帖子id
     * @return 点赞结果
     * @throws IOException
     * @throws JSONException
     * @throws MalformedURLException
     * @throws IllegalStateException
     * @see ErrorCode
     * @author Yang
     * @since 0.0.1
     */
    suspend fun likeComment(id: Int = this.id): ErrorCode {
        val url = URL(Const.BASE_URL + "like/comment?id=$id")
        val response = get(url)
        val responseJson = JSONObject.parseObject(response.body?.string().toString())
        val code = Parser.parse(responseJson.getInteger("code"))
        response.close()
        return code
    }

    /**
     * 评论取消点赞
     * @param id 帖子id
     * @return 点赞结果
     * @throws IOException
     * @throws JSONException
     * @throws MalformedURLException
     * @throws IllegalStateException
     * @see ErrorCode
     * @author Yang
     * @since 0.0.1
     */
    suspend fun disLikeComment(id: Int = this.id): ErrorCode {
        val url = URL(Const.BASE_URL + "like/comment?id=$id&cancel=true")
        val response = get(url)
        val responseJson = JSONObject.parseObject(response.body?.string().toString())
        val code = Parser.parse(responseJson.getInteger("code"))
        response.close()
        return code
    }

    // endregion

    // region 其他操作

    /**
     * 清除字段
     * @author Yang
     * @since 0.0.1
     * @return 清空的PostManager
     */
    fun clear(): PostManager {
        id = -1

        title = ""
        createdUserId = -1
        content = ""
        createdDate = ""
        lastCommentId = null
        lastCommentUserId = null
        lastCommentDate = null
        editedDate = null
        editedUserId = null
        viewCount = -1
        commentCount = -1
        likeCount = -1

        return this
    }

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
                    Headers.headersOf(
                        "User-Agent", Community.userAgent,
                        "Authorization", "Bearer $token"
                    )
                )
                .get()
                .build()
        ).execute()
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
                        "Content-Type", "application/json; charset=utf-8",
                        "Authorization", "Bearer $token"
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
        return "PostManager(id=$id, title='$title', createdUserId=$createdUserId, content='$content', createdDate='$createdDate', lastCommentId=$lastCommentId, lastCommentUserId=$lastCommentUserId, lastCommentDate='$lastCommentDate', editedDate='$editedDate', editedUserId=$editedUserId, viewCount=$viewCount, commentCount=$commentCount, likeCount=$likeCount, included='$included', allPosts='$allPosts')"
    }

    // endregion
}