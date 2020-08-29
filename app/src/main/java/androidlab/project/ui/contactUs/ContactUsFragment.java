package androidlab.project.ui.contactUs;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import androidlab.project.HomeActivity;
import androidlab.project.R;
import androidlab.project.models.Movie;
import androidlab.project.ui.movieList.MovieListFragment;
import androidlab.project.ui.movieList.MovieListViewModel;

public class ContactUsFragment extends Fragment {

    ContactUsViewModel contactUsViewModel;

    public ContactUsFragment() {
    }

    public static ContactUsFragment newInstance(String param1, String param2) {
        ContactUsFragment fragment = new ContactUsFragment();
        Bundle args = new Bundle();

        fragment.setArguments(args);
        return fragment;
    }

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        contactUsViewModel =
                ViewModelProviders.of(this).get(ContactUsViewModel.class);
        View root = inflater.inflate(R.layout.fragment_contact_us, container, false);
        return root;
    }

    @Override
    public void onStart() {
        super.onStart();
        EditText editTextEmail = getActivity().findViewById(R.id.editTextEmail);
        final EditText editTextSubject = getActivity().findViewById(R.id.editTextSubject);
        final EditText editTextBody = getActivity().findViewById(R.id.editTextBody);


        Button submit = getActivity().findViewById(R.id.buttonSubmit);
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent gmailIntent = new Intent();
                gmailIntent.setAction(Intent.ACTION_SENDTO);
                gmailIntent.setType("message/rfc822");
                gmailIntent.setData(Uri.parse("mailto:"));
                gmailIntent.putExtra(Intent.EXTRA_EMAIL, new String[]{"movies@movies.com"});
                gmailIntent.putExtra(Intent.EXTRA_SUBJECT, editTextSubject.getText().toString());
                gmailIntent.putExtra(Intent.EXTRA_TEXT, editTextBody.getText());
                startActivity(gmailIntent);
            }
        });
    }
}
