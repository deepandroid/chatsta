package com.tridhya.chatsta.design.viewModel

import android.content.Context
import androidx.databinding.ObservableField
import androidx.lifecycle.MutableLiveData
import com.tridhya.chatsta.Model.response.AllInterestResponseModel
import com.tridhya.chatsta.Model.Images
import com.tridhya.chatsta.Model.response.EnumDataModel
import com.tridhya.chatsta.base.ActivityBase

class CompleteProfileViewModel(mContext: Context) : BaseViewModel(mContext) {

    /*    val responseUpdate: MutableLiveData<BaseModel<User>> = MutableLiveData()
        private val responseUserData: MutableLiveData<User> = MutableLiveData()*/
    var allInterestList: MutableLiveData<ArrayList<AllInterestResponseModel>> = MutableLiveData()
    val firstName: ObservableField<String> = ObservableField()
    val lastName: ObservableField<String> = ObservableField()
    val userName: ObservableField<String> = ObservableField()
    val email: ObservableField<String> = ObservableField()
    val location: ObservableField<String> = ObservableField()
    val bio: ObservableField<String> = ObservableField()
    val quotes: ObservableField<String> = ObservableField()
    val height: ObservableField<String> = ObservableField()
    var gender: EnumDataModel? = null
    var images: ArrayList<Images> = arrayListOf()
    var relationshipStatus: EnumDataModel? = null
    var starSign: EnumDataModel? = null

    init {
        userName.set((mContext as ActivityBase).session?.savedLogin?.userName)
    }
}