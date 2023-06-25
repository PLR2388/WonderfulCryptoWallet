package fr.wonderfulappstudio.wonderfulcryptowallet

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import fr.wonderfulappstudio.wonderfulcryptowallet.data.repository.LocalRepository
import fr.wonderfulappstudio.wonderfulcryptowallet.data.repository.RemoteRepository
import fr.wonderfulappstudio.wonderfulcryptowallet.model.WalletData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    localRepository: LocalRepository,
    remoteRepository: RemoteRepository
) : ViewModel() {

    var uiState: HomeUiState = HomeUiState.Loading

    init {
        viewModelScope.launch(Dispatchers.IO) {
            localRepository.wallets.collect {
                uiState = HomeUiState.Success(it)
            }
        }
    }

}

sealed interface HomeUiState {
    object Loading : HomeUiState

    data class Success(val walletData: WalletData) : HomeUiState
}