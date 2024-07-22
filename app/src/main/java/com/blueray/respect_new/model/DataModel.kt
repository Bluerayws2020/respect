package com.blueray.respect_new.model

sealed class NetworkResults<out R> {
    data class Success<out T>(val data: T) : NetworkResults<T>()
    data class PartialSuccess<out T>(val data: T, val httpStatusCode: Int) : NetworkResults<T>()

    data class Error(val exception: Exception) : NetworkResults<Nothing>()
}

data class LoginResponse(
    val msg: LoginMsg
)

data class Msg(
    val message: String,
    val status: Int
)

data class LoginMsg(
    val `data`: LoginData,
    val message: String,
    val status: Int
)

data class LoginData(
    val email: String,
    val id: String,
    val player_id: String,
    val role: String,
    val userImage: String,
    val user_name: String
)

class GetFormResponse : ArrayList<GetFormResponseItem>()

data class GetFormResponseItem(
    val label: String,
    val machine_name: String,
    val options: List<String>,
    val originalType: String,
    val target_bundles: String,
    val target_type: String,
    val type: String,
    var value: String? = null
)

data class GetUserInfoResponse(
    val `data`: GetUserProfileData,
    val msg: Msg
)

data class GetUserProfileData(
    val mail: String,
    val role: String,
    val uid: String,
    val userImage: String
)

data class InsertInputResponse(
    val msg: InsertInputMsg
)

data class InsertInputMsg(
    val input_id: String,
    val message: String,
    val status: Int,
    val falg:Boolean
)

data class InsertInputModel(
    val fields: Map<String, FieldData>
)

data class FieldData(
    val originalType: String,
    val target_bundles: String,
    val target_type: String,
    val value: String
)

data class GetAvailableQuotationsResponse(
    val msg: GetAvailableQuotationsMsg
)

data class GetAvailableQuotationsData(
    val conutryName: String,
    val pdfFile: String,
    val programId: String,
    val programName: String,
    val qutationId: String,
    val total_price: String
)

data class GetAvailableQuotationsMsg(
    val `data`: List<GetAvailableQuotationsData>,
    val message: String,
    val status: Int
)

data class UpdateProfileResponse(
    val msg: Msg
)


data class GetMyTeamResponse(
    val msg: GetMyTeamMsg
)

data class GetMyTeamMsg(
    val `data`: List<GetMyTeamData>,
    val status: Int
)

data class GetMyTeamData(
    val full_name: String,
    val phone_number: String,
    val sid: String,
    val user_picture: String
)

data class GetQotationHistoryResponse(
    val `data`: List<GetQotationHistoryData>
)

data class GetQotationHistoryData(
    val client_name: String,
    val date: String,
    val edit: Boolean,
    val id: String,
    val input_id: String,
    val status: String,
    val view: Boolean
)

data class GetInputsDiscountResponse(
    val `data`: List<GetInputsDiscountData>
)

data class GetInputsDiscountData(
    val advisor: String,
    val advisor_name: String,
    val created_date: String,
    val discount_amount: Int,
    val nid: String,
    val title: String,
    val uid: String,
    var isYesChecked: Boolean? = null
)



data class GetMyTeamQutationsResponse(
    val msg: GetMyTeamQutationsMsg
)
data class GetMyTeamQutationsMsg(
    val `data`: List<GetMyTeamQutationsData>,
    val status: Int
)
data class GetMyTeamQutationsData(
    val full_name: String,
    val quotations_count: Int
)