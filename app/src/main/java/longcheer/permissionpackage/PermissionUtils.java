package longcheer.permissionpackage;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;

/**
 * Created by luominming on 2017/10/18.
 */
public class  PermissionUtils {
    public  void verifyStoragePermissions(Activity activity,String[] permissionArray ) {
        int permission = ActivityCompat.checkSelfPermission(activity,
                Manifest.permission.WRITE_SETTINGS);
        if (permission != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(activity, permissionArray, 1);

        } }
}
