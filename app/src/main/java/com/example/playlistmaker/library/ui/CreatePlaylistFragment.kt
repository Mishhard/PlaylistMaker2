package com.example.playlistmaker.library.ui

import android.net.Uri
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.core.view.isVisible
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.FragmentCreatePlaylistBinding
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.io.File
import java.io.FileOutputStream
import java.util.UUID

class CreatePlaylistFragment : Fragment() {

    private var _binding: FragmentCreatePlaylistBinding? = null
    private val binding get() = _binding!!
    private val viewModel: CreatePlaylistViewModel by viewModel()

    private var selectedImageUri: Uri? = null

    private var source: String? = null

    private val galleryLauncher =
        registerForActivityResult(ActivityResultContracts.GetContent()) { uri ->
            uri?.let {
                selectedImageUri = it
                binding.coverImageView.isVisible = true
                binding.plusIcon.isVisible = false

                Glide.with(this)
                    .load(it)
                    .transform(CenterCrop(), RoundedCorners(8))
                    .into(binding.coverImageView)
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        source = arguments?.getString("source")
            ?: activity?.intent?.getStringExtra("source")

        requireActivity().onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                if (hasUnsavedChanges()) {
                    showExitConfirmationDialog()
                } else {
                    closeScreen()
                }
            }
        })
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCreatePlaylistBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.coverContainer.setOnClickListener {
            galleryLauncher.launch("image/*")
        }

        binding.backButton.setOnClickListener {
            if (hasUnsavedChanges()) {
                showExitConfirmationDialog()
            } else {
                closeScreen()
            }
        }

        binding.nameEditText.doOnTextChanged { text, _, _, _ ->
            binding.createButton.isEnabled = !text.isNullOrBlank()
        }

        binding.createButton.setOnClickListener {
            val name = binding.nameEditText.text.toString()
            val description = binding.descriptionEditText.text.toString()
            val imagePath = selectedImageUri?.let { saveImageToInternalStorage(it) }

            viewModel.createPlaylist(name, description, imagePath)

            showCreatedToast(name)

            closeScreen()
        }
    }

    private fun showCreatedToast(name: String) {
        val layout = layoutInflater.inflate(R.layout.toast_playlist_created, null)
        val toastText = layout.findViewById<TextView>(R.id.toastText)
        toastText.text = "Плейлист $name создан"

        val toast = Toast(requireContext())
        toast.duration = Toast.LENGTH_SHORT
        toast.view = layout
        toast.setGravity(Gravity.FILL_HORIZONTAL or Gravity.BOTTOM, 0, 0)
        toast.show()
    }

    private fun closeScreen() {
        if (source == "player") {
            requireActivity().finish()
        } else {
            findNavController().popBackStack()
        }
    }

    private fun hasUnsavedChanges(): Boolean {
        return !binding.nameEditText.text.isNullOrBlank()
                || !binding.descriptionEditText.text.isNullOrBlank()
                || selectedImageUri != null
    }

    private fun showExitConfirmationDialog() {
        AlertDialog.Builder(requireContext())
            .setTitle("Завершить создание плейлиста?")
            .setMessage("Все несохраненные данные будут потеряны")
            .setNegativeButton("Отмена") { dialog, _ -> dialog.dismiss() }
            .setPositiveButton("Завершить") { _, _ -> closeScreen() }
            .show()
    }

    private fun saveImageToInternalStorage(uri: Uri): String {
        val inputStream = requireContext().contentResolver.openInputStream(uri)
        val fileName = UUID.randomUUID().toString() + ".jpg"
        val file = File(requireContext().filesDir, fileName)
        val outputStream = FileOutputStream(file)
        inputStream?.copyTo(outputStream)
        inputStream?.close()
        outputStream.close()
        return file.absolutePath
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
