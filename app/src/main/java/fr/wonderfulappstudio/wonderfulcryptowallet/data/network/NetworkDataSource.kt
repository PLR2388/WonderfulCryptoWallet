package fr.wonderfulappstudio.wonderfulcryptowallet.data.network

interface NetworkDataSource {
    suspend fun getCoinsPrices(ids: List<String>, currencies: List<String>): Map<String, Map<String, Double>>
}