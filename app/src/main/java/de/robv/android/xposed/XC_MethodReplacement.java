package de.robv.android.xposed;

public abstract class XC_MethodReplacement extends XCallback {
    protected abstract Object replaceHookedMethod(XC_MethodHook.MethodHookParam param) throws Throwable;
}
