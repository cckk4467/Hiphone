package com.example.hellophone.function;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import com.example.hellophone.MainActivity;
import com.example.hellophone.RunningActivity;

public class PhoneCall {
    RunningActivity runningActivity;
    String number;
    public PhoneCall(RunningActivity _runningActivity,String _number){
        runningActivity=_runningActivity;
        number=_number;
    }

    public void call() {
        try{
            Intent intent = new Intent(Intent.ACTION_CALL);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);
            intent.setData(Uri.parse("tel:"+number));
            runningActivity.startActivity(intent);
        }catch (SecurityException e){
            e.printStackTrace();
        }
    }

    public int getPermission() {
        if (ContextCompat.checkSelfPermission(runningActivity, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(runningActivity,
                    new String[]{Manifest.permission.CALL_PHONE}, 1);
            return 5;
        } else {
            //拨打电话
            call();
            return 0;
        }
    }

    //@Override
//    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
//                                           @NonNull int[] grantResults) {
//        if (requestCode == 1) {
//            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//                //同意权限
//                call();//拨打电话
//            } else {
//                //拒绝权限
//                Toast.makeText(runningActivity, "您拒绝了权限的申请,无法使用！", Toast.LENGTH_SHORT).show();
//            }
//        }
//    }
}
