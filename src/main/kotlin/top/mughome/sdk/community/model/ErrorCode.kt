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
 * 所有错误码
 */
enum class ErrorCode {
    NO_ACCESS_TO_OPERATE,             // -1
    SUCCESS,                          // 0
    REGISTER_SAME_NAME,               // 10
    REGISTER_SAME_EMAIL,              // 11
    TERM_NOT_ACCEPTED,                // 12
    LOGIN_PASSWORD_OR_NAME_ERROR,     // 21
    NEED_TO_RE_LOGIN,                 // 22
    CANNOT_FIND_USER,                 // 30
    CANNOT_FIND_EMAIL_TOKEN,          // 41
    ACCOUNT_HAVE_VERIFIED,            // 42
    ACCOUNT_NOT_VERIFIED,             // 43
    REQUIRED_TOKEN,                   // 50
    TOKEN_FORMAT_INVALID,             // 51
    TOKEN_HAS_EXPIRED,                // 52
    TOKEN_SIGN_INVALID,               // 53
    NO_NEED_TO_UPDATE,                // 54
    CANNOT_FIND_POST,                 // 60
    POST_HAS_BEEN_HIDDEN,             // 61
    CANNOT_FIND_COMMENT,              // 70
    CANNOT_FIND_TAG,                  // 80
    FILE_SIZE_TOO_LARGE,              // 90
    FILE_FORMAT_INVALID,              // 91
    NO_FILE_CONTENTED,                // 92
    UN_AUTHORIZED,                    // 401
    PARAMETERS_INVALID_EXCEPTION,     // 1001
    UNKNOWN_EXCEPTION                 // 1999
}