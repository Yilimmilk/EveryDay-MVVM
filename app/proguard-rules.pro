# Add project specific ProGuard rules here.
# You can control the set of applied configuration files using the
# proguardFiles setting in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class name to the JavaScript interface
# class:
#-keepclassmembers class fqcn.of.javascript.interface.for.webview {
#   public *;
#}

# Uncomment this to preserve the line number information for
# debugging stack traces.
#-keepattributes SourceFile,LineNumberTable

# If you keep the line number information, uncomment this to
# hide the original source file name.
#-renamesourcefileattribute SourceFile
############################################

# 对于一些基本指令的添加

############################################
# 代码混淆压缩比，在0~7之间，默认为5，一般不做修改
-optimizationpasses 5

# 配置字典
-obfuscationdictionary proguard_dic.txt
-classobfuscationdictionary proguard_dic.txt
-packageobfuscationdictionary proguard_dic.txt

# 混合时不使用大小写混合，混合后的类名为小写
# -dontusemixedcaseclassnames

# 指定不去忽略非公共库的类
# -dontskipnonpubliclibraryclasses

# 指定不去忽略非公共库的类成员
# -dontskipnonpubliclibraryclassmembers

# 这句话能够使我们的项目混淆后产生映射文件
# 包含有类名->混淆后类名的映射关系
-verbose

# 不做预校验，preverify是proguard的四个步骤之一，Android不需要preverify，去掉这一步能够加快混淆速度。
-dontpreverify

# 保留Annotation不混淆
-keepattributes *Annotation*,InnerClasses

# 保留自定义异常类
-keep public class * extends java.lang.Exception

# 避免混淆泛型
-keepattributes Signature

# 抛出异常时保留代码行号
-keepattributes SourceFile,LineNumberTable

# 指定混淆是采用的算法，后面的参数是一个过滤器
# 这个过滤器是谷歌推荐的算法，一般不做更改
-optimizations !code/simplification/cast,!field/*,!class/merging/*

-keep class me.hgj.jetpackmvvm.**{*;}
-keep class com.google.android.material.** {*;}
-keep class androidx.** {*;}
-keep public class * extends androidx.**
-keep interface androidx.** {*;}
-keep public class com.tencent.bugly.**{*;}
-keep class com.tencent.mmkv.**{*;}
-keep class com.squareup.retrofit2.** { *; }
-keep class com.squareup.okhttp3.** { *; }
-keep interface com.squareup.okhttp3.** { *; }
-keep class com.squareup.moshi.**{*;}
-keep interface com.squareup.moshi.**{*;}
-keep class com.just.agentweb.** {*;}


-dontwarn com.google.android.material.**
-dontnote com.google.android.material.**
-dontwarn androidx.**
-dontwarn com.tencent.bugly.**
-dontwarn com.squareup.okhttp3.**
-dontwarn com.squareup.retrofit2.**
-dontwarn com.squareup.moshi.**
-dontwarn com.just.agentweb.**


################ ViewBinding & DataBinding ###############
-keepclassmembers class * implements androidx.viewbinding.ViewBinding {
  public static * inflate(android.view.LayoutInflater);
  public static * inflate(android.view.LayoutInflater, android.view.ViewGroup, boolean);
  public static * bind(android.view.View);
}