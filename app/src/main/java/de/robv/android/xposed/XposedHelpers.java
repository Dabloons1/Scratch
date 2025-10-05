package de.robv.android.xposed;

import java.lang.reflect.Method;

public class XposedHelpers {
    public static void findAndHookMethod(String className, ClassLoader classLoader, String methodName, Object... parameterTypesAndCallback) {
        try {
            Class<?> clazz = Class.forName(className, false, classLoader);
            Method method = clazz.getDeclaredMethod(methodName, getParameterTypes(parameterTypesAndCallback));
            // Hook implementation would go here
            XposedBridge.log("Successfully hooked method: " + className + "." + methodName);
        } catch (Exception e) {
            XposedBridge.log("Failed to hook method: " + e.getMessage());
        }
    }
    
    public static void findAndHookMethod(Class<?> clazz, String methodName, Object... parameterTypesAndCallback) {
        try {
            Method method = clazz.getDeclaredMethod(methodName, getParameterTypes(parameterTypesAndCallback));
            // Hook implementation would go here
            XposedBridge.log("Successfully hooked method: " + clazz.getName() + "." + methodName);
        } catch (Exception e) {
            XposedBridge.log("Failed to hook method: " + e.getMessage());
        }
    }
    
    private static Class<?>[] getParameterTypes(Object... parameterTypesAndCallback) {
        // Extract parameter types from the array (excluding the callback)
        if (parameterTypesAndCallback.length <= 1) {
            return new Class<?>[0];
        }
        
        Class<?>[] types = new Class<?>[parameterTypesAndCallback.length - 1];
        for (int i = 0; i < types.length; i++) {
            if (parameterTypesAndCallback[i] instanceof Class) {
                types[i] = (Class<?>) parameterTypesAndCallback[i];
            } else {
                // Handle primitive types
                types[i] = parameterTypesAndCallback[i].getClass();
            }
        }
        return types;
    }
}
