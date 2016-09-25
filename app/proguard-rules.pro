-dontpreverify
-repackageclasses ''
-allowaccessmodification
#-optimizations !code/simplification/arithmetic
-keepattributes *Annotation*
-keepattributes EnclosingMethod

-dontwarn butterknife.internal.**
#-dontwarn com.google.android.gms.**
-dontwarn android.content.**
-dontwarn android.graphics.**
-dontwarn android.util.**
-dontwarn android.view.**
-dontwarn java.lang.invoke.*
-dontwarn android.support.**
-dontwarn dagger.producers.monitoring.internal.**
-dontwarn okio.**
-dontwarn retrofit2.Platform$Java8

#-dontwarn com.squareup.okhttp.**
#-dontwarn retrofit.**
#-dontwarn rx.**

-keep class org.xmlpull.v1.** { *; }

-keep class com.bluelinelabs.logansquare.** { *; }
-keep @com.bluelinelabs.logansquare.annotation.JsonObject class *
-keep class **$$JsonObjectMapper { *; }

#-keep class javax..** { *; }
#-keep interface javax.** { *; }
#-keep class dagger.producers.monitoring.internal.**
#-keep interface javax.inject.** { *; }
#-keepattributes Signature