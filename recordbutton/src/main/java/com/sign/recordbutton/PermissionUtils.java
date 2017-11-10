package com.sign.recordbutton;


import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import java.util.ArrayList;

/**
 * 权限处理
 */
public class PermissionUtils {

    /**
     * Permission Group	Permissions
     * <p>
     * group:android.permission-group.CONTACTS
     * permission:android.permission.WRITE_CONTACTS
     * permission:android.permission.GET_ACCOUNTS
     * permission:android.permission.READ_CONTACTS
     * <p>
     * group:android.permission-group.PHONE
     * permission:android.permission.READ_CALL_LOG
     * permission:android.permission.READ_PHONE_STATE
     * permission:android.permission.CALL_PHONE
     * permission:android.permission.WRITE_CALL_LOG
     * permission:android.permission.USE_SIP
     * permission:android.permission.PROCESS_OUTGOING_CALLS
     * permission:com.android.voicemail.permission.ADD_VOICEMAIL
     * <p>
     * group:android.permission-group.CALENDAR
     * permission:android.permission.READ_CALENDAR
     * permission:android.permission.WRITE_CALENDAR
     * <p>
     * group:android.permission-group.CAMERA
     * permission:android.permission.CAMERA
     * <p>
     * group:android.permission-group.SENSORS
     * permission:android.permission.BODY_SENSORS
     * <p>
     * group:android.permission-group.LOCATION
     * permission:android.permission.ACCESS_FINE_LOCATION
     * permission:android.permission.ACCESS_COARSE_LOCATION
     * <p>
     * group:android.permission-group.STORAGE
     * permission:android.permission.READ_EXTERNAL_STORAGE
     * permission:android.permission.WRITE_EXTERNAL_STORAGE
     * <p>
     * group:android.permission-group.MICROPHONE
     * permission:android.permission.RECORD_AUDIO
     * <p>
     * group:android.permission-group.SMS
     * permission:android.permission.READ_SMS
     * permission:android.permission.RECEIVE_WAP_PUSH
     * permission:android.permission.RECEIVE_MMS
     * permission:android.permission.RECEIVE_SMS
     * permission:android.permission.SEND_SMS
     * permission:android.permission.READ_CELL_BROADCASTS
     */
    private static final String TAG = "PermissionUtils";

    @TargetApi(Build.VERSION_CODES.M)
    public static boolean checkPermission(Object cxt, String permission, int requestCode) {
        return checkPermission(cxt, permission, "", requestCode);
    }

    /**
     * 检查有无权限
     *
     * @param cxt          上下文
     * @param toastMessage 要显示的消息
     * @param permission   需要的权限
     * @param requestCode  返回code
     * @return 返回有无权限
     */
    @TargetApi(Build.VERSION_CODES.M)
    public static boolean checkPermission(Object cxt, String permission, String toastMessage, int requestCode) {
        if (!checkSelfPermissionWrapper(cxt, permission)) {
            requestPermissionsWrapper(cxt, new String[]{permission}, requestCode);
            if (!TextUtils.isEmpty(toastMessage)) {
                if (cxt instanceof Activity) {
                    Toast.makeText(((Activity) cxt).getApplicationContext(), toastMessage, Toast.LENGTH_SHORT).show();
                } else if (cxt instanceof Fragment) {
                    Toast.makeText(((Fragment) cxt).getContext(), toastMessage, Toast.LENGTH_SHORT).show();
                }
            }
            return false;
        }
        return true;
    }

    @TargetApi(Build.VERSION_CODES.M)
    public static boolean checkPermissions(Object cxt, String[] permissions, int requestCode) {
        return checkPermissions(cxt, permissions, "", requestCode);
    }

    /**
     * 检查有无权限
     *
     * @param cxt          上下文
     * @param permissions  权限数组
     * @param requestCode  返回code
     * @param toastMessage 要显示的消息
     * @return 有无权限
     */
    @TargetApi(Build.VERSION_CODES.M)
    public static boolean checkPermissions(Object cxt, String[] permissions, String toastMessage, int requestCode) {
        String[] tempPermissions = checkSelfPermissionArray(cxt, permissions);
        if (tempPermissions.length > 0) {
            requestPermissionsWrapper(cxt, tempPermissions, requestCode);
            if (!TextUtils.isEmpty(toastMessage)) {
                if (cxt instanceof Activity) {
                    Toast.makeText(((Activity) cxt).getApplicationContext(), toastMessage, Toast.LENGTH_SHORT).show();
                } else if (cxt instanceof Fragment) {
                    Toast.makeText(((Fragment) cxt).getContext(), toastMessage, Toast.LENGTH_SHORT).show();
                }
            }
            return false;
        } else {
            return true;
        }

    }

    /**
     * 请求权限
     *
     * @param cxt         上下文
     * @param permission  权限
     * @param requestCode 返回code
     */
    private static void requestPermissionsWrapper(Object cxt, String[] permission, int requestCode) {
        if (cxt instanceof Activity) {
            Activity activity = (Activity) cxt;
            ActivityCompat.requestPermissions(activity, permission, requestCode);
        } else if (cxt instanceof Fragment) {
            Fragment fragment = (Fragment) cxt;
            fragment.requestPermissions(permission, requestCode);
        } else {
            throw new RuntimeException("cxt is net a activity or fragment");
        }
    }

    /**
     * 不在提醒后的提醒
     * 向用户解释我们为何要这个权限
     *
     * @param cxt        上下文
     * @param permission 权限
     * @return true不再提醒false提醒
     */
    private static boolean shouldShowRequestPermissionRationaleWrapper(Object cxt, String permission) {
        if (cxt instanceof Activity) {
            Activity activity = (Activity) cxt;
            return ActivityCompat.shouldShowRequestPermissionRationale(activity,
                    permission);
        } else if (cxt instanceof Fragment) {
            Fragment fragment = (Fragment) cxt;
            return fragment.shouldShowRequestPermissionRationale(permission);
        } else {
            throw new RuntimeException("cxt is net a activity or fragment");
        }
    }

    /**
     * 检查某个权限
     *
     * @param cxt        上下文
     * @param permission 权限
     * @return 有无权限
     */
    @TargetApi(Build.VERSION_CODES.M)
    public static boolean checkSelfPermissionWrapper(Object cxt, String permission) {
        if (cxt instanceof Activity) {
            Activity activity = (Activity) cxt;
            return ActivityCompat.checkSelfPermission(activity,
                    permission) == PackageManager.PERMISSION_GRANTED;
        } else if (cxt instanceof Fragment) {
            Fragment fragment = (Fragment) cxt;
            return fragment.getActivity().checkSelfPermission(permission) == PackageManager.PERMISSION_GRANTED;
        } else {
            throw new RuntimeException("cxt is net a activity or fragment");
        }
    }

    /**
     * 检查权限数组
     *
     * @param cxt        上下文
     * @param permission 权限数组
     * @return 有无权限
     */
    private static String[] checkSelfPermissionArray(Object cxt, String[] permission) {
        ArrayList<String> permiList = new ArrayList<>();
        for (String p : permission) {
            if (!checkSelfPermissionWrapper(cxt, p)) {
                permiList.add(p);
            }
        }

        return permiList.toArray(new String[permiList.size()]);
    }

    /**
     * 权限权限数组
     *
     * @param cxt         上下文
     * @param permission  权限数组
     * @param requestCode 返回code
     * @return 有无权限
     */
    @TargetApi(Build.VERSION_CODES.M)
    public static boolean checkPermissionArray(Object cxt, String[] permission, int requestCode) {
        String[] permissionNo = checkSelfPermissionArray(cxt, permission);
        if (permissionNo.length > 0) {
            requestPermissionsWrapper(cxt, permissionNo, requestCode);
            return false;
        } else {
            return true;
        }
    }

    /***
     * 检查有无权限
     *
     * @param grantResults 权限数组
     * @return 有无
     */
    public static boolean verifyPermissions(int[] grantResults) {
        // At least one result must be checked.
        if (grantResults.length < 1) {
            return false;
        }

        // Verify that each required permission has been granted, otherwise return false.
        for (int result : grantResults) {
            if (result != PackageManager.PERMISSION_GRANTED) {
                return false;
            }
        }
        return true;
    }

    /**
     * 检测系统弹出权限
     *
     * @param cxt 上下文
     * @param req 返回码
     * @return 有无权限
     */
    @TargetApi(Build.VERSION_CODES.M)
    public static boolean checkSettingAlertPermission(Object cxt, int req) {
        if (cxt instanceof Activity) {
            Activity activity = (Activity) cxt;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (!Settings.canDrawOverlays(activity.getBaseContext())) {
                    Log.i(TAG, "Setting not permission");

                    Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                            Uri.parse("package:" + activity.getPackageName()));
                    activity.startActivityForResult(intent, req);
                    return false;
                }
            }
        } else if (cxt instanceof Fragment) {
            Fragment fragment = (Fragment) cxt;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (!Settings.canDrawOverlays(fragment.getActivity())) {
                    Log.i(TAG, "Setting not permission");

                    Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                            Uri.parse("package:" + fragment.getActivity().getPackageName()));
                    fragment.startActivityForResult(intent, req);
                    return false;
                }
            }
        } else {
            throw new RuntimeException("cxt is net a activity or fragment");
        }

        return true;
    }

    /**
     * 检测系统WriteSettings权限
     *
     * @param cxt 上下文
     * @param req 返回码
     * @return 有无权限
     */
    @TargetApi(Build.VERSION_CODES.M)
    public static boolean checkWriteSettingPermission(Object cxt, int req) {
        if (cxt instanceof Activity) {
            Activity activity = (Activity) cxt;
            if (!Settings.canDrawOverlays(activity.getBaseContext())) {
                Log.i(TAG, "Setting not permission");

                Intent intent = new Intent(Settings.ACTION_MANAGE_WRITE_SETTINGS,
                        Uri.parse("package:" + activity.getPackageName()));
                activity.startActivityForResult(intent, req);
                return false;
            }
        } else if (cxt instanceof Fragment) {
            Fragment fragment = (Fragment) cxt;
            if (!Settings.canDrawOverlays(fragment.getActivity())) {
                Log.i(TAG, "Setting not permission");

                Intent intent = new Intent(Settings.ACTION_MANAGE_WRITE_SETTINGS,
                        Uri.parse("package:" + fragment.getActivity().getPackageName()));
                fragment.startActivityForResult(intent, req);
                return false;
            }
        } else {
            throw new RuntimeException("cxt is net a activity or fragment");
        }

        return true;
    }
}
