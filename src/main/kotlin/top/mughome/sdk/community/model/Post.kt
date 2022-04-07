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
 * 帖子类型
 * @author Yang
 * @since 0.0.1
 * @see BaseModel
 * @see PostManager
 */
interface Post : BaseModel {
    /**
     * 帖子标题
     */
    var title: String

    /**
     * 创建用户id
     */
    var createdUserId: Int

    /**
     * 帖子内容
     */
    var content: String

    /**
     * 创建日期
     */
    var createdDate: String

    /**
     * 最后一个回复id
     */
    var lastCommentId: Int?

    /**
     * 最后一个回复用户id
     */
    var lastCommentUserId: Int?

    /**
     * 最后一个回复日期
     */
    var lastCommentDate: String?

    /**
     * 编辑日期
     */
    var editedDate: String?

    /**
     * 编辑用户id
     */
    var editedUserId: Int?

    /**
     * 观看数
     */
    var viewCount: Int

    /**
     * 回复数
     */
    var commentCount: Int

    /**
     * 点赞数
     */
    var likeCount: Int
}