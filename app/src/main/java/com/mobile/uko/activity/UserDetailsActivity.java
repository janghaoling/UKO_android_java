package com.mobile.uko.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.material.resources.CancelableFontCallback;
import com.google.android.material.textfield.TextInputLayout;
import com.mobile.uko.R;
import com.mobile.uko.model.LoginToken;
import com.mobile.uko.model.Profile;
import com.mobile.uko.model.UserDetails;
import com.mobile.uko.network.APIClient;
import com.mobile.uko.utils.Common;
import com.mobile.uko.utils.SharedHelper;

import org.json.JSONException;
import org.json.JSONObject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UserDetailsActivity extends AppCompatActivity implements Callback<UserDetails> {

    private String TAG = "UserDetailsActivity";

    @BindView(R.id.ed_first_name)
    EditText edtFirstName;
    @BindView(R.id.ed_last_name)
    EditText edtLastName;
    @BindView(R.id.ed_phone)
    EditText edtPhone;
    @BindView(R.id.ed_email)
    EditText edtEmail;
    @BindView(R.id.ed_date)
    EditText edtBirthDate;
    @BindView(R.id.ed_original_place)
    EditText edtPlace;
    @BindView(R.id.btn_save)
    Button btnSave;
    @BindView(R.id.img_back)
    ImageView back;
    @BindView(R.id.input_first_name)
    TextInputLayout ti_first_name;
    @BindView(R.id.input_last_name)
    TextInputLayout ti_last_name;
    @BindView(R.id.input_phone)
    TextInputLayout ti_phone;
    @BindView(R.id.input_email)
    TextInputLayout ti_email;
    @BindView(R.id.input_date)
    TextInputLayout ti_date;
    @BindView(R.id.input_original_place)
    TextInputLayout ti_place;
    @BindView(R.id.spin_gender)
    Spinner spinGender;

    Profile profile;
    APIClient apiClient;
    String[] genderList = {"Male", "Female", "Others"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_details);
        ButterKnife.bind(this);

        apiClient = new APIClient();
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            profile = bundle.getParcelable("profile_model");
        }

        ArrayAdapter adapter = new ArrayAdapter(this, android.R.layout.simple_spinner_item, genderList);
        adapter.setDropDownViewResource(R.layout.spin_list_item);
        spinGender.setAdapter(adapter);

        updateUI();
    }

    private void updateUI() {
        if (profile != null) {
            edtFirstName.setText(profile.getFirstName());
            edtLastName.setText(profile.getLastName());
            edtPhone.setText(profile.getPhoneNumber());
            edtEmail.setText(profile.getEmail());
            String srcString = profile.getDateOfBirth();
            String subDate = srcString.substring(0,10);
            edtBirthDate.setText(subDate);
            edtPlace.setText(profile.getCountryOrigin());
            if (profile.getGender().equalsIgnoreCase("Male"))
                spinGender.setSelection(0);
            else if (profile.getGender().equalsIgnoreCase("Female"))
                spinGender.setSelection(1);
            else spinGender.setSelection(2);
        }
    }

    @OnClick({R.id.img_back, R.id.btn_save})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.img_back:
                overridePendingTransition(R.anim.anim_nothing, R.anim.fade_in);
                onBackPressed();
                finish();
                break;
            case R.id.btn_save:
                if (validate())
                    updateProfile();
                break;
        }
    }

    private boolean validate() {
        if (edtFirstName.getText().toString().isEmpty()) {
            ti_first_name.setError(getString(R.string.invalid_value));
            return false;
        } else {
            ti_first_name.setError(null);
        }
        if (edtLastName.getText().toString().isEmpty()) {
            ti_last_name.setError(getString(R.string.invalid_value));
            return false;
        } else {
            ti_last_name.setError(null);
        }
        if (edtPhone.getText().toString().isEmpty()) {
            ti_phone.setError(getString(R.string.invalid_value));
            return false;
        } else {
            ti_phone.setError(null);
        }
        if (edtEmail.getText().toString().isEmpty()) {
            ti_email.setError(getString(R.string.invalid_value));
            return false;
        } else {
            ti_email.setError(null);
        }
        if (edtBirthDate.getText().toString().isEmpty()) {
            ti_date.setError(getString(R.string.invalid_value));
            return false;
        } else {
            ti_date.setError(null);
        }

        if (edtPlace.getText().toString().isEmpty()) {
            ti_place.setError(getString(R.string.invalid_value));
            return false;
        } else {
            ti_place.setError(null);
        }

        return true;
    }

    private void updateProfile() {
        Common.showLoading(this);

        String f_name = edtFirstName.getText().toString();
        String l_name = edtLastName.getText().toString();
        String phone = edtPhone.getText().toString();
        String employment = profile.getEmployment();
        String birth = edtBirthDate.getText().toString();
        String education = profile.getEducation();
        String gender = spinGender.getSelectedItem().toString();
        String country = edtPlace.getText().toString();

        UserDetails userDetails = new UserDetails(f_name, l_name, phone, gender, employment, birth, education, country);
        Call<UserDetails> call = apiClient.getAPIClient().updateProfile(userDetails);
        call.enqueue(this);
    }

    @Override
    public void onResponse(Call<UserDetails> call, Response<UserDetails> response) {
        Common.hideLoading();

        if (response.isSuccessful()) {
            Toast.makeText(UserDetailsActivity.this, getString(R.string.update_profile_successfully), Toast.LENGTH_SHORT).show();
            overridePendingTransition(R.anim.anim_nothing, R.anim.fade_in);
            onBackPressed();
            finish();
        } else {
            try {
                JSONObject object = new JSONObject(response.errorBody().string());
                Toast.makeText(UserDetailsActivity.this, object.getString("error"), Toast.LENGTH_LONG).show();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onFailure(Call<UserDetails> call, Throwable t) {
        Common.hideLoading();
    }
}