package com.mobile.uko.fragment;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.mobile.uko.BuildConfig;
import com.mobile.uko.R;
import com.mobile.uko.activity.ChangePasswordActivity;
import com.mobile.uko.activity.ConfirmPasswordActivity;
import com.mobile.uko.activity.LoginActivity;
import com.mobile.uko.activity.ProfileChangePasswordActivity;
import com.mobile.uko.activity.UserDetailsActivity;
import com.mobile.uko.model.GetLeaseAgreementUrl;
import com.mobile.uko.model.GetProfileImagePath;
import com.mobile.uko.model.Profile;
import com.mobile.uko.network.APIClient;
import com.mobile.uko.network.ApiInterface;
import com.mobile.uko.network.UpdateImageInterface;
import com.mobile.uko.utils.AmazonS3Client;
import com.mobile.uko.utils.Common;
import com.mobile.uko.utils.SharedHelper;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;
import io.reactivex.Single;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import pl.aprilapps.easyphotopicker.DefaultCallback;
import pl.aprilapps.easyphotopicker.EasyImage;
import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.EasyPermissions;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static com.mobile.uko.MvpApplication.MULTIPLE_PERMISSION;
import static com.mobile.uko.MvpApplication.MULTIPLE_PERMISSION_REQUEST_CODE;

public class ProfileFragment extends Fragment implements EasyPermissions.PermissionCallbacks {

    private String TAG = "Profile fragment";

    @BindView(R.id.profile_avatar)
    CircleImageView userAvatar;
    @BindView(R.id.txt_user_name)
    TextView tv_name;
    @BindView(R.id.txt_user_email)
    TextView tv_email;
    @BindView(R.id.linear_personal)
    RelativeLayout rl_personal;
    @BindView(R.id.linear_notification)
    RelativeLayout rl_notification;
    @BindView(R.id.linear_change_password)
    RelativeLayout rl_change_password;
    @BindView(R.id.linear_logout)
    RelativeLayout rl_logout;
    @BindView(R.id.switch_notification)
    Switch sw_notification;
    @BindView(R.id.avatar_progress)
    ProgressBar avatar_progress;

    private APIClient apiClient;
    private Context context;
    Profile model = new Profile();
    File imgFile = null;
    private String putUrl = "", getUrl = "";

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.context = getContext();
        apiClient = new APIClient();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);
        ButterKnife.bind(this, view);

        return view;
    }


    private void getProfile() {
        Common.showLoading(context);

        Call<Profile> call = apiClient.getAPIClient().getProfile();
        call.enqueue(new Callback<Profile>() {
            @Override
            public void onResponse(Call<Profile> call, Response<Profile> response) {
                Common.hideLoading();
                if (response.isSuccessful()) {
                    model = response.body();

                    updateUI();
                } else {
                    try {
                        JSONObject object = new JSONObject(response.errorBody().string());
                        String error_msg = object.getString("error");
                        Toast.makeText(context, error_msg, Toast.LENGTH_LONG).show();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<Profile> call, Throwable t) {
                Common.hideLoading();
            }
        });
    }


    private void updateUI() {
        if (model != null) {
            if (model.getUserImage() != null && !model.getUserImage().isEmpty()) {
                avatar_progress.setVisibility(View.VISIBLE);
                Picasso.get().load(model.getUserImage())
                        .into(userAvatar, new com.squareup.picasso.Callback() {
                            @Override
                            public void onSuccess() {
                                if (avatar_progress != null)
                                    avatar_progress.setVisibility(View.GONE);
                            }

                            @Override
                            public void onError(Exception e) {

                            }
                        });
            }
//            Glide.with(context)
//                    .load(model.getUserImage())
//                    .apply(RequestOptions.placeholderOf(R.drawable.err_blank).dontAnimate().error(R.drawable.err_blank))
//                    .into(userAvatar);

            tv_name.setText(model.getFirstName() + " " + model.getLastName());
            tv_email.setText(model.getEmail());
        }
    }


    private void getPresignedUrl(String name, String type) {

        Call<GetLeaseAgreementUrl> call = apiClient.getAPIClient().getProfilePresignedUrl(name, type);
        call.enqueue(new Callback<GetLeaseAgreementUrl>() {
            @Override
            public void onResponse(Call<GetLeaseAgreementUrl> call, Response<GetLeaseAgreementUrl> response) {
                Common.hideLoading();
                if (response.isSuccessful()) {
                    if (response.body().getStatus() == 200) {
                        putUrl = response.body().getPresignedUrl().getPutUrl();
                        getUrl = response.body().getPresignedUrl().getGetUrl();
                        Log.d(TAG, "PUT URL:::::::::" + putUrl);
                        Log.d(TAG, "GET URL:::::::::" + getUrl);

                        if (!putUrl.isEmpty() && imgFile != null)
                            uploadImage(imgFile, type, name);
                    }
                }
            }

            @Override
            public void onFailure(Call<GetLeaseAgreementUrl> call, Throwable t) {
                Common.hideLoading();
            }
        });
    }

    @OnClick({R.id.linear_personal, R.id.linear_notification, R.id.linear_change_password, R.id.linear_logout, R.id.profile_avatar})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.linear_personal:
                Intent intent = new Intent(context, UserDetailsActivity.class);
                intent.putExtra("profile_model", model);
                startActivity(intent);
                break;
            case R.id.linear_notification:
                break;
            case R.id.linear_change_password:
                Intent cIntent = new Intent(context, ProfileChangePasswordActivity.class);
                startActivity(cIntent);
                break;
            case R.id.linear_logout:
                logout();
                break;
            case R.id.profile_avatar:
                MultiplePermissionTask();
                break;
        }
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onResume() {
        super.onResume();
        getProfile();
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.context = context;
        apiClient = new APIClient();
    }

    private void logout() {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Logout");
        builder.setMessage("Are you sure you want to logout?");
        builder.setCancelable(true);
        builder.setIcon(android.R.drawable.ic_dialog_alert);

        builder.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                SharedHelper.clearSharedPreferences(context);
                dialog.cancel();
                getActivity().finishAffinity();
                startActivity(new Intent(context, LoginActivity.class));
            }
        });

        builder.setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    @AfterPermissionGranted(MULTIPLE_PERMISSION_REQUEST_CODE)
    void MultiplePermissionTask() {
        if (hasMultiplePermission()) pickImage();
        else EasyPermissions.requestPermissions(
                getActivity(), getString(R.string.please_accept_permission),
                MULTIPLE_PERMISSION_REQUEST_CODE,
                MULTIPLE_PERMISSION);
    }

    private void pickImage() {
        EasyImage.openChooserWithGallery(this, "", 0);
    }

    private boolean hasMultiplePermission() {
        return EasyPermissions.hasPermissions(context, MULTIPLE_PERMISSION);
    }

    @Override
    public void onPermissionsGranted(int requestCode, @NonNull List<String> perms) {

    }

    @Override
    public void onPermissionsDenied(int requestCode, @NonNull List<String> perms) {

    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        try {
            EasyImage.handleActivityResult(requestCode, resultCode, data, getActivity(),
                    new DefaultCallback() {
                        @Override
                        public void onImagesPicked(@NonNull List<File> imageFiles, EasyImage.ImageSource source, int type) {
                            imgFile = imageFiles.get(0);
                            Glide.with(context)
                                    .load(Uri.fromFile(imgFile))
                                    .apply(RequestOptions.placeholderOf(R.drawable.ic_default_avatar).dontAnimate().error(R.drawable.ic_default_avatar))
                                    .into(userAvatar);

                            String file_path = imgFile.getAbsolutePath();
                            String extension = file_path.substring(file_path.lastIndexOf(".") + 1);
                            String content_type = "image/" + extension;
                            String file_name = Common.getFileNameWithoutExtension(imgFile);
                            Log.d(TAG, "File Type & Name is::::" + content_type + ":::" + file_name);

                            getPresignedUrl(file_name, content_type);
                        }
                    });
        } catch (Exception e) {
            Toast.makeText(context, getString(R.string.invalid_doc_file), Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }

    private void uploadImage(File file, String content_type, String name) {
        if (!putUrl.isEmpty() && file != null) {
            Common.showLoading(context);

            RequestBody requestFile = RequestBody.create(MediaType.parse(content_type), file);

            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(BuildConfig.BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();

            UpdateImageInterface imageInterface = retrofit.create(UpdateImageInterface.class);
            Call<ResponseBody> call = imageInterface.updateImage(putUrl, requestFile);
            call.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    if (response.isSuccessful()) {
                        getProfileImagePath(name);
                    } else {
                        Common.hideLoading();
                        Toast.makeText(context, response.message(), Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    Common.hideLoading();
                    Toast.makeText(context, getString(R.string.something_wrong), Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    private void getProfileImagePath(String name) {
        if (!getUrl.isEmpty()) {
            GetProfileImagePath path = new GetProfileImagePath(getUrl, name);
            Call<GetProfileImagePath> call = apiClient.getAPIClient().getProfileImagePath(path);
            call.enqueue(new Callback<GetProfileImagePath>() {
                @Override
                public void onResponse(Call<GetProfileImagePath> call, Response<GetProfileImagePath> response) {
                    Common.hideLoading();
                    if (response.isSuccessful()) {
                        Toast.makeText(context, getString(R.string.update_profile_image_successfully), Toast.LENGTH_SHORT).show();
                    } else {
                        try {
                            JSONObject object = new JSONObject(response.errorBody().string());
                            Toast.makeText(context, object.getString("error"), Toast.LENGTH_SHORT).show();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }

                @Override
                public void onFailure(Call<GetProfileImagePath> call, Throwable t) {
                    Common.hideLoading();
                    Toast.makeText(context, getString(R.string.something_wrong), Toast.LENGTH_SHORT).show();
                }
            });
        }
    }
}
