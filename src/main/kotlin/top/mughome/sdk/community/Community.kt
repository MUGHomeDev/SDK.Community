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
package top.mughome.sdk.community

import top.mughome.sdk.community.manager.AccountManager
import top.mughome.sdk.community.util.Getter

/**
 * Community
 * @author Yang
 * @since 0.0.1
 */
object Community {
    /**
     * 需初始化：User-Agent
     */
    internal lateinit var userAgent: String

    /**
     * 徐初始化：用户token
     */
    internal lateinit var token: String

    /**
     * 初始化类
     * @author Yang
     * @since 0.0.1
     * @param userAgent User-Agent
     */
    fun init(userAgent: String) {
        this.userAgent = userAgent
    }

    /**
     * 登录后回调，初始化全局token
     * @author Yang
     * @since 0.0.1
     * @param token 用户token
     * @see AccountManager.login
     * @see Getter
     */
    internal fun loginCallback(token: String) {
        this.token = token
    }
}