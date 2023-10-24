package com.tridhya.basesetupnew.response

import android.text.Html
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.google.gson.annotations.SerializedName
import com.tridhya.basesetupnew.database.Conveter
import com.tridhya.basesetupnew.utils.Constant

import java.util.*

@Entity(tableName = Constant.TABLE_LOGIN)
@TypeConverters(Conveter::class)
data class LoginResponse(
    @field:SerializedName("Administrator") val administrator: String?, // null
    @field:SerializedName("AppName") val appName: String?, // iSellIt
    @field:SerializedName("Company") val company: String?, // reg
    @field:SerializedName("dteCreated") val dteCreated: DteCreated?,
    @field:SerializedName("Email") val email: String?, // null
    @field:SerializedName("GroupID") val groupID: Int?, // 1
    @field:SerializedName("hasBulk") val hasBulk: Int?, // 0
    @field:SerializedName("IP") val ip: String?, // https://linx-sellitapp.azurewebsites.net/NewBriefcaseGE/
    @field:SerializedName("intAutoId") val intAutoId: String?, // 488
    @field:SerializedName("intLastTransactionID") val intLastTransactionID: String?, // 1
    @field:SerializedName("isLoadingAppAddProduct") val isLoadingAppAddProduct: Int?, // 0
    @field:SerializedName("isScanEnabled") val isScanEnabled: Int?, // 0
    @field:SerializedName("isUserActive") val isUserActive: Int?, // 1
    @field:SerializedName("LocationId") val locationId: String?, // 1
    @field:SerializedName("LocationName") val locationName: String?, // MSTR
    @field:SerializedName("Password") val password: String?, // 10111
    @field:SerializedName("PinCode") val pinCode: Int?, // 10111
    @field:SerializedName("pricesOnOrder") val pricesOnOrder: String?, // y
    @field:SerializedName("strBriefcaseType") val strBriefcaseType: String?, // null
    @field:SerializedName("strCompany") val strCompany: String?, // reg
    @field:SerializedName("strCompanyFooter") val strCompanyFooter: String?, // null
    @field:SerializedName("strCompanyHeader") val strCompanyHeader: String?, // null
    @field:SerializedName("strCurrency") val strCurrency: String?, // null
    @field:SerializedName("strDbToDownload") val strDbToDownload: String?, // UniqueSQLITE/LinxBriefcaseDB20230201095507.db
    @field:SerializedName("strDepartmentsId") val strDepartmentsId: String?, // null
    @field:SerializedName("strDimsIP") val strDimsIP: String?, // http://dims.groceryexpress.co.za/myshop/dimsgroc/
    @field:SerializedName("strImagePath") val strImagePath: String?, // https://online.groceryexpress.co.za/public/images/pics/
    @field:SerializedName("strIp") val strIp: String?, // http://dims.groceryexpress.co.za/NewBriefcase/
    @field:SerializedName("strJournalRef") val strJournalRef: String?, // null
    @field:SerializedName("strQRCode") val strQRCode: String?, // null
    @field:SerializedName("strTripSheetLink") val strTripSheetLink: String?, // null
    @field:SerializedName("strUserName") val strUserName: String?, // Anja
    @field:SerializedName("TabletRegID") val tabletRegID: String?, // null
    @field:SerializedName("TripSheetLink") val tripSheetLink: String?, // null
    @field:SerializedName("UserDepartment") val userDepartment: String?, // null
    @PrimaryKey(autoGenerate = true) @field:SerializedName("UserId") val userId: Int?, // 1
    @field:SerializedName("UserName") val userName: String?, // keval@isell.com
    @field:SerializedName("intAutoIdUserId") val intAutoIdUserId: String?, // keval@isell.com
    @field:SerializedName("UserRoleOrTeam") val userRoleOrTeam: String? // 1
) {
    data class DteCreated(

        @field:SerializedName("date") val date: String?,

        @field:SerializedName("timezone") val timezone: String?,

        @field:SerializedName("timezone_type") val timezoneType: Int?
    ) : java.io.Serializable {
        fun getFunDate() = date ?: ""
        fun getFunTimezone() = timezone ?: ""
        fun getFunTimezoneType() = timezoneType ?: ""
    }


    fun getStringUserName(): String {
        return strUserName ?: ""
    }

    fun getMainUserName(): String {
        return userName ?: ""
    }

    fun getUserIdLogin(): String {
        return userId.toString()
    }

    fun getLocationIdLogin(): String {
        return locationId?.toString() ?: ""
    }

    fun getStrDbToDownloadLocal(): String {
        return strDbToDownload ?: ""
//        return "UniqueSQLITE/LinxBriefcaseDB20221123105720.db"
    }

    fun getStrIpLocal(): String {
        return strIp ?: ""
//        return Constant.apiUrl.HOME_URL
    }

    fun getUserPinCode(): String {
        return (pinCode ?: -1).toString()
    }

    fun getIpLocal(): String {
        return ip ?: ""
//        return Constant.apiUrl.HOME_URL
    }

    fun getRole(): String {
        return strBriefcaseType ?: ""
    }

    fun getRoleAndTeam(): String {
        return userRoleOrTeam ?: ""
    }

    fun getCompanyName(): String {
        return strCompany ?: ""
    }

    fun getCompanyHeader(): CharSequence {
        return Html.fromHtml(strCompanyHeader ?: "", Html.FROM_HTML_MODE_COMPACT)
    }

    fun getCompanyFooter(): CharSequence {
        return Html.fromHtml(strCompanyFooter ?: "", Html.FROM_HTML_MODE_COMPACT)
    }

    fun getHtmlCompanyHeader(): CharSequence {
        return strCompanyHeader ?: ""
    }

    fun getHtmlCompanyFooter(): CharSequence {
        return strCompanyFooter ?: ""
    }
}