# Preserve information about line numbers, exception etc. for debugging purposes
-keepattributes SourceFile, LineNumberTable, Exceptions

# Hide original source file names
-renamesourcefileattribute SourceFile

# Keep enumerations
-keepclassmembers enum ax.stardust.runcalc.** { *; }

# Proguard configuration for Gson
# Prevent R8 from leaving Data object members always null
-keepclassmembers,allowobfuscation class * {
  @com.google.gson.annotations.SerializedName <fields>;
}
