package com.mobile.uko.utils;

import android.util.Log;

import com.mobile.uko.network.UpdateImageInterface;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class AmazonS3Client {

    private static final String EMPTY_URL_TO_MAKE_RETROFIT_HAPPY = "";

    public boolean uploadMoment(Map<String, String> fields, String filePath, String s3BaseUrl) {

        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.NONE);
        OkHttpClient okClient = new OkHttpClient.Builder().addInterceptor(interceptor).build();
        Retrofit retrofit = new Retrofit.Builder().client(okClient).baseUrl("http://www.dummy.com/").build();

        RequestBody requestFile = RequestBody.create(MediaType.parse("image/jpg"), new File(filePath));
        MultipartBody.Part body = MultipartBody.Part.createFormData("file", fields.get("key"), requestFile);
        Log.d("111111111111111111111", "Request params::::" + body.toString());

        Map<String, RequestBody> parameters = new HashMap<>();
        for (Map.Entry<String, String> entry : fields.entrySet()) {
            parameters.put(entry.getKey(), createPartFromString(entry.getValue()));
        }

        Log.d("111111111111111111111", "Request params::::" + parameters.toString());
//        UpdateImageInterface service = retrofit.create(UpdateImageInterface.class);
//        Call<ResponseBody> call = service.updateImage(s3BaseUrl, parameters, body);
//        call.enqueue(new Callback<ResponseBody>() {
//            @Override
//            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
//                if (response.isSuccessful()) {
//                    Log.e("AmazonS3Client", "successful: " + response.code());
//                } else {
//                    Log.e("AmazonS3Client", "unexpected http response: " + response.code());
//                }
//            }
//
//            @Override
//            public void onFailure(Call<ResponseBody> call, Throwable t) {
//                Log.e("AmazonS3Client", "upload error", t);
//            }
//        });
//        try {
//            Response<ResponseBody> execute = call.execute();
//            if (execute.code() == 204) {
//                Log.e("AmazonS3Client", "successful: " + execute.code());
//                return true;
//            } else {
//                Log.e("AmazonS3Client", "unexpected http response: " + execute.code());
//            }
//        } catch (IOException e) {
//            Log.e("AmazonS3Client", "upload error", e);
//        }
        return false;
    }

    private RequestBody createPartFromString(String descriptionString) {
        return RequestBody.create(okhttp3.MultipartBody.FORM, descriptionString);
    }

}
