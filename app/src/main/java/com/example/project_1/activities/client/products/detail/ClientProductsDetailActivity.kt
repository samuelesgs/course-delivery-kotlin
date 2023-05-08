package com.example.project_1.activities.client.products.detail

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import com.denzcoskun.imageslider.ImageSlider
import com.denzcoskun.imageslider.constants.ScaleTypes
import com.denzcoskun.imageslider.models.SlideModel
import com.example.project_1.R
import com.example.project_1.models.Product
import com.example.project_1.utils.SharedPref
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class ClientProductsDetailActivity : AppCompatActivity() {

    val TAG = "CLIENT_PRODUCTS_DETAIL_ACTIVITY"

    var product:  Product ? = null
    val gson = Gson()

    var imageSlider : ImageSlider ? = null
    var textName : TextView ? = null
    var textDescription : TextView ? = null
    var textPrice : TextView ? = null
    var textQuantity : TextView? = null
    var imagePositive : ImageView ? = null
    var imageNegative : ImageView ? = null
    var buttonAdd : Button ? = null

    var quantity : Int = 1
    var productPrice : Double ? = 0.0

    var sharedPref : SharedPref? = null

    var selectedProducts = ArrayList<Product>()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_client_products_detail)
        sharedPref = SharedPref(this)

        product = gson.fromJson(intent.getStringExtra("product"), Product::class.java)
        imageSlider = findViewById(R.id.imageSlider)
        textName = findViewById(R.id.textName)
        textDescription = findViewById(R.id.textDescription)
        textPrice = findViewById(R.id.textPrice)
        textQuantity = findViewById(R.id.textQuantity)
        imagePositive = findViewById(R.id.imagePositive)
        imageNegative = findViewById(R.id.imageNegative)
        buttonAdd = findViewById(R.id.buttonAdd)

        buttonAdd?.setOnClickListener { addToBag() }

        loadData()
        getProductsFromSharedPref()
    }

    private fun addToBag() {
        val index = getIndexOf(product?.id!!)

        if (index == -1) {//NO EXISTE EN SHARED PREF
            if (product?.quantity == 0) {
                product?.quantity = 1
            }
            selectedProducts.add(product!!)
        } else { // YA existe el producto en shared pref - debemos editar la cantidad
            selectedProducts[index].quantity = quantity
        }

        selectedProducts.add(product!!)
        sharedPref?.save("order", selectedProducts)
    }

    private fun getProductsFromSharedPref() {
        if (!sharedPref?.getData("order").isNullOrBlank()) {
            val type = object : TypeToken<ArrayList<Product>>() {}.type
            selectedProducts = gson.fromJson(sharedPref?.getData("order"), type)
            val index = getIndexOf(product?.id!!)

            if (index != -1) {
                product?.quantity = selectedProducts[index].quantity
                quantity = product?.quantity!!
                textQuantity?.text = "${product?.quantity}"

                productPrice = product?.price!! * product?.quantity!!
                textPrice?.text = "${productPrice}$"
            }



            for (p in selectedProducts) {
                Log.d(TAG, "getProductsFromSharedPref: $p")
            }
        }
    }

    private fun getIndexOf(idProduct: String) : Int {
        for ((pos, p) in selectedProducts.withIndex()) {
            if (p.id == idProduct) {
                return pos
            }
        }
        return -1
    }

    private fun loadData() {
        Log.i(TAG, "loadData: ${product?.toJson()}")
        val imageList = ArrayList<SlideModel>()
        imageList.add(SlideModel(product?.image1, ScaleTypes.CENTER_CROP))
        imageList.add(SlideModel(product?.image2, ScaleTypes.CENTER_CROP))
        imageList.add(SlideModel(product?.image3, ScaleTypes.CENTER_CROP))
        imageSlider?.setImageList(imageList)
        textName?.text = product?.name
        textDescription?.text = product?.description
        val priceText = "${product?.price}$"
        textPrice?.text = priceText
        textQuantity?.text = "$quantity"
        imagePositive?.setOnClickListener { addItem() }
        imageNegative?.setOnClickListener { removeItem() }
    }

    private fun addItem() {
        quantity++
        calculate()
    }

    private fun removeItem() {
        if (quantity > 1) {
            quantity--
            calculate()
        }
    }

    private fun calculate() {
        productPrice = product?.price!! * quantity
        product?.quantity = quantity
        textQuantity?.text = "${product?.quantity}"
        textPrice?.text = "${productPrice}"
    }
}