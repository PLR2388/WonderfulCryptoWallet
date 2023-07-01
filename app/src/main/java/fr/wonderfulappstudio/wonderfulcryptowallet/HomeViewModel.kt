package fr.wonderfulappstudio.wonderfulcryptowallet

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import fr.wonderfulappstudio.wonderfulcryptowallet.data.repository.LocalRepository
import fr.wonderfulappstudio.wonderfulcryptowallet.data.repository.RemoteRepository
import fr.wonderfulappstudio.wonderfulcryptowallet.model.WalletData
import fr.wonderfulappstudio.wonderfulcryptowallet.ui.AddWalletUiState
import fr.wonderfulappstudio.wonderfulcryptowallet.ui.Crypto
import fr.wonderfulappstudio.wonderfulcryptowallet.ui.model.Wallet
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val localRepository: LocalRepository,
    private val remoteRepository: RemoteRepository
) : ViewModel() {

    var uiState: HomeUiState by mutableStateOf(HomeUiState.Loading)

    var addWalletUiState: AddWalletUiState by mutableStateOf(AddWalletUiState())
        private set

    init {
        refresh()
    }

    fun refresh() {
        uiState = HomeUiState.Loading
        viewModelScope.launch(Dispatchers.IO) {
            localRepository.wallets.collect {
                val cryptoList = it.wallets.map { crypto -> crypto.crypto }.toSet()
                val listOfPrice = arrayListOf<Double>()
                for (elt in cryptoList) {
                    val value = remoteRepository.getCoinsPrices(listOf(elt.name), listOf("usd"))
                    val price = value[elt.name.lowercase()]?.get("usd") as? Double
                    if (price != null) {
                        listOfPrice.add(price)
                    }
                }
                if (listOfPrice.size == cryptoList.size) {
                    val newCryptoList = cryptoList.mapIndexed { index, elt ->
                        elt.copy(currentPrice = listOfPrice[index])
                    }
                    val wallets = it.wallets.map { wallet ->
                        wallet.copy(crypto = newCryptoList.first { it.name == wallet.crypto.name })
                    }
                    withContext(Dispatchers.Main) {
                        uiState = HomeUiState.Success(WalletData(wallets))
                    }
                } else {
                    uiState = HomeUiState.Success(it)
                }
            }
        }
    }

    fun setSelectedCrypto(value: Crypto) {
        addWalletUiState = addWalletUiState.copy(crypto = value)
    }

    fun setNewAddress(value: String) {
        addWalletUiState = addWalletUiState.copy(address = value)
    }

    fun setWalletName(value: String) {
        addWalletUiState = addWalletUiState.copy(name = value)
    }

    fun addNewWallet(completion: () -> Unit) {
        viewModelScope.launch(Dispatchers.IO) {
            val stat = remoteRepository.getBtcAddressStat(addWalletUiState.address)
            val balance = stat.balance.toLong()
            val adaptBalance = balance / 100000000.0

            val wallet = Wallet(
                addWalletUiState.name,
                addWalletUiState.address,
                addWalletUiState.crypto,
                adaptBalance
            )
            localRepository.insertWallet(wallet)
            withContext(Dispatchers.Main) {
                refresh()
                completion()
            }
        }
    }

    fun removeWallet(wallet: Wallet) {
        viewModelScope.launch(Dispatchers.IO) {
            localRepository.deleteWallet(wallet)
            withContext(Dispatchers.Main) {
                refresh()
            }
        }
    }

}

sealed interface HomeUiState {
    object Loading : HomeUiState

    data class Success(val walletData: WalletData) : HomeUiState
}