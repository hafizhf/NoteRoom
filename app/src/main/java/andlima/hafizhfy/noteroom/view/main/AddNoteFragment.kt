package andlima.hafizhfy.noteroom.view.main

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import andlima.hafizhfy.noteroom.R
import andlima.hafizhfy.noteroom.func.snackbarLong
import andlima.hafizhfy.noteroom.func.toast
import andlima.hafizhfy.noteroom.local.datastore.UserManager
import andlima.hafizhfy.noteroom.local.room.NoteDatabase
import andlima.hafizhfy.noteroom.local.room.notetable.Note
import android.annotation.SuppressLint
import androidx.lifecycle.asLiveData
import androidx.navigation.Navigation
import kotlinx.android.synthetic.main.fragment_add_note.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import java.text.SimpleDateFormat
import java.util.*

class AddNoteFragment : Fragment() {

    lateinit var userManager: UserManager
    private var mDb: NoteDatabase? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_add_note, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // Get something from data store
        userManager = UserManager(requireContext())

        // Get room database instance
        mDb = NoteDatabase.getInstance(requireContext())

        btn_save_new_note.setOnClickListener {
            val title = et_new_title.text.toString()
            val description = et_new_description.text.toString()

            if (title != "" && description != "") {
                addNewNote(title, description)
            } else {
                toast(requireContext(), "Please input all field")
            }
        }
    }

    private fun addNewNote(title: String, description: String) {
        GlobalScope.async {
            userManager.id.asLiveData().observe(this@AddNoteFragment, { userID ->
                val submit = mDb?.noteDao()?.addNewNote(Note(
                    null,
                    getCurrentDateTime(),
                    userID,
                    title,
                    description
                ))

                requireActivity().runOnUiThread {
                    if (submit != 0.toLong()) {
                        snackbarLong(requireView(), "New note added")
                        Navigation.findNavController(view!!)
                            .navigate(R.id.action_addNoteFragment_to_homeFragment)
                    } else {
                        toast(requireContext(), "Save new note error")
                    }
                }
            })
        }
    }

    @SuppressLint("SimpleDateFormat")
    private fun getCurrentDateTime() : String {
//        val dateFormat = SimpleDateFormat("dd MMMM, yyyy hh:mm:ss")
        val dateFormat = SimpleDateFormat("dd MMMM, yyyy")
        return dateFormat.format(Date())
    }
}