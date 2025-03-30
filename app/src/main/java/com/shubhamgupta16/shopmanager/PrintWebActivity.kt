package com.shubhamgupta16.shopmanager

import android.annotation.SuppressLint
import android.content.Context
import android.os.Build
import android.os.Bundle
import android.print.PrintAttributes
import android.print.PrintDocumentAdapter
import android.print.PrintManager
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.updatePadding
import com.shubhamgupta16.shopmanager.databinding.ActivityPrintWebBinding
import com.shubhamgupta16.shopmanager.invoice.InvoiceMaker
import java.util.*

class PrintWebActivity : AppCompatActivity() {
    private lateinit var binding: ActivityPrintWebBinding

    @SuppressLint("SetJavaScriptEnabled")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityPrintWebBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(binding.toolbar) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.updatePadding(top = systemBars.top)
            insets
        }
        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.updatePadding(
                bottom = systemBars.bottom, left = systemBars.left, right = systemBars.right
            )
            insets
        }
        binding.toolbar1.setNavigationOnClickListener {
            onBackPressed()
        }
        val list = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            intent.extras?.getParcelableArrayList("list", InvoiceMaker.ProductInvoice::class.java)
                ?: ArrayList()
        } else {
            @Suppress("DEPRECATION")
            intent.extras?.getParcelableArrayList("list") ?: ArrayList()
        }


//        list.add(InvoiceMaker.ProductInvoice("MI TV (Android) - 32 INCH", 1499.0, 1, 18.0))
//        list.add(InvoiceMaker.ProductInvoice("Samsung M20 8GB RAM 32 GB STORAGE", 1789.0, 1, 28.0))
//        list.add(InvoiceMaker.ProductInvoice("Phillips Mega 12W LED Bulb", 149.0, 4, 18.0))

        val printCode: String = InvoiceMaker.getInvoiceHtml(
            InvoiceMaker.StoreInvoice(
                "Sore Name",
                "Store Full Name",
                "Address Line 1",
                "Store City",
                "400300",
                "State",
                "Country",
                "storemail@gmail.com",
                " IX98***********C",
                " IX98*********4",
                "+1 11119 11119",
                "+91 22229 22229",
            ),
            InvoiceMaker.OrderInvoice(
                "RKE/21-22/098",
                "18/12/2021",
                "Shubham Gupta",
                "Address Line 1, City, State, Country",
                "+91 99999 99999",
                "Mobile",
            ),
            list
        )
        binding.printWebView.settings.javaScriptEnabled = true
        binding.printWebView.setDesktopMode(true)
        binding.printWebView.loadDataWithBaseURL(
            "file:///android_asset/",
            printCode,
            "text/html",
            "UTF-8",
            null
        )
        binding.printWebView.webViewClient = object : WebViewClient() {
            override fun onPageFinished(view: WebView, url: String) {
                val printManager: PrintManager =
                    (getSystemService(Context.PRINT_SERVICE) as PrintManager?)!!
                val adapter: PrintDocumentAdapter =
                    binding.printWebView.createPrintDocumentAdapter("something")
                binding.printButton.setOnClickListener {
                    printManager.print(
                        "print",
                        adapter,
                        PrintAttributes.Builder().build()
                    )
                }
            }
        }
    }

    private fun WebView.setDesktopMode(enabled: Boolean) {
        var newUserAgent: String? = settings.userAgentString
        if (enabled) {
            try {
                val ua: String = settings.userAgentString
                val androidOSString: String = settings.userAgentString
                    .substring(ua.indexOf("("), ua.indexOf(")") + 1)
                newUserAgent = settings.userAgentString
                    .replace(androidOSString, "(X11; Linux x86_64)")
            } catch (e: Exception) {
                e.printStackTrace()
            }
        } else {
            newUserAgent = null
        }
        settings.setUserAgentString(newUserAgent)
        settings.useWideViewPort = enabled
        settings.loadWithOverviewMode = enabled
        reload()
    }
}