package com.blueray.raihan.viewModel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.blueray.respect_new.model.GetAvailableQuotationsResponse
import com.blueray.respect_new.model.GetFormResponse
import com.blueray.respect_new.model.GetInputsDiscountResponse
import com.blueray.respect_new.model.GetMyTeamQutationsResponse
import com.blueray.respect_new.model.GetMyTeamResponse
import com.blueray.respect_new.model.GetNotificationsResponse
import com.blueray.respect_new.model.GetQotationHistoryResponse
import com.blueray.respect_new.model.GetUserInfoResponse
import com.blueray.respect_new.model.InsertInputResponse
import com.blueray.respect_new.model.LoginResponse
import com.blueray.respect_new.model.NetworkResults
import com.blueray.respect_new.model.UpdateProfileResponse
import com.blueray.respect_new.repo.repo
import kotlinx.coroutines.launch
import java.io.File

class AppViewModel(application: Application) : AndroidViewModel(application) {
    private val loginLiveData = MutableLiveData<NetworkResults<LoginResponse>>()
    private val getFormLiveData = MutableLiveData<NetworkResults<GetFormResponse>>()
    private val getUserInfoLiveData = MutableLiveData<NetworkResults<GetUserInfoResponse>>()
    private val insertInputLiveData = MutableLiveData<NetworkResults<InsertInputResponse>>()
    private val availableQuotationsLiveData =
        MutableLiveData<NetworkResults<GetAvailableQuotationsResponse>>()
    private val updateProfileLiveData = MutableLiveData<NetworkResults<UpdateProfileResponse>>()
    private val logoutLiveData = MutableLiveData<NetworkResults<UpdateProfileResponse>>()
    private val getMyTeamLiveData = MutableLiveData<NetworkResults<GetMyTeamResponse>>()
    private val getQutationHistory = MutableLiveData<NetworkResults<GetQotationHistoryResponse>>()
    private val getMyInputsLiveData = MutableLiveData<NetworkResults<GetQotationHistoryResponse>>()
    private val getInputsDiscountLiveData =
        MutableLiveData<NetworkResults<GetInputsDiscountResponse>>()
    private val getMyTeamQuotationsLiveData =
        MutableLiveData<NetworkResults<GetMyTeamQutationsResponse>>()
    private val setInputsDiscountLiveData = MutableLiveData<NetworkResults<UpdateProfileResponse>>()
    private val getFormForEditLiveData = MutableLiveData<NetworkResults<GetFormResponse>>()
    private val notificationsLiveData = MutableLiveData<NetworkResults<GetNotificationsResponse>>()
    fun retrieveLogin(
        userName: String,
        password: String,
        //   playerId: String
    ) {
        viewModelScope.launch {
            loginLiveData.postValue(
                repo.login(
                    userName, password, "12"
                )
            )
        }
    }

    fun getLogin() = loginLiveData

    fun retrieveForm() {
        viewModelScope.launch {
            getFormLiveData.postValue(repo.getForm())
        }
    }

    fun getForm() = getFormLiveData

    fun retrieveUserInfo(uid: String) {
        viewModelScope.launch {
            getUserInfoLiveData.postValue(repo.getUserInfo(uid))
        }
    }

    fun getUserInfo() = getUserInfoLiveData

    fun retrieveInsertInput(data: MutableMap<String, Any>) {
        viewModelScope.launch {
            viewModelScope.launch {
                insertInputLiveData.postValue(repo.insertInput(data))
            }
        }
    }

    fun getInsertInput() = insertInputLiveData


    fun retrieveAvailableQuotations(input_id: String) {
        viewModelScope.launch {
            availableQuotationsLiveData.postValue(repo.getAvailableQuotations(input_id))
        }
    }

    fun getAvailableQuotations() = availableQuotationsLiveData

    fun retrieveUpdateProfile(
        uid: String,
        current_pass: String?,
        pass: String?,
        image: File
    ) {
        viewModelScope.launch {
            updateProfileLiveData.postValue(
                repo.updateProfile(
                    uid = uid,
                    current_pass = current_pass,
                    pass = pass,
                    image = image
                )
            )
        }
    }

    fun getUpdateProfile() = updateProfileLiveData

    fun retrieveLogout(uid: String) {
        viewModelScope.launch {
            logoutLiveData.postValue(repo.logout(uid))
        }
    }

    fun getLogout() = logoutLiveData

    fun retrieveMyTeam(uid: String) {
        viewModelScope.launch {
            getMyTeamLiveData.postValue(repo.getMyTeam(uid))
        }
    }

    fun getMyTeam() = getMyTeamLiveData

    fun retrieveQutationHistory(uid: String, input_id: String) {
        viewModelScope.launch {
            getQutationHistory.postValue(repo.getQotationHistory(uid, input_id))
        }
    }

    fun getQutationHistory() = getQutationHistory

    fun retrieveMyInputs(uid: String) {
        viewModelScope.launch {
            getMyInputsLiveData.postValue(
                repo.getMyInputs(uid)
            )
        }
    }

    fun getMyInputs() = getMyInputsLiveData

    fun retrieveInputsDiscount(uid: String) {
        viewModelScope.launch {
            getInputsDiscountLiveData.postValue(
                repo.getInputsDiscount(uid)
            )
        }
    }

    fun getInputsDiscount() = getInputsDiscountLiveData

    fun retrieveMyTeamQuotations(uid: String) {
        viewModelScope.launch {
            getMyTeamQuotationsLiveData.postValue(
                repo.getMyTeamQuotations(uid)
            )
        }
    }

    fun getMyTeamQuotations() = getMyTeamQuotationsLiveData

    fun retrieveSetInputDiscounts(data: String) {
        viewModelScope.launch {
            setInputsDiscountLiveData.postValue(repo.setInputsDiscountsApprove(data))
        }
    }

    fun getSetInputDiscounts() = setInputsDiscountLiveData


    fun retrieveFormForEdit(input_id: String) {
        viewModelScope.launch {
            getFormForEditLiveData.postValue(repo.getFormForEdit(input_id))
        }
    }

    fun getFormForEdit() = getFormForEditLiveData

    fun retrieveNotifications(uid: String) {
        viewModelScope.launch {
            notificationsLiveData.postValue(repo.getNotifications(uid))
        }
    }

    fun getNotifications() = notificationsLiveData
}