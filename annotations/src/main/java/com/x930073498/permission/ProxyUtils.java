package com.x930073498.permission;

import java.util.Arrays;

public class ProxyUtils {
    public static boolean isMatch(String[] targetPermissions, int[] targetCodes, String[] permissions, int[] codes, boolean isAccurate) {
        if (isAccurate) {
            return targetPermissions != null && targetCodes != null && Arrays.equals(targetPermissions, permissions) && Arrays.equals(targetCodes, codes);
        }
        System.out.println("enter this line 1");
        return targetPermissions != null && targetCodes != null && isMatch(targetPermissions, permissions, false) && isMatch(targetCodes, codes, false);
    }

    private static boolean isMatch(String[] target, String[] src, boolean isAccurate) {
        if (src == null) return !isAccurate;
        int index = Arrays.binarySearch(src, Constants.ALL);
        System.out.println("enter this line 2");
        if (index >= 0) {
            System.out.println("enter this line 3");
            return true;
        }
        System.out.println("enter this line 4");
        if (isAccurate) {
            System.out.println("enter this line 5");
            return Arrays.equals(target, src);
        }
        System.out.println("enter this line 6");
        for (String aSrc : src) {
            if (Arrays.binarySearch(target, aSrc) < 0) return false;
        }
        System.out.println("enter this line 7");
        return true;

    }

    private static boolean isMatch(int[] target, int[] src, boolean isAccurate) {
        if (src == null) return !isAccurate;
        System.out.println("enter this line 8");
        if (isAccurate) {
            System.out.println("enter this line 9");
            return Arrays.equals(target, src);
        }
        System.out.println("enter this line 10");
        for (int aSrc : src) {
            if (Arrays.binarySearch(target, aSrc) < 0) return false;
        }
        System.out.println("enter this line 11");
        return true;
    }

}
