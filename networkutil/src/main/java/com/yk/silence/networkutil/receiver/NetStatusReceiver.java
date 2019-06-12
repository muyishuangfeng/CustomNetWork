package com.yk.silence.networkutil.receiver;


import com.yk.silence.networkutil.MethodManager;
import com.yk.silence.networkutil.annotation.NetSubscribe;
import com.yk.silence.networkutil.type.NetType;

import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class NetStatusReceiver {

    private NetType mNetType;//网络类型

    private Map<Object, List<MethodManager>> networkList;


    public NetStatusReceiver() {
        mNetType = NetType.TYPE_NONE;
        networkList = new HashMap<>();
    }

    /**
     * 分发
     */
    public void post(NetType netType) {
        //所有的注册类
        Set<Object> subscribeClazzSet = networkList.keySet();
        this.mNetType = netType;
        for (Object subscribeClazz : subscribeClazzSet) {
            List<MethodManager> methodManagerList = networkList.get(subscribeClazz);
            executeInvoke(subscribeClazz, methodManagerList);
        }
    }

    private void executeInvoke(Object subscribeClazz, List<MethodManager> methodManagerList) {
        if (methodManagerList != null) {
            for (MethodManager subscribeMethod : methodManagerList) {

                switch (subscribeMethod.getMode()) {
                    case AUTO:
                        invoke(subscribeMethod, subscribeClazz, mNetType);
                        break;

                    case WIFI:
                        if (mNetType == NetType.TYPE_WIFI || mNetType == NetType.TYPE_NONE)
                            invoke(subscribeMethod, subscribeClazz, mNetType);
                        break;

                    case WIFI_CONNECT:
                        if (mNetType == NetType.TYPE_WIFI)
                            invoke(subscribeMethod, subscribeClazz, mNetType);
                        break;

                    case MOBILE:
                        if (mNetType == NetType.TYPE_MOBILE || mNetType == NetType.TYPE_NONE)
                            invoke(subscribeMethod, subscribeClazz, mNetType);
                        break;

                    case MOBILE_CONNECT:
                        if (mNetType == NetType.TYPE_MOBILE) {
                            invoke(subscribeMethod, subscribeClazz, mNetType);
                        }
                        break;

                    case NONE:
                        if (mNetType == NetType.TYPE_NONE)
                            invoke(subscribeMethod, subscribeClazz, mNetType);

                    default:
                }
            }
        }
    }

    private void invoke(MethodManager subscribeMethod, Object subscribeClazz, NetType netType) {
        Method execute = subscribeMethod.getMethod();
        try {
            //有参数时
            if (subscribeMethod.getParameterClazz() != null) {
                if (subscribeMethod.getParameterClazz().isAssignableFrom(mNetType.getClass())) {
                    execute.invoke(subscribeClazz, netType);
                }
            } else {
                execute.invoke(subscribeClazz);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 注册
     */
    public void registerObserver(Object mContext) {
        List<MethodManager> methodList = networkList.get(mContext);
        if (methodList == null) {
//        开始添加
            methodList = findAnnotationMethod(mContext);
            networkList.put(mContext, methodList);
        }
        executeInvoke(mContext, networkList.get(mContext));
    }

    private List<MethodManager> findAnnotationMethod(Object mContext) {
        List<MethodManager> methodManagerList = new ArrayList<>();
//        获取到activity fragment
        Class<?> clazz = mContext.getClass();
        Method[] methods = clazz.getDeclaredMethods();
        for (Method method : methods) {
            NetSubscribe netSubscribe = method.getAnnotation(NetSubscribe.class);
            if (netSubscribe == null) {
                continue;
            }
            //注解方法校验返回值
            Type genericReturnType = method.getGenericReturnType();
            if (!"void".equalsIgnoreCase(genericReturnType.toString())) {
                throw new IllegalArgumentException("you " + method.getName() + "method return value must be void");
            }
            //判断参数
            Class<?>[] parameterTypes = method.getParameterTypes();
            MethodManager methodManager;
            if (parameterTypes.length == 0) {
                methodManager = new MethodManager(null, netSubscribe.mode(), method);
            } else if (parameterTypes.length == 1) {
                methodManager = new MethodManager(parameterTypes[0], netSubscribe.mode(), method);
            } else {
                throw new IllegalArgumentException("Your method " + method.getName() + " can have at most one parameter of type NetType ");
            }
            methodManagerList.add(methodManager);
        }

        return methodManagerList;
    }

    /**
     * 反注册
     */
    public void unRegisterObserver(Object mContext) {
        if (!networkList.isEmpty()) {
            networkList.remove(mContext);
        }
    }

    public void unRegisterAllObserver() {
        if (!networkList.isEmpty()) {
            networkList.clear();
            networkList = null;
        }
    }
}
