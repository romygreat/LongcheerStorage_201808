package longcheer.memory.romygreat;
import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.provider.DocumentFile;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStream;

import longcheer.readwritestorage.ReadWriteutils;

import static longcheer.memory.romygreat.MainActivity.verifyStoragePermissions;
/**
 * Created by luominming on 2017/11/17.
 */
public class SyntasklongcheerActivity extends Activity {
    private boolean SDType = true;
    private static String testString ;
    private Button button;
    private Button deleteButton;
    private boolean choicemethod = true;
    private TextView DisTextView;
    private ProgressBar progressBar;
    private ReadWriteutils readWriteutils = new ReadWriteutils(SyntasklongcheerActivity.this);
    private static final String TAG = "ASYNC_TASK";
    private static String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.CAMERA
    };
    private TextView tatalremailpercentage;
    private int click = 1;
    private Button cancel;//即为暂停按钮
    private MyTask mTask, task2;
    private Button choiceButton;
    private EditText editText;
    private BufferedWriter bufferedOutputStream = null;
    private BufferedWriter buffered = null;
    private long SDAvailableSizeInit;
    private String editString;
    private int mempercentage = 100;
    boolean stop = false;
    boolean delete = false;
    private int secondExe = 1;
    private TextView Desc;
    private long l = 0;
    private double per;
    private final long SDTotalSize = readWriteutils.getSDTotalSize();
    private File file;
    private int deleteDis = 0;
    private Activity context = SyntasklongcheerActivity.this;
    private DocumentFile newFile;
    private OutputStream out;
    private File file2;
    private  boolean maddStaorage=false;
    private String pathName="/sdcard/LONGCHEER.txt";
    private String getPathNameInternal=getFilesDir()+"/longcheer.txt";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.test);
        initView();
        verifyStoragePermissions(SyntasklongcheerActivity.this);
        createFile();
        editText.setSelection(3);
        displaymemory();
        progressBar.setProgress(0);
        cancel.setClickable(false);
        clickEvent();
        Log.i("APPLICATION", getApplication().getPackageName().toString());
    }
    @Override
    protected void onResume() {
        super.onResume();
        Log.i("onResume", "Display");
    }
    @Override
    protected void onRestart() {
        super.onRestart();
        createFile();
        clickEvent();
        Log.i("onRe", "");
    }
    private void clickEvent() {
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cancel.setClickable(true);
                stop = false;
                delete = false;
                button.setKeepScreenOn(true);
                deleteDis = 0;
                mTask = new MyTask();
                String r = readWriteutils.forMat(readWriteutils.getSDTotalSize());
                Log.i("EDIT", "editstring");
                editString = editText.getText().toString();
                if (editString.equals("")) {
                    editString = "0";
                    dayin("请输入一个内存填充百分比");
                    Log.i("EDIT", "此时edit文本为空");
                    return;
                }
                Log.i("EDIT", "editstring123");
//                if(editString.trim().equals("")){editString="0";}
                mempercentage = Integer.parseInt(editString);
                DisTextView.setText("正在进行中，请耐心等待\n");
                if (choicemethod && mempercentage > 100) {
                    dayin("请输入0到100的其中一个数字");
                    return;
                }
                {  // button.setClickable(false);
                    if (choicemethod)// choicemethod=true; //按比例填充
                    {
                        if (per / 10 > mempercentage) {
                            mTask.onCancelled();
                            dayin("温馨提示，请输入一个大于的已使用占比");
                            return;
                        }
                        mTask.execute(1);
                        if (readWriteutils.getSDAvailableSize() > 1024 * 1024 * 16) {
                            if (mempercentage >= 90) {
                                dayin("主人，填充时间可能较长，请耐心等待");
                                dayin("主人，填充时间可能较长，请耐心等待");
                                dayin("主人，填充时间可能较长，请耐心等待");
                            }
                        }
                    }
                    else {
                       if(maddStaorage){
                           mTask.execute(3);
                           maddStaorage=false;
                           return;

                       }
                        //按剩余空间进行填充
                        mTask.execute(0);
                        Log.i("TASK2EXECUTE", "task2ing");
                        if (readWriteutils.getSDAvailableSize() > 1024 * 1024 * 16) {
                            if (mempercentage <= 200) {
                                dayin("主人，填充时间可能较长，请耐心等待");
                                dayin("主人，填充时间可能较长，请耐心等待");
                                dayin("主人，填充时间可能较长，请耐心等待");
                                if (mempercentage == 0) {
                                    DisTextView.setText("正在进行中" + "完全填充完后可能导致本应用短暂退出");
                                }
                            }
                        }
                    }
                }
            }

        });
        button.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {

                return false;
            }
        });
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteDis = 0;
                mTask.onCancelled();
            }
        });
        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                button.setClickable(true);
                delete = true;
                file.delete();
                dayin("正在删除，请稍等片刻");
                if (file.exists()) {
                    try {
                        bufferedOutputStream.close();
                        file.delete();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    Log.i("内存占比", per + " ");
                    file.delete();
                }
                if (choicemethod) {
                    DisTextView.setText(R.string.display);
                } else {
                    DisTextView.setText("按剩余空间填充 (单位MB)");
                }
                System.gc();
                SDAvailableSizeInit = readWriteutils.getSDAvailableSize();
                per = 1000 - ((SDAvailableSizeInit * 1000 / SDTotalSize));
                int dispercentage = (int) (per / 10);
                String showmem = "总共有内存：" + SDTotalSize + "KB" + "\n" + "剩余的内存："
                        + SDAvailableSizeInit + "KB" + "\n" + "已使用占比:" + "   " + dispercentage + "%\n";
                tatalremailpercentage.setText(showmem);
                Log.i("neicun", per + "\n" + SDAvailableSizeInit + "\nSDtatal" + readWriteutils.getSDTotalSize() + "");
                tatalremailpercentage.invalidate();
                tatalremailpercentage.setText(showmem);
                deleteDis++;
                if (per == 100) {
                    if (deleteDis == 3) {
                        deleteDis = 0;
                        dayin("内容已删除，界面暂时无法更新");
                    }
                }
            }
        });
        choiceButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               // createFile();
                button.setKeepScreenOn(false);
                if (click % 3 == 0) {
                    DisTextView.setText(R.string.display); //点击次数选择模式，值0为选择方式是按剩余内存
                    editText.setText("100");
                    editText.setSelection(editText.getText().length());
                    choicemethod = true;
                }

                else if(click%3==1){
                    DisTextView.setText("按容量填充 (单位MB)");
                    choicemethod=false;
                    maddStaorage=true;
                }
                else {//按比例进行填充内存
                    DisTextView.setText("按剩余空间填充 (单位MB)");
                    if (editText.getText().toString().equals("100")) {
                        editText.setText("200");
                    }
                    editText.setText(editText.getText());
                    editText.setSelection(editText.getText().length());
                    choicemethod = false; //按剩余空间填充 (单位MB
                }
                click++;
                //   dayin(click+"");
                if (click == 100) {
                    click = 0;
                }
            }
        });
    }
    private void createFile() {
        file = new File(pathName);
      //  file2 = new File("/sdcard/LONGCHEER1.txt");
        if (file.exists() == false) {
            try {
                file.createNewFile();
                //file2.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
                Log.i("CreateNewFile", "/sdcard/LONGCHEER.txt创建文件失败");
                //dayin(e.toString()+"创建文件失败，重新启动该apk");
            }
        }
    }
    private void displaymemory() {
        long SDAvailableSizeInit = readWriteutils.getSDAvailableSize();
        per = 1000 - (SDAvailableSizeInit * 1000 / SDTotalSize);
        String showmem = "总共有内存：" + SDTotalSize + "KB" + "\n" + "剩余的内存："
                + SDAvailableSizeInit + "KB" + "\n" + "已使用占比:" + "   " + (int) (per / 10) + "%\n";
        tatalremailpercentage.setText(showmem);
    }
    private void initView() {
        cancel = (Button) findViewById(R.id.cancel);
        progressBar = (ProgressBar) findViewById(R.id.progress_bar);
        tatalremailpercentage = (TextView) findViewById(R.id.textView);
        button = (Button) findViewById(R.id.button);
        editText = (EditText) findViewById(R.id.editText2);
        deleteButton = (Button) findViewById(R.id.delete);
        choiceButton = (Button) findViewById(R.id.choiceButton);
        DisTextView = (TextView) findViewById(R.id.display);
        Desc = (TextView) findViewById(R.id.settting);
    }

    public void dayin(String string) {
        Toast.makeText(getApplicationContext(), string, Toast.LENGTH_SHORT).show();
    }
    private class MyTask extends AsyncTask<Integer, Integer, String> {
        //onPreExecute方法用于在执行后台任务前做一些UI操作
        @Override
        protected void onPreExecute() {
            Log.i(TAG, "onPreExecute() called");
        }
        //doInBackground方法内部执行后台任务,不可在此方法内修改UI
        @Override
        protected String doInBackground(Integer... params) {
            Log.i(TAG, "doInBackground(Params... params) called");
            BufferedWriter bufferedWriter = null;
            // int b = Integer.valueOf(params[0]).intValue();
            Log.i(TAG, "b = Integer.valueOf(params[0]).intValue()");
            int prams0 = params[0].intValue();
            //   long SDTotalSize = readWriteutils.getSDTotalSize(); //choicemethod=false; //按剩余空间填充 (单位MB
            double FillSizeAready =
                    (SDTotalSize * mempercentage) / 100;//需要在edit中获取得到比例，需要进行完善，及该修改比例50
            if (prams0 == 1) {
                Log.i(TAG, " 按比例进行填充");
                int progress = 100;
                try {
                    bufferedOutputStream = new BufferedWriter(
                            new FileWriter(file, true));
                    for (int i = 0; i < 1024 * 2048; i++) {
                        long FillSizeAreaded = SDTotalSize - readWriteutils.getSDAvailableSize();
                        Log.i(TAG, "doInBackground: FillSizeAreaded"+FillSizeAreaded+"=====FillSizeAready"+FillSizeAready);
                        progress = (int) (FillSizeAreaded * 100) / (int) (FillSizeAready);
                        // l=FillSizeAready-FillSizeAreaded;
                        if (FillSizeAready < FillSizeAreaded)
//                        if (true)
                        {
                            secondExe = 2;
                            break;
                        }
                        if (stop) return "stop";
                        if (delete) return "delete";
                        publishProgress(progress);
                        int j = 0;
                        for (j = 0; j < 1024; j++) {
                            if (delete) return "delete";
                            try{
                                testString = getString(R.string.fill);
                                System.gc();
                            }catch (Exception e){
                                testString = "hello,china,hello,china";
                                Log.i(TAG, "doInBackground: hellochina");
                                System.gc();
                            }
                            bufferedOutputStream.write(testString);
                        }
                    }
                } catch (Exception e) {
                    file=file2;
                    Log.i(TAG, "doInBackground: Exception"+e.toString());
                    e.printStackTrace();
                }
                publishProgress(progress);
            }
            //外置SD卡的写入代码
            else if (params[0] == 3) {
                int progress = 100;
                char[] by= new char[1024];//
                long availablebegin=readWriteutils.getSDAvailableSize();
                try {
                    bufferedOutputStream = new BufferedWriter(
                            new FileWriter(file, true));
                    for (int i = 0; i < 2048 * 4096; i++) {
                       long hasFill=availablebegin-readWriteutils.getSDAvailableSize();
                        if (stop) return "stop";
                        if (delete) return "delete";
                        publishProgress(progress);
                        int j = 0;
                        for (j = 0; j < 256; j++) {
                            if (mempercentage<=hasFill/978)//一般调小数据
//                        if (true)
                            {
                                secondExe = 2;
                                return "按固定大小填充完成";
//                                break;
                            }
                            if (delete) return "delete";
                            try{
                                testString = getString(R.string.fill);
                                System.gc();
                            }catch (Exception e){
                                testString = "hello,china,hello,china";
                                Log.i(TAG, "doInBackground: hellochina");
                                System.gc();
                            }
                            bufferedOutputStream.write(by);
//                            bufferedOutputStream.write(testString);
                        }
                    }
                } catch (Exception e) {
                    file=file2;
                    Log.i(TAG, "doInBackground: Exception"+e.toString());
                    e.printStackTrace();
                }
                publishProgress(progress);

            }//结束else if

            else {
                int progress = 100;
                try {
                    bufferedOutputStream = new BufferedWriter(
                            new FileWriter(file, true));
                    long longInt = 2048 * 4096;
                    //mempercentage=mempercentage*1024;
                    for (long i = 0; i < longInt; i++) {
                        long FillSizeAreaded = (SDTotalSize - readWriteutils.getSDAvailableSize());
                        long SDAvailableSizeelse = (readWriteutils.getSDAvailableSize()) / 1024;
                        if (mempercentage >= SDAvailableSizeelse) {
                            secondExe = 2;
                            break;
                        }
                        progress = (int) (FillSizeAreaded * 100) / (int) (SDTotalSize);
                        if (stop) return "stop";
                        if (delete) return "delete";
                        publishProgress(progress);
                        int j = 0;
                        for (j = 0; j < 524; j++) {
                            if (delete) return "delete";
                            testString = getString(R.string.fill);
                            bufferedOutputStream.write(testString);
                        }
                    }
                } catch (Exception e) {
                    // dayin(e.toString());
                    e.printStackTrace();
                }
                publishProgress(progress);
            }
            Log.i("DO IN BACKGroud", "DO IN BACK");
            return "welcome use next time";
        }
        //onProgressUpdate方法用于更新进度信息
        @Override
        protected void onProgressUpdate(Integer... progresses) {
            Log.i(TAG, "onProgressUpdate(Progress... progresses) called");
            progressBar.setProgress(progresses[0]);
            displaymemory();

        }
        //onPostExecute方法用于在执行完后台任务后更新UI,显示结果
        @Override
        protected void onPostExecute(String result) {
            button.setKeepScreenOn(false);
            button.setClickable(true); //可以继续点击执行
            try {
                bufferedOutputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            if (choicemethod) {
                DisTextView.setText(R.string.display);
            } else {
                DisTextView.setText("按剩余空间填充 (单位MB)");
            }
            Log.i(TAG, "onPostExecute(Result result) called");
            {
                try {
                    bufferedOutputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (result.equals("delete")) {
                delete = false;
                // File file = new File("/sdcard/LONGCHEER.txt");
                file.delete();
                delete = false;  //11月9号更改
                // displaymemory();
                dayin("正在删除，请稍等片刻");
            }
            choiceButton.setLongClickable(true);
            button.setClickable(true);
            dayin(result);
            displaymemory();
        }

        //onCancelled方法用于在取消执行中的任务时更改UI
        @Override
        protected void onCancelled() {
            Log.i(TAG, "onCancelled() called");
            super.onCancelled();
            if (choicemethod) {
                DisTextView.setText("填充方式切换按钮");
            } else {
                DisTextView.setText("按剩余空间填充 (单位MB)");
            }
            stop = true;
            choiceButton.setClickable(true);
            cancel.setClickable(false);
            button.setKeepScreenOn(false);
        }
    }
    public void Description(View view) {
        Intent intentCostMax=new Intent(SyntasklongcheerActivity.this,CostMaxActivity.class);
        startActivity(intentCostMax);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == 1){
            Uri uri = data.getData();
            mkdirsOnTFCard(uri);
            Log.i("onActivityResult", "没有返回的resultCode" + uri);
        }
    }
    public void mkdirsOnTFCard(Uri uri) {
        //创建DocumentFile，注意只能一级一级创建，不能像File那样一次创建多级目录
        DocumentFile pickedDir = DocumentFile.fromTreeUri(this, uri);
        /**
         * 如果没有该文件夹,则创建一个新的文件并写入内容
         * 查询文件是否存在时,返回的是DocumentFile对象
         * 所以这里应该用try-catch来判断,出现异常则不存在此文件
         */
        boolean ishasDirectory;
        try {
            ishasDirectory = pickedDir.findFile("sss").exists();
        } catch (Exception e) {
            ishasDirectory = false;
        }
        if (!ishasDirectory) {
            try {
                //创建新的一个文件夹
                pickedDir.createDirectory("sss");
                //找到新文件夹的路径
                pickedDir = pickedDir.findFile("sss");
                //创建新的文件
                DocumentFile newFile = pickedDir.createFile("text/plain","hello.txt");
                //写入内容到新建文件
                 out = getContentResolver().openOutputStream(newFile.getUri());
//                mTask.execute(3);
                MyTask task3=new MyTask();
                task3.execute(3);
//                if (out != null) {
//                    out.write("test".getBytes());
//                    out.close();
//                }
                Toast.makeText(this, "创建成功", Toast.LENGTH_SHORT).show();
            } catch (Exception e) {
                e.printStackTrace();

                Toast.makeText(this, "创建失败"+e.toString(), Toast.LENGTH_SHORT).show();
            }
        }
    }
}