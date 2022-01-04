package com.shubhamgupta16.shopmanager

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.print.PrintAttributes
import android.print.PrintDocumentAdapter
import android.print.PrintManager
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.shubhamgupta16.shopmanager.invoice.InvoiceMaker
import java.util.*

class PrintWebActivity : AppCompatActivity() {
    private lateinit var printWebView: WebView
    private lateinit var printButton: Button

    @SuppressLint("SetJavaScriptEnabled")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_print_web)
        val toolbar: Toolbar = findViewById(R.id.toolbar)
        toolbar.setNavigationOnClickListener { finish() }

        printWebView = findViewById(R.id.printWebView)
        printButton = findViewById(R.id.printButton)

        val list = ArrayList<InvoiceMaker.ProductInvoice>()
        list.add(InvoiceMaker.ProductInvoice("MI TV (Android) - 32 INCH", 1499.0, 1, 18))
        list.add(InvoiceMaker.ProductInvoice("Samsung M20 8GB RAM 32 GB STORAGE", 1789.0, 1, 28))
        list.add(InvoiceMaker.ProductInvoice("Phillips Mega 12W LED Bulb", 149.0, 4, 18))

        val printCode: String = InvoiceMaker.getInvoiceHtml(
            InvoiceMaker.StoreInvoice(
                "RK Electronics",
                "RK Electronics",
                "Mohansarai, Bairwan",
                "Varanasi,",
                "220118",
                "Uttar Pradesh",
                "INDIA",
                "ashishmohansarai@gmail.com",
                " IX98***********C",
                " IX98*********4",
                "+91 98898 71300",
                "+91 93365 08099",
            ),
            InvoiceMaker.OrderInvoice(
                "RKE/21-22/098",
                "18/12/2021",
                "Shubham Gupta",
                "Madhhiya, Parao, Near Mansari Mata Temple, VARANASI, 221001",
                "+91 93365 08099",
                "Mobile",
            ),
            list
        )
        printWebView.settings.javaScriptEnabled = true
        printWebView.setDesktopMode(true)
        printWebView.loadDataWithBaseURL(
            "file:///android_asset/",
            printCode,
            "text/html",
            "UTF-8",
            null
        )
        printWebView.webViewClient = object : WebViewClient() {
            override fun onPageFinished(view: WebView, url: String) {
                val printManager: PrintManager =
                    (getSystemService(Context.PRINT_SERVICE) as PrintManager?)!!
                val adapter: PrintDocumentAdapter =
                    printWebView.createPrintDocumentAdapter("something")
                printButton.setOnClickListener {
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