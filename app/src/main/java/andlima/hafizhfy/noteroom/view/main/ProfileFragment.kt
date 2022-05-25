package andlima.hafizhfy.noteroom.view.main

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import andlima.hafizhfy.noteroom.R
import andlima.hafizhfy.noteroom.local.datastore.UserManager
import android.net.Uri
import androidx.lifecycle.asLiveData
import androidx.navigation.Navigation
import kotlinx.android.synthetic.main.fragment_profile.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class ProfileFragment : Fragment() {

    lateinit var userManager: UserManager

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_profile, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Get something from data store
        userManager = UserManager(requireContext())

        userManager.username.asLiveData().observe(this, { username ->
            userManager.email.asLiveData().observe(this, { email ->
                userManager.image.asLiveData().observe(this, { image ->

                    if (image != "") {
                        iv_image_detail.setImageURI(Uri.parse(image))
                    }
                    tv_username_detail.text = username
                    tv_email_detail.text = email

                })
            })
        })

        btn_logout.setOnClickListener {
            GlobalScope.launch {
                userManager.clearData()
            }
            Navigation.findNavController(view).navigate(R.id.action_profileFragment_to_loginFragment)
        }
    }
}