package com.devmob.alaya.ui.screen.searchUser

import androidx.lifecycle.ViewModel
import com.devmob.alaya.domain.model.UsersProvider

class SearchUserViewModel : ViewModel() {
    private val users = UsersProvider.users

    fun getUsersFilter(searchText: String) = users.filter {
        it.name.contains(searchText, ignoreCase = true)
    }

}