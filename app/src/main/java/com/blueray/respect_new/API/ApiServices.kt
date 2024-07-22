package com.blueray.respect_new.API

import com.blueray.respect_new.model.GetAvailableQuotationsResponse
import com.blueray.respect_new.model.GetFormResponse
import com.blueray.respect_new.model.GetInputsDiscountResponse
import com.blueray.respect_new.model.GetMyTeamQutationsResponse
import com.blueray.respect_new.model.GetMyTeamResponse
import com.blueray.respect_new.model.GetQotationHistoryResponse
import com.blueray.respect_new.model.GetUserInfoResponse
import com.blueray.respect_new.model.InsertInputResponse
import com.blueray.respect_new.model.LoginResponse
import com.blueray.respect_new.model.UpdateProfileResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part

interface ApiServices {
    @Multipart
    @POST("app/login")
    suspend fun login(
        @Part("userName") userName: RequestBody,
        @Part("password") password: RequestBody,
        @Part("player_id") player_id: RequestBody,
    ): LoginResponse

    @POST("app/getForm")
    suspend fun getForm(

    ): GetFormResponse


    @Multipart
    @POST("app/getUserInfo")
    suspend fun getUserInfo(
        @Part("uid") uid: RequestBody
    ): GetUserInfoResponse

    @POST("app/insertInputOrUpdate")
    @Headers("Accept: application/json", "Content-Type: application/json")
    suspend fun insertInput(
        @Body data: MutableMap<String, Any>
    ): InsertInputResponse

    @Multipart
    @POST("app/getAvailableQuotations")
    suspend fun getAvailableQuotations(
        @Part("input_id") input_id: RequestBody
    ): GetAvailableQuotationsResponse

    @Multipart
    @POST("app/updateUser")
    suspend fun updateUser(
        @Part("uid") uid: RequestBody,
        @Part("current_pass") current_pass: RequestBody?,
        @Part("pass") pass: RequestBody?,
        @Part image: MultipartBody.Part
    ): UpdateProfileResponse

    @Multipart
    @POST("app/logout")
    suspend fun logout(
        @Part("uid") uid: RequestBody,
    ): UpdateProfileResponse

    @Multipart
    @POST("app/getMyTeam")
    suspend fun getMyTeam(
        @Part("uid") uid: RequestBody,
    ): GetMyTeamResponse

    @Multipart
    @POST("app/getQotationHistory")
    suspend fun getQotationHistory(
        @Part("uid") uid: RequestBody
    ): GetQotationHistoryResponse

    @Multipart
    @POST("app/getinputsDiscountNeedApprove")
    suspend fun getInputsDiscount(
        @Part("uid") uid: RequestBody
    ): GetInputsDiscountResponse

    @Multipart
    @POST("app/getMyTeamQuotations")
    suspend fun getMyTeamQuotations(
        @Part("uid") uid: RequestBody
    ): GetMyTeamQutationsResponse

    @Headers("Accept: application/json", "Content-Type: application/json")
    @POST("app/setInputsDiscountsApprove")
    suspend fun setInputsDiscountsApprove(@Body requestBody: RequestBody): UpdateProfileResponse

    @Multipart
    @POST("app/getForm")
    suspend fun getFormForEdit(
        @Part("input_id") input_id: RequestBody
    ): GetFormResponse
}