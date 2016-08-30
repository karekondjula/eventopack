-dontpreverify
-repackageclasses ''
-allowaccessmodification
-optimizations !code/simplification/arithmetic
-keepattributes *Annotation*

-dontwarn butterknife.internal.**
#-dontwarn com.google.android.gms.**
-dontwarn android.content.**
-dontwarn android.graphics.**
-dontwarn android.util.**
-dontwarn android.view.**
-dontwarn java.lang.invoke.*
-dontwarn android.support.**
-dontwarn dagger.producers.monitoring.internal.**

#-dontwarn com.squareup.okhttp.**
#-dontwarn okio.**
#-dontwarn retrofit.**
#-dontwarn rx.**

-keep public class * extends android.app.Activity
-keep public class * extends android.app.Application
#-keep public class * extends android.app.Service # not used
-keep public class * extends android.content.BroadcastReceiver
-keep public class * extends android.content.ContentProvider
-keep class org.xmlpull.v1.** { *; }

#-keep class com.parse.** { *; } # not used
-keep class com.evento.team2.eventspack.** { *; } # TODO try to keep only activities or dagger related classes?!
-keep class com.evento.team2.eventspack.models.** { *; } # TODO try to keep only activities or dagger related classes?!
-keep interface com.evento.team2.eventspack.** { *; }
#-keep public class com.google.android.gms.* { public *; }
-keep class javax..** { *; }
-keep interface javax.** { *; }
#-keep interface javax.inject.** { *; }
-keep class dagger.producers.monitoring.internal.**
#-keepattributes Signature
#-keepattributes InnerClasses

-keep class * extends java.util.ListResourceBundle {
    protected Object[][] getContents();
}

-keep public class * extends android.view.View {
    public <init>(android.content.Context);
    public <init>(android.content.Context, android.util.AttributeSet);
    public <init>(android.content.Context, android.util.AttributeSet, int);
    public void set*(...);
}

#-keep class com.squareup.okhttp.** { *; }
#-keep class retrofit.** { *; }
#-keep interface com.squareup.okhttp.** { *; }

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

-keepclassmembers class **.R$* {
    public static <fields>;
}

#-keepclasseswithmembers class * {
#    @retrofit.http.* <methods>;
#}

#-keepclassmembers class * {
#    @android.webkit.JavascriptInterface <methods>;
#}