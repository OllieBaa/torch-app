package com.ollie.torch;

import android.content.Context;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraCharacteristics;
import android.hardware.camera2.CameraManager;
import android.os.Handler;
import android.os.Looper;
import android.os.VibrationEffect;
import android.os.VibratorManager;
import android.util.Log;

/**
 * Turns the rear flash on and off. Android has no "is the torch on?" query,
 * so we listen for torch changes and remember the latest state.
 */
final class Torch {

    private static final String TAG = "Torch";

    private static CameraManager cameraManager;
    private static String cameraId;
    private static Boolean isOn; // null until the system has told us
    private static boolean togglePending;
    private static Context appContext;

    private Torch() {}

    static void toggle(Context context) {
        if (!init(context)) return;
        if (isOn == null) {
            // State arrives on the first callback; toggle as soon as it does.
            togglePending = true;
            return;
        }
        set(!isOn);
    }

    private static void set(boolean on) {
        try {
            cameraManager.setTorchMode(cameraId, on);
            buzz();
        } catch (CameraAccessException e) {
            // Usually means the camera app is open and holding the flash.
            Log.w(TAG, "Could not change torch", e);
        }
    }

    private static boolean init(Context context) {
        if (cameraManager != null) return cameraId != null;
        appContext = context.getApplicationContext();
        cameraManager = appContext.getSystemService(CameraManager.class);
        cameraId = findRearFlash();
        if (cameraId == null) {
            Log.w(TAG, "No camera with a flash found");
            return false;
        }
        cameraManager.registerTorchCallback(new CameraManager.TorchCallback() {
            @Override
            public void onTorchModeChanged(String id, boolean enabled) {
                if (!id.equals(cameraId)) return;
                isOn = enabled;
                if (togglePending) {
                    togglePending = false;
                    set(!enabled);
                }
            }
        }, new Handler(Looper.getMainLooper()));
        return true;
    }

    private static String findRearFlash() {
        try {
            for (String id : cameraManager.getCameraIdList()) {
                CameraCharacteristics c = cameraManager.getCameraCharacteristics(id);
                Boolean flash = c.get(CameraCharacteristics.FLASH_INFO_AVAILABLE);
                Integer facing = c.get(CameraCharacteristics.LENS_FACING);
                if (Boolean.TRUE.equals(flash) && facing != null
                        && facing == CameraCharacteristics.LENS_FACING_BACK) {
                    return id;
                }
            }
        } catch (CameraAccessException e) {
            Log.w(TAG, "Could not list cameras", e);
        }
        return null;
    }

    /** A short tap so you can feel it worked without looking. */
    private static void buzz() {
        VibratorManager vm = appContext.getSystemService(VibratorManager.class);
        if (vm == null) return;
        vm.getDefaultVibrator().vibrate(
                VibrationEffect.createPredefined(VibrationEffect.EFFECT_HEAVY_CLICK));
    }
}
