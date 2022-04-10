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
import top.mughome.sdk.community.manager.AccountManager
import top.mughome.sdk.community.manager.PostManager
import top.mughome.sdk.community.model.*
import top.mughome.sdk.community.model.ErrorCode.*

/**
 * 数据解析器
 * @author Yang
 * @since 0.0.1
 * @see ErrorCode
 * @throws JSONException
 */
internal object Parser {
    /**
     * 解析错误码
     * @author Yang
     * @since 0.0.1
     * @param input 错误码
     * @return ErrorCode 返回值
     * @see ErrorCode
     */
    fun parse(input: Int): ErrorCode {
        return when (input) {
            -1 -> NO_ACCESS_TO_OPERATE
            0 -> SUCCESS
            10 -> REGISTER_SAME_NAME
            11 -> REGISTER_SAME_EMAIL
            12 -> TERM_NOT_ACCEPTED
            21 -> LOGIN_PASSWORD_OR_NAME_ERROR
            22 -> NEED_TO_RE_LOGIN
            30 -> CANNOT_FIND_USER
            41 -> CANNOT_FIND_EMAIL_TOKEN
            42 -> ACCOUNT_HAVE_VERIFIED
            43 -> ACCOUNT_NOT_VERIFIED
            50 -> REQUIRED_TOKEN
            51 -> TOKEN_FORMAT_INVALID
            52 -> TOKEN_HAS_EXPIRED
            53 -> TOKEN_SIGN_INVALID
            54 -> NO_NEED_TO_UPDATE
            60 -> CANNOT_FIND_POST
            61 -> POST_HAS_BEEN_HIDDEN
            70 -> CANNOT_FIND_COMMENT
            80 -> CANNOT_FIND_TAG
            90 -> FILE_SIZE_TOO_LARGE
            91 -> FILE_FORMAT_INVALID
            92 -> NO_FILE_CONTENTED
            401 -> UN_AUTHORIZED
            1001 -> PARAMETERS_INVALID_EXCEPTION
            1999 -> UNKNOWN_EXCEPTION
            else -> UNKNOWN_EXCEPTION
        }
    }

    /**
     * 解析用户信息
     * @author Yang
     * @since 0.0.1
     * @param input should be userDataBase
     * @param user 用户
     */
    fun parseUser(input: JSONObject, user: User) {
        user.let {
            it.id = input.getInteger("userId")
            it.userName = input.getString("userName")
            it.userDisplayName = input.getString("userDisplayName")
            it.userRole = input.getInteger("userRole")
            it.userAvatar = if (input["userAvatar"] == null) "" else input.getString("userAvatar")
            it.userCreatedDate = input.getString("userCreatedDate")
        }
    }

    /**
     * 解析用户信息
     * @author Yang
     * @since 0.0.1
     * @param input should be userDataBase
     */
    private fun parseUser(input: JSONObject): User {
        return object : User {
            override val type = ModelType.USER
            override var id = input.getInteger("userId")

            override var userName = input.getString("userName")
            override var userDisplayName = input.getString("userDisplayName")
            override var userAvatar = input.getString("userAvatar")
            override var userRole = input.getInteger("userRole")
            override var userCreatedDate = input.getString("userCreatedDate")

            override fun toString(): String {
                return "User(id=$id, userName='$userName', userNickname='$userDisplayName', userRole=$userRole, userAvatar='$userAvatar', userCreatedDate='$userCreatedDate')"
            }
        }
    }

    /**
     * 解析Token
     * @author Yang
     * @since 0.0.1
     * @param input should be token
     * @param token Token
     */
    fun parseToken(input: JSONObject, token: IToken) {
        token.let {
            it.token = input.getString("value")
            it.exp = input.getLong("exp")
        }
    }

    /**
     * 解析用户登录信息
     * @author Yang
     * @since 0.0.1
     * @param input should be login
     * @param manager AccountManager
     */
    fun parseLogin(input: JSONObject, manager: AccountManager) {
        manager.let {
            parseUser(input.getJSONObject("data").getJSONObject("user"), it)
            parseToken(input.getJSONObject("data").getJSONObject("token"), it)
        }
    }

    /**
     * 解析多个用户信息
     * @author Yang
     * @since 0.0.1
     * @param input should be users
     * @param users 用户列表
     */
    fun parseUsers(input: JSONObject, users: MutableMap<Int, User>) {
        users.clear()
        input.getJSONArray("data")
            .forEach {
                val user = parseUser(it as JSONObject)
                users[user.id] = user
            }
    }

    /**
     * 解析多个用户信息
     * @author Yang
     * @since 0.0.1
     * @param input should be users
     * @return 用户列表<UserId, User>
     */
    fun parseUsers(input: JSONObject): Map<Int, User> {
        val users = mutableMapOf<Int, User>()
        input.getJSONArray("data")
            .forEach {
                val user = parseUser(it as JSONObject)
                users[user.id] = user
            }
        return users
    }

    /**
     * 解析帖子
     * @author Yang
     * @since 0.0.1
     * @param input should be post
     * @param manager PostManager
     */
    fun parsePost(input: JSONObject, manager: PostManager) {
        val postInfo = input.getJSONObject("postInfo")
        manager.let {
            it.included.clear()

            it.id = postInfo.getInteger("postId")
            it.title = postInfo.getString("title")
            it.createdUserId = postInfo.getInteger("createdUserId")
            it.content = postInfo.getString("content")
            it.createdDate = postInfo.getString("createdDate")
            it.lastCommentId =
                if (postInfo["lastCommentId"] == null) null else postInfo.getInteger("lastCommentId")
            it.lastCommentUserId =
                if (postInfo["lastCommentUserId"] == null) null else postInfo.getInteger("lastCommentUserId")
            it.lastCommentDate =
                if (postInfo["lastCommentDate"] == null) null else postInfo.getString("lastCommentDate")
            it.editedDate =
                if (postInfo["editedDate"] == null) null else postInfo.getString("editedDate")
            it.editedUserId =
                if (postInfo["editedUserId"] == null) null else postInfo.getInteger("editedUserId")
            it.viewCount = postInfo.getInteger("viewCount")
            it.commentCount = postInfo.getInteger("commentCount")
            it.likeCount = postInfo.getInteger("likeCount")

            input.getJSONArray("comments")
                .forEach { comment ->
                    it.included.add(parseComment(comment as JSONObject))
                }

            input.getJSONArray("users")
                .forEach { user ->
                    it.included.add(parseUser(user as JSONObject))
                }
        }
    }

    /**
     * 解析帖子
     * @author Yang
     * @since 0.0.1
     * @param input should be posts
     * @return 帖子
     */
    private fun parsePost(input: JSONObject): Post {
        return object : Post {
            override val type = ModelType.POST
            override var id = input.getInteger("postId")

            override var title = input.getString("title")
            override var createdUserId = input.getInteger("createdUserId")
            override var content = input.getString("content")
            override var createdDate = input.getString("createdDate")
            override var lastCommentId: Int? =
                if (input["lastCommentId"] == null) null else input.getInteger("lastCommentId")
            override var lastCommentUserId: Int? =
                if (input["lastCommentUserId"] == null) null else input.getInteger("lastCommentUserId")
            override var lastCommentDate: String? =
                if (input["lastCommentDate"] == null) null else input.getString("lastCommentDate")
            override var editedDate: String? =
                if (input["editedDate"] == null) null else input.getString("editedDate")
            override var editedUserId: Int? =
                if (input["editedUserId"] == null) null else input.getInteger("editedUserId")
            override var viewCount = input.getInteger("viewCount")
            override var commentCount = input.getInteger("commentCount")
            override var likeCount = input.getInteger("likeCount")

            override fun toString(): String {
                return "Post(id=$id, title='$title', createdUserId=$createdUserId, content='$content', createdDate='$createdDate', lastCommentId=$lastCommentId, lastCommentUserId=$lastCommentUserId, lastCommentDate='$lastCommentDate', editedDate='$editedDate', editedUserId=$editedUserId)"
            }
        }
    }

    /**
     * 解析多个帖子
     * @author Yang
     * @since 0.0.1
     * @param input should be posts
     * @param manager PostManager
     */
    fun parsePosts(input: JSONObject, manager: PostManager) {
        manager.let {
            it.allPosts.clear()
            input.getJSONArray("posts")
                .forEach { json ->
                    if (json is JSONObject) {
                        val post = parsePost(json)
                        it.allPosts[post.id] = post
                    }
                }
            input.getJSONArray("users")
                .forEach { user ->
                    if (user is JSONObject) {
                        it.included.add(parseUser(user))
                    }
                }
        }
    }

    /**
     * 解析多个帖子
     * @author Yang
     * @since 0.0.1
     * @param input should be posts
     * @return Map<PostId, Post>
     */
    fun parsePosts(input: JSONObject): Map<Int, Post> {
        val posts = mutableMapOf<Int, Post>()
        input.getJSONObject("data").getJSONArray("posts")
            .forEach {
                if (it is JSONObject) {
                    val post = parsePost(it)
                    posts[post.id] = post
                }
            }
        return posts
    }

    /**
     * 解析评论
     * @author Yang
     * @since 0.0.1
     * @param input should be comments
     * @return 评论
     */
    private fun parseComment(input: JSONObject): Comment {
        return object : Comment {
            override val type = ModelType.COMMENT
            override var id = input.getInteger("id")

            override var postId = input.getInteger("postId")
            override var createdDate = input.getString("createdDate")
            override var createdUserId = input.getInteger("createdUserId")
            override var content = input.getString("content")
            override var likeCount = input.getInteger("likeCount")
            override var editedDate =
                if (input["editedDate"] == null) null else input.getString("editedDate")
            override var editedUserId =
                if (input["editedUserId"] == null) null else input.getString("editedUserId")
            override var isHidden = input.getBoolean("isHidden")
            override var hiddenDate =
                if (input["hiddenDate"] == null) null else input.getString("hiddenDate")
            override var hiddenUserId =
                if (input["hiddenUserId"] == null) null else input.getInteger("hiddenUserId")
            override var ipAddress = input.getString("ipAddress")

            override fun toString(): String {
                return "Comment(id=$id, postId=$postId, createdDate='$createdDate', createdUserId=$createdUserId, content='$content', likeCount=$likeCount, editedDate='$editedDate', editedUserId=$editedUserId, isHidden=$isHidden, hiddenDate='$hiddenDate', hiddenUserId=$hiddenUserId, ipAddress='$ipAddress')"
            }
        }
    }
}