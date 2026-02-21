package br.edu.ifsp.scl.sdm.dummyproducts.ui

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.renderscript.ScriptGroup
import android.view.View
import android.view.ViewParent
import android.widget.Adapter
import android.widget.AdapterView
import android.widget.ImageView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import br.edu.ifsp.scl.sdm.dummyproducts.R
import br.edu.ifsp.scl.sdm.dummyproducts.adapter.ProductAdapter
import br.edu.ifsp.scl.sdm.dummyproducts.adapter.ProductImageAdapter
import br.edu.ifsp.scl.sdm.dummyproducts.databinding.ActivityMainBinding
import br.edu.ifsp.scl.sdm.dummyproducts.model.DummyJSONAPI
import br.edu.ifsp.scl.sdm.dummyproducts.model.Product
import br.edu.ifsp.scl.sdm.dummyproducts.model.ProductList
import com.android.volley.Request
import com.android.volley.toolbox.ImageRequest
import com.android.volley.toolbox.StringRequest
import com.google.gson.Gson
import com.google.gson.JsonSyntaxException
import java.io.BufferedInputStream
import java.io.IOException
import java.io.InputStreamReader
import java.net.HttpURLConnection.HTTP_OK
import java.net.URL
import java.nio.Buffer
import javax.net.ssl.HttpsURLConnection

class MainActivity : AppCompatActivity() {
    private val amb: ActivityMainBinding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }
    private val productList: MutableList<Product> = mutableListOf()
    private val productAdapter: ProductAdapter by lazy {
        ProductAdapter(
            this,
            productList
        )
    }
    private val productImagesList: MutableList<Bitmap> = mutableListOf()
    private val productImagesAdapter: ProductImageAdapter by lazy {
        ProductImageAdapter(this, productImagesList)
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(amb.root)

        setSupportActionBar(amb.mainTb.apply {
            title = getString(R.string.app_name)
        })

        amb.productsSp.apply {
            adapter = productAdapter
            onItemSelectedListener = object: AdapterView.OnItemSelectedListener {
                //quando o ususario selecionar um produto esse listener vai tratar de buscar a imagem do produto
                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
                    val size = productImagesList.size
                    productImagesList.clear()
                    productImagesAdapter.notifyItemRangeRemoved(0, size)
                    retrieveProductsImages(productList[position])
                }

                    override fun onNothingSelected(parent: AdapterView<*>?) {
                        // NSA

                }
            }
            }

        amb.productImagesRv.apply {
            layoutManager = LinearLayoutManager(this@MainActivity)
            adapter = productImagesAdapter
        }

        retrieveProducts()
    }

    private fun retrieveProducts() =
        DummyJSONAPI.ProductListRequest({productList ->
            productList.products.also {
                productAdapter.addAll(it)
            }
        },{
            Toast.makeText(this, getString(R.string.request_problem), Toast.LENGTH_SHORT).show()
        }).also {
            DummyJSONAPI.getInstance(this).addToRequestQueue(it) }


    private fun retrieveProductsImages(product: Product) =
        product.images.forEach{ imageUrl ->
            ImageRequest(imageUrl, { response ->
                productImagesList.add(response)
                productImagesAdapter.notifyItemInserted(productImagesList.lastIndex)
            }, 0, 0, ImageView.ScaleType.CENTER, Bitmap.Config.ARGB_8888, {
                Toast.makeText(this, getString(R.string.request_problem), Toast.LENGTH_SHORT).show()
            }).also{
                DummyJSONAPI.getInstance(this).addToRequestQueue(it)
            }
        }

}