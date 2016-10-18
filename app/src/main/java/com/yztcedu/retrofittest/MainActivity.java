package com.yztcedu.retrofittest;

import android.os.AsyncTask;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;

public class MainActivity extends AppCompatActivity {
    private Button button;
    private ProgressBar progressBar;
    private ImageView imageView;

    private int i;

    private MyRetrofitApi myRetrofitApi;
    //http://ww1.sinaimg.cn/large/006y8lVajw1f8p8qab7dkj31kw0zjah5.jpg
    public static final String PIC_URL="http://ww1.sinaimg.cn/large/006y8lVajw1f8p8qab7dkj31kw0zjah5.jpg/";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //创建MyRetrofitApi接口对象
        Retrofit retrofit=new Retrofit.Builder()
                .baseUrl(PIC_URL)
                .build();
        //实例化MyRetrofitApi接口对象
        myRetrofitApi = retrofit.create(MyRetrofitApi.class);

        initView();
    }
    private void downPic(){
        new DownPic().execute();
    }

    private void initView() {
        button= (Button) findViewById(R.id.btn_down);
        progressBar= (ProgressBar) findViewById(R.id.pb_down);
        imageView= (ImageView) findViewById(R.id.iv_down);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                downPic();
            }
        });
    }

    class DownPic extends AsyncTask<Void,Integer,Void>{

        @Override
        protected Void doInBackground(Void... voids) {
                Call<ResponseBody> call = myRetrofitApi.gethttp("http://ww1.sinaimg.cn/large/006y8lVajw1f8p8qab7dkj31kw0zjah5.jpg");
                try {
                    retrofit2.Response<ResponseBody> response = call.execute();
                    ResponseBody responseBody = response.body();
                    InputStream byteStream = responseBody.byteStream();
                    long length = responseBody.contentLength();
                    Log.e("Tag","length="+length);
                    File file=new File(Environment.getExternalStorageDirectory()+"/down.png");
                    file.createNewFile();
                    FileOutputStream fileOutputStream=new FileOutputStream(file);
                    byte bytes[]=new byte[1024];
                    int len=0;
                    while((len=byteStream.read(bytes))!=-1){
                        fileOutputStream.write(bytes,0,len);
                        i+=len;

                        long l = i * 100 / length;
                        Log.e("Tag","i"+l);
                        publishProgress((int)l);
                    }
                    Log.e("Tag","OK");
                    fileOutputStream.flush();

                    byteStream.close();
                    fileOutputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                    Log.e("Tag","NO");
                }

            return null;
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
            progressBar.setProgress(values[0]);
        }
    }
}
