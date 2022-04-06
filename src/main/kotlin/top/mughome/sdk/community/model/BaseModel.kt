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
package top.mughome.sdk.community.model

/**
 * 基础数据模型
 * @author Yang
 * @since 0.0.1
 * @see ModelType
 */
internal interface BaseModel {
    /**
     * 数据类型
     */
    val type: ModelType

    /**
     * id
     */
    var id: Int

    /**
     * 重写toString
     */
    override fun toString(): String
}

/**
 * 数据类型
 */
enum class ModelType {
    /**
     * 用户
     */
    USER,

    /**
     * 帖子
     */
    POST,

    /**
     * 回复
     */
    COMMENT
}