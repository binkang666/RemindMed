package com.example.cecs491project;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentTransaction;

import com.bumptech.glide.Glide;
import com.example.cecs491project.ui.home.HomeFragment;
import com.example.cecs491project.ui.login.LoginActivity;
import com.example.cecs491project.ui.map.MapFragment;
import com.example.cecs491project.ui.medication.AddMedication;
import com.example.cecs491project.ui.medication.MedicationFragment;
import com.example.cecs491project.ui.notifications.NotificationsFragment;
import com.example.cecs491project.ui.reminder.ReminderFragment;
import com.example.cecs491project.ui.reminder.addReminderActivity;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {


    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private Toolbar toolbar;
    private CircleImageView profilePic;
    private TextView myUserName, toolbarText;
    public Uri imageURI;
    private FirebaseStorage firebaseStorage;
    private StorageReference storageReference;
    private DatabaseReference databaseReference;
    FirebaseAuth auth;

    HomeFragment homeFragment;
    MedicationFragment medicationFragment;
    ReminderFragment reminderFragment;
    NotificationsFragment notificationsFragment;
    MapFragment mapFragment;

    private Button cameraBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initializePage();
    }

    private void initializePage(){
        toolbar = findViewById(R.id.main_toolbar);
        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.side_nav_view);
        cameraBtn = findViewById(R.id.camera);
        toolbarText = findViewById(R.id.toolbarText);

        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setTitle(0);

        ActionBarDrawerToggle actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.openNavDrawer, R.string.closeNavDrawer);
        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();

        navigationView.setNavigationItemSelectedListener(this);

        firebaseStorage = FirebaseStorage.getInstance();
        storageReference = firebaseStorage.getReference();
        getUserInfo();

        homeFragment = new HomeFragment();
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.add(R.id.container, homeFragment);
        ft.commit();
        bottomNavigationOnClick();
    }

    //bot nav item options
    private void bottomNavigationOnClick() {
        BottomNavigationView bottomNav = findViewById(R.id.nav_view);
        bottomNav.setOnItemSelectedListener(item -> {
                    medicationFragment = new MedicationFragment();
                    homeFragment = new HomeFragment();
                    reminderFragment = new ReminderFragment();
                    notificationsFragment = new NotificationsFragment();
                    mapFragment = new MapFragment();

                    FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                    int id = item.getItemId();
                    if ( id == R.id.navigation_dashboard){

                        ft.replace(R.id.container, homeFragment);
                        toolbarText.setText("RemindMed");
                        ((NavigationView)findViewById(R.id.side_nav_view)).setCheckedItem(R.id.nav_home);

                    }else if ( id == R.id.navigation_medication){

                        ft.replace(R.id.container, medicationFragment);
                        toolbarText.setText("Medications");
                        ((NavigationView)findViewById(R.id.side_nav_view)).setCheckedItem(R.id.nav_medication);

                    }else if ( id == R.id.navigation_notifications){

                        ft.replace(R.id.container, notificationsFragment);
                        toolbarText.setText("Notifications");
                        ((NavigationView)findViewById(R.id.side_nav_view)).setCheckedItem(R.id.nav_notification);
                    }else if( id == R.id.navigation_reminder){
                        ft.replace(R.id.container, reminderFragment);
                        toolbarText.setText("Reminders");
                        ((NavigationView)findViewById(R.id.side_nav_view)).setCheckedItem(R.id.nav_reminder);
                    }else if(id == R.id.navigation_map){
                        ft.replace(R.id.container, mapFragment);
                        toolbarText.setText("Map");
                        ((NavigationView)findViewById(R.id.side_nav_view)).setCheckedItem(R.id.nav_map);
                    }
                    ft.commit();
                    return true ;
                });
    }

    //side nav item selected options
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

            if (id == R.id.profilePic){
                profilePic = findViewById(R.id.profilePic);
                profilePic.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Toast.makeText(MainActivity.this, "Clicked",Toast.LENGTH_SHORT).show();
                        choosePic();
                        drawerLayout.closeDrawer(GravityCompat.START);
                    }
                });
                choosePic();
            }
            else if(id == R.id.nav_medication){
                medicationFragment = new MedicationFragment();
                FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                ft.replace(R.id.container, medicationFragment);
                toolbarText.setText("Medications");
                ((NavigationView)findViewById(R.id.side_nav_view)).setCheckedItem(R.id.nav_medication);
                ft.commit();

            }

        //close navigation drawer
        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    private void choosePic() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction((Intent.ACTION_GET_CONTENT));
        startActivityForResult(intent,1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //select image to upload as profile image
        if(requestCode == 1 && resultCode == RESULT_OK && data !=null && data.getData() !=null){
            imageURI = data.getData();
            profilePic.setImageURI(imageURI);
            uploadPic();
        }
        //take photos for anything(notes, medication) using camera
        if(requestCode == 100){
            Bitmap captureImage = (Bitmap) data.getExtras().get("data");

        }
    }

    private void uploadPic() {
        final ProgressDialog pd = new ProgressDialog(this);

        pd.setTitle("Uploading Image...");
        pd.show();
        String user = Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid();

        StorageReference ref = FirebaseStorage.getInstance().getReference().child("images/" + user +"_profile_picture.jpg");
        ref.putFile(imageURI).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                pd.dismiss();
                Snackbar.make(findViewById(android.R.id.content), "Image uploaded", Snackbar.LENGTH_LONG).show();

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                pd.dismiss();
                Toast.makeText(getApplicationContext(),"Upload Failed", Toast.LENGTH_SHORT).show();
            }
        }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onProgress(@NonNull UploadTask.TaskSnapshot snapshot) {
                double progressPercent = (100.00 * snapshot.getBytesTransferred() /snapshot.getTotalByteCount());
                pd.setMessage("Progress" + (int)progressPercent + " %");
            }
        });

    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }

    public void addReminder(View view) {
        Intent i = new Intent(this, addReminderActivity.class);
        startActivity(i);
    }


    public void launchAddOrEditMedication(View v)
    {
        Intent i = new Intent(MainActivity.this, AddMedication.class);
        startActivity(i);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.top_nav_setting, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch(item.getItemId())
        {

        }
        return super.onOptionsItemSelected(item);
    }

    //pop up logout window
    public void logout(MenuItem item) {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
        alertDialog.setMessage("Are you sure you want to log out?");
        alertDialog.setCancelable(false);

        alertDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                FirebaseAuth.getInstance().signOut();
                Toast.makeText(MainActivity.this, "Logged out", Toast.LENGTH_LONG).show();
                startActivity(new Intent(MainActivity.this, LoginActivity.class));
            }
        });
        alertDialog.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });
        alertDialog.create().show();
    }

    //pop up logout window
    public void changePic(View view) {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
        alertDialog.setMessage("Do you want to upload a profile picture?");
        alertDialog.setCancelable(false);

        alertDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                profilePic = findViewById(R.id.profilePic);
                choosePic();
            }
        });
        alertDialog.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });
        alertDialog.create().show();
    }


    public void getUserInfo(){
        String user = Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid();

        NavigationView navigationView = (NavigationView) findViewById(R.id.side_nav_view);
        View headerView = navigationView.getHeaderView(0);
        TextView navUsername = (TextView) headerView.findViewById(R.id.my_user_name);
        navUsername.setText(user);

        StorageReference ref = FirebaseStorage.getInstance().getReference().child("images/" + user +"_profile_picture.jpg");

        ref.getDownloadUrl().addOnSuccessListener(uri -> {
            CircleImageView pic = (CircleImageView) headerView.findViewById(R.id.profilePic);
            profilePic = findViewById(R.id.profilePic);
            Glide.with(pic).load(uri).centerCrop().into(profilePic);
        });



    }


    public void setting(MenuItem item) {

    }


    public void openCamera(MenuItem item) {

        if(ContextCompat.checkSelfPermission(MainActivity.this,
                Manifest.permission.CAMERA)!= PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(MainActivity.this,
                    new String[]{
                            Manifest.permission.CAMERA
                    }, 100);
        }

        Intent i = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(i, 100);
    }
}