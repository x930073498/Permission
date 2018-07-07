/*
 * PermissionAspect      2016-05-11
 * Copyright (c) 2016 hujiang Co.Ltd. All right reserved(http://www.hujiang.com).
 *
 */
package com.x930073498.permission;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;
import android.util.Log;

import com.x930073498.annotations.NeedPermission;

import junit.framework.Assert;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

@Aspect
public class PermissionAspect {

    private static final String TAG = "PermissionAspect";
    private static Map<String, CheckPermissionItem> checkPermissionItems = new HashMap<String, CheckPermissionItem>();
    private static Map<String, Boolean> sActivitySessions = new HashMap<String, Boolean>();
    private static Handler sHandler = new Handler(Looper.getMainLooper());
    private static final long DELAY_PERMISSION_DIALOG = 100;

    //    @Pointcut("execution(@com.x930073498.annotations.NeedPermission(..)&& @annotation(needPermission)")
//    public void pointcutOnNeedPermissionMethod(NeedPermission needPermission) {
//
//    }
    @Pointcut("execution(@com.x930073498.annotations.NeedPermission * *(..)) && @annotation(needPermission)")
    public void pointcutOnNeedPermissionMethod(NeedPermission needPermission) {

    }

    @Pointcut("execution(* android.app.Activity.onCreate(..)) && !within(android.support.v7.app.AppCompatActivity)" +
            " && !within(android.support.v4.app.FragmentActivity)" +
            " && !within(android.support.v4.app.BaseFragmentActivityDonut)" +
            " && !within(com.x930073498.permission.ShadowPermissionActivity)")
    public void pointcutOnActivityCreate() {

    }

    //用在返回为void的方法上，包括private, public , static等修饰的方法
    @Around("pointcutOnNeedPermissionMethod(needPermission)")
    public void adviceOnNeedPermissionMethod(final ProceedingJoinPoint joinPoint, final NeedPermission needPermission) throws Throwable {
        Log.d(TAG, "adviceOnNeedPermissionMethod: ");
        final Object target = joinPoint.getTarget();
        String targetName = target.getClass().getName();

        try {
            if (needPermission == null) {
                joinPoint.proceed();
                return;
            }
            final PermissionsProxy proxy = getProxy(calculateProxyName(targetName), needPermission.impl());
            String[] permissions = needPermission.permissions();
            if (permissions.length > 0) {
                Context context = PermissionCheckSDK.application;
                PermissionItem permissionItem = new PermissionItem(permissions);
                permissionItem.requestCodes = needPermission.requestCode();
                String rationalMsg = chooseContent(context, needPermission.rationalMessage(), needPermission.rationalMsgResId());
                String rationalBtn = chooseContent(context, needPermission.rationalButton(), needPermission.rationalBtnResId());
                String deniedMsg = chooseContent(context, needPermission.deniedMessage(), needPermission.deniedMsgResId());
                String deniedBtn = chooseContent(context, needPermission.deniedButton(), needPermission.deniedBtnResId());
                String settingBtn = chooseContent(context, needPermission.settingText(), needPermission.settingResId());
                permissionItem.requestCodes = needPermission.requestCode();

                if (!TextUtils.isEmpty(rationalMsg)
                        && !TextUtils.isEmpty(rationalBtn)) {
                    permissionItem.rationalMessage(rationalMsg).rationalButton(rationalBtn);
                }

                if (!TextUtils.isEmpty(deniedMsg)
                        && !TextUtils.isEmpty(deniedBtn)) {
                    permissionItem.deniedMessage(deniedMsg).deniedButton(deniedBtn);
                }

                if (!TextUtils.isEmpty(settingBtn)) {
                    permissionItem.settingText(settingBtn);
                }

                permissionItem.needGotoSetting(needPermission.needGotoSetting());

                CheckPermission.instance(context).check(permissionItem, new PermissionListener() {
                    @Override
                    public void permissionGranted(String[] permissions, int[] requestCodes) {
                        try {
                            if (proxy != null) {
                                proxy.granted(target, permissions, requestCodes);
                            }
                            joinPoint.proceed();
                        } catch (Throwable throwable) {
                            throwable.printStackTrace();
                        }
                    }

                    @Override
                    public void permissionDenied(String[] permissions, int[] requestCodes) {
                        if (proxy != null) {
                            proxy.denied(target, permissions, requestCodes);
                        }
                        if (needPermission.runIgnorePermission()) {
                            try {
                                joinPoint.proceed();
                            } catch (Throwable throwable) {
                                throwable.printStackTrace();
                            }
                        }
                    }
                });
            }

        } catch (NoSuchMethodException e) {
            e.printStackTrace();
            Log.d(TAG, "adviceOnNeedPermissionMethod: ");
            joinPoint.proceed();
        }
    }

//    @Around("pointcutOnActivityCreate()")
//    public void adviceOnActivityCreate(final ProceedingJoinPoint joinPoint) throws Throwable {
//        final Activity target = (Activity) joinPoint.getTarget();
//        final String targetName = target.getClass().getName();
//
//        if (!sActivitySessions.containsKey(targetName)) {
//
//            sHandler.postDelayed(new Runnable() {
//                @Override
//                public void run() {
//                    NeedPermission np = target.getClass().getAnnotation(NeedPermission.class);
//                    final PermissionsProxy proxy = getProxy(calculateProxyName(targetName));
//                    if (np != null) {
//                        String[] permissions = np.permissions();
//
//                        if (permissions.length > 0) {
//                            processCheckPermissionOnActivity(target
//                                    , permissions
//                                    , np.requestCode()
//                                    , chooseContent(target, np.rationalMessage(), np.rationalMsgResId())
//                                    , chooseContent(target, np.rationalButton(), np.rationalBtnResId())
//                                    , chooseContent(target, np.deniedMessage(), np.deniedMsgResId())
//                                    , chooseContent(target, np.deniedButton(), np.deniedBtnResId())
//                                    , chooseContent(target, np.settingText(), np.settingResId())
//                                    , np.needGotoSetting()
//                                    , np.runIgnorePermission(), proxy);
//                        }
//                    } else {
//                        String classPath = joinPoint.getSourceLocation().getWithinType().getName();
//
//                        if (checkPermissionItems.containsKey(classPath)) {
//                            CheckPermissionItem checkPermissionItem = checkPermissionItems.get(classPath);
//                            processCheckPermissionOnActivity(target
//                                    , checkPermissionItem.permissionItem.permissions
//                                    , checkPermissionItem.permissionItem.requestCodes
//                                    , checkPermissionItem.permissionItem.rationalMessage
//                                    , checkPermissionItem.permissionItem.rationalButton
//                                    , checkPermissionItem.permissionItem.deniedMessage
//                                    , checkPermissionItem.permissionItem.deniedButton
//                                    , checkPermissionItem.permissionItem.settingText
//                                    , checkPermissionItem.permissionItem.needGotoSetting
//                                    , checkPermissionItem.permissionItem.runIgnorePermission, proxy);
//                        }
//                    }
//                }
//            }, DELAY_PERMISSION_DIALOG);
//
//            sActivitySessions.put(target.getClass().getName(), true);
//        }
//
//        joinPoint.proceed();
//    }

    private static String chooseContent(Context context, String strContent, int resId) {
        if (context == null) {
            return null;
        }

        if (TextUtils.isEmpty(strContent)) {
            if (resId <= 0) {
                return strContent;
            }

            try {
                return context.getString(resId);
            } catch (Resources.NotFoundException e) {
                return strContent;
            }
        }

        return strContent;
    }

    private static void processCheckPermissionOnActivity(final Activity target, String[] permissions, int[] requestCodes, String rationalMessage, String rationalButton
            , String deniedMessage, String deniedButton, String settingText, boolean needGotoSetting, final boolean runIgnorePermission, final PermissionsProxy proxy) {

        Assert.assertTrue(permissions != null && permissions.length > 0);

        final PermissionItem permissionItem = new PermissionItem(permissions);
        permissionItem.requestCodes = requestCodes;

        if (!TextUtils.isEmpty(rationalMessage)
                && !TextUtils.isEmpty(rationalButton)) {
            permissionItem.rationalMessage(rationalMessage).rationalButton(rationalButton);
        }

        if (!TextUtils.isEmpty(deniedMessage)
                && !TextUtils.isEmpty(deniedButton)) {
            permissionItem.deniedMessage(deniedMessage).deniedButton(deniedButton);
        }

        if (!TextUtils.isEmpty(settingText)) {
            permissionItem.settingText(settingText);
        }

        permissionItem.needGotoSetting(needGotoSetting);

        CheckPermission.instance(target).check(permissionItem, new PermissionListener() {
            @Override
            public void permissionGranted(String[] permissions, int[] requestCodes) {

                if (proxy != null) {
                    proxy.granted(target, permissions, requestCodes);
                }
                sActivitySessions.remove(target.getClass().getName());
            }

            @Override
            public void permissionDenied(String[] permissions, int[] requestCodes) {
                sActivitySessions.remove(target.getClass().getName());
                if (proxy != null) {
                    proxy.denied(target, permissions, requestCodes);
                }
                if (!runIgnorePermission) {
                    target.finish();
                }
            }
        });
    }

    public static void addCheckPermissionItem(CheckPermissionItem item) {
        if (item != null && item.classPath != null) {
            checkPermissionItems.put(item.classPath, item);
        }
    }

    private static PermissionsProxy getProxy(String name, Class<? extends PermissionsProxy> impl) {

        boolean useName = !TextUtils.isEmpty(name) && impl == PermissionsProxy.class;

        try {
            Class clazz = useName ? Class.forName(name) : impl;
            clazz = clazz == null ? impl : clazz;
            if (clazz.isInterface()) return null;
            return (PermissionsProxy) clazz.newInstance();
        } catch (ClassNotFoundException e) {
            return getProxy("", impl);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
            return null;
        } catch (InstantiationException e) {
            e.printStackTrace();
            return null;
        }


    }

    private static String calculateProxyName(String targetName) {
        return String.format(Locale.CHINA, "%s%s", targetName, "$$Proxy");
    }

}