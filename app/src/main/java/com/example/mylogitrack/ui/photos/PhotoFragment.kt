package com.example.mylogitrack.ui.photos

import android.app.Activity.RESULT_OK
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.dsphotoeditor.sdk.activity.DsPhotoEditorActivity
import com.dsphotoeditor.sdk.utils.DsPhotoEditorConstants
import com.example.mylogitrack.databinding.FragmentPhotoBinding
import com.github.dhaval2404.imagepicker.ImagePicker

class PhotoFragment : Fragment() {

    private var _binding: FragmentPhotoBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    private var imageUri: Uri? = null
    private var fromEditingSession: Boolean = false

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPhotoBinding.inflate(inflater, container, false)
        binding.selectImage.setOnClickListener {
            fromEditingSession = false
            ImagePicker.with(this).crop().start()
        }

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (fromEditingSession) {
            if (resultCode == RESULT_OK && requestCode == 200) {
                binding.editedImage.setImageURI(data!!.data)
            }
        } else {
            try {
                if (data != null) {
                    imageUri = data.data
                    val dsPhotoEditorIndent = Intent(requireActivity(), DsPhotoEditorActivity::class.java)
                    dsPhotoEditorIndent.data = imageUri
                    dsPhotoEditorIndent.putExtra(DsPhotoEditorConstants.DS_PHOTO_EDITOR_OUTPUT_DIRECTORY, "MyLogiTrack")
                    dsPhotoEditorIndent.putExtra(DsPhotoEditorConstants.DS_PHOTO_EDITOR_TOOLS_TO_HIDE, intArrayOf(DsPhotoEditorActivity.TOOL_CONTRAST, DsPhotoEditorActivity.TOOL_EXPOSURE, DsPhotoEditorActivity.TOOL_FILTER, DsPhotoEditorActivity.TOOL_FRAME, DsPhotoEditorActivity.TOOL_ORIENTATION, DsPhotoEditorActivity.TOOL_PIXELATE, DsPhotoEditorActivity.TOOL_ROUND, DsPhotoEditorActivity.TOOL_SATURATION, DsPhotoEditorActivity.TOOL_SHARPNESS, DsPhotoEditorActivity.TOOL_VIGNETTE, DsPhotoEditorActivity.TOOL_WARMTH))
                    fromEditingSession = true
                    startActivityForResult(dsPhotoEditorIndent, 200)
                }
            } catch (e: Exception) {}
        }
    }
}