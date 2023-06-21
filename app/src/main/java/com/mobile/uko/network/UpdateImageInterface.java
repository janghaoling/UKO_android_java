package com.mobile.uko.network;


import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.Multipart;
import retrofit2.http.PUT;
import retrofit2.http.Part;
import retrofit2.http.PartMap;
import retrofit2.http.Url;

import static android.provider.MediaStore.Images.Media.CONTENT_TYPE;


public interface UpdateImageInterface {

    @PUT
    Call<ResponseBody> updateImage(@Url String url, @Body RequestBody file);

    @PUT
    Call<ResponseBody> uploadDocument(@Url String url, @Body RequestBody file);
}
