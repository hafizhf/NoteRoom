package andlima.hafizhfy.noteroom.view.main

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import andlima.hafizhfy.noteroom.R
import andlima.hafizhfy.noteroom.adapter.AdapterNote
import andlima.hafizhfy.noteroom.func.toast
import andlima.hafizhfy.noteroom.local.datastore.UserManager
import andlima.hafizhfy.noteroom.local.room.NoteDatabase
import android.os.Handler
import android.os.Looper
import androidx.activity.OnBackPressedCallback
import androidx.core.os.bundleOf
import androidx.lifecycle.asLiveData
import androidx.navigation.Navigation
import androidx.recyclerview.widget.GridLayoutManager
import kotlinx.android.synthetic.main.fragment_home.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class HomeFragment : Fragment() {

    lateinit var userManager: UserManager
    private var mDb: NoteDatabase? = null

    // Used for double back to exit app
    private var doubleBackToExit = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Get something from data store
        userManager = UserManager(requireContext())

        // Get room database instance
        mDb = NoteDatabase.getInstance(requireContext())

        // Check if user click back button twice
        doubleBackExit()

        userManager.username.asLiveData().observe(this, {
            tv_username.text = it
        })

        btn_goto_profile.setOnClickListener {
            Navigation.findNavController(view).navigate(R.id.action_homeFragment_to_profileFragment)
        }

        fab_goto_add.setOnClickListener {
            Navigation.findNavController(view).navigate(R.id.action_homeFragment_to_addNoteFragment)
        }
    }

    override fun onResume() {
        super.onResume()
        getUserNote()
    }

    override fun onDestroy() {
        super.onDestroy()
        NoteDatabase.destroyInstance()
    }

    private fun getUserNote() {
        rv_note_list.layoutManager = GridLayoutManager(requireContext(), 2)

        userManager.id.asLiveData().observe(this, { userID ->
            GlobalScope.launch {
                val listData = mDb?.noteDao()?.getUserNotes(userID.toInt())

                requireActivity().runOnUiThread {
                    if (listData?.isNotEmpty()!!) {
                        nothing_handler.visibility = View.GONE
                        loading_content.visibility = View.GONE

                        val adapter = AdapterNote(listData) {
                            val selectedData = bundleOf("SELECTED_DATA" to it)
                            Navigation.findNavController(view!!)
                                .navigate(R.id.action_homeFragment_to_detailFragment)
                        }
                        rv_note_list.adapter = adapter
                    } else {
                        nothing_handler.visibility = View.VISIBLE
                        loading_content.visibility = View.GONE
                    }
                }
            }
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