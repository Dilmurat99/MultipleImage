package com.uyghar.multipleimage

import android.R.attr
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import com.google.android.material.bottomsheet.BottomSheetDialog
import android.R.attr.data

import android.os.Parcelable

import android.app.Activity
import android.net.Uri
import android.content.ClipData

import android.R.attr.data
import android.database.Cursor
import android.provider.DocumentsContract
import android.provider.MediaStore
import android.util.Log
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView


class MainActivity : AppCompatActivity() {
    private val ALBUM_REQUEST = 100
    private var imagesPathList = ArrayList<String>()
    private var recyclerView: RecyclerView? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        recyclerView = findViewById(R.id.recyclerView)
        recyclerView?.layoutManager = GridLayoutManager(this,2)
        val button = findViewById<Button>(R.id.buttonImageDialog)
        button.setOnClickListener {
            val intent = Intent(Intent.ACTION_PICK)
            intent.type = "image/*"
            intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true)
            startActivityForResult(intent, ALBUM_REQUEST)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            ALBUM_REQUEST -> if (resultCode === RESULT_OK) {

                if (data?.getClipData() != null) {
                    val mClipData: ClipData? = data?.getClipData()
                    val mArrayUri = ArrayList<Uri?>()
                    for (i in 0 until (mClipData?.itemCount ?: 0)) {
                        val item = mClipData?.getItemAt(i)
                        val uri = item?.uri
                        mArrayUri.add(uri)

                    }
                    recyclerView?.adapter = ImageAdapter(mArrayUri)
                    Log.i("size:", "Selected Images" + mArrayUri.size)
                }


                //Do something with the uris array
            }
            else -> super.onActivityResult(requestCode, resultCode, data)
        }
    }

    private fun getPathFromURI(uri: Uri) {
        var path: String? = uri.path // uri = any content Uri

        val databaseUri: Uri
        val selection: String?
        val selectionArgs: Array<String>?
        if (path!!.contains("/document/image:")) { // files selected from "Documents"
            databaseUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI
            selection = "_id=?"
            selectionArgs = arrayOf(DocumentsContract.getDocumentId(uri).split(":")[1])
        } else { // files selected from all other sources, especially on Samsung devices
            databaseUri = uri
            selection = null
            selectionArgs = null
        }
        try {
            val projection = arrayOf(
                MediaStore.Images.Media.DATA,
                MediaStore.Images.Media._ID,
                MediaStore.Images.Media.ORIENTATION,
                MediaStore.Images.Media.DATE_TAKEN
            ) // some example data you can query
            val cursor = contentResolver.query(
                databaseUri,
                projection, selection, selectionArgs, null
            )
            if (cursor!!.moveToFirst()) {
                val columnIndex = cursor.getColumnIndex(projection[0])
                val imagePath = cursor.getString(columnIndex)
                // Log.e("path", imagePath);
                imagesPathList.add(imagePath)
            }
            cursor.close()
        } catch (e: Exception) {
            Log.i("error", e.message, e)
        }
    }
}