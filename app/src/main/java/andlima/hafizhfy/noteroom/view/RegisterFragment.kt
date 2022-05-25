package andlima.hafizhfy.noteroom.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import andlima.hafizhfy.noteroom.R
import andlima.hafizhfy.noteroom.func.hideAllPopUp
import andlima.hafizhfy.noteroom.func.showPopUp
import andlima.hafizhfy.noteroom.func.snackbarLong
import andlima.hafizhfy.noteroom.func.toast
import andlima.hafizhfy.noteroom.local.room.NoteDatabase
import andlima.hafizhfy.noteroom.local.room.usertable.User
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.Navigation
import kotlinx.android.synthetic.main.fragment_register.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class RegisterFragment : Fragment() {

    private var mDb: NoteDatabase? = null
    private var imageURI = ""

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_register, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Get room database instance
        mDb = NoteDatabase.getInstance(requireContext())

        btn_add_new_image.setOnClickListener {
            pickImageFromGallery()
        }

        btn_goto_login.setOnClickListener {
            Navigation.findNavController(view)
                .navigate(R.id.action_registerFragment_to_loginFragment)
        }

        btn_register.setOnClickListener {
            hideAllPopUp(cv_new_email_popup, cv_re_pwd_popup)

            val username = et_new_name.text.toString()
            val email = et_new_email.text.toString()
            val password = et_new_password.text.toString()
            val repassword = et_reenter_password.text.toString()

            if (username != "" && email != "" && password != "" && repassword != "") {
                registerUser(username, email, password, repassword)
            } else {
                toast(requireContext(), "Please input all field")
            }
        }

    }

    private fun registerUser(username: String, email: String, password: String, repassword: String) {
        GlobalScope.launch {
            // Get room database instance
            mDb = NoteDatabase.getInstance(requireContext())

            val registeredUser = mDb?.userDao()?.checkUser(email)

            when {
                registeredUser!!.isNotEmpty() -> {
                    requireActivity().runOnUiThread {
                        showPopUp(cv_new_email_popup, tv_new_email_popup, "Email already registered")
                    }
                }
                repassword != password -> {
                    requireActivity().runOnUiThread {
                        showPopUp(cv_re_pwd_popup, tv_re_pwd_popup, "Password not match")
                    }
                }
                else -> {
                    val register = mDb?.userDao()?.registerUser(User(
                        null,
                        username,
                        email,
                        password,
                        imageURI
                    ))

                    requireActivity().runOnUiThread {
                        if (register != 0.toLong()) {
                            snackbarLong(requireView(), "Register success")
                            Navigation.findNavController(view!!)
                                .navigate(R.id.action_registerFragment_to_loginFragment)
                        } else {
                            toast(requireContext(), "Register failed")
                        }
                    }
                }
            }
        }
    }

    private fun pickImageFromGallery() {
        val intent = Intent(Intent.ACTION_OPEN_DOCUMENT)
        intent.type = "image/*"

        startActivityForResult(intent, 100)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 100 && resultCode == AppCompatActivity.RESULT_OK) {
            iv_new_image.setImageURI(data?.data)
            getImageURI(data?.data.toString())
        }
    }

    private fun getImageURI(uri: String) {
        this.imageURI = uri
    }
}