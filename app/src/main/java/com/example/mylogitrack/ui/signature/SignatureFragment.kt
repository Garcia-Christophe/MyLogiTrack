package com.example.mylogitrack.ui.signature

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.mylogitrack.R
import com.example.mylogitrack.databinding.FragmentSignatureBinding
import com.github.gcacace.signaturepad.views.SignaturePad

class SignatureFragment : Fragment() {

    private var _binding: FragmentSignatureBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSignatureBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val signaturePad: SignaturePad = binding.root.findViewById(R.id.signature_pad)
        val clearBtn: Button = binding.root.findViewById(R.id.clear_btn)
        val validateBtn: Button = binding.root.findViewById(R.id.validate_btn)
        val signatureView: ImageView = binding.root.findViewById(R.id.signature_view)

        clearBtn.setOnClickListener {
            signaturePad.clear()
        }
        validateBtn.setOnClickListener {
            signatureView.setImageBitmap(signaturePad.signatureBitmap)
            signaturePad.clear()
        }

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}