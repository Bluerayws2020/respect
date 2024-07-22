package com.blueray.respect_new.repo

import android.util.Log
import com.blueray.respect_new.API.ApiClient
import com.blueray.respect_new.model.GetAvailableQuotationsResponse
import com.blueray.respect_new.model.GetFormResponse
import com.blueray.respect_new.model.GetInputsDiscountResponse
import com.blueray.respect_new.model.GetMyTeamQutationsResponse
import com.blueray.respect_new.model.GetMyTeamResponse
import com.blueray.respect_new.model.GetQotationHistoryResponse
import com.blueray.respect_new.model.GetUserInfoResponse
import com.blueray.respect_new.model.InsertInputResponse
import com.blueray.respect_new.model.LoginResponse
import com.blueray.respect_new.model.Msg
import com.blueray.respect_new.model.NetworkResults
import com.blueray.respect_new.model.UpdateProfileResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File

object repo {

    suspend fun login(
        userName: String,
        password: String,
        playerId: String
    ): NetworkResults<LoginResponse> {
        return withContext(Dispatchers.IO) {
            val userNameBody = userName.toRequestBody("multipart/form-data".toMediaTypeOrNull())
            val passwordBody = password.toRequestBody("multipart/form-data".toMediaTypeOrNull())
            val playerIdBody = playerId.toRequestBody("multipart/form-data".toMediaTypeOrNull())
            try {
                val results = ApiClient.retrofitService.login(
                    userNameBody,
                    passwordBody,
                    playerIdBody
                )
                NetworkResults.Success(results)
            } catch (e: Exception) {
                NetworkResults.Error(e)
            }
        }
    }

    suspend fun getForm(): NetworkResults<GetFormResponse> {
        return withContext(Dispatchers.IO) {
            try {
                val results = ApiClient.retrofitService.getForm()
                NetworkResults.Success(results)
            } catch (e: Exception) {
                NetworkResults.Error(e)
            }
        }
    }

    suspend fun getUserInfo(uid: String): NetworkResults<GetUserInfoResponse> {
        return withContext(Dispatchers.IO) {
            val uidBody = uid.toRequestBody("multipart/form-data".toMediaTypeOrNull())
            try {
                val results = ApiClient.retrofitService.getUserInfo(uidBody)
                NetworkResults.Success(results)
            } catch (e: Exception) {
                NetworkResults.Error(e)
            }
        }
    }

    suspend fun insertInput(data: MutableMap<String, Any>): NetworkResults<InsertInputResponse> {
        return withContext(Dispatchers.IO) {
            try {
                val result = ApiClient.retrofitService.insertInput(data)
                NetworkResults.Success(result)
            } catch (e: Exception) {
                NetworkResults.Error(e)
            }
        }
    }

    suspend fun getAvailableQuotations(input_id: String): NetworkResults<GetAvailableQuotationsResponse> {
        return withContext(Dispatchers.IO) {
            val input_idBody = input_id.toRequestBody("multipart/form-data".toMediaTypeOrNull())
            try {
                val results = ApiClient.retrofitService.getAvailableQuotations(input_idBody)
                NetworkResults.Success(results)
            } catch (e: Exception) {
                NetworkResults.Error(e)
            }
        }
    }

    suspend fun updateProfile(
        uid: String,
        current_pass: String?,
        pass: String?,
        image: File
    ): NetworkResults<UpdateProfileResponse> {
        val uidBody = uid.toRequestBody("multipart/form-data".toMediaTypeOrNull())
        val currentPassBody = current_pass?.toRequestBody("multipart/form-data".toMediaTypeOrNull())
        val passBody = pass?.toRequestBody("multipart/form-data".toMediaTypeOrNull())
        val imageBody = RequestBody.create("multipart/form-data".toMediaTypeOrNull(), image)
        val imagePart = MultipartBody.Part.createFormData("image", image.name, imageBody)

        return withContext(Dispatchers.IO) {
            try {
                val results = ApiClient.retrofitService.updateUser(
                    uid = uidBody,
                    current_pass = currentPassBody,
                    pass = passBody,
                    image = imagePart
                )
                NetworkResults.Success(results)
            } catch (e: Exception) {
                NetworkResults.Error(e)
            }
        }
    }


    suspend fun logout(uid: String): NetworkResults<UpdateProfileResponse> {
        val uidBody = uid.toRequestBody("multipart/form-data".toMediaTypeOrNull())
        return withContext(Dispatchers.IO) {
            try {
                val results = ApiClient.retrofitService.logout(uidBody)
                NetworkResults.Success(results)
            } catch (e: Exception) {
                NetworkResults.Error(e)
            }
        }
    }

    suspend fun getMyTeam(uid: String): NetworkResults<GetMyTeamResponse> {
        val uidBody = uid.toRequestBody("multipart/form-data".toMediaTypeOrNull())
        return withContext(Dispatchers.IO) {
            try {
                val results = ApiClient.retrofitService.getMyTeam(uidBody)
                NetworkResults.Success(results)

            } catch (e: Exception) {

                NetworkResults.Error(e)
            }
        }
    }

    suspend fun getQotationHistory(uid: String): NetworkResults<GetQotationHistoryResponse> {
        val uidBody = uid.toRequestBody("multipart/form-data".toMediaTypeOrNull())
        return withContext(Dispatchers.IO) {
            try {
                val results = ApiClient.retrofitService.getQotationHistory(
                    uidBody
                )
                NetworkResults.Success(results)
            } catch (e: Exception) {
                NetworkResults.Error(e)
            }
        }
    }

    suspend fun getInputsDiscount(uid: String): NetworkResults<GetInputsDiscountResponse> {
        val uidBody = uid.toRequestBody("multipart/form-data".toMediaTypeOrNull())
        return withContext(Dispatchers.IO) {
            try {
                val results = ApiClient.retrofitService.getInputsDiscount(uidBody)
                NetworkResults.Success(results)
            } catch (e: Exception) {
                NetworkResults.Error(e)
            }
        }
    }

    suspend fun getMyTeamQuotations(uid: String): NetworkResults<GetMyTeamQutationsResponse> {
        val uidBody = uid.toRequestBody("multipart/form-data".toMediaTypeOrNull())
        return withContext(Dispatchers.IO) {
            try {
                val results = ApiClient.retrofitService.getMyTeamQuotations(uidBody)
                NetworkResults.Success(results)
            } catch (e: Exception) {
                NetworkResults.Error(e)
            }
        }
    }

    suspend fun setInputsDiscountsApprove(data: String): NetworkResults<UpdateProfileResponse> {
        return withContext(Dispatchers.IO) {
            try {
                val mediaType = "application/json".toMediaTypeOrNull()
                val requestBody = data.toRequestBody(mediaType)
                val results = ApiClient.retrofitService.setInputsDiscountsApprove(requestBody)
                Log.d("POPOERTA", results.toString())
                NetworkResults.Success(results)
            } catch (e: Exception) {
                NetworkResults.Error(e)
            }
        }
    }

    suspend fun getFormForEdit(input_id:String): NetworkResults<GetFormResponse> {
        val input_idBody = input_id.toRequestBody("multipart/form-data".toMediaTypeOrNull())
        return withContext(Dispatchers.IO) {
            try {
                val results = ApiClient.retrofitService.getFormForEdit(input_idBody)
                NetworkResults.Success(results)
            } catch (e: Exception) {
                NetworkResults.Error(e)
            }
        }
    }
}