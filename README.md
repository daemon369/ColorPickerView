# ColorPickerView
A color picker view for Android
Android颜色选择器

 [ ![Download](https://api.bintray.com/packages/daemon336699/maven/colorpickerview/images/download.svg) ](https://bintray.com/daemon336699/maven/colorpickerview/_latestVersion)

----

# 使用方法

## 1. 项目依赖

项目根目录下`build.gradle`中加入：
```
allprojects {
    repositories {
        ...
        jcenter()
    }
}
```
添加依赖：
```
dependencies {
    implementation 'me.daemon:colorpickerview:0.1.5'
}
```

## 2. 使用

```xml
<?xml version="1.0" encoding="utf-8"?>
<LinearLayout
    xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:app="http://schemas.android.com/apk/res-auto"
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    android:background="#ff00aa00"
    android:clipChildren="false"
    android:gravity="center"
    android:orientation="vertical">

    <me.daemon.colorpicker.ColorPickerView
        android:id="@+id/color_picker"
        android:layout_width="300dp"
        android:layout_height="300dp"
        android:background="@android:color/white"
        app:disallowInterceptTouchEvent="true"
        app:initialColor="@android:color/white"
        app:palettePadding="10dp" />

</LinearLayout>
```