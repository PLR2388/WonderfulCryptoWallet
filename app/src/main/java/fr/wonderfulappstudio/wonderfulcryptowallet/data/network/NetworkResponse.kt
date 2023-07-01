package fr.wonderfulappstudio.wonderfulcryptowallet.data.network

import kotlinx.serialization.Serializable

@Serializable
data class NetworkResponse<T>(
    val data: T,
)
