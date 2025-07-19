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
import com.example.playlistmaker.utils.dpToPx
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf
import java.io.File
import java.io.FileOutputStream
import java.util.UUID

class CreatePlaylistFragment : Fragment() {

    companion object {
        const val ARG_PLAYLIST_ID = "playlist_id"
        const val MODE_CREATE = 0
        const val MODE_EDIT = 1
    }

    private var _binding: FragmentCreatePlaylistBinding? = null
    private val binding get() = _binding!!

    private val viewModel: CreatePlaylistViewModel by viewModel {
        parametersOf(arguments?.getLong(ARG_PLAYLIST_ID, -1L))
    }

    private var selectedImageUri: Uri? = null
    private var source: String? = null
    private var mode = MODE_CREATE
    private var playlistId: Long = -1L

    private val galleryLauncher =
        registerForActivityResult(ActivityResultContracts.GetContent()) { uri ->
            uri?.let {
                selectedImageUri = it
                showSelectedImage(it)
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        source = arguments?.getString("source")
        playlistId = arguments?.getLong(ARG_PLAYLIST_ID, -1L) ?: -1L
        mode = if (playlistId != -1L) MODE_EDIT else MODE_CREATE
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

        setupUI()
        observeViewModel()

        if (mode == MODE_EDIT) {
            viewModel.loadPlaylist(playlistId)
        }

        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner,
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    if (hasUnsavedChanges()) {
                        showExitConfirmationDialog()
                    } else {
                        closeScreen()
                    }
                }
            }
        )
    }

    private fun setupUI() {
        binding.coverContainer.setOnClickListener { galleryLauncher.launch("image/*") }

        binding.backButton.setOnClickListener {
            if (mode == MODE_EDIT) {
                closeScreen()
            } else {
                if (hasUnsavedChanges()) showExitConfirmationDialog() else closeScreen()
            }
        }

        binding.nameEditText.doOnTextChanged { text, _, _, _ ->
            binding.createButton.isEnabled = !text.isNullOrBlank()
        }

        binding.createButton.setOnClickListener {
            val name = binding.nameEditText.text.toString()
            val desc = binding.descriptionEditText.text.toString()
            val imagePath = selectedImageUri?.let { saveImageToInternalStorage(it) }

            if (mode == MODE_EDIT) {
                viewModel.updatePlaylist(playlistId, name, desc, imagePath)
            } else {
                viewModel.createPlaylist(name, desc, imagePath)
                showCreatedToast(name)
                closeScreen()
            }
        }
    }

    private fun observeViewModel() {
        viewModel.playlist.observe(viewLifecycleOwner) { playlist ->
            binding.nameEditText.setText(playlist.name)
            binding.descriptionEditText.setText(playlist.description ?: "")

            playlist.imagePath?.let { path ->
                selectedImageUri = Uri.fromFile(File(path))
                showSelectedImage(selectedImageUri!!)
            }

            binding.createButton.text = getString(R.string.save)
        }

        viewModel.playlistUpdated.observe(viewLifecycleOwner) { updated ->
            if (updated == true) {
                closeScreen()
            }
        }
    }

    private fun showSelectedImage(uri: Uri) {
        binding.coverImageView.isVisible = true
        binding.plusIcon.isVisible = false

        Glide.with(this)
            .load(uri)
            .transform(CenterCrop(), RoundedCorners(8.dpToPx(this)))
            .into(binding.coverImageView)
    }

    private fun showCreatedToast(name: String) {
        val layout = layoutInflater.inflate(R.layout.toast_playlist_created, null)
        layout.findViewById<TextView>(R.id.toastText)
            .text = getString(R.string.playlist_created, name)

        Toast(requireContext()).apply {
            duration = Toast.LENGTH_SHORT
            view = layout
            setGravity(Gravity.FILL_HORIZONTAL or Gravity.BOTTOM, 0, 0)
        }.show()
    }

    private fun closeScreen() {
        when (source) {
            "player" -> requireActivity().finish()
            else -> findNavController().popBackStack()
        }
    }

    private fun hasUnsavedChanges(): Boolean {
        return !binding.nameEditText.text.isNullOrBlank() ||
                !binding.descriptionEditText.text.isNullOrBlank() ||
                selectedImageUri != null
    }

    private fun showExitConfirmationDialog() {
        AlertDialog.Builder(requireContext())
            .setTitle(getString(R.string.dialog_exit_title))
            .setMessage(getString(R.string.dialog_exit_message))
            .setNegativeButton(getString(R.string.dialog_exit_negative)) { dialog, _ -> dialog.dismiss() }
            .setPositiveButton(getString(R.string.dialog_exit_positive)) { _, _ -> closeScreen() }
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
