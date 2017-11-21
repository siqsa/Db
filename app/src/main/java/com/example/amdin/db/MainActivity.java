package com.example.amdin.db;

import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

public class MainActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private Button btn;
    private EditText editName;
    private EditText editPhone;
    private DatabaseReference myRef;
    private TextView showText;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btn = (Button) findViewById(R.id.addBtn);
        editName = (EditText) findViewById(R.id.editName);
        editPhone = (EditText) findViewById(R.id.editPhone);
        showText = (TextView) findViewById(R.id.showPhone);
        mAuth = FirebaseAuth.getInstance();
        FirebaseUser user = mAuth.getCurrentUser();
        myRef = FirebaseDatabase.getInstance().getReference("users");
        Query contacts=myRef.orderByChild("name").limitToFirst(10);


        contacts.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String add="";
                for(DataSnapshot snapshot:dataSnapshot.getChildren()){
                    snapshot.child("name").getValue(String.class);
                    String save=snapshot.child("name").getValue(String.class);
                    String saveTwo=snapshot.child("phone").getValue(String.class);
                    String saveThree="\n";
                    add+=save+" "+saveTwo+saveThree;

                }
                showText.setText(add);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public void submitPost() {
        final String name = editName.getText().toString();
        final String phone = editPhone.getText().toString();
        final String key = myRef.push().getKey();
        HashMap<String,Object> postValues=new HashMap<>();
        postValues.put(name,phone);
        myRef.child(key).setValue(postValues);
        Toast.makeText(getApplicationContext(), "post Success", Toast.LENGTH_SHORT).show();

    }








    public void onBackPressed(){
        mAuth.signOut();

        Toast.makeText(getApplicationContext(),"Sign out",Toast.LENGTH_SHORT).show();
        finish();
    }

}
