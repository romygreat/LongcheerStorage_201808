package longcheer.memory.romygreat;
import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import longcheer.readwritestorage.ReadWriteutils;
public class MainActivity extends AppCompatActivity {
    private static  final String testString="惠州龙旗欢迎你";
    private Button button;
    private final File file1=null;
    private ReadWriteutils readWriteutils=new ReadWriteutils(MainActivity.this);
    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE ,
            Manifest.permission.MANAGE_DOCUMENTS,
            Manifest.permission.READ_CONTACTS
    };
    private TextView textView;
    public Handler mhandler =new Handler(Looper.getMainLooper()){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case  0 : textView.setText((String)msg.obj);
                case  1 : textView.setText((String)msg.obj);
            }
            super.handleMessage(msg);
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.test);
        verifyStoragePermissions(MainActivity.this);
        final File file1 = new File("/sdcard/LONGCHEER.txt");
        try {file1.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
            dayin(e.toString()+"Oncreate");
        }
        button=(Button)findViewById(R.id.button);
        textView=(TextView)findViewById(R.id.textView);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                textView.setText("进入button，仍未进入线程运行");
               mhandler.post(runnable);
            }

        });
        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT_TREE);
        startActivityForResult(intent, 1);
    }
    public void dayin(String string){
        Toast.makeText(getApplicationContext(),string,Toast.LENGTH_LONG).show();
    }
    public static void verifyStoragePermissions(Activity activity) {
// Check if we have write permission
        int permission = ActivityCompat.checkSelfPermission(activity,
                Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if (permission != PackageManager.PERMISSION_GRANTED) {
// We don't have permission so prompt the user
            ActivityCompat.requestPermissions(activity, PERMISSIONS_STORAGE,
                    REQUEST_EXTERNAL_STORAGE);
            int permission1=ActivityCompat.checkSelfPermission(activity,
                    Manifest.permission.CAMERA);
            if (permission1!=PackageManager.PERMISSION_GRANTED){
                ActivityCompat.requestPermissions(activity, PERMISSIONS_STORAGE,
                        1);
            }
        }
}
    public  Thread runnable=new Thread(new Runnable() {
        @Override
        public void run() {
            Message message=mhandler.obtainMessage();
            message.what=1;
            String mem="进入了线程，开始执行，请等待";
            message.obj= mem;
            mhandler.sendMessage(message);
            long size = 0;
            File file = new File("/sdcard/LONGCHEER.txt");
            String str;
            BufferedWriter bufferedOutputStream=null;
            try {
                bufferedOutputStream=new BufferedWriter(
                    new FileWriter(file));
                //  byte[] by=new byte[50];
                for(int i=0;i<1024;i++){
                    for(int j=0;j<200*1024;j++){
                        bufferedOutputStream.write(testString);
                    }
                }
             bufferedOutputStream.close();
            } catch (Exception e) {
                dayin(e.toString());
            }
            message=mhandler.obtainMessage();
            message.what=0;
            mem="填充完成";
            message.obj= mem;
            mhandler.sendMessage(message);
            String test=size+"";
//            ReadWriteutils readWriteutils=new ReadWriteutils(MainActivity.this);

        }
    });


}
