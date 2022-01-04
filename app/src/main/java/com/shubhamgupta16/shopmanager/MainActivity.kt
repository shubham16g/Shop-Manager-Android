package com.shubhamgupta16.shopmanager

import android.Manifest
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.dantsu.escposprinter.EscPosPrinter
import com.shubhamgupta16.shopmanager.databinding.ActivityMainBinding
import com.shubhamgupta16.shopmanager.invoice.ThermalPrinter
import com.dantsu.escposprinter.connection.tcp.TcpConnection




class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    companion object {
        private const val PERMISSION_BLUETOOTH = 12
    }
    private lateinit var thermalPrinter: ThermalPrinter
    private var printer: EscPosPrinter?=null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        thermalPrinter = ThermalPrinter(this)

        binding.products.setOnClickListener {
            startActivity(Intent(this, ProductsActivity::class.java))
        }
        binding.sellProduct.setOnClickListener {
            startActivity(Intent(this, SellActivity::class.java))
        }

        val pageData = ThermalPrinter.PageData(203, 48f, 32)
        val tcpData  = ThermalPrinter.TCPData("192.168.1.3", 9300, 15)


        binding.demoPrint.setOnClickListener {
            Thread{
                printer = thermalPrinter.getTCPPrinter(tcpData, pageData)
                if (printer == null) {
                    Log.d("TAG", "onCreate: no printer")
                    ActivityCompat.requestPermissions(
                        this, arrayOf(Manifest.permission.BLUETOOTH), PERMISSION_BLUETOOTH
                    )
                } else{
                    Log.d("TAG", "onCreate: printing")
                    printer!!.printFormattedText(thermalPrinter.getSamplePrintText())
//                printing
                }
            }.start()

        }
    }
}