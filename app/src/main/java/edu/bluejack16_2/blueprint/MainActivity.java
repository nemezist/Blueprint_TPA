package edu.bluejack16_2.blueprint;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.login.LoginManager;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    FirebaseAuth.AuthStateListener listen;
    String userId, email, photoUrl, username;
    Context thisContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        thisContext = this;

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(thisContext, AddPostActivity.class);
                startActivity(i);
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        listen = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                if(firebaseAuth.getCurrentUser() == null){
                    finish();
                    goToFront();
                }
            }
        };

        FirebaseAuth.getInstance().addAuthStateListener(listen);

        TextView tv = (TextView) navigationView.getHeaderView(0).findViewById(R.id.tvUsernameMain);
        tv.setText(FirebaseAuth.getInstance().getCurrentUser().getDisplayName());

        userId = FirebaseAuth.getInstance().getCurrentUser().getUid().toString();
        email = "";
        username = "";
        photoUrl = "";

        if(FirebaseAuth.getInstance().getCurrentUser().getEmail() != null){
            email = FirebaseAuth.getInstance().getCurrentUser().getEmail().toString();
        }

        if(FirebaseAuth.getInstance().getCurrentUser().getDisplayName() != null ){
            username = FirebaseAuth.getInstance().getCurrentUser().getDisplayName().toString();
        }

        if(FirebaseAuth.getInstance().getCurrentUser().getPhotoUrl() != null) {
            photoUrl = FirebaseAuth.getInstance().getCurrentUser().getPhotoUrl().toString();
        }
        else{
            //photo url ambil dari firebase storage assets
            photoUrl = "https://firebasestorage.googleapis.com/v0/b/blueprint-12dd0.appspot.com/o/images%2Fassets%2Fperson.png?alt=media&token=1f289c98-fa49-4d51-84e6-f1503517b444";
        }
        //disini nanti
        final FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference userReference = firebaseDatabase.getReference("users/" + userId);
        Query userQuery = userReference;
        userQuery.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.child("id").getValue() == null){
                    User currUser = new User(userId, email, username, photoUrl);
                    User.addUser(currUser, thisContext);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });



        ViewPager vp = (ViewPager) findViewById(R.id.viewPagerMain);
        TabLayout tl = (TabLayout) findViewById(R.id.tabMain);


        MainAdapter adap = new MainAdapter(getSupportFragmentManager());

        adap.addItem(new FeedsFragment(),"Feeds", R.drawable.home);
        adap.addItem(new NotificationFragment(),"Notification", R.drawable.notif);
        adap.addItem(new ExploreFragment(),"Explore", R.drawable.search);
        adap.addItem(new ProfileFragment(),"Profile", R.drawable.profile);

        vp.setAdapter(adap);
        tl.setupWithViewPager(vp);

        for (int i = 0 ; i < adap.getCount(); i++){
            tl.getTabAt(i).setIcon(adap.getIcon(i));
        }
        //Toast.makeText(this, vp.getId() + " : " + tl.getId(), Toast.LENGTH_SHORT).show();

    }

    void goToFront(){
        Intent in = new Intent(getApplicationContext(),FrontActivity.class);
        startActivity(in);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id == R.id.nav_about) {

        }
        else if (id == R.id.nav_logout) {
            FirebaseAuth.getInstance().signOut();
            LoginManager.getInstance().logOut();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
