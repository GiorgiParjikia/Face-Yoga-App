package ru.netology.faceyoga.data.media

import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FirebaseVideoUrlResolver @Inject constructor() : VideoUrlResolver {

    override suspend fun resolve(gsOrHttp: String): String {

        if (gsOrHttp.startsWith("http")) return gsOrHttp

        // gs:// → downloadUrl
        val ref = Firebase.storage.getReferenceFromUrl(gsOrHttp)
        return ref.downloadUrl.await().toString()
    }
}
