//package com.cnytync.retinentia.v2.fragment
//
//import android.Manifest
//import android.content.Intent
//import android.content.pm.PackageManager
//import android.os.Build
//import android.os.Bundle
//import android.os.Environment
//import android.util.Log
//import android.view.LayoutInflater
//import android.view.View
//import android.view.ViewGroup
//import android.widget.Toast
//import androidx.annotation.RequiresApi
//import androidx.core.app.ActivityCompat
//import androidx.fragment.app.Fragment
//import com.cnytync.retinentia.v2.MainActivity
//import com.cnytync.retinentia.v2.R
//import com.cnytync.retinentia.v2.ReaderActivity
//import com.cnytync.retinentia.v2.adapter.BookListAdapter
////import com.cnytync.retinentia.v2.data.Book
////import com.cnytync.retinentia.v2.data.Entity
//import com.cnytync.retinentia.v2.databinding.FragmentBooksMainBinding
//import com.cnytync.retinentia.v2.service.knowledgebase.APIInterface
////import com.folioreader.FolioReader
//import retrofit2.Call
//import retrofit2.Callback
//import retrofit2.Response
//import java.io.File
//import java.io.FileOutputStream
//
//// TODO: Rename parameter arguments, choose names that match
//// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
//private const val ARG_TOKEN = "token"
//private const val ARG_USER_ID = "userId"
//
///**
// * A simple [Fragment] subclass.
// * Use the [BooksFragment.newInstance] factory method to
// * create an instance of this fragment.
// */
//class BooksMainFragment : Fragment() {
//    // TODO: Rename and change types of parameters
//    private var token: String? = null
//    private var userId: Int? = null
//    private var currentBook: Book? = null
//    private var books: List<Book>? = ArrayList()
//    private var retinentiaPath: String = "data/com.cnytync.retinentia.v2/books/"
//
//    private lateinit var binding: FragmentBooksMainBinding
//
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        arguments?.let {
//            token = it.getString(ARG_TOKEN)
//            userId = it.getInt(ARG_USER_ID)
//        }
//    }
//
//    override fun onCreateView(
//        inflater: LayoutInflater, container: ViewGroup?,
//        savedInstanceState: Bundle?
//    ): View? {
//
//
//        binding = FragmentBooksMainBinding.inflate(inflater)
//
//        val apiInterface = APIInterface.create().getAllBooks("Token " + token!!, userId!!)
//
//        apiInterface.enqueue(object : Callback<List<Book>> {
//            @RequiresApi(Build.VERSION_CODES.R)
//            override fun onResponse(
//                call: Call<List<Book>>?,
//                response: Response<List<Book>>?
//            ) {
//                if (response?.errorBody() != null) {
//                    println("Books api failure")
//                } else {
//                    books = response?.body()
//
//                    binding.listBooks.isClickable = true
//                    binding.listBooks.adapter = BookListAdapter(activity!!, books!!)
//                    binding.listBooks.setOnItemClickListener { parent, view, position, id ->
//                        val book = books!![position]
//
//                        if (!book.downloaded) {
//                            if (context!!.checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE.toString()) == PackageManager.PERMISSION_GRANTED) {
//                                Log.w("BOOKS", "Downloading book " + book.name)
//                                Toast.makeText(view.context,"Downloading book...", Toast.LENGTH_SHORT).show()
//                                downloadBookAndOpen(book)
//                            } else {
//                                Log.w("BOOKS", "Asking for permission...")
//                                currentBook = book
//                                askPermissionAndOpen()
//                            }
//
//
//                        } else {
//                            Log.w("BOOKS", "Book is already downloaded, opening... -> " + book.name)
//                            Toast.makeText(view.context,"Opening book...", Toast.LENGTH_SHORT).show()
//                            openBook(book)
//                        }
//                    }
//                }
//            }
//
//            override fun onFailure(call: Call<List<Book>>?, t: Throwable?) {
//                println("Books api failure")
//                println(t.toString())
//            }
//        })
//        // Inflate the layout for this fragment
//        return binding.root
//    }
//
//    fun openBook(book: Book) {
//        Thread {
////            val response: Response<Book>  = APIInterface.create().getBook("Token " + token!!, book.id, userId!!,10000).execute()
////            var book = response.body()!!
//            book.localPath = Environment.getDataDirectory().path + "/" + retinentiaPath + book.fileName
////            book.entities = arrayListOf<Entity>()
////            val folioReader = FolioReader.get()
////            folioReader.openBook(book.localPath, book.toFolioBook(), token!!, userId!!.toLong())
//        }.start()
//    }
//    fun askPermissionAndOpen() {
//        ActivityCompat.requestPermissions(
//            activity!!,
//            arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE),
//            100
//        )
//    }
//
//    override fun onRequestPermissionsResult(
//        requestCode: Int,
//        permissions: Array<out String>,
//        grantResults: IntArray
//    ) {
//        if (requestCode == 100) {
//            if (grantResults.size > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//                Log.w("BOOKS", "Permission given, downloading book...")
//                downloadBookAndOpen(currentBook!!)
//            } else {
//                Log.w("BOOKS", "Permission denied.")
//            }
//        }
//        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
//    }
//
//    fun downloadBookAndOpen(book: Book) {
//        val filePath: String =
//            Environment.getDataDirectory().path + "/" + retinentiaPath + book.fileName
//
//        val retinentiaFolder: File =
//            File(Environment.getDataDirectory().path, retinentiaPath)
//        if (!retinentiaFolder.exists()) {
//            retinentiaFolder.mkdirs()
//        }
//        val outputFile = File(filePath)
//        Thread {
//            val response =
//                APIInterface.create().downloadBook("Token " + token!!, book!!.id).execute()
//
//            response.body()?.byteStream()?.use {
//                FileOutputStream(outputFile).use { targetOutputStream ->
//                    it.copyTo(targetOutputStream)
//                    Log.w("BOOKS", "Download completed, opening book " + book.name)
//                    book.downloaded = true
//                    APIInterface.create().editBook("Token " + token!!, book!!.id, userId!!, book!!)
//                        .execute()
//
//                    openBook(book)
//                }
//            }
//        }.start()
//    }
//
//    companion object {
//        /**
//         * Use this factory method to create a new instance of
//         * this fragment using the provided parameters.
//         *
//         * @param param1 Parameter 1.
//         * @param param2 Parameter 2.
//         * @return A new instance of fragment BooksFragment.
//         */
//        // TODO: Rename and change types and number of parameters
//        @JvmStatic
//        fun newInstance(token: String, userId: String) =
//            BooksMainFragment().apply {
//                arguments = Bundle().apply {
//                    putString(ARG_TOKEN, token)
//                    putString(ARG_USER_ID, userId)
//                }
//            }
//    }
//}