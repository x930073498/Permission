package com.x930073498.permission;

import java.util.Arrays;

public class ProxyUtils {
    public static boolean isMatch(String[] targetPermissions, int[] targetCodes, String[] permissions, int[] codes, boolean isAccurate) {
        if (isAccurate) {
            return targetPermissions != null && targetCodes != null && Arrays.equals(targetPermissions, permissions) && Arrays.equals(targetCodes, codes);
        }
        return targetPermissions != null && targetCodes != null && isMatch(targetPermissions, permissions, false) && isMatch(targetCodes, codes, false);
    }

    private static boolean isMatch(String[] target, String[] src, boolean isAccurate) {
        if (src == null) return !isAccurate;
        int index = Arrays.binarySearch(src, Constants.ALL);
        if (index >= 0) {
            return true;
        }
        System.out.println("enter this line 4");
        if (isAccurate) {
            return Arrays.equals(target, src);
        }
        for (String aSrc : src) {
            if (Arrays.binarySearch(target, aSrc) < 0) return false;
        }
        return true;

    }

    private static boolean isMatch(int[] target, int[] src, boolean isAccurate) {
        if (src == null) return !isAccurate;
        if (isAccurate) {
            return Arrays.equals(target, src);
        }
        for (int aSrc : src) {
            if (Arrays.binarySearch(target, aSrc) < 0) return false;
        }
        return true;
    }

}
