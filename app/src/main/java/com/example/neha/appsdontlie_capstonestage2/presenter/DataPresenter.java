package com.example.neha.appsdontlie_capstonestage2.presenter;

import android.app.Activity;
import android.content.Intent;
import android.content.IntentSender;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.widget.Toast;

import com.example.neha.appsdontlie_capstonestage2.HomeFragment;
import com.example.neha.appsdontlie_capstonestage2.MainActivity;
import com.example.neha.appsdontlie_capstonestage2.R;
import com.example.neha.appsdontlie_capstonestage2.data.MyProfileData;
import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.Scopes;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.Scope;
import com.google.android.gms.fitness.Fitness;
import com.google.android.gms.fitness.data.DataSet;
import com.google.android.gms.fitness.data.DataType;
import com.google.android.gms.fitness.data.Field;
import com.google.android.gms.fitness.result.DailyTotalResult;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.Arrays;
import java.util.concurrent.Executor;
import java.util.concurrent.TimeUnit;

import static android.app.Activity.RESULT_CANCELED;
import static android.app.Activity.RESULT_OK;
import static android.content.ContentValues.TAG;

/**
 * Created by neha on 5/3/17.
 */

public class DataPresenter {

    private Activity activity;
    private static final int REQUEST_OAUTH = 1;
    private static final String AUTH_PENDING = "auth_state_pending";
    private boolean authInProgress = false;
    private GoogleApiClient mApiClient;
    private GoogleApiClient mClient;
    private FirebaseDatabase mFirebaseDb;
    private DatabaseReference  mDbUserRefernce;
    private ChildEventListener mChildEventListener;
    private FirebaseAuth mFirebaseAuth;
    private FirebaseStorage mFirebaseStorage;
    private StorageReference mStorageRefernce;
    private Fragment fragment;
    private FirebaseAuth.AuthStateListener mAuthListener;
    public static final int RC_SIGN_IN = 1;
    private static final int RC_PHOTO_PICKER = 2;
    private boolean newUser = true;


    private  String steps, calories, userKey;
    private MyProfileData  readProfileData = new MyProfileData(), profileData = new MyProfileData();

    public DataPresenter(Activity mView){

           activity = mView;

    }

    public DataPresenter(Fragment mfrag){

        fragment = mfrag;
    }

    public void initAuthListener(){

        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {

                FirebaseUser user = firebaseAuth.getCurrentUser();
                if(user !=null){

                     onSigninInitialize(user);
                    Toast.makeText(activity,"You are in my app",Toast.LENGTH_SHORT).show();

                }
                else{
                     onSignedOutCleanup();
                    activity.startActivityForResult(AuthUI.getInstance()
                            .createSignInIntentBuilder()
                            .setTheme(R.style.MyNewLogin)
                            .setIsSmartLockEnabled(false)
                            .setProviders(Arrays.asList(new AuthUI.IdpConfig.Builder(AuthUI.EMAIL_PROVIDER).build(),
                                    new AuthUI.IdpConfig.Builder(AuthUI.GOOGLE_PROVIDER).build())).build(),RC_SIGN_IN);


                }

               mCreateFitnessClientforSteps();
            }
        };

    }


    public void onResumeEvent(){

        mFirebaseAuth.addAuthStateListener(mAuthListener);

    }

    public void onPauseEvent(){
        if (mAuthListener != null) {
            mFirebaseAuth.removeAuthStateListener(mAuthListener);
        }
        detachDatabaseReadListener();

    }

    public void initFirebase(){

        mFirebaseDb = FirebaseDatabase.getInstance();
        mFirebaseAuth = FirebaseAuth.getInstance();
        mFirebaseStorage = FirebaseStorage.getInstance("gs://capstone-project-20ec5.appspot.com/");
        mDbUserRefernce = mFirebaseDb.getReference().child("data");
        mStorageRefernce = mFirebaseStorage.getReference().child("photos");


    }

    public void callChildListener(){
        if (mChildEventListener == null) {
            mChildEventListener = new ChildEventListener() {
                @Override
                public void onChildAdded(DataSnapshot dataSnapshot, String s) {


                }

                @Override
                public void onChildChanged(DataSnapshot dataSnapshot, String s) {


                }

                @Override
                public void onChildRemoved(DataSnapshot dataSnapshot) {

                }

                @Override
                public void onChildMoved(DataSnapshot dataSnapshot, String s) {

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            };
            mDbUserRefernce.addChildEventListener(mChildEventListener);

        }

    }


    public void mCreateFitnessClientforSteps() {

        // Create the Google API Client
        mClient = new GoogleApiClient.Builder(activity)
                .addApi(Fitness.HISTORY_API)
                .addApi(Fitness.CONFIG_API)
                .addScope(new Scope(Scopes.FITNESS_ACTIVITY_READ))
                .useDefaultAccount()
                .addConnectionCallbacks(
                        new GoogleApiClient.ConnectionCallbacks() {

                            @Override
                            public void onConnected(Bundle bundle) {


                                //Async To fetch steps
                                new FetchStepsAsync().execute();
                                new FetchCalorieAsync().execute();


                            }

                            @Override
                            public void onConnectionSuspended(int i) {
                                // If your connection to the sensor gets lost at some point,
                                // you'll be able to determine the reason and react to it here.
                                if (i == GoogleApiClient.ConnectionCallbacks.CAUSE_NETWORK_LOST) {
                                    Log.i(TAG, "Connection lost.  Cause: Network Lost.");
                                } else if (i == GoogleApiClient.ConnectionCallbacks.CAUSE_SERVICE_DISCONNECTED) {
                                    Log.i(TAG, "Connection lost.  Reason: Service Disconnected");
                                }
                            }
                        }
                ).addOnConnectionFailedListener(
                        new GoogleApiClient.OnConnectionFailedListener() {
                            // Called whenever the API client fails to connect.
                            @Override
                            public void onConnectionFailed(ConnectionResult result) {
                                Log.i(TAG, "Connection failed. Cause: " + result.toString());
                                if (!result.hasResolution()) {
                                    // Show the localized error dialog
                                    GooglePlayServicesUtil.getErrorDialog(result.getErrorCode(),
                                            activity, 0).show();
                                    return;
                                }
                                // The failure has a resolution. Resolve it.
                                // Called typically when the app is not yet authorized, and an
                                // authorization dialog is displayed to the user.
                                if (!authInProgress) {
                                    try {
                                        Log.i(TAG, "Attempting to resolve failed connection");
                                        authInProgress = true;
                                        result.startResolutionForResult(activity, REQUEST_OAUTH);
                                    } catch (IntentSender.SendIntentException e) {
                                        Log.e(TAG,
                                                "Exception while starting resolution activity", e);
                                    }
                                }
                            }
                        }
                ).build();
        mClient.connect();
    }

    private class FetchStepsAsync extends AsyncTask<Object, Object, Long> {
        protected Long doInBackground(Object... params) {
            long total = 0;
            PendingResult<DailyTotalResult> result = Fitness.HistoryApi.readDailyTotal(mClient, DataType.TYPE_STEP_COUNT_DELTA);
            DailyTotalResult totalResult = result.await(30, TimeUnit.SECONDS);
            if (totalResult.getStatus().isSuccess()) {
                DataSet totalSet = totalResult.getTotal();
                if (totalSet != null) {
                    total = totalSet.isEmpty()
                            ? 0
                            : totalSet.getDataPoints().get(0).getValue(Field.FIELD_STEPS).asInt();
                }
            } else {
                Log.w(TAG, "There was a problem getting the step count.");
            }
            return total;
        }


        @Override
        protected void onPostExecute(Long aLong) {
            super.onPostExecute(aLong);
            profileData.setSteps(aLong.toString());

            //Total steps covered for that day
            Log.i(TAG, "Total steps: " + aLong);

        }
    }

    private class FetchCalorieAsync extends AsyncTask<Object, Object, Long> {

        protected Long doInBackground(Object... params) {
            long total = 0;
            PendingResult<DailyTotalResult> result = Fitness.HistoryApi.readDailyTotal(mClient, DataType. TYPE_CALORIES_EXPENDED);
            DailyTotalResult totalResult = result.await(30, TimeUnit.SECONDS);
            if (totalResult.getStatus().isSuccess()) {
                DataSet totalSet = totalResult.getTotal();
                if (totalSet != null) {
                    total = totalSet.isEmpty()
                            ? 0
                            : (long) totalSet.getDataPoints().get(0).getValue(Field.FIELD_CALORIES).asFloat();
                }
            } else {
                Log.w(TAG, "There was a problem getting the calories.");
            }
            return total;
        }


        @Override
        protected void onPostExecute(Long aLong) {
            super.onPostExecute(aLong);
            profileData.setCalories(aLong.toString());


            //Total calories burned for that day
            Log.i(TAG, "Total calories: " + aLong);

        }


    }

public void setData(MyProfileData settingsData){

    mDbUserRefernce.child("FirstName").setValue(settingsData.getName());
    mDbUserRefernce.child("Lastname").setValue(settingsData.getLastName());
    mDbUserRefernce.child("Weight").setValue(settingsData.getWeight());
    mDbUserRefernce.child("Height").setValue(settingsData.getHeight());
    mDbUserRefernce.child("gender").setValue(settingsData.getGender());


}

public void uploadProfilePhoto(final Fragment frag, Intent data){

    Uri selectedImageUri = data.getData();

    // Get a reference to store file at chat_photos/<FILENAME>
    final StorageReference photoRef = mStorageRefernce.child(selectedImageUri.getLastPathSegment());

    // Upload file to Firebase Storage
        //   photoRef.putFile(selectedImageUri);


    UploadTask uploadTask =  photoRef.putFile(selectedImageUri);
    uploadTask.addOnFailureListener(new OnFailureListener() {
        @Override
        public void onFailure(@NonNull Exception exception) {
            // Handle unsuccessful uploads
        }
    }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
        @Override
        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
            Uri downloadUrl = taskSnapshot.getDownloadUrl();
            Toast.makeText(activity,"uploaded the data to " + downloadUrl,Toast.LENGTH_SHORT).show();
            String newPhotoUrl = downloadUrl.toString();

            if(newUser){
                profileData.setOldPhotoUrl(newPhotoUrl);
                profileData.setNewPhotoUrl(newPhotoUrl);
                newUser = false;

            }

            else {
                String oldphotoUrl = profileData.getNewPhotoUrl();
                profileData.setOldPhotoUrl(oldphotoUrl);
                profileData.setNewPhotoUrl(newPhotoUrl);

            }

            ((HomeFragment)frag).setPhoto(profileData.getNewPhotoUrl());


        }
    });

}


   public void photoPicker(Fragment frag,Intent intent){


    intent.setType("image/jpeg");
    intent.putExtra(Intent.EXTRA_LOCAL_ONLY, true);
    frag.startActivityForResult(Intent.createChooser(intent, "Complete action using"), RC_PHOTO_PICKER);

}

    public void onActivityResult(Fragment frag,int requestCode, int resultCode, Intent data) {

       // frag.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RC_SIGN_IN) {
            if (resultCode == RESULT_OK) {
                // Sign-in succeeded, set up the UI
                Toast.makeText(activity, "Signed in!", Toast.LENGTH_SHORT).show();
            } else if (resultCode == RESULT_CANCELED) {
                // Sign in was canceled by the user, finish the activity
                Toast.makeText(activity, "Sign in canceled", Toast.LENGTH_SHORT).show();
                // finish();
            }
        }
            else if (requestCode == RC_PHOTO_PICKER && resultCode == RESULT_OK) {
                uploadProfilePhoto(frag,data);
            }

    }
    public MyProfileData loadData(){

            return profileData;

    }

  public void onSigninInitialize(FirebaseUser user){

      profileData.setName(user.getDisplayName());
      profileData.setUserID(user.getUid());
      mDbUserRefernce = mFirebaseDb.getReference().child(profileData.getUserID());
      callChildListener();
  }


    private void onSignedOutCleanup() {

                detachDatabaseReadListener();
            }


    private void detachDatabaseReadListener() {
                if (mChildEventListener != null) {
                        mDbUserRefernce.removeEventListener(mChildEventListener);
                        mChildEventListener = null;
                    }
            }
}
