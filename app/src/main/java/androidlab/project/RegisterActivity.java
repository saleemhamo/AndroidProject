package androidlab.project;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

//import org.apache.commons.validator.routines.EmailValidator;
;

import java.util.regex.Pattern;

import androidlab.project.models.User;

public class RegisterActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {


    String genderText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        Spinner genderSpinner = findViewById(R.id.spinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.gender_values, R.layout.support_simple_spinner_dropdown_item);
        adapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        genderSpinner.setAdapter(adapter);
        genderSpinner.setOnItemSelectedListener(this);

        Button buttonRegister = findViewById(R.id.buttonSignupRegister);
        Button buttonBackToLogin = findViewById(R.id.buttonBackToLogin);

        final EditText editTextEmail = findViewById(R.id.editTextEmailRegister);
        final EditText editTextName = findViewById(R.id.editTextNameRegister);
        final EditText editTextPassword = findViewById(R.id.editTextPasswordRegister);
        EditText editTextConfirmPassword = findViewById(R.id.editTextConfirmPasswordRegister);
        final EditText editTextPhone = findViewById(R.id.editTextPhoneNumberRegister);

        buttonBackToLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                RegisterActivity.this.startActivity(intent);
                finish();
            }
        });


        buttonRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (formValidate()) {
                    //add user
                    PasswordEncryptionManager passwordEncryptionManager = PasswordEncryptionManager.getInstance();
                    User user = new User();
                    user.setEmail(editTextEmail.getText().toString());
                    user.setFirstName(editTextName.getText().toString());
                    user.setPhoneNumber(editTextPhone.getText().toString());
                    user.setGender(genderText);
                    user.setPassword(passwordEncryptionManager.getEncodedString(editTextPassword.getText().toString()));
                    DataBaseHelper dataBaseHelper = new DataBaseHelper(RegisterActivity.this, "PROJECT", null, 1);
                    dataBaseHelper.insertUser(user);
                    Toast toast = Toast.makeText(RegisterActivity.this, "User Registered!", Toast.LENGTH_SHORT);
                    toast.show();
                    final Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                    startActivity(intent);
                    finish();

                } else {


                    Toast toast = Toast.makeText(RegisterActivity.this, "Invalid Input!", Toast.LENGTH_SHORT);
                    toast.show();
                }

            }
        });


    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        genderText = adapterView.getItemAtPosition(i).toString();
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

    public boolean formValidate() {

        boolean isValid = true;

        EditText editTextEmail = findViewById(R.id.editTextEmailRegister);
        EditText editTextName = findViewById(R.id.editTextNameRegister);
        EditText editTextPassword = findViewById(R.id.editTextPasswordRegister);
        EditText editTextConfirmPassword = findViewById(R.id.editTextConfirmPasswordRegister);
        EditText editTextPhone = findViewById(R.id.editTextPhoneNumberRegister);

        if (genderText == null) {
            isValid = false;
            Spinner spinner = findViewById(R.id.spinner);

        }
        if (!isValidName(editTextName.getText().toString())) {
            isValid = false;
            editTextName.setTextColor(Color.rgb(255, 0, 0));

        }
        if (!isValidEmail(editTextEmail.getText().toString())) {
            isValid = false;
            editTextEmail.setTextColor(Color.rgb(255, 0, 0));

        }
        if (!validPassword(editTextPassword.getText().toString())) {
            isValid = false;
            editTextPassword.setTextColor(Color.rgb(255, 0, 0));

        }

        if (!editTextConfirmPassword.getText().toString().equals(editTextPassword.getText().toString())) {
            isValid = false;
            editTextConfirmPassword.setTextColor(Color.rgb(255, 0, 0));

        }
        if (!validPhoneNumber(editTextPhone.getText().toString())) {
            isValid = false;
            editTextPhone.setTextColor(Color.rgb(255, 0, 0));

        }


        return isValid;
    }

    public boolean isValidEmail(String email) {
        boolean isValid = true;
        if (email == null)
            return false;
        String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\." +
                "[a-zA-Z0-9_+&*-]+)*@" +
                "(?:[a-zA-Z0-9-]+\\.)+[a-z" +
                "A-Z]{2,7}$";
        Pattern pat = Pattern.compile(emailRegex);
        if (!pat.matcher(email).matches())
            return false;

        DataBaseHelper dataBaseHelper = new DataBaseHelper(RegisterActivity.this, "PROJECT", null, 1);
        Cursor userCursor = dataBaseHelper.getUserByEmail(email);
        if (userCursor.getCount() > 0) {
            userCursor.close();
            isValid = false;
        }
        return isValid;
    }

    public boolean isValidName(String name) {
        return name.length() >= 3;
    }

    public boolean validPhoneNumber(String phoneNumber) {
        boolean isValid = true;
        if (phoneNumber.length() < 14) {
            isValid = false;
        }
//        String pre = ";
//        if (!phoneNumber.matches(pre)) {
        if (!phoneNumber.startsWith("00970")) {
            isValid = false;
        }

        return isValid;
    }


    public static boolean validPassword(String password) {
        boolean isValid = true;
        if (password.length() < 4) {
            isValid = false;
        }
        String upperCaseChars = "(.*[A-Z].*)";
        if (!password.matches(upperCaseChars)) {
            isValid = false;
        }
        String lowerCaseChars = "(.*[a-z].*)";
        if (!password.matches(lowerCaseChars)) {
            isValid = false;
        }
        String numbers = "(.*[0-9].*)";
        if (!password.matches(numbers)) {
            isValid = false;
        }
        String specialChars = "(.*[@,#,$,%].*$)";
        if (!password.matches(specialChars)) {
            isValid = false;
        }
        return isValid;
    }
}
