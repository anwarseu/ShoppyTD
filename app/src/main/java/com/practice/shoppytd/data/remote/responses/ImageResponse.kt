package com.practice.shoppytd.data.remote.responses

data class ImageResponse(
        val hits: List<ImageResult>,
        val total: Int,
        val totalHits: Int
)
