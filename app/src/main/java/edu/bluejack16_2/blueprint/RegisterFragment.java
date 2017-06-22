package edu.bluejack16_2.blueprint;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.constraint.solver.widgets.Snapshot;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


public class RegisterFragment extends Fragment {


    public RegisterFragment(){}

    Button regisBtn;
    EditText emailEt, passEt, nameEt;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.register_fragment,container,false);

        regisBtn = (Button) v.findViewById(R.id.btnRegis);
        emailEt = (EditText) v.findViewById(R.id.etEmail);
        passEt = (EditText) v.findViewById(R.id.etPassword);
        nameEt = (EditText) v.findViewById(R.id.etFullname);

        regisBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                User.createUser(getContext(),emailEt.getText().toString(),passEt.getText().toString(),nameEt.getText().toString());
            }
        });

        return v;
    }

}
