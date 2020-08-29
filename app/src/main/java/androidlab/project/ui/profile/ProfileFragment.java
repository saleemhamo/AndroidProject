package androidlab.project.ui.profile;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import androidlab.project.DataBaseHelper;
import androidlab.project.LoginActivity;
import androidlab.project.PasswordEncryptionManager;
import androidlab.project.R;
import androidlab.project.SharedPrefManager;

import static android.view.View.VISIBLE;


public class ProfileFragment extends Fragment {
    ProfileViewModel profileViewModel;

    public ProfileFragment() {
    }

    public static ProfileFragment newInstance(String param1, String param2) {
        ProfileFragment fragment = new ProfileFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        profileViewModel =
                ViewModelProviders.of(this).get(ProfileViewModel.class);
        View root = inflater.inflate(R.layout.fragment_profile, container, false);
        return root;
    }

    @Override
    public void onStart() {
        super.onStart();

        final SharedPrefManager sharedPrefManager = SharedPrefManager.getInstance(getActivity());

        //add functionality here..
        TextView textViewEmail = (TextView) getActivity().findViewById(R.id.textEmail);
        final TextView textViewName = (TextView) getActivity().findViewById(R.id.textFirstName);
        TextView textViewGender = (TextView) getActivity().findViewById(R.id.textGender);
        final TextView textViewPassword = (TextView) getActivity().findViewById(R.id.textPassword);
        TextView textViewPhoneNumber = (TextView) getActivity().findViewById(R.id.textPhoneNumber);

        final EditText editTextName = (EditText) getActivity().findViewById(R.id.editTextFirstNameEdit);
        final EditText editTextOldPassword = (EditText) getActivity().findViewById(R.id.editTextOldPasswordEdit);
        final EditText editTextNewPassword = (EditText) getActivity().findViewById(R.id.editTextNewPasswordEdit);
        final EditText editTextConfirmNewPassword = (EditText) getActivity().findViewById(R.id.editTextConfirmNewPasswordEdit);

        final Button editNameButton = (Button) getActivity().findViewById(R.id.buttonEditFirstName);
        final Button editPasswordButton = (Button) getActivity().findViewById(R.id.buttonEditPassword);


        editTextName.setVisibility(View.GONE);
        editTextOldPassword.setVisibility(View.GONE);
        editTextNewPassword.setVisibility(View.GONE);
        editTextConfirmNewPassword.setVisibility(View.GONE);

        editNameButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (editNameButton.getText().equals("EDIT")) {
                    textViewName.setVisibility(View.GONE);
                    editTextName.setVisibility(View.VISIBLE);
                    editNameButton.setText("SAVE");

                } else {
                    String newName = editTextName.getText().toString();
                    if (newName.length() >= 3) {

                        textViewName.setVisibility(View.VISIBLE);
                        editTextName.setVisibility(View.GONE);
                        editNameButton.setText("EDIT");
                        DataBaseHelper dataBaseHelper = new DataBaseHelper(getActivity(), "PROJECT", null, 1);
                        dataBaseHelper.updateUserName(sharedPrefManager.readString("userEmail", "Email"), newName);
                        textViewName.setText(newName);
                        sharedPrefManager.writeString("userName", newName);


                        Toast toast = Toast.makeText(getActivity(), "Name Changed", Toast.LENGTH_SHORT);
                        toast.show();
                    } else {
                        Toast toast = Toast.makeText(getActivity(), "Name must be longer that 3 letters!", Toast.LENGTH_SHORT);
                        toast.show();
                    }

                }
            }
        });

        editPasswordButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (editPasswordButton.getText().equals("EDIT")) {
                    textViewPassword.setVisibility(View.GONE);
                    editTextOldPassword.setVisibility(VISIBLE);
                    editTextNewPassword.setVisibility(VISIBLE);
                    editTextConfirmNewPassword.setVisibility(VISIBLE);
                    editPasswordButton.setText("SAVE");

                } else {
                    String oldPassword = editTextOldPassword.getText().toString();
                    String newPassword = editTextNewPassword.getText().toString();
                    String confirmNewPassword = editTextConfirmNewPassword.getText().toString();

                    if (oldPassword.equals(sharedPrefManager.readString("userPassword", "Password"))) {
                        if (newPassword.equals(confirmNewPassword)) {
                            if (validPassword(newPassword)) {
                                textViewPassword.setVisibility(VISIBLE);
                                editTextOldPassword.setVisibility(View.GONE);
                                editTextNewPassword.setVisibility(View.GONE);
                                editTextConfirmNewPassword.setVisibility(View.GONE);
                                editPasswordButton.setText("EDIT");
                                PasswordEncryptionManager passwordEncryptionManager = PasswordEncryptionManager.getInstance();
                                DataBaseHelper dataBaseHelper = new DataBaseHelper(getActivity(), "PROJECT", null, 1);
                                dataBaseHelper.updateUserPassword(sharedPrefManager.readString("userEmail", "Email"), passwordEncryptionManager.getEncodedString(newPassword));
                                sharedPrefManager.writeString("userName", newPassword);

                                //toast message
                            } else {
                                Toast toast = Toast.makeText(getActivity(), "Invalid New Password!", Toast.LENGTH_SHORT);
                                toast.show();
                            }
                        } else {
                            Toast toast = Toast.makeText(getActivity(), "Passwords do not match!", Toast.LENGTH_SHORT);
                            toast.show();
                        }
                    } else {
                        Toast toast = Toast.makeText(getActivity(), "Wrong Password!", Toast.LENGTH_SHORT);
                        toast.show();
                    }


                }
            }
        });


        textViewEmail.setText(sharedPrefManager.readString("userEmail", "Email"));
        textViewName.setText(sharedPrefManager.readString("userName", "First Name"));
        textViewGender.setText(sharedPrefManager.readString("userGender", "Gender"));
        String password = sharedPrefManager.readString("userPassword", "Password");
        int passwordLength = password.length();
        String pass = "";
        for (int i = 0; i < passwordLength; i++) {
            pass += "*";
        }
        textViewPassword.setText(pass);
        textViewPhoneNumber.setText(sharedPrefManager.readString("userPhoneNumber", "PhoneNumber"));

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
