package de.robv.android.xposed;

public class XposedBridge {
    public static void log(String text) {
        android.util.Log.d("Xposed", text);
    }
    
    public static void log(Throwable t) {
        android.util.Log.e("Xposed", "Exception", t);
    }
}
