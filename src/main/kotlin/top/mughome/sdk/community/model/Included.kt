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

import top.mughome.sdk.community.manager.PostManager

/**
 * Included数据
 * @author Yang
 * @since 0.0.1
 * @see BaseModel
 * @see PostManager
 */
abstract class Included {
    /**
     * 包含的数据
     * @author Yang
     * @see BaseModel
     * @since 0.0.1
     */
    open lateinit var included: MutableList<BaseModel>

    /**
     * 获取一组依赖于BasicModel的数据
     * @author Yang
     * @see BaseModel
     * @since 0.0.1
     */
    inline fun <reified T : BaseModel> getModel(): Map<Int, T> {
        val map = mutableMapOf<Int, T>()
        included.forEach {
            if (it is T) {
                map[it.id] = it
            }
        }
        return map
    }
}