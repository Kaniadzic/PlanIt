package com.example.planit

import java.util.Date

data class Workspace (
    val id: String? = null,
    val name: String? = null,
    val creationDate: String? = null,
    val creatorId: String? = null,
    val type: String? = null,
    val admins: String? = null,
    val users: String? = null
)