package com.example.planit
data class WorkspaceWithUsers (
    val id: String? = null,
    val name: String? = null,
    val creationDate: String? = null,
    val creatorId: String? = null,
    val type: String? = null,
    val users: HashMap<String, WorkspaceUser>? = null
)