package androidlab.project.ui.logout;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import androidlab.project.LoginActivity;
import androidlab.project.R;
import androidlab.project.SharedPrefManager;
import androidlab.project.ui.profile.ProfileFragment;
import androidlab.project.ui.profile.ProfileViewModel;

public class LogoutFragment extends Fragment {

    LogoutViewModel logoutViewModel;

    public LogoutFragment() {
    }

    public static ProfileFragment newInstance(String param1, String param2) {
        ProfileFragment fragment = new ProfileFragment();
        Bundle args = new Bundle();

        fragment.setArguments(args);
        return fragment;
    }

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        logoutViewModel =
                ViewModelProviders.of(this).get(LogoutViewModel.class);
        View root = inflater.inflate(R.layout.fragment_logout, container, false);
        return root;
    }

    @Override
    public void onStart() {
        super.onStart();
        SharedPrefManager sharedPrefManager = SharedPrefManager.getInstance(getActivity());
        sharedPrefManager.writeString("userEmail", "");
        sharedPrefManager.writeString("userName", "");
        sharedPrefManager.writeString("userGender", "");
        sharedPrefManager.writeString("userPassword", "");
        sharedPrefManager.writeString("userPhoneNumber", "");
        Intent intent = new Intent(getActivity(), LoginActivity.class);
        getActivity().startActivity(intent);
//                finish();

    }
}
