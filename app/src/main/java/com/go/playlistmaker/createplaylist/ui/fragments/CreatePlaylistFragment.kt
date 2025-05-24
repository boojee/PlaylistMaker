package com.go.playlistmaker.createplaylist.ui.fragments

import android.annotation.SuppressLint
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.TextView
import androidx.activity.addCallback
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.go.playlistmaker.R
import com.go.playlistmaker.createplaylist.ui.CreatePlaylistViewModel
import com.go.playlistmaker.createplaylist.ui.cancelcreateplaylist.CancelCreatingPlaylistDialogFragment
import com.go.playlistmaker.databinding.FragmentCreatePlaylistBinding
import com.go.playlistmaker.playlists.data.db.Playlist
import com.google.android.material.snackbar.Snackbar
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.io.File
import java.io.IOException

class CreatePlaylistFragment : Fragment() {

    companion object {
        const val CANCEL_PLAYLIST_REQUEST_KEY = "cancel_playlist_request_key"
    }

    private var hasValidName = false
    private var hasDescriptionText = false
    private var hasImagePlaylist = false
    private var playlistName = ""
    private var playlistDescription = ""
    private var playlistImageUri: Uri? = null


    private val createPlaylistViewModel: CreatePlaylistViewModel by viewModel()
    private lateinit var binding: FragmentCreatePlaylistBinding

    private val pickImage = registerForActivityResult(ActivityResultContracts.GetContent()) { uri ->
        uri?.let {
            val savedPath = saveImageToInternalStorage(it)
            if (savedPath != null) {
                hasImagePlaylist = true
                playlistImageUri = Uri.fromFile(File(savedPath))
                binding.imageContainer.setImageURI(playlistImageUri)
                buttonChangedAction()
            } else {
                hasImagePlaylist = false
                buttonChangedAction()
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentCreatePlaylistBinding.inflate(inflater, container, false)
        return binding.root
    }

    @SuppressLint("ResourceAsColor")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        parentFragmentManager.setFragmentResultListener(
            CANCEL_PLAYLIST_REQUEST_KEY,
            viewLifecycleOwner
        ) { requestKey, bundle ->
            if (bundle.getBoolean("finish_creation")) {
                findNavController().popBackStack()
            }
        }

        textNameAndDescriptionListener()
        textNameAndDescriptionChangeFocusListener()

        binding.imageContainer.setOnClickListener {
            pickImage.launch("image/*")
        }
        binding.backButton.setOnClickListener {
            if (hasValidName || hasDescriptionText || hasImagePlaylist) {
                openBottomSheetCancelCreatingPlaylist()
            } else {
                findNavController().popBackStack()
            }
        }
        binding.createButton.setOnClickListener {
            val trimmedName = playlistName.trim()
            if (trimmedName.isNotEmpty()) {
                createPlaylistViewModel.insertPlaylist(
                    Playlist(
                        playlistName = playlistName,
                        playlistDescription = playlistDescription,
                        playlistUri = playlistImageUri.toString(),
                        playlistTrackIds = emptyList(),
                        playlistTracksCount = 0
                    )
                )
                findNavController().popBackStack()
                val text =
                    requireContext().resources.getString(R.string.playlist_created, playlistName)
                Snackbar.make(view, text, Snackbar.LENGTH_SHORT)
                    .setBackgroundTint(
                        ContextCompat.getColor(
                            requireContext(),
                            R.color.snackbar_background
                        )
                    )
                    .setTextColor(ContextCompat.getColor(requireContext(), R.color.snackbar_text))
                    .apply {
                        val snackbarView = this.view
                        val textView =
                            snackbarView.findViewById<TextView>(com.google.android.material.R.id.snackbar_text)
                        textView.textAlignment = View.TEXT_ALIGNMENT_CENTER
                    }
                    .show()
            }
        }

        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner) {
            if (hasValidName || hasDescriptionText || hasImagePlaylist) {
                openBottomSheetCancelCreatingPlaylist()
            } else {
                findNavController().popBackStack()
            }
        }
    }

    private fun saveImageToInternalStorage(uri: Uri): String? {
        return try {
            val inputStream = requireContext().contentResolver.openInputStream(uri) ?: return null
            val fileName = "playlist_${System.currentTimeMillis()}.jpg"
            val file = File(requireContext().filesDir, fileName)

            file.outputStream().use { output ->
                inputStream.use { input ->
                    input.copyTo(output)
                }
            }
            file.absolutePath
        } catch (e: IOException) {
            e.printStackTrace()
            null
        }
    }

    private fun textNameAndDescriptionListener() {
        binding.apply {
            editTextTitle.doOnTextChanged { text, _, _, count ->
                val hasText = count != 0
                val isValidName = playlistName.trim().isNotEmpty()
                playlistName = text.toString()
                hasValidName = isValidName
                buttonChangedAction()
                editTextHasFocus(hasText, editTextTitle, true)
            }
            editTextDescription.doOnTextChanged { text, _, _, count ->
                hasDescriptionText = count != 0
                playlistDescription = text.toString()
                editTextHasFocus(count != 0, editTextDescription, false)
            }
        }
    }

    private fun textNameAndDescriptionChangeFocusListener() {
        binding.apply {
            editTextTitle.setOnFocusChangeListener { _, hasFocus ->
                val hasText = playlistName.isNotEmpty()
                editTextHasFocus((hasFocus || hasText), editTextTitle, true)
            }
            editTextDescription.setOnFocusChangeListener { _, hasFocus ->
                editTextHasFocus((hasFocus || hasDescriptionText), editTextDescription, false)
            }
        }
    }

    private fun editTextHasFocus(hasFocus: Boolean, editText: EditText, isTitle: Boolean) {
        if (hasFocus) {
            editText.setBackgroundResource(R.drawable.background_text_field_outline_active)
            if (isTitle) {
                binding.editTextFieldName.isVisible = true
            } else {
                binding.editTextFieldDescription.isVisible = true
            }
        } else {
            editText.setBackgroundResource(R.drawable.background_text_field_outline_inactive)
            if (isTitle) {
                binding.editTextFieldName.isVisible = false
            } else {
                binding.editTextFieldDescription.isVisible = false
            }

        }
    }

    private fun buttonChangedAction() {
        binding.createButton.isEnabled = hasValidName
        if (hasValidName) {
            binding.createButton.setBackgroundColor(this.resources.getColor(R.color.color_royal_blue))
        } else {
            binding.createButton.setBackgroundColor(this.resources.getColor(R.color.color_silver_foil))
        }
    }

    private fun openBottomSheetCancelCreatingPlaylist() {
        val bottomSheetFragment = CancelCreatingPlaylistDialogFragment()
        bottomSheetFragment.show(
            parentFragmentManager,
            "CancelCreatingPlaylistBottomSheetTag"
        )
    }
}