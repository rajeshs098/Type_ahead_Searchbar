package com.example.task1
import com.google.firebase.database.*

class SuggestionRepository {

    private val database: DatabaseReference = FirebaseDatabase.getInstance().reference.child("suggestions")

    fun fetchSuggestions(callback: (List<String>) -> Unit) {
        database.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val suggestions = mutableListOf<String>()
                for (childSnapshot in dataSnapshot.children) {
                    val suggestion = childSnapshot.getValue(String::class.java)
                    suggestion?.let { suggestions.add(it) }
                }
                callback(suggestions)
            }

            override fun onCancelled(databaseError: DatabaseError) {
                // Handle database error
            }
        })
    }
}
