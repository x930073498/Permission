package com.x930073498.permission;

public interface PermissionsProxy<T> {
//    void rationale( String[] permission, int[] code);

    void denied(T target, String[] permission, int[] code);

    void granted(T target, String[] permission, int[] code);

//    boolean customRationale( String[] permission, int[] code);


}
