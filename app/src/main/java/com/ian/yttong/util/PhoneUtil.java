package com.ian.yttong.util;

import android.content.Context;
import android.os.Build;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.text.TextUtils;

import java.io.UnsupportedEncodingException;
import java.util.UUID;

/**
 * Description
 * Created by jianhongxu on 2021/12/13
 */
public class PhoneUtil {
    private PhoneUtil() {
        throw new UnsupportedOperationException("u can't instantiate me...");
    }


    /**
     * Return the unique device id.
     * <p>If the version of SDK is greater than 28, it will return an empty string.</p>
     * <p>Must hold {@code <uses-permission android:name="android.permission.READ_PHONE_STATE" />}</p>
     *
     * @return the unique device id
     */
//    @SuppressLint("HardwareIds")
//    @RequiresPermission("android.permission.READ_PHONE_STATE")
//    public static String getDeviceId(Context context) {
//        if (Build.VERSION.SDK_INT >= 29) {
//            return getUniqueID(context);
//        }
//        TelephonyManager tm = getTelephonyManager(context);
//        String deviceId = tm.getDeviceId();
//        if (!TextUtils.isEmpty(deviceId)) return deviceId;
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//            String imei = tm.getImei();
//            if (!TextUtils.isEmpty(imei)) return imei;
//            String meid = tm.getMeid();
//            return TextUtils.isEmpty(meid) ? "" : meid;
//        }
//        return "";
//    }

    public static String getUniqueID(Context context) {
        String id = null;
        final String androidId = Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);
        if (!TextUtils.isEmpty(androidId) && !"9774d56d682e549c".equals(androidId)) {
            try {
                UUID uuid = UUID.nameUUIDFromBytes(androidId.getBytes("utf8"));
                id = uuid.toString();
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }

        if (TextUtils.isEmpty(id)) {
            id = getUUID();
        }

        return TextUtils.isEmpty(id) ? UUID.randomUUID().toString() : id;
    }

//    @SuppressLint("MissingPermission")
    private static String getUUID() {
        String serial = null;

        String m_szDevIDShort = "35" +
                Build.BOARD.length() % 10 + Build.BRAND.length() % 10 +

                ((null != Build.CPU_ABI) ? Build.CPU_ABI.length() : 0) % 10 +

                Build.DEVICE.length() % 10 + Build.DISPLAY.length() % 10 +

                Build.HOST.length() % 10 + Build.ID.length() % 10 +

                Build.MANUFACTURER.length() % 10 + Build.MODEL.length() % 10 +

                Build.PRODUCT.length() % 10 + Build.TAGS.length() % 10 +

                Build.TYPE.length() % 10 + Build.USER.length() % 10; //13 位

//        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.P) {
//            try {
//                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//                    serial = Build.getSerial();
//                } else {
//                    serial = Build.SERIAL;
//                }
//                //API>=9 使用serial号
//                return new UUID(m_szDevIDShort.hashCode(), serial.hashCode()).toString();
//            } catch (Exception exception) {
//                serial = "serial" + UUID.randomUUID().toString(); // 随便一个初始化
//            }
//        } else {
            serial = Build.UNKNOWN + UUID.randomUUID().toString(); // 随便一个初始化
//        }

        //使用硬件信息拼凑出来的15位号码
        return new UUID(m_szDevIDShort.hashCode(), serial.hashCode()).toString();
    }




    private static TelephonyManager getTelephonyManager(Context context) {
        return (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
    }

}
