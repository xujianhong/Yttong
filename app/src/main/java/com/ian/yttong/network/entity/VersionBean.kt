package com.ian.yttong.network.entity

/**
 * @param versionName 版本名称
 * @param versionCode 版本号
 * @param remark 版本描述
 * @param path Android的下载地址
 * @param isMust 0不强制1必须更新
 * @param isOpenNews 是否开启资讯，登录注册（1是0否）
 */
data class VersionBean(
    val versionName: String,
    val versionCode: Int,
    val remark: String,
    val path: String,
    val isMust: Int,
    val isOpenNews: Int
)