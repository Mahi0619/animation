# General Attributes
-keepattributes Signature, *Annotation*


# Debugging Stack Traces
-keepattributes SourceFile,LineNumberTable

# AndroidX Lifecycle Components
-keep class androidx.lifecycle.LiveData { *; }
-keep class androidx.lifecycle.MutableLiveData { *; }

# JSON Serialization/Deserialization (Gson)
-keepattributes *Annotation*
-keep class com.google.gson.** { *; }
-keepclassmembers class * {
    @com.google.gson.annotations.SerializedName <fields>;
}

# Kotlin Metadata
-keepattributes *Annotation*
-keep class kotlin.Metadata { *; }

# OkHttp and Networking
-keep class okhttp3.internal.platform.** { *; }
-keep class javax.net.ssl.** { *; }
-dontwarn org.conscrypt.**
-dontwarn org.bouncycastle.**
-dontwarn org.openjsse.**
-dontwarn org.apache.harmony.**

# Module-Specific Rules
-keep class com.anime.application.** { *; }
-dontwarn com.anime.application.**


# Keep generic signature of Call, Response (R8 full mode strips signatures from non-kept items)
-keep,allowobfuscation,allowshrinking interface retrofit2.Call
-keep,allowobfuscation,allowshrinking class retrofit2.Response

# With R8 full mode generic signatures are stripped for classes that are not kept
-keep,allowobfuscation,allowshrinking class kotlin.coroutines.Continuation




# Print mapping for debug purposes
-printmapping build/outputs/mapping/release/mapping.txt


