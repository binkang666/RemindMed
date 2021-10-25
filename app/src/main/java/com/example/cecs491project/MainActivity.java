package com.example.cecs491project;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
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
import com.example.cecs491project.ui.setting.SettingActivity;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.WriteBatch;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.List;
import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {


    private DrawerLayout drawerLayout;
    private CircleImageView profilePic;
    private TextView toolbarText;
    public Uri imageURI;

    HomeFragment homeFragment;
    MedicationFragment medicationFragment;
    ReminderFragment reminderFragment;
    NotificationsFragment notificationsFragment;
    MapFragment mapFragment;

    public MainActivity() {
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initializePage();
    }

    private void initializePage(){
        Toolbar toolbar = findViewById(R.id.main_toolbar);
        drawerLayout = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.side_nav_view);
        Button cameraBtn = findViewById(R.id.camera);
        toolbarText = findViewById(R.id.toolbarText);

        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setTitle(0);

        ActionBarDrawerToggle actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.openNavDrawer, R.string.closeNavDrawer);
        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();

        navigationView.setNavigationItemSelectedListener(this);

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
                    if ( id == R.id.navigation_home){

                        ft.replace(R.id.container, homeFragment);
                        toolbarText.setText(R.string.RemindMed);
                        ((NavigationView)findViewById(R.id.side_nav_view)).setCheckedItem(R.id.nav_home);

                    }else if ( id == R.id.navigation_medication){

                        ft.replace(R.id.container, medicationFragment);
                        toolbarText.setText(R.string.Medications);
                        ((NavigationView)findViewById(R.id.side_nav_view)).setCheckedItem(R.id.nav_medication);

                    }else if ( id == R.id.navigation_notifications){

                        ft.replace(R.id.container, notificationsFragment);
                        toolbarText.setText(R.string.Notifications);
                        ((NavigationView)findViewById(R.id.side_nav_view)).setCheckedItem(R.id.nav_notification);
                    }else if( id == R.id.navigation_reminder){
                        ft.replace(R.id.container, reminderFragment);
                        toolbarText.setText(R.string.Reminders);
                        ((NavigationView)findViewById(R.id.side_nav_view)).setCheckedItem(R.id.nav_reminder);
                    }else if(id == R.id.navigation_map){
                        ft.replace(R.id.container, mapFragment);
                        toolbarText.setText(R.string.Map);
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
                profilePic.setOnClickListener(view -> {
                    Toast.makeText(MainActivity.this, "Clicked",Toast.LENGTH_SHORT).show();
                    choosePic();
                    drawerLayout.closeDrawer(GravityCompat.START);
                });
                choosePic();
            }
            else if(id == R.id.nav_medication){
                medicationFragment = new MedicationFragment();
                FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                ft.replace(R.id.container, medicationFragment);
                toolbarText.setText(R.string.Medications);
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

        try {
            StorageReference ref = FirebaseStorage.getInstance().getReference().child("images/" + user + "_profile_picture.jpg");
            ref.putFile(imageURI).addOnSuccessListener(taskSnapshot -> {
                pd.dismiss();
                Snackbar.make(findViewById(android.R.id.content), "Image uploaded", Snackbar.LENGTH_LONG).show();

            }).addOnFailureListener(e -> {
                pd.dismiss();
                Toast.makeText(getApplicationContext(), "Upload Failed", Toast.LENGTH_SHORT).show();
            }).addOnProgressListener(snapshot -> {
                double progressPercent = (100.00 * snapshot.getBytesTransferred() / snapshot.getTotalByteCount());
                pd.setMessage("Progress" + (int) progressPercent + " %");
            });
        }catch (Exception e){
            Log.e("account","anonymous account");
        }

    }

    public void addReminder(View v)
    {
        Intent i = new Intent(MainActivity.this, addReminderActivity.class);
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

    //pop up logout window
    public void logout(MenuItem item) {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
        alertDialog.setMessage("Are you sure you want to log out?");
        alertDialog.setCancelable(false);

        alertDialog.setPositiveButton("Yes", (dialogInterface, i) -> {

            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
            if(Objects.requireNonNull(user).isAnonymous()){
                FirebaseFirestore.getInstance().collection("Anonymous User Database")
                        .document(user.getUid()).collection("Medication")
                        .get().addOnSuccessListener(queryDocumentSnapshots -> {
                            WriteBatch batch = FirebaseFirestore.getInstance().batch();
                            List<DocumentSnapshot> snapshotList = queryDocumentSnapshots.getDocuments();
                            for(DocumentSnapshot snapshot: snapshotList){
                                batch.delete(snapshot.getReference());
                            }
                            batch.commit().addOnSuccessListener(unused -> Log.d("TAG", "Success")).addOnFailureListener(e -> Log.d("TAG","Failed",e));
                        });

                FirebaseFirestore.getInstance()
                        .collection("Anonymous User Database")
                        .document(user.getUid())
                        .delete();
                user.delete();
            }
            FirebaseAuth.getInstance().signOut();
            Toast.makeText(MainActivity.this, "Logged out", Toast.LENGTH_LONG).show();
            startActivity(new Intent(MainActivity.this, LoginActivity.class));

        });
        alertDialog.setNegativeButton("No", (dialogInterface, i) -> {

        });
        alertDialog.create().show();
    }

    //pop up logout window
    public void changePic(View view) {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
        alertDialog.setMessage("Do you want to upload a profile picture?");
        alertDialog.setCancelable(false);

        alertDialog.setPositiveButton("Yes", (dialogInterface, i) -> {
            profilePic = findViewById(R.id.profilePic);
            choosePic();
        });
        alertDialog.setNegativeButton("No", (dialogInterface, i) -> {

        });
        alertDialog.create().show();
    }


    public void getUserInfo(){
        String user = FirebaseAuth.getInstance().getCurrentUser().getUid();

        NavigationView navigationView = findViewById(R.id.side_nav_view);
        View headerView = navigationView.getHeaderView(0);
        TextView navUsername = headerView.findViewById(R.id.my_user_name);
        navUsername.setText(user);

            StorageReference ref = FirebaseStorage.getInstance().getReference().child("images/" + user + "_profile_picture.jpg");

            ref.getDownloadUrl().addOnSuccessListener(uri -> {
                CircleImageView pic = headerView.findViewById(R.id.profilePic);
                profilePic = findViewById(R.id.profilePic);
                Glide.with(pic).load(uri).centerCrop().into(profilePic);
            }).addOnFailureListener(e -> Log.i("log in info", "anonymous account"));

    }


    public void setting(MenuItem item) {
        startActivity(new Intent(MainActivity.this, SettingActivity.class));
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