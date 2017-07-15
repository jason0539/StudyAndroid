# Add project specific ProGuard rules here.
# By default, the flags in this file are appended to flags specified
# in /Users/baidu/as/sdk/tools/proguard/proguard-android.txt
# You can edit the include path and order by changing the proguardFiles
# directive in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# Add any project specific keep options here:

# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class name to the JavaScript interface
# class:
#-keepclassmembers class fqcn.of.javascript.interface.for.webview {
#   public *;
#}
-dontwarn java.awt.**

-dontwarn net.sf.json.**
-keep class net.sf.json.** { *; }

-dontwarn com.google.common.**
-keep class com.google.common.** { *; }

-dontwarn com.fasterxml.jackson.**
-keep class com.fasterxml.jackson.** { *; }

-dontwarn com.squareup.picasso.**
-keep class com.squareup.picasso.** { *; }

-keep class android.net.http.SslError
-keep class android.webkit.**{*;}
-keep class cn.sharesdk.**{*;}
-keep class m.framework.**{*;}
-dontwarn cn.sharesdk.**

-dontwarn com.umeng.**
-keep class com.umeng.** { *; }
-dontwarn com.umeng.**
-keep class com.umeng.** { *; }

-keep public interface com.umeng.socialize.**
-keep public interface com.umeng.socialize.sensor.**
-keep public interface com.umeng.scrshot.**
-keep public class com.umeng.socialize.* {*;}
-keep class com.umeng.scrshot.**
-keep class com.umeng.socialize.sensor.**
-keep class com.tencent.mm.sdk.** {
   *;
}
-keep public class com.tuotuo.solo.R$*{
    public static final int *;
}


-dontwarn org.androidannotations.api.rest.*

-dontwarn com.actionbarsherlock.**
-keep class com.actionbarsherlock.** { *; }

-dontwarn com.alibaba.fastjson.**
-keep class com.alibaba.fastjson.** { *; }

-dontwarn com.amap.api.**
-keep class com.amap.api.** { *; }

-dontwarn com.a.a.**
-keep class com.a.a.** { *; }

-keep class com.tuotuo.solo.dto.** { *; }
-keep class PrefUtils
-keep class com.tuotuo.solo.view.deploy.PublishAudio {
    public void setProgressbar(int);
}
-keep public class * implements java.io.Serializable {
        public *;
}
-keep class com.tuotuo.solo.view.base.TuoFragment
-keep class * extends com.tuotuo.solo.view.base.TuoFragment { *; }
-keep class com.tuotuo.solo.view.base.TuoActivity
-keep class * extends com.tuotuo.solo.view.base.TuoActivity { *; }
-keep class * extends java.lang.annotation.Annotation { *; }
-keepclassmembers class ** {
    public void onEvent(**);
}

#-keepattributes *
-keepattributes Exceptions, Signature, Deprecated, SourceFile, SourceDir, LineNumberTable, LocalVariableTable, LocalVariableTypeTable, Synthetic, EnclosingMethod, InnerClasses


-keepclasseswithmembernames class * {
    native <methods>;
}

-keepclasseswithmembers class * {
    public <init>(android.content.Context, android.util.AttributeSet);
}

-keepclasseswithmembers class * {
    public <init>(android.content.Context, android.util.AttributeSet, int);
}

-keepclassmembers class * extends android.app.Activity {
   public void *(android.view.View);
}

-keepclassmembers enum * {
    public static **[] values();
    public static ** valueOf(java.lang.String);
}

-keep class **.R$* {
    *;
}

-keep class * implements android.os.Parcelable {
  public static final android.os.Parcelable$Creator *;
}

-keep public class pl.droidsonroids.gif.GifIOException{<init>(int);}
-keep class pl.droidsonroids.gif.GifInfoHandle{<init>(long,int,int,int);}

-dontwarn com.squareup.okhttp.**
-keep class com.squareup.okhttp.** { *; }
-keep interface com.squareup.okhttp.** { *; }
-dontwarn okio.**
-dontwarn javax.annotation.Nullable
-dontwarn javax.annotation.ParametersAreNonnullByDefault

-keep public class * extends android.content.BroadcastReceiver
-keep class com.tuotuo.solo.broadcast.MiPushMessageReceiver {*;}

-keep,allowobfuscation @interface com.facebook.common.internal.DoNotStrip
-keep @com.facebook.common.internal.DoNotStrip class *
-keepclassmembers class * {
    @com.facebook.common.internal.DoNotStrip *;
}
-keepclassmembers class * {
    native <methods>;
}
-dontwarn okio.**
-dontwarn javax.annotation.**

-keepattributes Signature
-keep class sun.misc.Unsafe { *; }
-keep class com.taobao.* {*;}
-keep class com.alibaba.** {*;}
-keep class com.alipay.** {*;}
-dontwarn com.taobao.**
-dontwarn com.alibaba.**
-dontwarn com.alipay.**
-keep class com.ut.** {*;}
-dontwarn com.ut.**
-keep class com.ta.** {*;}
-dontwarn com.ta.**
-keep class org.json.** {*;}
-keep class com.ali.auth.** {*;}

# 个推
-dontwarn com.igexin.**
-keep class com.igexin.**{*;}

-keepclasseswithmembernames class com.xiaomi.**{*;}
-keep public class * extends com.xiaomi.mipush.sdk.PushMessageReceiver
-keep class com.tuotuo.solo.viewholder.** { *; }

#HotFix
-keep class * extends java.lang.annotation.Annotation
-keepclasseswithmembernames class * {
    native <methods>;
}
-keep class com.alipay.euler.andfix.**{
    *;
}
-keep class com.taobao.hotfix.aidl.**{*;}
-keep class com.ta.utdid2.device.**{*;}
-keep class com.taobao.hotfix.HotFixManager{
    public *;
}

-keep class org.apache.http.** { *; }
-dontwarn org.apache.http.**
-keep public class android.net.http.SslError
-keep public class android.webkit.WebViewClient

-dontwarn android.webkit.WebView
-dontwarn android.net.http.SslError
-dontwarn android.webkit.WebViewClient

-keep public class com.tuotuo.solo.view.chat.TuoChattingPageOperateion { *;}
-keep public class com.tuotuo.solo.view.chat.TuoIMConversationUI { *;}

#openIm
-dontwarn com.google.gson.**

-dontwarn com.tuotuo.solo.widgetlibrary.**
-keep class com.tuotuo.solo.widgetlibrary.** { *;}

-dontwarn com.tuotuo.solo.selfwidget.**
-keep class com.tuotuo.solo.selfwidget.** { *;}

-keepclassmembers class * {
    @android.webkit.JavascriptInterface <methods>;
}
-keepattributes *JavascriptInterface*

#tencent live start
-keep class com.tencent.**{*;}
-dontwarn com.tencent.**

-keep class tencent.**{*;}
-dontwarn tencent.**

-keep class qalsdk.**{*;}
-dontwarn qalsdk.**
-keep class * extends com.tuotuo.solo.view.base.fragment.waterfall.WaterfallRecyclerViewHolder { *; }
-keep class * extends com.tuotuo.solo.view.base.fragment.waterfall.WaterfallEventBusRecyclerViewHolder { *; }
-keep class com.tuotuo.solo.live.dto.** { *; }
-keep class com.tuotuo.solo.live.views.viewHolder.** { *; }
-keep class com.tuotuo.solo.live.models.http.** { *; }
-keep class com.tuotuo.solo.live.models.common.** { *; }
-keep class com.tuotuo.solo.live.models.model.** { *; }
-keep class com.tuotuo.solo.citysite.bean.**{ *;}
-keep class com.tuotuo.solo.shopping_cart.bean.**{ *;}
-keep class com.tuotuo.solo.citysite.view.viewHolder.**{ *;}

-keep class com.tuotuo.solo.view.main.dto.** {*;}
-keep class com.tuotuo.solo.view.main.viewHolder.** { *; }
#tencent live end

#FMAgent
-dontwarn android.os.**
-dontwarn com.android.internal.**
-keep class cn.tongdun.android.**{*;}
#FMAgent end

#zego
-keep class com.zego.** { *;}
#zego end


#神策
-dontwarn com.sensorsdata.analytics.android.sdk.**
-keep class com.sensorsdata.analytics.android.sdk.** {
*;
}
#神策end

#rx
-dontwarn sun.misc.**
-keepclassmembers class rx.internal.util.unsafe.*ArrayQueue*Field* {
 long producerIndex;
 long consumerIndex;
}
-keepclassmembers class rx.internal.util.unsafe.BaseLinkedQueueProducerNodeRef {
 rx.internal.util.atomic.LinkedQueueNode producerNode;
}
-keepclassmembers class rx.internal.util.unsafe.BaseLinkedQueueConsumerNodeRef {
 rx.internal.util.atomic.LinkedQueueNode consumerNode;
}
#rx end

#uploader
-keep class com.tuotuo.uploader.bean.**{*;}
-keep class com.tuotuo.uploader.util.network.http.**{*;}
#uploader end

-ignorewarnings

-dontwarn com.tencent.bugly.**
-keep public class com.tencent.bugly.**{*;}
-keep class android.support.**{*;}

-keep public class com.alibaba.android.arouter.routes.**{*;}
-keep class * implements com.alibaba.android.arouter.facade.template.ISyringe{*;}

#只有bugfix的时候才将此选项打开
#-applymapping mapping.txt
