package edu.bluejack16_2.blueprint;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class EditProfileActivity extends AppCompatActivity {

    EditText usernameEt, passwordEt, confirmPassEt;
    Button saveProfileBtn;

    //masih bingung apa yang mau diupdate
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        usernameEt = (EditText) findViewById(R.id.editProfileUsernameEt);
        passwordEt = (EditText) findViewById(R.id.editProfilePasswordEt);
        confirmPassEt = (EditText) findViewById(R.id.editProfileConfirmPasswordEt);
        saveProfileBtn = (Button) findViewById(R.id.saveProfileBtn);

        final FirebaseUser currUser = FirebaseAuth.getInstance().getCurrentUser();

        final FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference userReference = firebaseDatabase.getReference("users/" + currUser.getUid());
        Query userQuery = userReference;

        saveProfileBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = usernameEt.getText().toString();
                String password = passwordEt.getText().toString();
                String confirmPassword = confirmPassEt.getText().toString();
                boolean isUsername = true, isPassword = true, isConfirmPassword = true;
                boolean isValid = true;

                if(username.equals("") || username == null)
                    isUsername = false;
                if(password.equals("") || password == null)
                    isPassword = false;
                if(confirmPassword.equals("") || confirmPassword == null)
                    isConfirmPassword = false;

                if(!isUsername)
                    isValid = false;
                if(!password.equals(confirmPassword) && isPassword && isConfirmPassword)
                    isValid = false;

                if(isUsername && (!isPassword || !isConfirmPassword) && isValid){
                    //update username
                    FirebaseUser currLoginUser = FirebaseAuth.getInstance().getCurrentUser();
                    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();

                    databaseReference.child("users/" + currLoginUser.getUid() + "/username").setValue(username).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Toast.makeText(EditProfileActivity.this, "Update Success", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
                else if(isUsername && isPassword && isConfirmPassword && isValid){
                    //update username & password
                    FirebaseUser currLoginUser = FirebaseAuth.getInstance().getCurrentUser();
                    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();

                    currLoginUser.updatePassword(password).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {

                        }
                    });

                    databaseReference.child("users/" + currLoginUser.getUid() + "/username").setValue(username).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Toast.makeText(EditProfileActivity.this, "Update Success", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });

        userQuery.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String username = dataSnapshot.child("username").getValue().toString();
                usernameEt.setText(username);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}
