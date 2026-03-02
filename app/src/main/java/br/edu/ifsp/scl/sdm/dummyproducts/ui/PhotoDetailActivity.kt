package br.edu.ifsp.scl.sdm.dummyproducts.ui

import android.graphics.Bitmap
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import br.edu.ifsp.scl.sdm.dummyproducts.R
import com.android.volley.toolbox.ImageRequest


class PhotoDetailActivity : AppCompatActivity() {

    private lateinit var tvTitle: TextView
    private lateinit var imgPhoto: ImageView
    private lateinit var imgThumb: ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_photo_detail)

        tvTitle = findViewById(R.id.tvTitle)
        imgPhoto = findViewById(R.id.imgPhoto)
        imgThumb = findViewById(R.id.imgThumb)

        val title = intent.getStringExtra("title") ?: ""
        val url = intent.getStringExtra("url") ?: ""
        val thumb = intent.getStringExtra("thumb") ?: ""

        tvTitle.text = title

        loadImageWithVolley(url, imgPhoto)
        loadImageWithVolley(thumb, imgThumb)
    }

    private fun loadImageWithVolley(imageUrl: String, imageView: ImageView) {

        val fixedImageUrl = "${imageUrl.replace("via.placeholder.com", "placehold.co")}/FFF.png"

        val request = ImageRequest(
            fixedImageUrl,
            { bitmap ->
                imageView.setImageBitmap(bitmap)
            },
            0,
            0,
            ImageView.ScaleType.CENTER_CROP,
            Bitmap.Config.RGB_565
        ) { error ->
            Toast.makeText(
                this,
                "Erro ao carregar imagem: ${error.message}",
                Toast.LENGTH_LONG
            ).show()
        }

        MySingleton.getInstance(this).addToRequestQueue(request)
    }
}
