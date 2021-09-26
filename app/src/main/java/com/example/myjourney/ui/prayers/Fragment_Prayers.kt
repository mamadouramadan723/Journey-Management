package com.example.myjourney.ui.prayers

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebViewClient
import androidx.fragment.app.Fragment
import com.example.myjourney.R
import kotlinx.android.synthetic.main.fragment_prayers.*


class Fragment_Prayers : Fragment() {

    private val myurl = "https://lematin.ma/horaire-priere-rabat.html"

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

        }
    }
}