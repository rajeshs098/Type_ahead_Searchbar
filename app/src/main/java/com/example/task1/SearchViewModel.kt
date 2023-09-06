package com.example.task1

import com.google.firebase.firestore.FirebaseFirestore

class SuggestionRepository {

    private val firestore = FirebaseFirestore.getInstance()

    suspend fun searchSuggestions(query: String): List<String> {
        val suggestions = mutableListOf<String>()

        try {
            val snapshot = firestore.collection("suggestions")
                .whereGreaterThanOrEqualTo("name", query)
                .whereLessThanOrEqualTo("name", query + "\uf8ff") // This allows querying for substrings

            val queryResult = snapshot.get().await()

            for (document in queryResult.documents) {
                val suggestion = document.getString("name")
                suggestion?.let {
                    suggestions.add(it)
                }
            }
        } catch (e: Exception) {
            // Handle any exceptions here
        }

        return suggestions
    }
}
