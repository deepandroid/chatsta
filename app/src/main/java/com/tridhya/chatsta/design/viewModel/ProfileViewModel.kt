package com.tridhya.chatsta.design.viewModel

import android.content.Context
import androidx.databinding.ObservableField
import androidx.lifecycle.MutableLiveData
import com.tridhya.chatsta.model.Images
import com.tridhya.chatsta.model.User
import com.tridhya.chatsta.model.EnumDataModel
import com.tridhya.chatsta.base.ActivityBase

class ProfileViewModel(mContext: Context) : BaseViewModel(mContext) {


    val firstName: ObservableField<String> = ObservableField()
    val lastName: ObservableField<String> = ObservableField()
    var isPrivateAccount: ObservableField<Boolean> = ObservableField()
    var isContentProvider: ObservableField<Boolean> = ObservableField()
    val userName: ObservableField<String> = ObservableField()
    val email: ObservableField<String> = ObservableField()
    val location: ObservableField<String> = ObservableField()
    val contentCreatedLink: ObservableField<String> = ObservableField()
    val tikTokLink: ObservableField<String> = ObservableField()
    val instagramLink: ObservableField<String> = ObservableField()
    val facebookLink: ObservableField<String> = ObservableField()
    val youtubeLink: ObservableField<String> = ObservableField()
    val description: ObservableField<String> = ObservableField()
    val bio: ObservableField<String> = ObservableField()
    val quotes: ObservableField<String> = ObservableField()
    val height: ObservableField<String> = ObservableField()

    var images: ArrayList<Images> = arrayListOf()
    var gender: EnumDataModel? = null
    var relationshipStatus: EnumDataModel? = null
    var starSign: EnumDataModel? = null

    //    val responseUpdate: MutableLiveData<BaseModel<User>> = MutableLiveData()
//    val responsePaidContentProviderToNormalUser: MutableLiveData<BaseModel<User>> =
//        MutableLiveData()
    val responseUserData: MutableLiveData<User> = MutableLiveData()

    //    var allInterestList: MutableLiveData<ArrayList<AllInterestResponseModel>> = MutableLiveData()
    var responsePermanentlyDeleteAccount: MutableLiveData<String> = MutableLiveData()
    var responseContentProviderRequest: MutableLiveData<String> = MutableLiveData()
//    var responsePrivateAccount: MutableLiveData<BaseModel<PrivateAccountResponseModel>> =
//        MutableLiveData()

    init {
        userName.set((mContext as ActivityBase).session?.savedLogin?.userName)
    }

}