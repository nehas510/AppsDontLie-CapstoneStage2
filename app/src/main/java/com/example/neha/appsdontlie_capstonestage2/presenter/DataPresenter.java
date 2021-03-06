package com.example.neha.appsdontlie_capstonestage2.presenter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.util.Log;
import com.example.neha.appsdontlie_capstonestage2.MySettingsFragment;
import com.example.neha.appsdontlie_capstonestage2.R;
import com.example.neha.appsdontlie_capstonestage2.SimpleProgressBar;
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
import com.google.firebase.database.Query;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.Serializable;
import java.util.Arrays;
import java.util.concurrent.TimeUnit;

import static android.app.Activity.RESULT_CANCELED;
import static android.app.Activity.RESULT_OK;
import static android.content.ContentValues.TAG;

/**
 * Created by neha on 5/3/17.
 */

public class DataPresenter implements Serializable,Parcelable{

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
    protected SimpleProgressBar spb;
    private FirebaseAuth.AuthStateListener mAuthListener;
    public static final int RC_SIGN_IN = 1;
    private static final int RC_PHOTO_PICKER = 2;
    public static  String pushID;
    private MyProfileData  profileData = new MyProfileData();

    public DataPresenter(Activity mView){
           activity = mView;

    }

    protected DataPresenter(Parcel in) {
        authInProgress = in.readByte() != 0;
    }

    public static final Creator<DataPresenter> CREATOR = new Creator<DataPresenter>() {
        @Override
        public DataPresenter createFromParcel(Parcel in) {
            return new DataPresenter(in);
        }

        @Override
        public DataPresenter[] newArray(int size) {
            return new DataPresenter[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeByte((byte) (authInProgress ? 1 : 0));
    }

    public interface MyPresenterCallback{

        void onSuccess(MyProfileData data);
        void onFailure(DatabaseError error);
    }

    public void initAuthListener(){

        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {

                FirebaseUser user = firebaseAuth.getCurrentUser();
                if(user !=null){

                     onSigninInitialize(user);


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


            }
        };

    }

    public void onResumeEvent(){

        mFirebaseAuth.addAuthStateListener(mAuthListener);

    }

    public void onPauseEvent(){


        detachDatabaseReadListener();
        if (mAuthListener != null) {
            mFirebaseAuth.removeAuthStateListener(mAuthListener);
        }
        detachDatabaseReadListener();

    }

    public void initFirebase(){

        mFirebaseDb = FirebaseDatabase.getInstance();
        mFirebaseAuth = FirebaseAuth.getInstance();
       // mFirebaseDb.setPersistenceEnabled(true);
        mFirebaseStorage = FirebaseStorage.getInstance("gs://capstone-project-20ec5.appspot.com/");
        mDbUserRefernce = mFirebaseDb.getReference().child("Users");
        mStorageRefernce = mFirebaseStorage.getReference().child("photos");
        initAuthListener();


    }


    public void showProgress(){

        spb = SimpleProgressBar.show(activity);

    }

    public void hideProgress(){
        if (spb != null) {
            spb.dismiss();
        }
    }

    public void callChildListener(final MyPresenterCallback callback) {

        if (mChildEventListener == null) {

            mChildEventListener = new ChildEventListener() {


                @Override
                public void onChildAdded(DataSnapshot dataSnapshot, String s) {

                    MyProfileData listOfData = dataSnapshot.getValue(MyProfileData.class);

                    if(callback!=null){

                               if(dataSnapshot.getKey().equals(pushID)) {

                                   callback.onSuccess(listOfData);
                               }

                    }

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

                    if(callback!=null){

                        callback.onFailure(databaseError);
                    }

                }};

            mDbUserRefernce.addChildEventListener(mChildEventListener);


            }
    }


    public Query getmQueryRef(){

        return  this.mDbUserRefernce.orderByChild("steps");
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

                            }

                            @Override
                            public void onConnectionSuspended(int i) {
                                // If your connection to the sensor gets lost at some point,
                                // you'll be able to determine the reason and react to it here.
                                if (i == GoogleApiClient.ConnectionCallbacks.CAUSE_NETWORK_LOST) {
                                    Log.i(TAG, "@string/connection_lost");
                                } else if (i == GoogleApiClient.ConnectionCallbacks.CAUSE_SERVICE_DISCONNECTED) {
                                    Log.i(TAG, "@string/connection_error");
                                }
                            }
                        }
                ).addOnConnectionFailedListener(
                        new GoogleApiClient.OnConnectionFailedListener() {
                            // Called whenever the API client fails to connect.
                            @Override
                            public void onConnectionFailed(ConnectionResult result) {
                                Log.i(TAG, "@string/connection_failed" + result.toString());
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
            new FetchCalorieAsync().execute();
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
            mDbUserRefernce.getRef().child(pushID).child("name").setValue(profileData.getName());
            mDbUserRefernce.getRef().child(pushID).child("userID").setValue(profileData.getUserID());
            mDbUserRefernce.getRef().child(pushID).child("steps").setValue(profileData.getSteps());
            mDbUserRefernce.getRef().child(pushID).child("calories").setValue(profileData.getCalories());
            Log.i(TAG, "Total calories: " + aLong);

        }


    }

public void setData(String weight, String height, String gender){

    profileData.setWeight(weight);
    profileData.setHeight(height);
    profileData.setGender(gender);
    mDbUserRefernce.getRef().child(pushID).child("weight").setValue(profileData.getWeight());
    mDbUserRefernce.getRef().child(pushID).child("height").setValue(profileData.getHeight());
    mDbUserRefernce.getRef().child(pushID).child("gender").setValue(profileData.getGender());


}

public void uploadProfilePhoto(final Fragment frag, Intent data){

    Uri selectedImageUri = data.getData();

    final StorageReference photoRef = mStorageRefernce.child(selectedImageUri.getLastPathSegment());

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
           final String newPhotoUrl = downloadUrl.toString();

            callChildListener(new MyPresenterCallback() {
                @Override
                public void onSuccess(MyProfileData data) {
                    if(data.getNewUrl()==null){
                        profileData.setOldUrl(newPhotoUrl);
                        profileData.setNewUrl(newPhotoUrl);
                        mDbUserRefernce.getRef().child(pushID).child("oldurl").setValue( profileData.getOldUrl());
                        mDbUserRefernce.getRef().child(pushID).child("newurl").setValue( profileData.getNewUrl());
                        if(frag instanceof MySettingsFragment){
                            ((MySettingsFragment) frag).setPhoto(profileData.getNewUrl());
                        }

                    }

                    else {
                        String oldphotoUrl = data.getNewUrl();
                        profileData.setOldUrl(oldphotoUrl);
                        profileData.setNewUrl(newPhotoUrl);
                        mDbUserRefernce.getRef().child(pushID).child("oldurl").setValue(profileData.getOldUrl());
                        mDbUserRefernce.getRef().child(pushID).child("newurl").setValue(profileData.getNewUrl());
                        if(frag instanceof MySettingsFragment){
                            ((MySettingsFragment) frag).setPhoto(profileData.getNewUrl());
                        }

                    }

                }

                @Override
                public void onFailure(DatabaseError error) {

                }
            });


        }
    });

}


   public void photoPicker(Fragment frag,Intent intent){

    intent.setType("image/jpeg");
    intent.putExtra(Intent.EXTRA_LOCAL_ONLY, true);
    frag.startActivityForResult(Intent.createChooser(intent, "Complete action using"), RC_PHOTO_PICKER);

}

    public void onActivityResult(Fragment frag,int requestCode, int resultCode, Intent data) {


        if (requestCode == RC_SIGN_IN) {
            if (resultCode == RESULT_OK) {


            } else if (resultCode == RESULT_CANCELED) {

                activity.finish();

            }
        }

        else if (requestCode == RC_PHOTO_PICKER && resultCode == RESULT_OK) {
                uploadProfilePhoto(frag, data);
            }

    }




  public void onSigninInitialize(FirebaseUser user){

      pushID = user.getUid();
      profileData.setName(user.getDisplayName());
      profileData.setUserID(user.getUid());

      mCreateFitnessClientforSteps();

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
