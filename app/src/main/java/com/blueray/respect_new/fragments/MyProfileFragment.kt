package com.blueray.respect_new.fragments

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.blueray.raihan.viewModel.AppViewModel
import com.blueray.respect_new.DrawerController
import com.blueray.respect_new.R
import com.blueray.respect_new.databinding.FragmentMyProfileBinding
import com.blueray.respect_new.helpers.HelperUtils
import com.blueray.respect_new.model.NetworkResults
import com.bumptech.glide.Glide
import java.io.File
import java.io.FileOutputStream

class MyProfileFragment : BaseFragment<FragmentMyProfileBinding, AppViewModel>() {
    private var drawerController: DrawerController? = null
    override val viewModel by viewModels<AppViewModel>()
    override fun getViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentMyProfileBinding {
        return FragmentMyProfileBinding.inflate(inflater, container, false)
    }

    companion object {
        const val REQUEST_IMAGE_PICK = 100
    }

    private var imageUri: Uri? = null
    private var imageFile: File? = null
    private var newImageUriString: String? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is DrawerController) {
            drawerController = context
        } else {
            throw RuntimeException("$context must implement DrawerController")
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.saveBtn.setOnClickListener {
            imageFile?.let { it1 ->
                viewModel.retrieveUpdateProfile(
                    uid = HelperUtils.getUID(requireActivity()),
                    current_pass = null,
                    pass = null,
                    image = it1
                )
                // Save the new image URI to shared preferences
                HelperUtils.setUserImage(requireContext(), newImageUriString)
            }
        }

        binding.includeTab.backIcon.setOnClickListener {
            drawerController?.openDrawer()
        }

        binding.changeImage.setOnClickListener {
            openGallery()
        }

        viewModel.retrieveUserInfo(HelperUtils.getUID(requireActivity()))
        getUserInfo()
        getUpdateProfile()
    }

    private fun openGallery() {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        startActivityForResult(intent, REQUEST_IMAGE_PICK)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_IMAGE_PICK && resultCode == Activity.RESULT_OK) {
            imageUri = data?.data
            imageUri?.let {
                imageFile = createFileFromUri(it)
                newImageUriString = it.toString()
                // Do something with the file, e.g., upload or display
                Glide.with(requireActivity())
                    .load(it)
                    .placeholder(R.drawable.sample_pic)
                    .into(binding.profileImage)
            }
        }
    }

    private fun createFileFromUri(uri: Uri): File {
        val fileName = "selected_image.jpg"
        val file = File(requireContext().cacheDir, fileName)
        val inputStream = requireContext().contentResolver.openInputStream(uri)
        val outputStream = FileOutputStream(file)
        inputStream?.use { input ->
            outputStream.use { output ->
                input.copyTo(output)
            }
        }
        return file
    }

    private fun getUserInfo() {
        viewModel.getUserInfo().observe(requireActivity()) { result ->
            when (result) {
                is NetworkResults.Success -> {
                    if (result.data.msg.status == 200) {
                        Glide.with(requireActivity())
                            .load(HelperUtils.BASE_URL + result.data.data.userImage)
                            .placeholder(R.drawable.sample_pic)
                            .into(binding.includeTab.barUserImage)
                        Glide.with(requireActivity())
                            .load(HelperUtils.BASE_URL + result.data.data.userImage)
                            .placeholder(R.drawable.sample_pic)
                            .into(binding.profileImage)

                        binding.userNameEt.setText(result.data.data.mail)
                        binding.position.setText(result.data.data.role)
                    }
                }

                is NetworkResults.Error -> {

                }

                else -> {

                }
            }
        }
    }
    private fun getUpdateProfile(){
        viewModel.getUpdateProfile().observe(requireActivity()){
                result ->
            when(result){
                is NetworkResults.Success ->{
                    toast(result.data.msg.message)

                }
                is NetworkResults.Error ->{
                    toast(result.exception.localizedMessage.toString())
                    Log.d("TREDXZA" ,result.exception.localizedMessage.toString() )
                }
                else ->{

                }
            }
        }
    }
}
