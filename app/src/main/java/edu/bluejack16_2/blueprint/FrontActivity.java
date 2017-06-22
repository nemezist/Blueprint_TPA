package edu.bluejack16_2.blueprint;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.facebook.FacebookSdk;
import com.facebook.login.widget.LoginButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class FrontActivity extends AppCompatActivity {

    FirebaseAuth.AuthStateListener listen;
    FirebaseUser user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_front);

        FirebaseAuth authF = FirebaseAuth.getInstance();
        FirebaseUser usr = authF.getCurrentUser();

        if(usr!=null){
            Intent in = new Intent(getApplicationContext(),MainActivity.class);
//            Toast.makeText(this, usr.getUid(), Toast.LENGTH_SHORT).show();
            finish();
            startActivity(in);
        }

        ViewPager vp = (ViewPager) findViewById(R.id.viewPagerLogin);
        TabLayout tab = (TabLayout) findViewById(R.id.tabLogin);

        Adapter adap = new Adapter(getSupportFragmentManager());

        adap.addItem(new LoginFragment(),"Login",R.drawable.ic_login);
        adap.addItem(new RegisterFragment(),"Register",R.drawable.ic_login);

        vp.setAdapter(adap);
        tab.setupWithViewPager(vp);

        listen = new FirebaseAuth.AuthStateListener() {
         @Override
         public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser usr = firebaseAuth.getCurrentUser();
                if(usr != null){
                    user = usr;
                    goToMain();
               }
           }
       };

       FirebaseAuth.getInstance().addAuthStateListener(listen);
        for (int i = 0 ; i < adap.getCount(); i++){
            tab.getTabAt(i).setIcon(R.drawable.ic_login);
        }

    }

    public void goToMain(){
        Intent in = new Intent(this.getApplicationContext(),MainActivity.class);
        startActivity(in);
    }

}
