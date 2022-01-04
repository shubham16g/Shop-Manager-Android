package com.shubhamgupta16.shopmanager.invoice

import java.text.MessageFormat
import java.text.NumberFormat
import java.util.*
import com.ibm.icu.text.RuleBasedNumberFormat




object InvoiceMaker {
    data class StoreInvoice(
        val name: String,
        val fullName: String,
        val address: String,
        val city: String,
        val pincode: String,
        val state: String,
        val country: String,
        val email: String,
        val gst: String,
        val pan: String,
        val phone1: String,
        val phone2: String?,
    )

    data class OrderInvoice(
        val invoiceId: String,
        val date: String,
        val customerName: String,
        val customerAddress: String,
        val customerContact: String,
        val customerContactType: String
    )

    data class ProductInvoice(
        val name: String,
        val amount: Double,
        val quantity: Int,
        val gst: Int,
    )

    fun getInvoiceHtml(
        store: StoreInvoice,
        order: OrderInvoice,
        list: List<ProductInvoice>,
        country: String = "in"
    ): String {
        val locale = Locale("en", country)
        val pf = NumberFormat.getCurrencyInstance(locale)
        val nf = RuleBasedNumberFormat(locale, RuleBasedNumberFormat.SPELLOUT)
        val code = StringBuilder()
        code.append(header)
        code.append(
            """
        <body>
	<div class="invoice">
		<div class="row">
			<span class="store-name bold fs-xxl mt-5">${store.name}</span>
			<span class="invoice bold text-colored fs-xxxl ls-5">INVOICE</span>
		</div>
		<div class="row mt-20">
			<div class="lh-20 max-w-300">
				<div>${store.address},<br>${store.city} - ${store.pincode}, ${store.state}, ${store.country}</div>
				<div><span class="bold text-colored">Phone:</span> ${store.phone1} ${if (store.phone2 != null) ", ${store.phone2}" else ""}</div>
				<div><span class="bold text-colored">Email:</span> ${store.email}</div><br>
				<div><span class="bold text-colored fs-l">GST Reg No.:</span> <span class="fs-l">${store.gst}</span>
				</div>
			</div>
			<div class="right-content lh-20">
				<span class="bold text-colored fs-l">INVOICE #</span> <span class="fs-l">${order.invoiceId}</span><br>
				<span class="bold text-colored">DATED</span> <span>${order.invoiceId}</span><br><br>
				<span class="bold text-colored fs-xl">Bill To</span><br>
				<span>${order.customerName}</span><br>
				<span class="max-w-300">${order.customerAddress}</span><br>
				<span><span class="bold">${order.customerContactType}:</span> ${order.customerContact}</span><br>
			</div>
		</div>

		<table class="mt-20" width="100%">
			<!-- <tfoot style="border: none;">
				<tr>
					<td colspan="5">${store.fullName}</td>
				</tr>
			</tfoot> -->
			<thead>
                <tr>
					<th class="outliner center" style="width: 15px;">S.No</th>
					<th class="outliner left" width="40%">Product Name</th>
					<th class="outliner right">Rate</th>
					<th class="outliner center">Qty</th>
					<th class="outliner right">Amount</th>
					<th class="outliner center">CGST</th>
					<th class="outliner center">SGST</th>
					<th class="outliner right">Total</th>
				</tr>
			</thead>
            """.trimIndent()
        )
        var total: Double = 0.0
        for (i in list.indices) {
            val product = list[i]
            val gstAmount = product.amount * product.gst / 100
            val rate = product.amount - gstAmount
            total += product.amount * product.quantity
            code.append(
                """
			<tr>
				<td class="outliner center">${i + 1}</td>
				<td class="outliner left">${product.name}</td>
				<td class="outliner right">${rate.currency(pf)}</td>
				<td class="outliner center">${product.quantity}</td>
				<td class="outliner right">${(rate * product.quantity).currency(pf)}</td>
				<td class="outliner center">${(product.quantity * gstAmount / 2).currency(pf)}</td>
				<td class="outliner center">${(product.quantity * gstAmount / 2).currency(pf)}</td>
				<td class="outliner right">${(product.quantity * product.amount).currency(pf)}</td>
			</tr>
            """.trimIndent()
            )
        }
        code.append(
            """
			<tr>
				<td colspan="7" class="outliner left bold">TOTAL</td>
				<td class="outliner right bold focus">${total.currency(pf)}</td>
			</tr>
			<tr>
				<td colspan="8" class="outliner">
                    <span class="fs-xs">Amount In Words:</span><br>
                    <span class="bold">${nf.format(total)}</span>
                </td>
			</tr>
		</table>

		<div class="row mt-20">
			<div class=" max-w-500">
				<span class="bold text-colored">Delcleration</span>
				<div class="fs-xs">We declare that this invoice shows the actual price of
				the goods described and that all particulars are true and
				correct. There is no flow of additional consideration
				directly or indirectly from the buyers.</div>
			<div class="fs-xs mt-3">ALL DISPUTES SUBJECT TO ${store.city.uppercase()} JURISDICTION ONLY</div>

			</div>
			<div class="right-content lh-20">
				<span>For ${store.fullName}</span><br><br>
				<span class="fs-s">Authorised Signatory</span><br>
			</div>
		</div>
		<hr>
		<div class="row">
			<span class="fs-s">Thank you for your business.</span><br>
			<span class="text-colored fs-xs">This is a Computer Generated Invoice</span>
		</div>
	</div>
</body>

</html>
            """.trimIndent()
        )
        return code.toString()
    }
    private val header = """
        <!DOCTYPE html>
<html>

<head>
	<title></title>
	<style type="text/css">

		@font-face {
			font-family: 'Poppins';
			src: url('poppins_regular.woff') format('woff'),
				url('poppins_regular.ttf') format('truetype');
		}

		body {
			font-size: 15px;
			font-family: 'Poppins', Calibri, 'Trebuchet MS', sans-serif;
		}
		
		.invoice{
			max-width: 720px;
			margin: 0 auto;
		}
		table {
			border-collapse: collapse;
			margin-top: 5px;
			page-break-inside: auto;
		}

		div {
			page-break-inside: avoid;
		}

		/* This is the key */
		thead {
			display: table-header-group
		}

		tfoot {
			display: table-footer-group
		}

		th, .focus {
			background-color: rgb(227, 238, 255);
		}

		hr{
			border: none;
			border-top: 1px solid #579eee;
		}
		.outliner {
			border: 1px solid #579eee;
			padding: 2px;
		}

		.left {
			text-align: left;
		}

		.right {
			text-align: right;
		}

		.center {
			text-align: center;
		}

		.row {
			display: flex;
			justify-content: space-between;
		}

		.mt-20 {
			margin-top: 20px;
		}

		.ls-5 {
			letter-spacing: 5px;
			margin-right: -5px;
		}

		.lh-20 {
			line-height: 24px;
		}

		.bold {
			font-weight: bold;
		}


		.text-colored {
			color: #2360a6;
		}

		.fs-xl {
			font-size: 130%;
		}

		.fs-l {
			font-size: 115%;
		}
		.fs-xs{
			font-size: 75%;
		}
		.fs-s{
			font-size: 88%;
		}

		.fs-xxl {
			font-size: 190%;
		}
		.fs-xxxl {
			font-size: 220%;
		}

		.right-content {
			text-align: right;

		}

		.max-w-300 {
			max-width: 300px;
		}
		.max-w-500 {
			max-width: 500px;
		}

		span {
			display: inline-block;
		}

		.mt-5 {
			margin-top: 5px;
		}
		.mt-3 {
			margin-top: 3px;
		}
	</style>
	<script src="JsBarcode.all.min.js"></script>

</head>
    """.trimIndent()
    private fun Number.currency(pf: NumberFormat) = pf.format(this)
}