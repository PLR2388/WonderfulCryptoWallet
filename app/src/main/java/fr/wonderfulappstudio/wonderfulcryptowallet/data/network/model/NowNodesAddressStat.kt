package fr.wonderfulappstudio.wonderfulcryptowallet.data.network.model

import kotlinx.serialization.Serializable

@Serializable
data class NowNodesAddressStat(
    val page: Int,
    val totalPages: Int,
    val itemsOnPage: Int,
    val address: String,
    val balance: String,
    val totalReceived: String,
    val totalSent: String,
    val unconfirmedBalance: String,
    val unconfirmedTxs: Int,
    val txs: Int,
    val txids: List<String>
)
