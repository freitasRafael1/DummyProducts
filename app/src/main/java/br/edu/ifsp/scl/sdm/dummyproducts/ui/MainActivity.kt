package br.edu.ifsp.scl.sdm.dummyproducts.ui

import android.content.Intent
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.ListView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import br.edu.ifsp.scl.sdm.dummyproducts.R
import com.android.volley.Request
import com.android.volley.toolbox.JsonArrayRequest


class MainActivity : AppCompatActivity() {

    private lateinit var listView: ListView
    private var photos: List<Photo> = emptyList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        listView = findViewById(R.id.lvPhotos)

        loadPhotos()
    }

    private fun loadPhotos() {
        val url = "https://jsonplaceholder.typicode.com/photos"

        val request = JsonArrayRequest(
            Request.Method.GET,
            url,
            null,
            { response ->
                val list = mutableListOf<Photo>()
                for (i in 0 until response.length()) {
                    val obj = response.getJSONObject(i)
                    val photo = Photo(
                        albumId = obj.getInt("albumId"),
                        id = obj.getInt("id"),
                        title = obj.getString("title"),
                        url = obj.getString("url"),
                        thumbnailUrl = obj.getString("thumbnailUrl")
                    )
                    list.add(photo)
                }
                photos = list
                showTitles()
            },
            { error ->
                Toast.makeText(
                    this,
                    "Erro ao carregar fotos: ${error.message}",
                    Toast.LENGTH_LONG
                ).show()
            }
        )

        MySingleton.getInstance(this).addToRequestQueue(request)
    }

    private fun showTitles() {
        val titles = photos.map { it.title }

        val adapter = ArrayAdapter(
            this,
            android.R.layout.simple_list_item_1,
            titles
        )

        listView.adapter = adapter

        listView.setOnItemClickListener { _, _, position, _ ->
            val selected = photos[position]

            val intent = Intent(this, PhotoDetailActivity::class.java).apply {
                putExtra("title", selected.title)
                putExtra("url", selected.url)
                putExtra("thumb", selected.thumbnailUrl)
            }
            startActivity(intent)
        }
    }
}
