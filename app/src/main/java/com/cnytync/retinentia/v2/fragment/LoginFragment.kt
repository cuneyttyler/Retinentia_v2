package com.cnytync.retinentia.v2.fragment

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.cnytync.retinentia.v2.MainActivity
import com.cnytync.retinentia.v2.R
import com.cnytync.retinentia.v2.service.knowledgebase.APIInterface
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.SignInButton
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [LoginFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class LoginFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    private var mGoogleSignInClient: GoogleSignInClient? = null
    private var RC_SIGN_IN: Int = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }

        // Configure sign-in to request the user's ID, email address, and basic
// profile. ID and basic profile are included in DEFAULT_SIGN_IN.
        // Configure sign-in to request the user's ID, email address, and basic
// profile. ID and basic profile are included in DEFAULT_SIGN_IN.
        val gso: GoogleSignInOptions = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestEmail()
            .build()

        mGoogleSignInClient = GoogleSignIn.getClient(this.activity!!, gso);

    }

    fun precedeToMainPage(token: String?, userId:Int?) {
        val myIntent = Intent(
            view!!.context,
            MainActivity::class.java
        )
        myIntent.putExtra("token", token) //Optional parameters
        myIntent.putExtra("userId", userId) //Optional parameters

        view!!.context.startActivity(myIntent)
    }
    override fun onStart() {
        super.onStart()
        // Check for existing Google Sign In account, if the user is already signed in
        // the GoogleSignInAccount will be non-null.
        val account = GoogleSignIn.getLastSignedInAccount(this.activity!!)
        if(account != null)
            getTokenAndPrecede(account!!)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        var view = inflater.inflate(R.layout.fragment_login, container, false)
        var username: EditText = view.findViewById(R.id.username)
        var password: EditText = view.findViewById(R.id.password)

        // Just precede
        precede()

        var loginButton: Button = view.findViewById(R.id.button_login)
        loginButton.setOnClickListener {
            val apiInterface = APIInterface.create().login(
                APIInterface.User(
                    username.getText().toString(),
                    password.getText().toString()
                )
            )

            apiInterface.enqueue(object : Callback<APIInterface.LoginResponse> {
                override fun onResponse(
                    call: Call<APIInterface.LoginResponse>?,
                    response: Response<APIInterface.LoginResponse>?
                ) {
                    if(response?.errorBody() != null) {
                        print("Authentication failure")
                    } else {
                        println("Login Successful")
                        val token = response?.body()?.token
                        val userId = response?.body()?.userId
                        precedeToMainPage(token, userId)
                    }
                }

                override fun onFailure(call: Call<APIInterface.LoginResponse>?, t: Throwable?) {
                    print("Authentication failure")
                    println(t.toString())
                }
            })
        }

        var googleSignInButton: SignInButton = view.findViewById<SignInButton?>(R.id.button_google_login)
        googleSignInButton.setOnClickListener {
            precede()
//            googleSignIn()
        }

        return view
    }

    fun googleSignIn() {
        var signInIntent: Intent = mGoogleSignInClient!!.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        // Result returned from launching the Intent from GoogleSignInClient.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            // The Task returned from this call is always completed, no need to attach
            // a listener.
            val task: Task<GoogleSignInAccount> = GoogleSignIn.getSignedInAccountFromIntent(data)
            handleSignInResult(task)
        }
    }

    private fun handleSignInResult(completedTask: Task<GoogleSignInAccount>) {
        try {
            val account = completedTask.getResult(ApiException::class.java)

            getTokenAndPrecede(account)
        } catch (e: ApiException) {
            // The ApiException status code indicates the detailed failure reason.
            // Please refer to the GoogleSignInStatusCodes class reference for more information.
            Log.w("GSI", "signInResult:failed code=" + e.statusCode)
            view!!.findViewById<TextView>(R.id.signin_result).setText("Login failed.")
        }
    }

    fun getTokenAndPrecede(account: GoogleSignInAccount) {
        // Signed in successfully, show authenticated UI.
        val request = APIInterface.TokenRequest(account.email!!, account.givenName!!, account.familyName!!)
        val apiInterface =  APIInterface.create().googleLogin(request)

        apiInterface.enqueue(object : Callback<APIInterface.LoginResponse> {
            override fun onResponse(
                call: Call<APIInterface.LoginResponse>?,
                response: Response<APIInterface.LoginResponse>?
            ) {
                if(response?.errorBody() != null) {
                    print("Authentication failure")
                } else {
                    println("Login Successful")
                    val token = response?.body()?.token
                    val userId = response?.body()?.userId
                    precedeToMainPage(token,userId)
                }
            }

            override fun onFailure(call: Call<APIInterface.LoginResponse>?, t: Throwable?) {
                print("Authentication failure")
                println(t.toString())
                view!!.findViewById<TextView>(R.id.signin_result).setText("Login failed.")
            }
        })
    }

    fun precede() {
        // Signed in successfully, show authenticated UI.
        val request = APIInterface.TokenRequest("cuneyttyler@gmail.com", "Cuneyt", "Tyler")
        val apiInterface =  APIInterface.create().googleLogin(request)

        apiInterface.enqueue(object : Callback<APIInterface.LoginResponse> {
            override fun onResponse(
                call: Call<APIInterface.LoginResponse>?,
                response: Response<APIInterface.LoginResponse>?
            ) {
                if(response?.errorBody() != null) {
                    print("Authentication failure")
                } else {
                    println("Login Successful")
                    val token = response?.body()?.token
                    val userId = response?.body()?.userId
                    precedeToMainPage(token,userId)
                }
            }

            override fun onFailure(call: Call<APIInterface.LoginResponse>?, t: Throwable?) {
                print("Authentication failure")
                println(t.toString())
                view!!.findViewById<TextView>(R.id.signin_result).setText("Login failed.")
            }
        })
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment LoginFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            LoginFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}