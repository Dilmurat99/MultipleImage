package com.uyghar.multipleimage

import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView

class ImageAdapter(var uriList: ArrayList<Uri?>): RecyclerView.Adapter<ImageAdapter.ImageHolder>() {
    inner class ImageHolder(val view: View): RecyclerView.ViewHolder(view)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageHolder {
        val layoutInflater = LayoutInflater.from(parent?.context)
        val cellForRow = layoutInflater.inflate(R.layout.image_item, parent, false)
        return ImageHolder(cellForRow)
    }

    override fun onBindViewHolder(holder: ImageHolder, position: Int) {
        val imageView = holder?.view.findViewById<ImageView>(R.id.image)
        val buttonRemove = holder?.view.findViewById<Button>(R.id.buttonClose)
        val uri = uriList.get(position)
        imageView.setImageURI(uri)

    }

    override fun getItemCount(): Int {
        return uriList.size
    }
}