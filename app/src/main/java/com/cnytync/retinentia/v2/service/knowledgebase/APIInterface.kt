package com.cnytync.retinentia.v2.service.knowledgebase

import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import okhttp3.MultipartBody
import okhttp3.OkHttpClient
import okhttp3.Response
import okhttp3.ResponseBody
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.*
import java.util.zip.ZipOutputStream
//import com.cnytync.retinentia.v2.data.Book
import java.util.concurrent.TimeUnit

internal interface APIInterface {
    class User(val username: String, val password: String)
    class LoginResponse(val token: String, val userId: Int)
    class TokenRequest(val zaf: String, val zal: String, val zam: String)

    @Multipart
    @POST("/api/books/annotate/")
    fun annotateBook(@Part file: MultipartBody.Part?): Call<List<AnnotationItem>>

//    @Headers("Content-Type: application/json")
//    @GET("/api/books/0/{userId}")
//    fun getAllBooks(
//        @Header("Authorization") authHeader: String,
//        @Path("userId") userId: Int
//    ): Call<List<Book>>

//    @Headers("Content-Type: application/json")
//    @GET("/api/books/{bookId}/{userId}")
//    fun getBook(@Header("Authorization") authHeader: String, @Path("bookId") bookId: Long,@Path("userId") userId: Int, @Query("limit") limit: Int): Call<Book>
//
//    @Headers("Content-Type: application/json")
//    @PUT("/api/books/{bookId}/{userId}")
//    fun editBook( @Header("Authorization") authHeader: String, @Path("bookId") bookId: Long,@Path("userId") userId: Int, @Body book: Book): Call<ResponseBody>

    @GET("/api/books/download/{bookId}")
    @Streaming
    fun downloadBook( @Header("Authorization") authHeader: String, @Path("bookId") bookId: Long): Call<ResponseBody>

    @Headers("Content-Type: application/json")
    @POST("/api/user/login")
    fun login(@Body user: User): Call<LoginResponse>

    @Headers("Content-Type: application/json")
    @POST("/api/user/login/google")
    fun googleLogin(@Body user: TokenRequest): Call<LoginResponse>

    companion object {
        fun create(): APIInterface {
            val interceptor = HttpLoggingInterceptor()
            interceptor.level = HttpLoggingInterceptor.Level.BODY
            val client = OkHttpClient.Builder()
                .addInterceptor(interceptor)
                .connectTimeout(30, TimeUnit.SECONDS)
                .writeTimeout(30, TimeUnit.SECONDS)
                .readTimeout(30, TimeUnit.SECONDS)
                .build()
            val retrofit = Retrofit.Builder()
                .baseUrl("http://192.168.1.34:8001")
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .build()
            return retrofit.create(APIInterface::class.java)
        }
    }
}