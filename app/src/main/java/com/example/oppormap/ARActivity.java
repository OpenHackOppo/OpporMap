package com.example.oppormap;
package com.google.ar.core.examples.java.common.helpers;

import androidx.appcompat.app.AppCompatActivity;
import com.google.ar.core.examples.java.augmentedimage.rendering.AugmentedImageRenderer;
import com.google.ar.core.examples.java.common.helpers.CameraPermissionHelper;

import android.os.Bundle;

public class ARActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ar);
    }

//    @Override
//    protected void onResume() {
//        super.onResume();
//
//        // ARCore requires camera permission to operate.
//        if (!CameraPermissionHelper.hasCameraPermission(this)) {
//            CameraPermissionHelper.requestCameraPermission(this);
//            return;
//        }
//






    }
