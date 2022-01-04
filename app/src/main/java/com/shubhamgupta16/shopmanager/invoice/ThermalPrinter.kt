package com.shubhamgupta16.shopmanager.invoice

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.SharedPreferences
import android.content.pm.PackageManager
import androidx.core.content.ContextCompat
import com.dantsu.escposprinter.EscPosPrinter
import com.dantsu.escposprinter.connection.bluetooth.BluetoothPrintersConnections
import com.dantsu.escposprinter.connection.tcp.TcpConnection


class ThermalPrinter(context: Context) {
    companion object {
        private const val PREF_NAME = "3userApp"
    }

    val settings: Settings = Settings(context)

    fun getBluetoothPrinter(context: Activity, pageData: PageData): EscPosPrinter? {
        return if (ContextCompat.checkSelfPermission(
                context, Manifest.permission.BLUETOOTH
            ) != PackageManager.PERMISSION_GRANTED
        ) null else {
            EscPosPrinter(BluetoothPrintersConnections.selectFirstPaired(),pageData.printerDpi, pageData.printerWidthMM, pageData.printerNbrCharactersPerLine)
        }
    }

    fun getTCPPrinter(tcpData: TCPData, pageData: PageData): EscPosPrinter {
        return EscPosPrinter(TcpConnection(tcpData.address, tcpData.port, tcpData.timeout), pageData.printerDpi, pageData.printerWidthMM, pageData.printerNbrCharactersPerLine)
    }

    fun getSamplePrintText(): String {
        /*<img>
        ${PrinterTextParserImg.bitmapToHexadecimalString(printer, drawable)}
        </img>*/
        return """
        [C]================================
        [L]
        [C]<u><font size='big'>ORDER N°045</font></u>
        [L]
        [C]================================
        [L]
        [L]<b>BEAUTIFUL SHIRT</b>[R]9.99e
        [L]  + Size : S
        [L]
        [L]<b>AWESOME HAT</b>[R]24.99e
        [L]  + Size : 57/58
        [L]
        [C]--------------------------------
        [R]TOTAL PRICE :[R]34.98e
        [R]TAX :[R]4.23e
        [L]
        [C]================================
        [L]
        [L]<font size='tall'>Customer :</font>
        [L]Raymond DUPONT
        [L]5 rue des girafes
        [L]31547 PERPETES
        [L]Tel : +33801201456
        [L]
        [C]<barcode type='ean13' height='10'>831254784551</barcode>
        [C]<qrcode size='20'>http://www.developpeur-web.dantsu.com/</qrcode>
        """.trimIndent()
    }

    data class TCPData(val address: String, val port: Int, val timeout: Int = 30)
    data class PageData(
        val printerDpi: Int, val printerWidthMM: Float,
        val printerNbrCharactersPerLine: Int
    )

    class Settings(context: Context) {
        private var pref: SharedPreferences = context.getSharedPreferences(
            PREF_NAME,
            Context.MODE_PRIVATE
        )

        private val editor
            get() = pref.edit()

        fun setTCP(tcpData: TCPData) {
            editor.putString("address", tcpData.address)
                .putInt("port", tcpData.port)
                .putInt("timeout", tcpData.timeout)
                .apply()
        }
        fun getTCPData(): TCPData? {
            val address = pref.getString("address", null)?:return null
            val port = pref.getInt("port", -1)
            val timeout = pref.getInt("timeout", -1)
            if (port < 0 || timeout < 0) return null
            return TCPData(address, port, timeout)
        }

        fun setPaperData(pageData:PageData) {
            editor.putInt("pDpi", pageData.printerDpi)
                .putFloat("pWidthMM", pageData.printerWidthMM)
                .putInt("pCharPerLine", pageData.printerNbrCharactersPerLine)
                .apply()
        }
        fun getPageData(): PageData? {
            val pDpi = pref.getInt("pDpi", -1)
            val pWidthMM = pref.getFloat("pWidthMM", -1f)
            val pCharPerLine = pref.getInt("pCharPerLine", -1)
            if (pDpi < 0 || pWidthMM < 0 || pCharPerLine < 0) return null
            return PageData(pDpi, pWidthMM, pCharPerLine)
        }
    }
}