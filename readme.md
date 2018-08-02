
virtualapk对编译环境有很大的限制，我宿主项目和插件项目中使用的编译环境是：'com.android.tools.build:gradle:2.3.3'

一、插件集成

1、项目的build.gradle添加依赖
buildscript {
    repositories {
        jcenter()
    }
    dependencies {
        classpath 'com.android.tools.build:gradle:2.3.3'
        classpath 'com.didi.virtualapk:gradle:0.9.4'

        // NOTE: Do not place your application dependencies here; they belong
        // in the individual module build.gradle files
    }
}

2、宿主app中build.gradle添加依赖
头部添加：
apply plugin: 'com.didi.virtualapk.host'
在dependencies添加：
compile 'com.didi.virtualapk:core:0.9.5'


3、在在App的工程模块proguard-rules.pro文件添加混淆规则：
-keep class com.didi.virtualapk.internal.VAInstrumentation { *; }
-keep class com.didi.virtualapk.internal.PluginContentResolver { *; }

-dontwarn com.didi.virtualapk.**
-dontwarn android.**
-keep class android.** { *; }

4、初始化
在合适的地方添加：
PluginManager.getInstance(context).init();

5、加载模块
//初始化插件管理器
PluginManager pluginManager = PluginManager.getInstance(context.getApplicationContext());
//获取插件APK的文件对象
File apk = getPluginFile(context, pluginId);
if (apk.exists()){
    try {
        //加载插件
        pluginManager.loadPlugin(apk);
        return true;
    } catch (Exception e) {
        e.printStackTrace();
    }
}

检测模块是否已经加载：
PluginManager pluginManager = PluginManager.getInstance(context.getApplicationContext());
//获取指定包名的插件对象
if (pluginManager.getLoadedPlugin(PackageName) != null){
    return true; //已经加载
}

启动模块界面：
if(isPluginLoaded(activity,pluginId)){ //模块是否加载，pluginId为model中设置的插件id
    //代表模块加载成功 页面可赢正常跳转功能

    try {
        Intent intent = new Intent();
        intent.setClassName(packageName, className);
        activity.startActivity(intent);
    }catch (Exception e){
        e.printStackTrace();
        Log.e("sun", "startActivity: " +"启动插件失败");
    }
    return true;
}


二、插件工程接入

头部添加：
apply plugin: 'com.didi.virtualapk.plugin'
添加依赖：
//引入virtualAPK依赖
compile 'com.didi.virtualapk:core:0.9.5'

插件信息配置：
virtualApk {
    packageId = 0x61             // 插件资源id，避免资源id冲突
    targetHost ='app'            // 宿主工程的路径
    applyHostMapping = true      // 插件编译时是否启用应用宿主的apply mapping
}

生成插件：
最后一步生成插件，需要使用Gradle命令（在Terminal中执行，或者在醒目目录中执行）
gradle clean assemblePlugin
或者
gradlew clean assemblePlugin


三、运行插件

1、demo中的plugin是push到模拟器或者手机的指定目录中。（真实项目应该是从服务器下载）

2、运行宿主app，在指定位置启动model就可以了（参考：一、插件集成中的5步骤）

注意：
模块的文件名称和路径要和代码中保持一致
/**
 * 通过插件id获取插件文件对象
 * @param context
 * @param pluginId
 * @return, 返回插件文件对象
 */
public static File getPluginFile(Context context, int pluginId){
    //定义插件apk的名称
    String pluginApkName = getPluginName(pluginId);  //demo中做了简单封装，根据pluginId获model的包名
    //定义插件文件对象
    File file = new File(getExternalStorageDirectory(), pluginApkName);
    Log.e("sun", "getPluginFile: " +  file.getAbsolutePath());
    return file;
}

问题记录：

1、Error:This Gradle plugin requires Studio 3.0 minimum
这是由于项目build.gradle依赖了版本：classpath 'com.didi.virtualapk:gradle:0.9.8.3'
解决办法:1、升级androidstudio；2、依赖较低版本的库；3、将以下内容添加到gradle.properties中：android.injected.build.model.only.versioned = 3

2、Error:Failed to resolve: com.android.support:support-fragment:27.0.1
在项目的build.gradle使用：
allprojects {
    repositories {
        jcenter()
        maven { url "https://maven.google.com" }
    }
}

3、获取模拟器读写权限，把包导入文件夹
在android sdk的platform-tools路径下，打开控制台，依次输入 adb shell / su，运行前缀变为#，则获取到权限  (android7.0以下)；

4、通过宿主app启动插件，报错：ActivityNotFoundException
原因：Application未在xml中注册，导致sdk的初始化工作没有完成。

5、宿主app启动插件，插件没有启动，而是重新开启了一个宿主Activity。
原因：插件界面的布局名和宿主app的布局名称重复。
解决办法：virtualapk中插件的资源文件名称避免和宿主重复，包括图片、布局、颜色和尺寸资源，否则会直接调用宿主中的资源。
插件如果和宿主同事依赖了相同的包，打包时插件也不会携带该包，直接使用宿主的包文件。






