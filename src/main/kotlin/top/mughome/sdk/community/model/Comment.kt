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
 * 评论类型
 * @author Yang
 * @since 0.0.1
 * @see BaseModel
 */
interface Comment : BaseModel {
    /**
     * 评论对应的帖子id
     */
    var postId: Int

    /**
     * 创建日期
     */
    var createdDate: String

    /**
     * 创建用户id
     */
    var createdUserId: Int

    /**
     * 内容
     */
    var content: String

    /**
     * 点赞数
     */
    var likeCount: Int

    /**
     * 编辑日期
     */
    var editedDate: String?

    /**
     * 编辑用户id
     */
    var editedUserId: String?

    /**
     * 是否隐藏
     */
    var isHidden: Boolean

    /**
     * 隐藏日期
     */
    var hiddenDate: String?

    /**
     * 隐藏用户id
     */
    var hiddenUserId: Int?

    /**
     * ip地址
     */
    var ipAddress: String
}