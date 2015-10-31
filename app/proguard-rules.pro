# Add project specific ProGuard rules here.
# By default, the flags in this file are appended to flags specified
# in C:\Program Files (x86)\Android\android-sdk/tools/proguard/proguard-android.txt
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

#-dontwarn android.support.v7.**
#-dontwarn com.google.android.gms.**
#-dontwarn javax.**
#
#-keep class android.support.v7.** { *; }
#-keep interface android.support.v7.** { *; }
#-keep public class com.google.android.gms.* { public *; }
#-keep class javax.annotation.processing.** { *; }
#-keep interface javax.** { *; }
#-keep org.** { *; }

-dontpreverify
-repackageclasses ''
-allowaccessmodification
-optimizations !code/simplification/arithmetic
-keepattributes *Annotation*

-keep public class * extends android.app.Activity
-keep public class * extends android.app.Application
-keep public class * extends android.app.Service # not used
-keep public class * extends android.content.BroadcastReceiver # not used
-keep public class * extends android.content.ContentProvider

-keep class * extends java.util.ListResourceBundle {
    protected Object[][] getContents();
}

-keep public class * extends android.view.View {
    public <init>(android.content.Context);
    public <init>(android.content.Context, android.util.AttributeSet);
    public <init>(android.content.Context, android.util.AttributeSet, int);
    public void set*(...);
}

-keepclasseswithmembers class * {
    public <init>(android.content.Context, android.util.AttributeSet);
}

-keepclasseswithmembers class * {
    public <init>(android.content.Context, android.util.AttributeSet, int);
}

-keepclassmembers class * extends android.content.Context {
   public void *(android.view.View);
   public void *(android.view.MenuItem);
}

#-keepclassmembers class * implements android.os.Parcelable {
#    static android.os.Parcelable$Creator CREATOR;
#}

-keepclassmembers class **.R$* {
    public static <fields>;
}

-keepclassmembers class * {
    @android.webkit.JavascriptInterface <methods>;
}

-keep class android.support.v4.** { *; }
-keep interface android.support.v4.** { *; }
#-keep class com.actionbarsherlock.** { *; } # not used
#-keep interface com.actionbarsherlock.** { *; } # not used
-keep class com.parse.** { *; } # not used
#-keep class com.mixpanel.** { *; } # not used
#-keep class com.testflightapp.** { *; } # not used
#-keep class org.json.simple.** { *; } # not used
-keep class com.evento.team2.eventspack.** { *; }
-keep interface com.evento.team2.eventspack.** { *; }
-keep public class com.google.android.gms.* { public *; }
-keep class javax..** { *; }
-keep interface javax.** { *; }