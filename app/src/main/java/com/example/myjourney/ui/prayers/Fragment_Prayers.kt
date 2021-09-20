package com.example.myjourney.ui.prayers

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebViewClient
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.myjourney.R
import kotlinx.android.synthetic.main.fragment_prayers.*
import kotlinx.android.synthetic.main.fragment_prayers.myweb_view
import kotlinx.android.synthetic.main.fragment_prayers.view.*

class Fragment_Prayers : Fragment() {

    private val myurl = "http://192.168.43.254/"

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_prayers, container, false)
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        myweb_view.apply {
            loadUrl(myurl)
            //for having embedded webserver:
            webViewClient = WebViewClient()
            settings.javaScriptEnabled = true
            settings.blockNetworkImage = false
            settings.domStorageEnabled = true

            evaluateJavascript("document.body.style.background = 'blue';", null)
            evaluateJavascript("(function() { return document.getElementById('toastMessage').value; })();") { returnValue ->
                Toast.makeText(requireContext(), returnValue, Toast.LENGTH_SHORT).show()
            }
            // webView.loadUrl("javascript:(function() { document.getElementById('email_field').value = '" + email + "'; })()");
            /*evaluateJavascript("getToastMessage();") { returnValue ->
                Toast.makeText(requireContext(), returnValue, Toast.LENGTH_SHORT).show()
            }*/
            ssdfghjkl
        }
    }
}