package com.example.planit
import java.util.*

data class Post(
    val id: String? = null,
    val name: String? = null,
    val date: String? = null,
    val postCreated: String? = null,
    val platformCode: String? = null,
    val typeCode: String? = null,
    val content: String? = null,
    val photoUrl: String? = null
): java.io.Serializable
