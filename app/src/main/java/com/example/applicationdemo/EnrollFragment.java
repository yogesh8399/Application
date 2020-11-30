package com.example.applicationdemo;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.ImageDecoder;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.provider.MediaStore;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.net.URI;
import java.util.Objects;
import java.util.UUID;

import static android.app.Activity.RESULT_OK;
import static android.content.ContentValues.TAG;


public class EnrollFragment extends Fragment {


    private View enrollview;
    StorageReference ref;
    ImageButton imgButton;
    private Button btnUpload;
    public static final int RESULT_LOAD_IMAGE = 1;
    // instance for firebase storage and StorageReference
    FirebaseStorage storage;
    DatabaseReference reff;
    StorageReference storageReference;

    private static Uri filePath=null;

    private EditText firstName;
    private EditText Age;
    private EditText lastName;
    private  EditText gender;
    private EditText country;
    private EditText state;
    private EditText phoneNumber;
    private EditText telephoneNumber;
    member Member;
    private static String downUrl;
    private static int k=99;

    public EnrollFragment() {

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
       enrollview= inflater.inflate(R.layout.fragment_enroll, container, false);

        //Adding the picture bit

        imgButton = (ImageButton) enrollview.findViewById(R.id.imageToUpload);
        btnUpload=(Button)enrollview.findViewById(R.id.addUser);


        firstName = (EditText) enrollview.findViewById(R.id.firstName);
        lastName = (EditText) enrollview.findViewById(R.id.lastName);
        state = (EditText) enrollview.findViewById(R.id.state);
        country = (EditText) enrollview.findViewById(R.id.country);
        gender = (EditText) enrollview.findViewById(R.id.gender);
        phoneNumber = (EditText) enrollview.findViewById(R.id.phoneNumber);
        telephoneNumber = (EditText) enrollview.findViewById(R.id.telephone);
        Age = (EditText) enrollview.findViewById(R.id.age);

        Member=new member();
        reff= FirebaseDatabase.getInstance().getReference().child("users");

        //uploading of other data of user

        // get the Firebase  storage reference
        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();

        imgButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent galleryIntent = new Intent(Intent.ACTION_PICK,MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(galleryIntent,RESULT_LOAD_IMAGE);
        }
        });



        // on pressing btnUpload uploadImage() is called

            btnUpload.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {


                   if(filePath!=null && checkCredentials()==1) {
                       uploadImage();
                       uploadData();
                    }
                    else
                        Toast.makeText(getActivity(),"Select profile photo",Toast.LENGTH_SHORT).show();


                    }
            });





        return enrollview;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if((requestCode == RESULT_LOAD_IMAGE ) && ( resultCode == RESULT_OK) && (data != null)) {

            Uri selectedImage = data.getData();
            filePath=selectedImage;
            imgButton.setImageURI(selectedImage);
        }
    }
    // UploadImage method
    private void uploadImage()
    {
        if (filePath != null) {


            final Uri mImage = filePath;


            //uploading the image
            ref = storageReference.child("images/" + UUID.randomUUID().toString());
            UploadTask uploadTask = ref.putFile(filePath);
            UploadTask.TaskSnapshot taskSnapshot = uploadTask.getSnapshot();
            //add file on Firebase and got Download Link
            ref.putFile(filePath).continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                @Override
                public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                    if (!task.isSuccessful()) {
                        throw task.getException();
                    }
                    return ref.getDownloadUrl();
                }
            }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                @Override
                public void onComplete(@NonNull Task<Uri> task) {
                    if (task.isSuccessful()) {
                        Uri downUri = task.getResult();
                        downUrl = downUri.toString();
                        Log.d(TAG, "onComplete: Url: " + downUrl);
                    }
                }
            });
        }
}
public int checkCredentials(){
    int ph=Integer.parseInt(phoneNumber.getText().toString().trim());
    int tph=Integer.parseInt(telephoneNumber.getText().toString().trim());
    int age=Integer.parseInt(Age.getText().toString().trim());
    String fName = firstName.getText().toString().trim();
    String lName = lastName.getText().toString().trim();
    String s = state.getText().toString().trim();
    String c = country.getText().toString().trim();
    String g = gender.getText().toString().trim();
    if(ph==tph)
        {
            Toast.makeText(getActivity(),"Both Numbers must be different",Toast.LENGTH_SHORT).show();
            return 0;
        }
        else
        {
            return 1;

        }


}

public void uploadData() {


           int age2=Integer.parseInt(Age.getText().toString().trim());
           Member.setFname(firstName.getText().toString().trim());
           Member.setLname(lastName.getText().toString().trim());
           Member.setGender(gender.getText().toString().trim());
           Member.setCountry(country.getText().toString().trim());
           Member.setUrl(downUrl);
           Member.setAge(age2);
           reff.child("user"+ k--).setValue(Member);
           Toast.makeText(getActivity(),"Data is uploading",Toast.LENGTH_SHORT).show();



    }
    int countDigit(int n)
    {
        int count = 0;
        while (n != 0)
        {
            n = n / 10;
            ++count;
        }
        return count;
    }
   /* public void reset()
    {
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        ft.detach(YourFragment.this).attach(YourFragment.this).commit();
    }*/




}


