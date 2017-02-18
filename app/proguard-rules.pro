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

-keep class org.xmlpull.v1.** { *; }

-keep class com.bluelinelabs.logansquare.** { *; }
-keep @com.bluelinelabs.logansquare.annotation.JsonObject class *
-keep class **$$JsonObjectMapper { *; }

-keep class android.support.v7.widget.SearchView { *; }

-keep class com.evento.team2.eventspack.services.models.JsonTranslation { *; } # required for yandex translation

# Platform calls Class.forName on types which do not exist on Android to determine platform.
-dontnote retrofit2.Platform
# Platform used when running on RoboVM on iOS. Will not be used at runtime.
-dontnote retrofit2.Platform$IOS$MainThreadExecutor
# Platform used when running on Java 8 VMs. Will not be used at runtime.
-dontwarn retrofit2.Platform$Java8
# Retain generic type information for use by reflection by converters and adapters.
-keepattributes Signature
# Retain declared checked exceptions for use by a Proxy instance.
-keepattributes Exceptions