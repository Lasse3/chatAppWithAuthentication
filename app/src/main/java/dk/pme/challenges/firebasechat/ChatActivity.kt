package dk.pme.challenges.firebasechat

import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.widget.ArrayAdapter
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.activity_main.*
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DataSnapshot


data class Message(var value: String = "", var user: String = "")

class ChatActivity : AppCompatActivity() {
    private lateinit var userEmail:String
    private val MESSAGES_REF = "messages"
    val keys = mutableListOf<String>()
    lateinit var arrayAdapter: ArrayAdapter<String>
    private lateinit var firebaseDbRef: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        recvUserFromFirebase()

        //Initialize ArrayAdapter
        arrayAdapter = ArrayAdapter(applicationContext, android.R.layout.simple_list_item_1, ArrayList<String>())
        listView.adapter = arrayAdapter

        //Get database reference
        firebaseDbRef=FirebaseDatabase.getInstance().reference

        //A Reference represents a specific location in your Database and can be used for reading
        // or writing data to that Database location. We create a child reference
        val messagesRef = firebaseDbRef.child(MESSAGES_REF)

        messagesRef.addChildEventListener(object: ChildEventListener{
            override fun onCancelled(p0: DatabaseError) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

            override fun onChildMoved(p0: DataSnapshot, p1: String?) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

            override fun onChildChanged(p0: DataSnapshot, p1: String?) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

            override fun onChildRemoved(snapshot: DataSnapshot) {
                val message = snapshot.getValue(Message::class.java)
                    arrayAdapter.remove(message?.value)
            }

            override fun onChildAdded(snapshot: DataSnapshot, p1: String?) {
                val message = snapshot.getValue(Message::class.java)

                if (message != null) {
                    keys.add(snapshot.key.toString())
                    arrayAdapter.add(message.value)}
            }
        })

        listView.setOnItemLongClickListener{  _, _, position, _->
            val alert = AlertDialog.Builder(this)
            alert.setTitle(getString(R.string.alertDeleteConfirmationTitle))
            alert.setMessage(getString(R.string.alertDeleteConfirmationText))
            alert.setPositiveButton(getString(R.string.alertSetPositiveButtonText)) { _, _->
                //Delete list entry
                firebaseDbRef.child(MESSAGES_REF).child(keys[position]).removeValue()
                keys.removeAt(position)
            }
            alert.setNegativeButton(getString(R.string.alertSetNegativeButtonText)){ _, _->
                //Do nothing
            }
            val dialog: AlertDialog = alert.create()
            dialog.show()
            true
        }
    }
    fun sendChatMessage() {
        val text = editText.text.toString()

        if (!text.isEmpty()) {
            val message = Message(text, userEmail)
            firebaseDbRef.child(MESSAGES_REF).push().setValue(message)
            editText.setText("")
        }
    }

    private fun recvUserFromFirebase(){
        val user = FirebaseAuth.getInstance().currentUser

        if (user != null) {
            userEmail = user.email.toString()
        }
    }

}






