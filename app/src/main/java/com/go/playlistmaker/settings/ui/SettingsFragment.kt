package com.go.playlistmaker.settings.ui

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.go.playlistmaker.R
import com.go.playlistmaker.databinding.FragmentSettingsBinding
import org.koin.androidx.viewmodel.ext.android.viewModel

class SettingsFragment : Fragment() {

    private val settingsViewModel by viewModel<SettingsViewModel>()
    private lateinit var binding: FragmentSettingsBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSettingsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.buttonShare.setOnClickListener { shareCourse() }
        binding.buttonSupport.setOnClickListener { sendEmailToSupport() }
        binding.buttonUsersAgreement.setOnClickListener { sendUserAgreement() }

        settingsViewModel.getThemeStateLiveData().observe(viewLifecycleOwner) { settingsState ->
            when (settingsState) {
                is SettingsState.CurrentTheme -> {
                    binding.themeSwitcher.isChecked = settingsState.isThemeDark
                }
            }
        }

        binding.themeSwitcher.setOnCheckedChangeListener { _, checked ->
            settingsViewModel.switchTheme(checked)
        }
    }

    private fun shareCourse() {
        val message = getString(R.string.share_course)
        val sendMessage = Intent(Intent.ACTION_SEND)
        sendMessage.putExtra(Intent.EXTRA_TEXT, message)
        sendMessage.type = "text/plain"
        val shareIntent = Intent.createChooser(sendMessage, null)
        startActivity(shareIntent)
    }

    private fun sendEmailToSupport() {
        val email = getString(R.string.student_email)
        val themeEmail = getString(R.string.theme_email)
        val textEmail = getString(R.string.text_email)
        val emailIntent = Intent(Intent.ACTION_SENDTO)

        emailIntent.data = Uri.parse("mailto:")
        emailIntent.putExtra(Intent.EXTRA_EMAIL, arrayOf(email))
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, themeEmail)
        emailIntent.putExtra(Intent.EXTRA_TEXT, textEmail)
        startActivity(emailIntent)
    }

    private fun sendUserAgreement() {
        val urlUserAgreement = getString(R.string.url_users_agreement)
        val userAgreement = Intent(Intent.ACTION_VIEW, Uri.parse(urlUserAgreement))
        startActivity(userAgreement)
    }
}