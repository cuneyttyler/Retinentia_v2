//package com.cnytync.retinentia.v2.adapter
//
//import android.app.Activity
//import android.view.LayoutInflater
//import android.view.View
//import android.view.ViewGroup
//import android.widget.ArrayAdapter
//import android.widget.ImageView
//import android.widget.TextView
//import com.cnytync.retinentia.v2.R
//import com.cnytync.retinentia.v2.data.Book
//
//class BookListAdapter(private val context: Activity, private val arrayList:List<Book>):  ArrayAdapter<Book>(context,
//    R.layout.books_list_item,arrayList){
//
//    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
//        val inflater: LayoutInflater = LayoutInflater.from(context)
//        val view: View = inflater.inflate(R.layout.books_list_item ,null)
//
//        val imageView: ImageView = view.findViewById(R.id.book_image)
//        val titleView: TextView = view.findViewById(R.id.book_title)
//
//        titleView.text = arrayList[position].name
//
//        return view
//    }
//}