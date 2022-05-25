package andlima.hafizhfy.noteroom.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import andlima.hafizhfy.noteroom.R
import andlima.hafizhfy.noteroom.func.hideAllPopUp
import andlima.hafizhfy.noteroom.func.showPopUp
import andlima.hafizhfy.noteroom.func.toast
import andlima.hafizhfy.noteroom.local.datastore.UserManager
import andlima.hafizhfy.noteroom.local.room.NoteDatabase
import android.os.Handler
import android.os.Looper
import androidx.activity.OnBackPressedCallback
import androidx.lifecycle.asLiveData
import androidx.navigation.Navigation
import kotlinx.android.synthetic.main.fragment_login.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class LoginFragment : Fragment() {

    lateinit var userManager: UserManager
    private var mDb: NoteDatabase? = null

    // Used for double back to exit app
    private var doubleBackToExit = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_login, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Get something from data store
        userManager = UserManager(requireContext())

        // Get room database instance
        mDb = NoteDatabase.getInstance(requireContext())

        // Check if user click back button twice
        doubleBackExit()

        // Check if user logged in
        isUserLoggedIn()

        btn_goto_register.setOnClickListener {
            Navigation.findNavController(view).navigate(R.id.action_loginFragment_to_registerFragment)
        }

        btn_login.setOnClickListener {
            hideAllPopUp(cv_email_popup, cv_password_popup)

            val email = et_email.text.toString()
            val password = et_password.text.toString()

            if (email != "" && password != "") {
                loginAuth(email, password)
            } else {
                showPopUp(cv_email_popup, tv_email_popup, "Please input all field")
            }
        }
    }

    private fun loginAuth(email: String, password: String) {
        GlobalScope.launch {
            // Get room database instance
            mDb = NoteDatabase.getInstance(requireContext())

            val checkUser = mDb?.userDao()?.checkUser(email)

            when {
                checkUser!!.isEmpty() -> {
                    requireActivity().runOnUiThread {
                        showPopUp(cv_email_popup, tv_email_popup, "Email not registered")
                    }
                }
                checkUser.size > 1 -> {
                    requireActivity().runOnUiThread {
                        toast(requireContext(), "Login failed due to redundant account")
                    }
                }
                password != checkUser[0].password -> {
                    requireActivity().runOnUiThread {
                        showPopUp(cv_password_popup, tv_password_popup, "Wrong password")
                    }
                }
                else -> {
                    userManager.loginUser(
                        checkUser[0].id.toString(),
                        checkUser[0].username!!,
                        checkUser[0].email!!,
                        checkUser[0].password!!,
                        checkUser[0].image!!
                    )

                    requireActivity().runOnUiThread {
                        Navigation.findNavController(view!!)
                            .navigate(R.id.action_loginFragment_to_homeFragment)
                    }
                }
            }
        }
    }

    private fun isUserLoggedIn() {
        userManager.email.asLiveData().observe(this, { email ->
            userManager.password.asLiveData().observe(this, { password ->
                if (email != "" && password != "") {
                    Navigation.findNavController(view!!)
                        .navigate(R.id.action_loginFragment_to_homeFragment)
                }
            })
        })
    }

    // Function to exit app with double click on back button----------------------------------------
    private fun doubleBackExit() {
        activity?.onBackPressedDispatcher
            ?.addCallback(this, object : OnBackPressedCallback(true){
                override fun handleOnBackPressed() {
                    if (doubleBackToExit) {
                        activity!!.finish()
                    } else {
                        doubleBackToExit = true
                        toast(requireContext(), "Press again to exit")

                        Handler(Looper.getMainLooper()).postDelayed(Runnable {
                            kotlin.run {
                                doubleBackToExit = false
                            }
                        }, 2500)
                    }
                }
            })
    }
}