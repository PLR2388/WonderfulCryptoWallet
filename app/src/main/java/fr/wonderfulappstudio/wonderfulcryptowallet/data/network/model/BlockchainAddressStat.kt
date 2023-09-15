package fr.wonderfulappstudio.wonderfulcryptowallet.data.network.model

import kotlinx.serialization.Serializable

@Serializable
data class BlockchainAddressStat(
    val addresses: List<Address>
)

@Serializable
data class Address(
    val address: String,
    val final_balance: Long,
    val total_received: Long,
    val total_sent: Long,
    val n_tx: Int
)
