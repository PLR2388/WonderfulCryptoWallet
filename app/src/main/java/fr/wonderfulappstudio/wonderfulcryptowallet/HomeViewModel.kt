package fr.wonderfulappstudio.wonderfulcryptowallet

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import fr.wonderfulappstudio.wonderfulcryptowallet.data.repository.LocalRepository
import fr.wonderfulappstudio.wonderfulcryptowallet.data.repository.RemoteRepository
import fr.wonderfulappstudio.wonderfulcryptowallet.ui.AddWalletUiState
import fr.wonderfulappstudio.wonderfulcryptowallet.ui.Crypto
import fr.wonderfulappstudio.wonderfulcryptowallet.ui.model.Wallet
import kotlinx.coroutines.Dispatchers
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
        viewModelScope.launch {
            uiState = HomeUiState.Loading
            remoteRepository.fetchAndUpdateData()
            val wallets = localRepository.getAllWallets()
            uiState = HomeUiState.Success(wallets)
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
        viewModelScope.launch {
            val balance = remoteRepository.getBalance(addWalletUiState.address, addWalletUiState.crypto)

            val wallet = Wallet(
                addWalletUiState.name,
                addWalletUiState.address,
                addWalletUiState.crypto,
                balance ?: 0.0
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

    data class Success(val walletData: List<Wallet>) : HomeUiState
}