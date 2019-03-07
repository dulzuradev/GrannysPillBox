package com.floorists.grannyspillbox;

import android.os.Bundle;
import android.app.Activity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.floorists.grannyspillbox.classes.User;
import com.floorists.grannyspillbox.utilities.SQLiteHelper;

public class UserInfoActivity extends Activity {
    private SQLiteHelper sqlhelper;
    private EditText userfirstEditText;
    private EditText userLastEditText;
    private EditText userPhoneEditText;
    private EditText userEmailEditText;
    private EditText emergencyFirstEditText;
    private EditText emergencyLastEditText;
    private EditText emergencyPhoneEditText;
    private Button submitBtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_info);
        
        User user = sqlhelper.getUser(1);

        userfirstEditText = (EditText) findViewById(R.id.userFist);
        userLastEditText= (EditText) findViewById(R.id.userLast);
        userPhoneEditText= (EditText) findViewById(R.id.userPhone);
        userEmailEditText= (EditText) findViewById(R.id.userEmail);
        emergencyFirstEditText = (EditText) findViewById(R.id.emergencyFirst);
        emergencyLastEditText = (EditText) findViewById(R.id.emergencyLast);
        emergencyPhoneEditText = (EditText) findViewById(R.id.emergencyPhone);
        submitBtn = (Button) findViewById(R.id.submitBtn);
        submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                submitInfo();
            }
        });

        if(user != null) {
            userfirstEditText.setText(user.getFirstName());
            userLastEditText.setText(user.getLastName());
            userPhoneEditText.setText(user.getPhone());
            userEmailEditText.setText(user.getEmail());
            if (user.getEmergencyContact() != null) {
                emergencyFirstEditText.setText(user.getEmergencyContact().getFirstName());
                emergencyLastEditText.setText(user.getEmergencyContact().getLastName());
                emergencyPhoneEditText.setText(user.getEmergencyContact().getPhone());
            }

        }

    }

    public void submitInfo(){
      User emergencyContact = new User(2,
              emergencyFirstEditText.getText().toString(),
              emergencyLastEditText.getText().toString(),
              emergencyPhoneEditText.getText().toString(),
              "",
              null);

      User user = new User(1,
              userfirstEditText.getText().toString(),
              userLastEditText.getText().toString(),
              userPhoneEditText.getText().toString(),
              userEmailEditText.getText().toString(),
              emergencyContact);
      sqlhelper.addUser(user);
      finish();
    }

}
