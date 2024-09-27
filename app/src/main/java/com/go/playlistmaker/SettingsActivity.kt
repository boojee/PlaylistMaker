package com.go.playlistmaker

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.ImageView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class SettingsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_settings)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.settings)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val buttonBackward = findViewById<ImageView>(R.id.button_back)
        val buttonShare = findViewById<ImageView>(R.id.button_share)
        val buttonSupport = findViewById<ImageView>(R.id.button_support)
        val buttonUserAgreement = findViewById<ImageView>(R.id.button_users_agreement)

        buttonBackward.setOnClickListener {
            finish()
        }

        buttonShare.setOnClickListener {
            shareCourse()
        }

        buttonSupport.setOnClickListener {
            sendEmailToSupport()
        }

        buttonUserAgreement.setOnClickListener {
            sendUserAgreement()
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