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
import top.mughome.sdk.community.model.ModelType
import top.mughome.sdk.community.model.User
import top.mughome.sdk.community.util.Getter
import top.mughome.sdk.community.util.Parser
import java.io.IOException
import java.net.MalformedURLException

/**
 * 用户信息操作类
 * @author Yang
 * @since 0.0.1
 * @see IManager
 */
open class UserManager : IManager, User {
    // region 初始化

    /**
     * 初始化BaseModel
     */
    override val type = ModelType.USER
    override var id = -1

    /**
     * 初始化User
     */
    override var name = ""
    override var displayName = ""
    override var avatar = ""
    override var role = -1
    override var createdDate = ""
    override var points = 0

    // endregion

    //TODO: 更改、保存用户信息

    // region 用户相关操作

    /**
     * 获取用户
     * @param id 用户ID
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
    override suspend fun get(id: Int) {
        val json = Getter.getUser(id)
        Parser.parseUser(json.getJSONObject("data"), this)
    }

    suspend fun update() {
        throw NotImplementedError()
    }

    // endregion

    // region 其他操作

    /**
     * 重写toString
     * @return toString
     * @author Yang
     * @since 0.0.1
     */
    override fun toString(): String {
        return "UserManager(id=$id, name='$name', displayName='$displayName', role=$role, avatar='$avatar', createdDate='$createdDate', points=$points)"
    }

    /**
     * 清除字段
     * @author Yang
     * @since 0.0.1
     * @return 清除后的UserManager
     */
    open fun clear(): UserManager {
        id = -1
        name = ""
        displayName = ""
        role = -1
        avatar = ""
        createdDate = ""
        points = 0

        return this
    }

    // endregion
}