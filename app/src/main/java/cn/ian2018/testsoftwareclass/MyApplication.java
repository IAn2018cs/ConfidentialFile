package cn.ian2018.testsoftwareclass;

import android.app.Application;

import com.zhy.http.okhttp.OkHttpUtils;

import org.json.JSONObject;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Authenticator;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.Route;
import okio.BufferedSink;

/**
 * Created by Administrator on 2016/11/8/008.
 */

public class MyApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();

        // 配置OkHttpUtils
        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .connectTimeout(10000L, TimeUnit.MILLISECONDS)
                .readTimeout(10000L, TimeUnit.MILLISECONDS)
                .authenticator(new Authenticator() {
                    @Override
                    public Request authenticate(Route route, Response response) throws IOException {
                        return response.request().newBuilder()
                                .header("Authorization", "Bearer " + getToken())
                                .build();
                    }
                })
                .build();

        OkHttpUtils.initClient(okHttpClient);
    }

    /**
     * 获取token令牌
     */
    public static String getToken() {
        String token = "";
        // 创建一个okhttp连接
        OkHttpClient client = new OkHttpClient.Builder().build();

        // 创建一个请求体
        RequestBody requestBody = new RequestBody() {
            @Override
            public MediaType contentType() {
                // 设置请求体类型
                return MediaType.parse("text/plain");
            }

            @Override
            public void writeTo(BufferedSink sink) throws IOException {
                // 写入请求体数据
                String post = "grant_type=client_credentials&client_id=StudentForest&client_secret=information12016921";
                sink.writeUtf8(post);
            }
        };

        // 创建一个post请求
        Request request = new Request.Builder()
                .url("http://api.hicc.cn//token")
                .post(requestBody)
                .build();

        try {
            // 通过okhttp发起一次请求，获得响应
            Response response = client.newCall(request).execute();

            // 获得响应体
            String result = response.body().string();
            System.out.println(result);
            // 解析json数据
            JSONObject jsonObject = new JSONObject(result);
            // 获得返回的token
            token = jsonObject.getString("access_token");
        } catch (Exception e) {
            e.printStackTrace();
        }

        return token;
    }
}
