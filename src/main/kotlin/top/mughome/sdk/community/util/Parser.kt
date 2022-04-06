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

import top.mughome.sdk.community.model.ErrorCode
import top.mughome.sdk.community.model.ErrorCode.*

/**
 * 数据解析器
 * @author Yang
 * @since 0.0.1
 * @see ErrorCode
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
}