package com.tridhya.basesetupnew.database

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

import com.tridhya.basesetupnew.response.LoginResponse
import org.json.JSONArray
import org.json.JSONException

class Conveter {

    @TypeConverter
    fun fromAny(value: Any?): String {
        return value.toString()
    }

//    @TypeConverter
//    fun stringToOrderSelectedProductsListResponse(value: String?): List<OrderSelectedProductsListResponse>? {
//        if (value == null) {
//            return null
//        }
//        val gson = Gson()
//        val type = object : TypeToken<List<OrderSelectedProductsListResponse>?>() {}.type
//        return gson.fromJson<List<OrderSelectedProductsListResponse>>(value, type)
//    }

//    @TypeConverter
//    fun orderSelectedProductsListResponseToString(orders: List<OrderSelectedProductsListResponse>?): String? {
//        if (orders == null) {
//            return null
//        }
//        val gson = Gson()
//        val type = object : TypeToken<List<OrderSelectedProductsListResponse>?>() {}.type
//        return gson.toJson(orders, type)
//    }
//
//    @TypeConverter
//    fun fromDtmCreate(value: VisitLogQuestionsResponse.DtmCreate?): String? {
//        return Gson().toJson(value)
//    }
//
//    @TypeConverter
//    fun toDtmCreate(value: String?): VisitLogQuestionsResponse.DtmCreate? {
//        return Gson().fromJson(value, VisitLogQuestionsResponse.DtmCreate::class.java)
//    }

    @TypeConverter
    fun fromLoginDtmCreate(value: LoginResponse.DteCreated?): String? {
        return Gson().toJson(value)
    }

    @TypeConverter
    fun toLoginDtmCreate(value: String?): LoginResponse.DteCreated? {
        return Gson().fromJson(value, LoginResponse.DteCreated::class.java)
    }

//    @TypeConverter
//    fun fromDateVisited(value: ScheduleVisitListResponse.Datevisited?): String? {
//        return Gson().toJson(value)
//    }
//
//    @TypeConverter
//    fun toDateVisited(value: String?): ScheduleVisitListResponse.Datevisited? {
//        return Gson().fromJson(value, ScheduleVisitListResponse.Datevisited::class.java)
//    }
//
//    @TypeConverter
//    fun fromStringToCollaborationDepartmentMessageDateResponse(value: String?): CollaborationDepartmentMessageListResponse.CollaborationDepartmentMessageDateResponse? {
//        return Gson().fromJson(
//            value,
//            CollaborationDepartmentMessageListResponse.CollaborationDepartmentMessageDateResponse::class.java
//        )
//    }
//
//    @TypeConverter
//    fun fromCollaborationDepartmentMessageDateResponseToString(list: CollaborationDepartmentMessageListResponse.CollaborationDepartmentMessageDateResponse?): String? {
//        val gson = Gson()
//        return gson.toJson(list)
//    }
//
//    @TypeConverter
//    fun toSalesHistoryResponse(value: String?): List<SalesHistoryResponse>? {
//        val listType = object : TypeToken<ArrayList<SalesHistoryResponse?>?>() {}.type
//        return Gson().fromJson(value, listType)
//    }
//
//    @TypeConverter
//    fun fromSalesHistoryResponse(list: List<SalesHistoryResponse>?): JSONArray {
//        val gson = Gson()
//        val stringGson = gson.toJson(list)
//        return try {
//            JSONArray(stringGson)
//        } catch (e: JSONException) {
//            e.localizedMessage?.let { Constant.logE("catch Error", it) }
//            JSONArray()
//        }
//    }
//
//    @TypeConverter
//    fun toOrderListResponse(value: String?): List<OrderListResponse>? {
//        val listType = object : TypeToken<ArrayList<OrderListResponse?>?>() {}.type
//        return Gson().fromJson(value, listType)
//    }
//
//    @TypeConverter
//    fun fromOrderListResponse(list: List<OrderListResponse>?): String? {
//        return Gson().toJson(list)
//    }
//
//
//    @TypeConverter
//    fun toOrderProductListResponse(value: String?): List<OrderProductListResponse>? {
//        val listType = object : TypeToken<ArrayList<OrderProductListResponse?>?>() {}.type
//        return Gson().fromJson(value, listType)
//    }
//
//    @TypeConverter
//    fun fromOrderProductListResponse(list: List<OrderProductListResponse>?): String? {
//        return Gson().toJson(list)
//    }
//
//    @TypeConverter
//    fun toScheduleVisitListResponse(value: String?): List<ScheduleVisitListResponse>? {
//        val listType = object : TypeToken<ArrayList<ScheduleVisitListResponse?>?>() {}.type
//        return Gson().fromJson(value, listType)
//    }
//
//    @TypeConverter
//    fun fromScheduleVisitListResponse(list: List<ScheduleVisitListResponse>?): String? {
//        return Gson().toJson(list)
//    }
//
//    @TypeConverter
//    fun toCustomerSpecialPriceProductDetailsDraftResponse(value: String?): List<CustomerSpecialPriceSelectedProductsListResponse>? {
//        val listType = object : TypeToken<ArrayList<CustomerSpecialPriceSelectedProductsListResponse?>?>() {}.type
//        return Gson().fromJson(value, listType)
//    }
//
//    @TypeConverter
//    fun fromCustomerSpecialPriceProductDetailsDraftResponse(list: List<CustomerSpecialPriceSelectedProductsListResponse>?): String? {
//        return Gson().toJson(list)
//    }
//
//
//    @TypeConverter
//    fun fromStringToOrderListResponseDeliveryDate(value: String?): OrderListResponse.DeliveryDate? {
//        return Gson().fromJson(
//            value, OrderListResponse.DeliveryDate::class.java
//        )
//    }
//
//    @TypeConverter
//    fun fromOrderListResponseDeliveryDateToString(list: OrderListResponse.DeliveryDate?): String? {
//        val gson = Gson()
//        return gson.toJson(list)
//    }
//
//
//    @TypeConverter
//    fun fromStringToOrderListResponseOrderDate(value: String?): OrderListResponse.OrderDate? {
//        return Gson().fromJson(
//            value, OrderListResponse.OrderDate::class.java
//        )
//    }
//
//    @TypeConverter
//    fun fromOrderListResponseOrderDateToString(list: OrderListResponse.OrderDate?): String? {
//        val gson = Gson()
//        return gson.toJson(list)
//    }
}