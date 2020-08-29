package androidlab.project;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import androidlab.project.models.User;

public class LoginActivity extends AppCompatActivity {
    SharedPrefManager sharedPrefManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        Button buttonSignup = findViewById(R.id.buttonCreateAccount);
        Button buttonLogin = findViewById(R.id.buttonLogin);
        final EditText editTextEmail = findViewById(R.id.editTextEmail);
        final EditText editTextPassword = findViewById(R.id.editTextPassword);
        final CheckBox checkBox = findViewById(R.id.checkBox);
        final PasswordEncryptionManager passwordEncryptionManager = PasswordEncryptionManager.getInstance();
        SharedPrefManager sharedPrefManager2 = SharedPrefManager.getInstance(this);
        String email = sharedPrefManager2.readString("savedEmail", "nothing");
        String password = sharedPrefManager2.readString("savedPassword", "nothing");
        if (!email.equals("nothing") && !password.equals("nothing")) {
            editTextEmail.setText(email);
            editTextPassword.setText(passwordEncryptionManager.getDecodedString(password));

        }


        buttonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DataBaseHelper dataBaseHelper = new DataBaseHelper(LoginActivity.this, "PROJECT", null, 1);
                Cursor userCursor = dataBaseHelper.getUserByEmail(editTextEmail.getText().toString());


                if (userCursor.getCount() <= 0) {
                    userCursor.close();
                    Toast toast = Toast.makeText(LoginActivity.this, "User is not Registered!", Toast.LENGTH_SHORT);
                    toast.show();
                } else {
                    userCursor.moveToFirst();
                    User user = new User();
                    user.setEmail(userCursor.getString(0));
                    user.setFirstName(userCursor.getString(1));
                    user.setGender(userCursor.getString(3));
                    user.setPassword(userCursor.getString(4));
                    user.setPhoneNumber(userCursor.getString(2));
                    if (passwordEncryptionManager.getDecodedString(user.getPassword()).equals(editTextPassword.getText().toString())) {
//                    if (user.getPassword().equals(editTextPassword.getText().toString())) {
                        //add user to preferences
                        sharedPrefManager = SharedPrefManager.getInstance(LoginActivity.this);
                        sharedPrefManager.writeString("userEmail", user.getEmail());
                        sharedPrefManager.writeString("userName", user.getFirstName());
                        sharedPrefManager.writeString("userGender", user.getGender());
                        sharedPrefManager.writeString("userPassword", user.getPassword());
                        sharedPrefManager.writeString("userPhoneNumber", user.getPhoneNumber());

                        if (checkBox.isChecked()) {
                            sharedPrefManager.writeString("savedEmail", user.getEmail());
                            sharedPrefManager.writeString("savedPassword", user.getPassword());

                        }
                        final Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
                        startActivity(intent);
                        finish();

                    } else {
                        Toast toast = Toast.makeText(LoginActivity.this, "Wrong Password!", Toast.LENGTH_SHORT);
                        toast.show();
//                        editTextPassword.setHighlightColor(Color.parseColor("#FF0000"));
                    }
                }
            }
        });

        final Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
        buttonSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(intent);
                finish();
            }
        });

    }
}