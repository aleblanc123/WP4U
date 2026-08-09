package com.example.wp4u.ui.board

import android.app.Application
import android.net.Uri
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.wp4u.data.ImageStorage
import com.example.wp4u.data.ServiceLocator
import com.example.wp4u.database.model.Image
import kotlinx.coroutines.launch

/**
 * ViewModel for one category's board. Exposes the ordered image list and
 * runs the four image flows (Demo 2 use cases): add, delete, replace, and
 * reorder. File copying happens on a background thread; all database writes
 * go through the repository interface.
 */
class BoardViewModel(
    application: Application,
    categoryId: Int
) : AndroidViewModel(application) {

    private val repository = ServiceLocator.repository

    // Boards are per-user: the query is scoped to the signed-in user's id.
    // If no user is signed in (should not happen past the login screen),
    // -1 matches no rows and the board is simply empty.
    val images: LiveData<List<Image>> =
        repository.getImagesForCategory(
            categoryId,
            ServiceLocator.authRepository.currentUser?.userPK ?: -1
        )

    private val _uploadError = MutableLiveData<Boolean>()
    val uploadError: LiveData<Boolean> = _uploadError

    private val boardCategoryId = categoryId

    fun addImage(pickedUri: Uri) {
        viewModelScope.launch {
            // Demo 4 fix: images are owned by the actual signed-in user
            // instead of a hardcoded placeholder id.
            val user = ServiceLocator.authRepository.currentUser
            if (user == null) {
                _uploadError.postValue(true)
                return@launch
            }
            val path = ImageStorage.copyToInternalStorage(
                getApplication(), pickedUri
            )
            if (path != null) {
                repository.addImage(boardCategoryId, path, user.userPK)
            } else {
                _uploadError.postValue(true)
            }
        }
    }

    fun deleteImage(image: Image) {
        viewModelScope.launch {
            repository.deleteImage(image)
        }
    }

    fun replaceImage(image: Image, pickedUri: Uri) {
        viewModelScope.launch {
            val path = ImageStorage.copyToInternalStorage(
                getApplication(), pickedUri
            )
            if (path != null) {
                repository.replaceImage(image, path)
            } else {
                _uploadError.postValue(true)
            }
        }
    }

    /** Called once when a drag ends, with the images in their new order. */
    fun persistOrder(images: List<Image>) {
        viewModelScope.launch {
            repository.reorderImages(images)
        }
    }

    fun onErrorShown() {
        _uploadError.value = false
    }

    /** Factory so the ViewModel can receive the category id at creation. */
    class Factory(
        private val application: Application,
        private val categoryId: Int
    ) : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T =
            BoardViewModel(application, categoryId) as T
    }
}
