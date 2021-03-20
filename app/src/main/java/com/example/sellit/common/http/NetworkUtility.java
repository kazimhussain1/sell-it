package com.example.sellit.common.http;

import androidx.annotation.Nullable;

import com.example.sellit.R;
import com.example.sellit.common.ContextService;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.util.Objects;

import javax.net.ssl.SSLHandshakeException;

import okhttp3.ResponseBody;
import retrofit2.Response;

public class NetworkUtility {

    public static synchronized boolean hasError(Response response) {

        if (response != null) {
            try {
                return Objects.requireNonNull(response.body()).toString().contains("error");
            } catch (NullPointerException e) {
                e.printStackTrace();
            }
        }
        return true;
    }

    public static String extractErrorBody(@Nullable ResponseBody errorBody) {

        JSONObject jObjError;

        if (errorBody != null) {
            try {
                jObjError = new JSONObject(errorBody.string());

                try {

                    return jObjError.getString("data");

                } catch (Exception e5) {
                    e5.printStackTrace();
                    try {

                        return jObjError.getJSONObject("message").getJSONArray("new_password").getString(0);

                    } catch (Exception e4) {
                        e4.printStackTrace();
                        try {

                            return jObjError.getJSONObject("error").getJSONArray("password").getString(0);

                        } catch (Exception e) {
                            e.printStackTrace();

                            try {

                                return jObjError.getJSONObject("error").getJSONArray("username").getString(0);

                            } catch (Exception e1) {
                                e1.printStackTrace();

                                try {

                                    return jObjError.getJSONObject("error").getJSONArray("email").getString(0);

                                } catch (Exception e2) {
                                    e2.printStackTrace();

                                    try {
                                        return jObjError.getJSONObject("error").getString("message");
                                    } catch (Exception e6) {
                                        e6.printStackTrace();

                                        try {

                                            return jObjError.getString("error");

                                        } catch (Exception e3) {
                                            e3.printStackTrace();

                                        }
                                    }


                                }
                            }
                        }
                    }
                }
                try {
                    return errorBody.string();
                } catch (IOException e) {
                    e.printStackTrace();
                }


            } catch (JSONException | IOException e) {
                e.printStackTrace();
            }
        }


        return ContextService.getInstance().getContext().getString(R.string.err_went_wrong);
    }

    public static String generateError(Throwable t) {
        if (t instanceof ConnectException) {
            //internet off
            return ContextService.getInstance().getContext().getResources().getString(R.string.err_internet_not_found);
        } else if (t instanceof SocketTimeoutException) {
            //time out
            return ContextService.getInstance().getContext().getResources().getString(R.string.err_went_wrong);
        } else if (t instanceof SSLHandshakeException) {
            return ContextService.getInstance().getContext().getResources().getString(R.string.err_device_time);
        } else {
            t.printStackTrace();
            return ContextService.getInstance().getContext().getResources().getString(R.string.err_went_wrong);
        }
    }
}
