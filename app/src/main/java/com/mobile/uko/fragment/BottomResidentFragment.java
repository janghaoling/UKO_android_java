package com.mobile.uko.fragment;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.os.StrictMode;
import android.provider.Settings;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.mobile.uko.BuildConfig;
import com.mobile.uko.R;
import com.mobile.uko.model.GetLeaseAgreementUrl;
import com.mobile.uko.network.APIClient;
import com.mobile.uko.network.UpdateImageInterface;
import com.mobile.uko.utils.Common;
import com.mobile.uko.utils.FileUtil;
import com.mobile.uko.utils.Global;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.List;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import okhttp3.logging.HttpLoggingInterceptor;
import pl.aprilapps.easyphotopicker.EasyImage;
import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.EasyPermissions;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static android.app.Activity.RESULT_OK;
import static com.mobile.uko.MvpApplication.MULTIPLE_PERMISSION;
import static com.mobile.uko.MvpApplication.MULTIPLE_PERMISSION_REQUEST_CODE;

public class BottomResidentFragment extends BottomSheetDialogFragment implements EasyPermissions.PermissionCallbacks {

    private String TAG = "BottomResidentFragment";

    @BindView(R.id.linear_upload)
    LinearLayout li_upload;
    @BindView(R.id.linear_view)
    LinearLayout li_view;
    @BindView(R.id.linear_download)
    LinearLayout li_download;
    @BindView(R.id.rel_progress)
    RelativeLayout rl_progress;
    @BindView(R.id.progressBar)
    ProgressBar progressBar;
    @BindView(R.id.txtProgress)
    TextView tv_progress;
    @BindView(R.id.linear_upload_progress)
    LinearLayout li_upload_progress;
    @BindView(R.id.txt_pdf_title)
    TextView tv_pdf_title;
    @BindView(R.id.txt_file_progress)
    TextView tv_file_progress;
    @BindView(R.id.progressbar_upload)
    ProgressBar progressbarUpload;
    @BindView(R.id.img_upload_close)
    ImageView img_upload_close;

    private Context context;
    private APIClient apiClient;
    int pStatus = 0;
    private String downloadLeaseAgreementLink = "";
    private String presignedUrl = "";
    private Integer RESULT_DOCUMENT = 1003;
    private Integer REQUEST_PERMISSION = 1023;
    private String pdfPath = "";
    File documentFile = null;
    private String mimeType = "pdf";


    @SuppressLint("RestrictedApi")
    @Override
    public void setupDialog(@NonNull Dialog dialog, int style) {
        super.setupDialog(dialog, style);
        final View view = View.inflate(getContext(), R.layout.bottom_upload_fragment, null);
        dialog.setContentView(view);
        ButterKnife.bind(this, view);
        context = getContext();
        apiClient = new APIClient();

        int SDK_INT = android.os.Build.VERSION.SDK_INT;
        if (SDK_INT > 8) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                    .permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }

        getLeaseAgreement();
        setClickListener();
    }

    private void setClickListener() {
        li_upload.setOnClickListener(v -> {
            if (ContextCompat.checkSelfPermission(context, android.Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(getActivity(),
                        new String[]{android.Manifest.permission.READ_EXTERNAL_STORAGE},
                        REQUEST_PERMISSION);
            } else {
                Intent galleryIntent = new Intent(Intent.ACTION_GET_CONTENT);
                galleryIntent.setType("*/*");
                startActivityForResult(Intent.createChooser(galleryIntent, "Select Document"), RESULT_DOCUMENT);
            }
        });

        li_view.setOnClickListener(v -> {
            if (hasMultiplePermission()) viewPDF();
            else EasyPermissions.requestPermissions(
                    getActivity(), getString(R.string.please_accept_permission),
                    MULTIPLE_PERMISSION_REQUEST_CODE,
                    MULTIPLE_PERMISSION);
        });

        li_download.setOnClickListener(v -> {
            if (hasMultiplePermission()) downLoadPDF();
            else EasyPermissions.requestPermissions(
                    getActivity(), getString(R.string.please_accept_permission),
                    MULTIPLE_PERMISSION_REQUEST_CODE,
                    MULTIPLE_PERMISSION);
        });
    }

    private boolean hasMultiplePermission() {
        return EasyPermissions.hasPermissions(context, MULTIPLE_PERMISSION);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RESULT_DOCUMENT && resultCode == RESULT_OK && data != null && data.getData() != null) {
            Uri uri = data.getData();
            try {
                documentFile = FileUtil.from(context, uri);
                pdfPath = documentFile.getName();

                uploadDocument();
                Log.d(TAG, "Selected document file::" + documentFile.getAbsolutePath());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void uploadDocument() {
        if (pdfPath.isEmpty() || documentFile == null) {
            Toast.makeText(context, "Please select a document", Toast.LENGTH_SHORT).show();
        } else {
            li_upload_progress.setVisibility(View.VISIBLE);
            tv_pdf_title.setText(documentFile.getName());

            RequestBody requestFile = RequestBody.create(MediaType.parse(mimeType), documentFile);

            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(BuildConfig.BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .client(getHttpClient())
                    .build();

            UpdateImageInterface imageInterface = retrofit.create(UpdateImageInterface.class);
            Call<ResponseBody> call = imageInterface.uploadDocument(presignedUrl, requestFile);
            call.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    Log.d(TAG, "File uploading::::" + response.toString());
                    li_upload_progress.setVisibility(View.GONE);
                    leaseAgreementUpdate();
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    li_upload_progress.setVisibility(View.GONE);
                    t.printStackTrace();
                }
            });

        }
    }

    private void leaseAgreementUpdate() {
        Call<ResponseBody> call = apiClient.getAPIClient().leaseAgreementUpdate();
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                   Toast.makeText(context, "Lease Agreement updated successfully", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(context, "Lease Agreement update was failed. Please try again", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (permissions.length == 0) {
            return;
        }
        boolean allPermissionsGranted = true;
        if (grantResults.length > 0) {
            for (int grantResult : grantResults) {
                if (grantResult != PackageManager.PERMISSION_GRANTED) {
                    allPermissionsGranted = false;
                    break;
                }
            }
        }
        if (!allPermissionsGranted) {
            boolean somePermissionsForeverDenied = false;
            for (String permission : permissions) {
                if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(), permission)) {
                    //denied
                    Log.e("denied", permission);
                } else {
                    if (ActivityCompat.checkSelfPermission(getActivity(), permission) == PackageManager.PERMISSION_GRANTED) {
                        //allowed
                        Log.e("allowed", permission);
                    } else {
                        //set to never ask again
                        Log.e("set to never ask again", permission);
                        somePermissionsForeverDenied = true;
                    }
                }
            }
            if (somePermissionsForeverDenied) {
                final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity());
                alertDialogBuilder.setTitle("Permissions Required")
                        .setMessage("You have forcefully denied some of the required permissions " +
                                "for this action. Please open settings, go to permissions and allow them.")
                        .setPositiveButton("Settings", (dialog, which) -> {
                            Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
                                    Uri.fromParts("package", getActivity().getPackageName(), null));
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(intent);
                        })
                        .setNegativeButton("Cancel", (dialog, which) -> {
                        })
                        .setCancelable(false)
                        .create()
                        .show();
            }
        } else {
            switch (requestCode) {
                case MULTIPLE_PERMISSION_REQUEST_CODE:
                    if (grantResults.length > 0) {
                        boolean permission1 = grantResults[1] == PackageManager.PERMISSION_GRANTED;
                        boolean permission2 = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                        if (permission1 && permission2) pickImage();
                        else
                            Toast.makeText(context, "Please give permission", Toast.LENGTH_SHORT).show();
                    }
                    break;
            }
        }
    }

    @Override
    public void onPermissionsGranted(int requestCode, @NonNull List<String> perms) {

    }

    @Override
    public void onPermissionsDenied(int requestCode, @NonNull List<String> perms) {

    }

    private void pickImage() {
        EasyImage.openChooserWithGallery(this, "", 0);
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    private void getLeaseAgreement() {
        Common.showLoading(context);

        Call<ResponseBody> call = apiClient.getAPIClient().downloadLeaseAgreement();
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                Common.hideLoading();
                if (response.isSuccessful()) {
                    try {
                        JSONObject object = new JSONObject(response.body().string());
                        downloadLeaseAgreementLink = object.getString("leaseAgreementDownloadLink");
                        if (!downloadLeaseAgreementLink.isEmpty()) {
                            getPresignedUrl();
                        }
                    } catch (JSONException | IOException e) {
                        e.printStackTrace();
                    }
                } else {
                    try {
                        JSONObject object = new JSONObject(response.errorBody().string());
                        String err_msg = object.getString("error");
                        showDialog(err_msg);
                        li_download.setEnabled(false);
                        li_upload.setEnabled(false);
                        li_view.setEnabled(false);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Common.hideLoading();
                t.printStackTrace();
                li_download.setEnabled(false);
                li_upload.setEnabled(false);
                li_view.setEnabled(false);
            }
        });
    }

    private void showDialog(String err_msg) {
        LayoutInflater factory = LayoutInflater.from(context);
        final View dialogView = factory.inflate(R.layout.profile_not_verified_dialog, null);
        final androidx.appcompat.app.AlertDialog dialog = new androidx.appcompat.app.AlertDialog.Builder(context).create();
        dialog.setView(dialogView);
        dialog.setCancelable(false);

        TextView tv_title = dialogView.findViewById(R.id.txt_profile_title);
        TextView tv_details = dialogView.findViewById(R.id.txt_profile_details);
        tv_title.setVisibility(View.GONE);
        tv_details.setText(err_msg);

        dialogView.findViewById(R.id.btn_okay).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        dialog.show();
    }

    private void getPresignedUrl() {
        Call<GetLeaseAgreementUrl> call = apiClient.getAPIClient().getPresignedUrl();
        call.enqueue(new Callback<GetLeaseAgreementUrl>() {
            @Override
            public void onResponse(Call<GetLeaseAgreementUrl> call, Response<GetLeaseAgreementUrl> response) {
                if (response.isSuccessful()) {
                    presignedUrl = response.body().getPresignedUrl().getPutUrl();
                    li_download.setEnabled(true);
                    li_view.setEnabled(true);
                    li_upload.setEnabled(true);
                    Log.e(TAG, "Response is success123123::" + presignedUrl);
                } else {
                    try {
                        JSONObject object = new JSONObject(response.errorBody().string());
                        String err_msg = object.getString("error");
                        showDialog(err_msg);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<GetLeaseAgreementUrl> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }

    private void viewPDF() {
        File pdfFile = new File(Environment.getExternalStorageDirectory().getAbsolutePath(),"/leaseAgreement.pdf");
        if (pdfFile.exists()) {
            Uri path = FileProvider.getUriForFile(context, context.getApplicationContext().getPackageName() + ".provider", pdfFile);
            Intent objIntent = new Intent(Intent.ACTION_VIEW);
            objIntent.setDataAndType(path, "application/pdf");
            objIntent.setFlags(Intent. FLAG_ACTIVITY_CLEAR_TOP);
            objIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            startActivity(objIntent);//Starting the pdf viewer
        } else {
            Toast.makeText(context, "No available to view PDF", Toast.LENGTH_SHORT).show();
        }
    }

    private void downLoadPDF() {
        rl_progress.setVisibility(View.VISIBLE);
        new DownloadFileFromURL().execute(downloadLeaseAgreementLink);
    }

    class DownloadFileFromURL extends AsyncTask<String, String, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... f_url) {
            int count;
            try {
                URL url = new URL(f_url[0]);
                URLConnection connection = url.openConnection();
                connection.connect();

                int lengthOfFile = connection.getContentLength();
                InputStream input = new BufferedInputStream(url.openStream(), 8192);

                String storageDir = Environment.getExternalStorageDirectory().getAbsolutePath();
                String fileName = "/" + "leaseAgreement.pdf";
                File pdfFile = new File(storageDir+fileName);
//                if (!pdfFile.exists()) {
                    Log.d(TAG, "File path:::" + pdfFile.toString());
                    OutputStream output = null;
                    try {
                        output = new FileOutputStream(pdfFile);
                        byte data[] = new byte[1024];
                        long total = 0;

                        while ((count = input.read(data)) != -1) {
                            total += count;
                            // publishing the progress....
                            // After this onProgressUpdate will be called
                            publishProgress(""+(int)((total*100)/lengthOfFile));

                            Log.d(TAG, "Progress::::" + String.valueOf(total));
                            // writing data to file
                            output.write(data, 0, count);
                        }

                        // flushing output
                        output.flush();
                        // closing streams
                        output.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
//                }

                input.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        protected void onProgressUpdate(String... progress) {
            progressBar.setProgress(Integer.parseInt(progress[0]));
            tv_progress.setText(progress[0] + "%");
        }

        @Override
        protected void onPostExecute(String s) {
            rl_progress.setVisibility(View.GONE);
        }
    }

    private static OkHttpClient getHttpClient() {
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        return new OkHttpClient().newBuilder()
                .connectTimeout(300, TimeUnit.SECONDS)
                .addNetworkInterceptor(new AddHeaderInterceptor())
                .readTimeout(300, TimeUnit.SECONDS)
                .writeTimeout(300, TimeUnit.SECONDS)
                .retryOnConnectionFailure(true)
                .addInterceptor(interceptor)
                .build();
    }

    private static class AddHeaderInterceptor implements Interceptor {
        @Override
        public okhttp3.Response intercept(@NonNull Chain chain) throws IOException {
            Request.Builder builder = chain.request().newBuilder();
            builder.addHeader("Content-Type", "pdf");
            return chain.proceed(builder.build());
        }
    }
}
