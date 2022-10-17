package com.example.firebasedynamiclink

import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.example.firebasedynamiclink.databinding.ActivityMainBinding
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.dynamiclinks.PendingDynamicLinkData
import com.google.firebase.dynamiclinks.ktx.dynamicLinks
import com.google.firebase.ktx.Firebase

class MainActivity : AppCompatActivity() {
    companion object {
        private const val TAG = "MainActivity"
        private const val DEEP_LINK_URL = "https://example.com/"
    }

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        catchDynamicLink()


    }

    private fun catchDynamicLink() {
        Firebase.dynamicLinks
            .getDynamicLink(intent)
            .addOnSuccessListener(this) { pendingDynamicLinkData: PendingDynamicLinkData? ->
                // Get deep link from result (may be null if no link is found)
                var deepLink: Uri? = null
                if (pendingDynamicLinkData != null) {
                    deepLink = pendingDynamicLinkData.link
                    Log.d(TAG, "catchDynamicLink REDIRECT: ${pendingDynamicLinkData.redirectUrl}")
                    Log.d(TAG, "catchDynamicLink EXTENSIONS: ${pendingDynamicLinkData.extensions}")

                }

                // Handle the deep link. For example, open the linked
                // content, or apply promotional credit to the user's
                // account.
                // ...

                // [START_EXCLUDE]
                // Display deep link in the UI
                when {
                    deepLink != null -> {
                        Snackbar.make(findViewById(android.R.id.content),
                            "Found deep link! $deepLink",
                            Snackbar.LENGTH_LONG).show()

                        binding.linkViewReceive.text = deepLink.toString()

                        Log.d(TAG, "catchDynamicLink: $deepLink")
                        Log.d(TAG, "catchDynamicLink PARAMS: ${deepLink.queryParameterNames}")

                        extractDataFromLink(deepLink)

                    }
                    else -> {
                        Log.d(TAG, "getDynamicLink: no link found")
                    }
                }

            }
            .addOnFailureListener(this) { e -> Log.w(TAG, "getDynamicLink:onFailure", e) }

    }

    private fun extractDataFromLink(deepLink: Uri) {
        val code = deepLink.getQueryParameter("referral_code")
        binding.edtReferralCode.editText!!.setText(code)
    }


}