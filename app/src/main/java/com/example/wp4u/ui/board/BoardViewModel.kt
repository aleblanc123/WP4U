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
import com.example.wp4u.model.BoardImage
import kotlinx.coroutines.launch

/**
 * ViewModel for one category's board. Exposes the ordered image list and
 * runs the upload flow: copy the picked image into internal storage on a
 * background thread, then insert its path through the repository.
 */
class BoardViewModel(
    application: Application,
    categoryId: Long
) : AndroidViewModel(application) {

    private val repository = ServiceLocator.repository

    val images: LiveData<List<BoardImage>> =
        repository.getImagesForCategory(categoryId)

    private val _uploadError = MutableLiveData<Boolean>()
    val uploadError: LiveData<Boolean> = _uploadError

    private val boardCategoryId = categoryId

    fun addImage(pickedUri: Uri) {
        viewModelScope.launch {
            val path = ImageStorage.copyToInternalStorage(
                getApplication(), pickedUri
            )
            if (path != null) {
                repository.addImage(boardCategoryId, path)
            } else {
                _uploadError.postValue(true)
            }
        }
    }

    fun onErrorShown() {
        _uploadError.value = false
    }

    /** Factory so the ViewModel can receive the category id at creation. */
    class Factory(
        private val application: Application,
        private val categoryId: Long
    ) : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T =
            BoardViewModel(application, categoryId) as T
    }
}
