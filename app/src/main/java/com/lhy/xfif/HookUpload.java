package com.lhy.xfif;


import android.content.Context;

import java.lang.reflect.Field;
import java.util.List;

import de.robv.android.xposed.IXposedHookLoadPackage;
import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.XposedHelpers;
import de.robv.android.xposed.callbacks.XC_LoadPackage;

/**
 * FIF 口语训练学生版任意改分 HOOK 1.0
 *
 * @author lhy 1595901624
 */
public class HookUpload implements IXposedHookLoadPackage {
    //    private SharedPreferences sharedPreferences;
    private ConfigPreferences configPreferences = ConfigPreferences.getInstance();

    @Override
    public void handleLoadPackage(XC_LoadPackage.LoadPackageParam lpparam) throws Throwable {

        /**
         * 加载自带的CLASSLOADER
         *
         */
        if (lpparam.packageName.equals("com.fifedu.tsdx")) {
//            XposedBridge.log("读取StubApp");
            Class clazz = lpparam.classLoader.loadClass("com.stub.StubApp");
            XposedBridge.log("classLoader: " + lpparam.classLoader.toString());
            XposedHelpers.findAndHookMethod("com.stub.StubApp", lpparam.classLoader,
                    "getOrigApplicationContext", Context.class, new XC_MethodHook() {
                        @Override
                        protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
//                            XposedBridge.log("Hook!" + configPreferences.getDefaultConfig().getScore() + "-------");
                        }

                        @Override
                        protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                            /**
                             * 获取360加固的CLASSLOADER
                             *
                             */
                            final Context context = (Context) param.args[0];
                            ClassLoader classLoader = context.getClassLoader();
//                            XposedBridge.log("AfterHook!" + classLoader.toString());

                            /**
                             * Hook com.fifedu.tsdx.ui.activity.report.challengereport.Report中的uploadAnsByCode方法
                             * 上传时截取questionList，修改分数
                             */
                            XposedHelpers.findAndHookMethod("com.fifedu.tsdx.ui.activity.report.challengereport.Report",
                                    classLoader, "uploadAnsByCode", String.class, new XC_MethodHook() {
                                        @Override
                                        protected void beforeHookedMethod(MethodHookParam param) throws Throwable {

                                            if (configPreferences.getDefaultConfig().isOpen()) {
                                                Field questionList = param.thisObject.getClass().getDeclaredField("questionList");
                                                questionList.setAccessible(true);
                                                List list = (List) questionList.get(param.thisObject);
                                                //获取对象的分数
                                                for (int i = 0; i < list.size(); i++) {
                                                    Field scoreField = list.get(i).getClass().getDeclaredField("totalScore");
                                                    scoreField.setAccessible(true);
                                                    /**
                                                     * 关键地方
                                                     *
                                                     * 修改分数
                                                     */
                                                    scoreField.set(list.get(i), configPreferences.getDefaultConfig().getScore());
                                                }
                                                //将list赋值给Activity中的list
                                                questionList.set(param.thisObject, list);
                                            } else {
                                                //不做任何事情
                                            }

                                        }

                                        @Override
                                        protected void afterHookedMethod(MethodHookParam param) throws Throwable {
//                                            XposedBridge.log("uploadAnsByCode after");
                                        }
                                    });
                        }
                    });
        }
    }

}