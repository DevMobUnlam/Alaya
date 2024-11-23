package com.devmob.alaya.data

import android.annotation.SuppressLint
import android.util.Log
import com.devmob.alaya.data.mapper.toData
import com.devmob.alaya.data.mapper.toDomain
import com.devmob.alaya.data.mapper.toResponseFirebase
import com.devmob.alaya.domain.DailyActivityRepository
import com.devmob.alaya.domain.model.DailyActivity
import com.devmob.alaya.domain.model.FirebaseResult
import com.google.firebase.firestore.FieldValue
import kotlinx.coroutines.cancel
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await
import java.util.Date
import javax.inject.Inject

class DailyActivityRepositoryImpl @Inject constructor(
    firebaseClient: FirebaseClient
): DailyActivityRepository {

    private val auth = firebaseClient.auth
    private val db = firebaseClient.db

    override suspend fun getDailyActivities(): Flow<List<DailyActivityNetwork>?> {
        return callbackFlow {
            try{
                val currentUserEmail = auth.currentUser?.email
                if(currentUserEmail == null){
                   trySend(null)
                }else{
                    db.collection("users")
                        .document(currentUserEmail)
                        .collection("daily_activities")
                        .addSnapshotListener{ snapshot, e ->
                            if(e != null){
                                Log.w("Firebase","DailyActivitiesListenFailed")
                                trySend(null)
                            }
                            if(snapshot != null && !snapshot.isEmpty){
                                val list = snapshot.map { it.toData()}
                                trySend(list)
                            } else {
                                Log.w("Firebase", "Data not found")
                                this.trySend(emptyList())
                            }
                        }

                }
            }catch(e:Exception){
                Log.e("DailyActivityRepository", e.message?:"")
                trySend(null)
            }
            awaitClose{cancel()}
        }
    }

    @SuppressLint("SuspiciousIndentation")
    override suspend fun changeDailyActivityStatus(dailyActivity: DailyActivity, newStatus: Boolean, updatedProgress: Int): FirebaseResult = runCatching {
        auth.currentUser?.email?.let {
            val collection = db.collection("users")
                .document(it)
                .collection("daily_activities")
                .document(dailyActivity.id)
                collection.update("currentProgress", updatedProgress)
                collection.update("lastCompleted", Date())
                .await()
        }
    }.toResponseFirebase()

    override suspend fun savePostActivityComments(
        activityId: String,
        comments: String
    ): FirebaseResult = runCatching{
        auth.currentUser?.email?.let {
            db.collection("users")
                .document(it)
                .collection("daily_activities")
                .document(activityId)
                .update("comments_list", FieldValue.arrayUnion(comments))
                .await()
        }
    }.toResponseFirebase()
}