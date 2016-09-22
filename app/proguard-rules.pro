# Add project specific ProGuard rules here.
# By default, the flags in this file are appended to flags specified
# in D:\DevTools\Android\android-sdk/tools/proguard/proguard-android.txt
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

#---------------------------------

-dontobfuscate
-dontwarn ru.cvoronin.android.**

-keep class ru.cvoronin.boilerplateapp.** { *; }
-keepclassmembers class ru.cvoronin.boilerplateapp.** { *; }

#---------------------------------
-dontnote **
-dontwarn com.viewpagerindicator.**

-keep class android.support.v7.widget.SearchView { *; }

# Retrofit
-dontwarn retrofit2.**
-keep class retrofit2.** { *; }
-keepattributes Signature
-keepattributes Exceptions
-keepattributes *Annotation*

-dontwarn okio.**
-keep class com.squareup.okhttp.** { *;}
-dontwarn com.squareup.okhttp.**

# EventBus 3.0
-keepclassmembers class ** {
    public void onEvent*(**);
}

# EventBus 3.0 annotation
-keepclassmembers class * {
    @org.greenrobot.eventbus.Subscribe <methods>;
}
-keep enum org.greenrobot.eventbus.ThreadMode { *; }

-keep class java.lang.ClassValue { *; }
-dontwarn java.lang.ClassValue

-keep class sun.misc.Unsafe { *; }
-dontwarn sun.misc.Unsafe

-dontwarn com.google.j2objc.annotations.Weak
-dontwarn org.codehaus.mojo.animal_sniffer.IgnoreJRERequirement

# Rx
-dontwarn rx.internal.util.unsafe.**
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

-keep class uk.co.chrisjenx.calligraphy.* { *; }
-keep class uk.co.chrisjenx.calligraphy.*$* { *; }

-dontwarn kotlin.**

-dontnote junit.framework.**
-dontnote junit.runner.**

-dontwarn android.test.**
-dontwarn android.support.test.**
-dontwarn org.junit.**
-dontwarn org.hamcrest.**
-dontwarn com.squareup.javawriter.JavaWriter
-dontwarn org.mockito.**