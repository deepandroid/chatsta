package com.tridhya.chatsta.design.viewModel

import android.content.Context
import androidx.databinding.ObservableField
import androidx.lifecycle.MutableLiveData
import com.tridhya.chatsta.model.CommentsModel
import com.tridhya.chatsta.model.CommentsResponseModel
import com.tridhya.chatsta.model.Images
import com.tridhya.chatsta.model.MyConnectionsResponseModel
import com.tridhya.chatsta.model.PostMediaModel
import com.tridhya.chatsta.model.PostResponseModel
import com.tridhya.chatsta.model.ReactionPostResponseModel
import com.tridhya.chatsta.model.User

class FeedViewModel(mContext: Context) : BaseViewModel(mContext) {

    val postText: ObservableField<String> = ObservableField()
    val responseUserData: MutableLiveData<User> = MutableLiveData()
    val mediaList = arrayListOf<PostMediaModel>()
    val responseAddPost: MutableLiveData<Any> = MutableLiveData()
    var images: ArrayList<Images> = arrayListOf()
    var commentText: ObservableField<String> = ObservableField()
    val responseGetPosts: MutableLiveData<PostResponseModel> = MutableLiveData()
    val responseDeletePosts: MutableLiveData<String> = MutableLiveData()
    val responseDeleteComments: MutableLiveData<String> = MutableLiveData()
    val responseDeleteCommentReply: MutableLiveData<CommentsModel> = MutableLiveData()
    val responseReportPosts: MutableLiveData<String> = MutableLiveData()
    val responseReactionsModel: MutableLiveData<ReactionPostResponseModel> = MutableLiveData()
    val responseSearchConnections: MutableLiveData<List<User>> = MutableLiveData()
    val responseMyConnections: MutableLiveData<MyConnectionsResponseModel> = MutableLiveData()
    val responseMyConnectionRequests: MutableLiveData<MyConnectionsResponseModel> =
        MutableLiveData()
    val responseUserComments: MutableLiveData<CommentsResponseModel> = MutableLiveData()
    val responseEditUserComments: MutableLiveData<CommentsModel> = MutableLiveData()
    val responseUserReplyComments: MutableLiveData<CommentsModel> = MutableLiveData()
    val responsePostUserComments: MutableLiveData<CommentsResponseModel> = MutableLiveData()

    //    val responseSponsorClockItems: MutableLiveData<List<SponsorItemsResponseModel>> = MutableLiveData()
    val responseAcceptConnectionRequest: MutableLiveData<String> = MutableLiveData()
    val responseDeclineConnectionRequest: MutableLiveData<String> = MutableLiveData()
    val responseDeleteAllConnectionRequest: MutableLiveData<String> = MutableLiveData()
    val responseDisconnectConnection: MutableLiveData<String> = MutableLiveData()
    val responseBlockUnblockConnection: MutableLiveData<String> = MutableLiveData()
    val responseSendConnectionRequest: MutableLiveData<String> = MutableLiveData()
    val responseCancelConnectionRequest: MutableLiveData<String> = MutableLiveData()

//    val responseUnreadNotificationCount: MutableLiveData<UnreadNotificationCountResponseModel> = MutableLiveData()
    /* private val feedUseCase = FeedUseCase(
         mContext,
         errorLiveData,
         responseUserProfile = responseUserData,
         responseAddPost = responseAddPost,
         responseGetPosts = responseGetPosts,
         responseDeletePosts = responseDeletePosts,
         responseDeleteComments = responseDeleteComments,
         responseDeleteCommentReply = responseDeleteCommentReply,
         responseEditUserComments = responseEditUserComments,
         responseReportPosts = responseReportPosts,
         responseSearchConnections = responseSearchConnections,
         responseReactionsModel = responseReactionsModel,
         responseUserComments = responseUserComments,
         responseUserReplyComments = responseUserReplyComments,
         responsePostUserComments = responsePostUserComments,
         responseSponsorClockItems = responseSponsorClockItems,
         responseMyConnections = responseMyConnections,
         responseMyConnectionRequests = responseMyConnectionRequests,
         responseAcceptConnectionRequest = responseAcceptConnectionRequest,
         responseDeclineConnectionRequest = responseDeclineConnectionRequest,
         responseDeleteAllConnectionRequest = responseDeleteAllConnectionRequest,
         responseDisconnectConnection = responseDisconnectConnection,
         responseCancelConnectionRequest = responseCancelConnectionRequest,
         responseBlockUnblockConnection = responseBlockUnblockConnection,
         responseSendConnectionRequest = responseSendConnectionRequest
     )*/

//    private val notificationUseCase =
//        NotificationUseCase(
//            mContext,
//            errorLiveData,
//            responseUnreadNotificationCount = responseUnreadNotificationCount
//        )

    /* fun addPost() = feedUseCase.addPost(
         AddPostRequestModel(
             text = postText.get(),
             postType = if (mediaList.isEmpty()) TEXT else MEDIA,
             medias = mediaList
         )
     )

     fun editPost(postId: String?) = feedUseCase.editPost(
         postId,
         AddPostRequestModel(
             text = postText.get(),
             postType = if (mediaList.isEmpty()) TEXT else MEDIA,
             medias = mediaList
         )
     )

     fun getPosts(
         filter: String = "",
         search: String = "",
         type: String = "",
         isSearch: Boolean = false,
         page: Int = 0,
     ) =
         feedUseCase.getPosts(
             filter = filter,
             search = search,
             type,
             isSearch,
             page
         )

     fun getMyPosts(userId: String, page: Int = 0) =
         feedUseCase.getMyPosts(
             userId, page
         )

     fun getSearchedConnections(search: String = "", page: Int = 0) =
         feedUseCase.searchConnections(
             search = search, page
         )

     fun deletePost(postId: String) = feedUseCase.deletePost(
         postId = postId
     )

     fun reportPost(postId: String, reportType: String = "") = feedUseCase.reportPost(
         postId = postId, reportType
     )

     fun postReaction(postId: String, reactionId: String) = feedUseCase.postReaction(
         postId,
         PostReactionRequestModel(
             reactionId = reactionId
         )
     )

 //    fun getUserComments(postId: String, page: Int = 0) =
 //        feedUseCase.getUserComments(
 //            postId, page
 //        )

     fun getUserComments(postId: String) =
         feedUseCase.getUserComments(
             postId
         )

     fun getUserCommentReply(postId: String) =
         feedUseCase.getUserCommentReply(
             postId,
             CommentsRequestModel(
                 comment = commentText.get(),
                 media = if (images.isEmpty()) "" else images[0].image
             )
         )

     fun postUserComments(postId: String) =
         feedUseCase.postUserComments(
             postId,
             CommentsRequestModel(
                 comment = commentText.get(),
                 media = if (images.isEmpty()) "" else images[0].image
             )
         )

     fun editUserComments(commentId: String) =
         feedUseCase.editUserComments(
             commentId,
             CommentsRequestModel(
                 comment = commentText.get(),
                 media = if (images.isEmpty()) "" else images[0].image
             )
         )

     fun getAllSponsorClock() = feedUseCase.getAllSponsorClock()

     fun myConnections(search: String = "", page: Int = 1) =
         feedUseCase.myConnections(search = search, page)

     fun myConnectionRequests(page: Int = 1) = feedUseCase.myConnectionRequests(page)

     fun acceptRequestConnection(userId: String, notificationId: String? = null) =
         feedUseCase.acceptRequestConnection(
             userId,
             notificationId
         )

     fun deleteRequestConnection(userId: String, notificationId: String? = null) =
         feedUseCase.deleteRequestConnection(
             userId,
             notificationId
         )

     fun deleteAllRequestConnection(userId: String, notificationId: String? = null) =
         feedUseCase.deleteAllRequestConnection(userId, notificationId)

     fun disconnectConnection(userId: String) = feedUseCase.deleteConnection(userId)

     fun cancelConnectionRequest(userId: String) = feedUseCase.cancelConnectionRequest(userId)

     fun blockUnblockConnection(userId: String) = feedUseCase.blockUnblockConnection(userId)

     fun requestConnection(userId: String) = feedUseCase.requestConnection(userId)

     fun getUserProfile(userId: String?) = feedUseCase.getUserProfile(userId)

     fun deleteComment(commentId: String) = feedUseCase.deleteComment(commentId = commentId)


     fun deleteCommentReply(commentId: String, replyId: String) = feedUseCase.deleteCommentReply(
         commentId = commentId,
         request = DeleteCommentReplyRequestModel(
             replyId
         )
     )

     fun getUnreadNotificationsCount() = notificationUseCase.getUnreadNotificationCount()
 */
}