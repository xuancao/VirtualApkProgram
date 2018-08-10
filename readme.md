
virtualapk对编译环境有很大的限制，我宿主项目和插件项目中使用的编译环境是：'com.android.tools.build:gradle:3.1.0'（刚更新了，支持3.1.0）
但是插件项目中编译环境还得依赖'com.android.tools.build:gradle:2.3.3'(使用3.1.0出现错误，所以插件和宿主暂时还是统一使用稳定的2.3.3)
宿主和插件app---build.gradle中均已更新至最新为 'com.didi.virtualapk:core:0.9.7-dev'

一、插件集成

1、项目的build.gradle添加依赖

buildscript {
    repositories {
        jcenter()
    }
    dependencies {
        classpath 'com.android.tools.build:gradle:3.1.0'
        classpath 'com.didi.virtualapk:gradle:0.9.4'

        // NOTE: Do not place your application dependencies here; they belong
        // in the individual module build.gradle files
    }
}

2、宿主app中build.gradle添加依赖

头部添加：

apply plugin: 'com.didi.virtualapk.host'

在dependencies添加：

compile 'com.didi.virtualapk:core:0.9.7-dev'


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
插件项目根的build.gradle添加依赖

buildscript {
    repositories {
        jcenter()
    }
    dependencies {
        classpath 'com.android.tools.build:gradle:2.3.3'
        classpath 'com.didi.virtualapk:gradle:0.9.4'
    }
}

头部添加：
apply plugin: 'com.didi.virtualapk.plugin'
添加依赖：
//引入virtualAPK依赖
compile 'com.didi.virtualapk:core:0.9.7-dev'

插件信息配置：

virtualApk {

    //Attention:::packageId 范围 0x02 - 0x7E
    packageId = 0x61             // 插件资源id，避免资源id冲突

    targetHost ='app'            // 宿主工程的路径

    applyHostMapping = true      // 插件编译时是否启用应用宿主的apply mapping

}

生成插件：

最后一步生成插件，需要使用Gradle命令（在Terminal中执行，或者在醒目目录中执行）

gradle clean assemblePlugin



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

    File file = new File(FileUtils.getPluginCacheDir(), pluginApkName);

    return file;

    }

本项目说明：

支持将插件存放到本地assets里面进行加载，也可从网络中下载插件进行加载（需在Func中修改apk存放服务器地址url）

通信:本项目中单个进程中（单个app内部）用的是EventBus消息传递机制，
     多个app通信(多个app通信，eg:登录插件NativePlugin中登录后通知宿主和RemotePlugin插件修改登录状态)采用的是广播机制进行的消息传递，取数据采用ContentProvider方式
     跨进程通信 可以采取Messenger，AIDL，ContentProvider，Socket的方式，根据个人喜好选择
     didi/VirtualAPK Demo中的宿主向插件中取数据采用ContentProvider方式

    RemotePlugin插件中需要进行修改用户信息，但是用户信息要从宿主中得到(此处用跨进程通信。可以用aidl进行通信(所操作实体类还是都要定义相同的包名和类名，感觉直接使用intent简单)，也可以直接用Intent传递，需要将所传的UserInfo类在宿主和插件中定义为相同包名和类名)

    可以采用共享缓存数据+广播来处理数据变化+通知UI更新



问题记录：


1、宿主app启动插件，插件没有启动，而是重新开启了一个宿主Activity。

原因：插件界面的布局名和宿主app的布局名称重复。

解决办法：virtualapk中插件的资源文件名称避免和宿主重复，包括图片、布局、颜色和尺寸资源，否则会直接调用宿主中的资源。

插件如果和宿主同事依赖了相同的包，打包时插件也不会携带该包，直接使用宿主的包文件。

2.在AIDL文件中引用的Java类不会自动引入引用包名，需要在UserInfoManager.aidl中手动添加
import com.virtual.xuancao.virtualapkprogram.model.UserInfoModel;

3.插件编译时报host异常，需要宿主添加 apply plugin: 'com.didi.virtualapk.host'，但项目中已添加，试试重启项目，再进行编译




