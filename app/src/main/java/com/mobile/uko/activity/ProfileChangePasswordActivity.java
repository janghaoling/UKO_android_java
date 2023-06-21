package com.mobile.uko.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputLayout;
import com.google.gson.Gson;
import com.mobile.uko.R;
import com.mobile.uko.model.ChangePassword;
import com.mobile.uko.model.ServerError;
import com.mobile.uko.network.APIClient;
import com.mobile.uko.utils.Common;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProfileChangePasswordActivity extends AppCompatActivity {

    private String TAG = "ProfileChangePasswordActivity";

    @BindView(R.id.img_back)
    ImageView imgBack;
    @BindView(R.id.ed_old_password)
    EditText edtOldPassword;
    @BindView(R.id.ed_new_password)
    EditText edtNewPassword;
    @BindView(R.id.ed_conf_password)
    EditText edtConfPassword;
    @BindView(R.id.input_old_password)
    TextInputLayout ti_old_password;
    @BindView(R.id.input_new_password)
    TextInputLayout ti_new_password;
    @BindView(R.id.input_conf_password)
    TextInputLayout ti_conf_password;
    @BindView(R.id.btn_confirm)
    Button btnConfirm;

    APIClient apiClient = new APIClient();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_change_password);

        ButterKnife.bind(this);
    }


    @OnClick({R.id.img_back, R.id.btn_confirm})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.img_back:
                onBackPressed();
                break;
            case R.id.btn_confirm:
                if (validate())
                    updatePassword();
                break;
        }
    }

    private boolean validate() {
        if (edtOldPassword.getText().toString().isEmpty() || edtOldPassword.getText().length() < 6) {
            ti_old_password.setError(getString(R.string.please_enter_old_password));
            return false;
        } else {
            ti_old_password.setError(null);
        }
        if (edtNewPassword.getText().toString().isEmpty() || edtNewPassword.getText().length() < 6) {
            ti_new_password.setError(getString(R.string.invalid_password));
            return false;
        } else {
            ti_new_password.setError(null);
        }
        if (edtConfPassword.getText().toString().isEmpty() || edtConfPassword.getText().length() < 6 || !edtConfPassword.getText().toString().equalsIgnoreCase(edtNewPassword.getText().toString())) {
            ti_conf_password.setError(getString(R.string.invalid_conf_password));
            return false;
        } else {
            ti_conf_password.setError(null);
        }
        return true;
    }

    private void updatePassword() {
        Common.showLoading(this);
        String newPassword = edtNewPassword.getText().toString().trim();
        String oldPassword = edtOldPassword.getText().toString().trim();
        ChangePassword changePassword = new ChangePassword(newPassword, oldPassword);

        Call<ChangePassword> call = apiClient.getAPIClient().changePassword(changePassword);
        call.enqueue(new Callback<ChangePassword>() {
            @Override
            public void onResponse(Call<ChangePassword> call, Response<ChangePassword> response) {
                Common.hideLoading();
                if (response.isSuccessful()) {
                    Toast.makeText(ProfileChangePasswordActivity.this, getString(R.string.update_password_successfully), Toast.LENGTH_SHORT).show();
                    onBackPressed();
                } else {
                    ServerError serverError = new Gson().fromJson(response.errorBody().charStream(), ServerError.class);
                    Toast.makeText(ProfileChangePasswordActivity.this, serverError.getError(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ChangePassword> call, Throwable t) {
                Common.hideLoading();
                Toast.makeText(ProfileChangePasswordActivity.this, getString(R.string.something_wrong), Toast.LENGTH_SHORT).show();
            }
        });
    }
}