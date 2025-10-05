package de.robv.android.xposed;

public abstract class XC_MethodHook extends XCallback {
    public static class MethodHookParam {
        public Object thisObject;
        public Object[] args;
        public Object result;
        public Throwable throwable;
        
        public MethodHookParam() {
            this.thisObject = null;
            this.args = null;
            this.result = null;
            this.throwable = null;
        }
    }
    
    protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
        // Override in subclasses
    }
    
    protected void afterHookedMethod(MethodHookParam param) throws Throwable {
        // Override in subclasses
    }
}
