# AirPdf
### An android library to download and render large PDF from within your app.

[![](https://jitpack.io/v/mumayank/airpdf.svg)](https://jitpack.io/#mumayank/airpdf)
___


### Presenting AirPdf

- Helps in rendering PDF pages as `Bitmap`
- Uses native `PdfRenderer` internally
- Provides `ZoomableImageView` so that users can zoom in into PDF pages
- Has helper methods to show PDFs from `Assets` folder (shipped with the app)
- Or you could just provide the URL and it will download PDF internally using Buffer (no file size limit) and then render the PDF
- Uses `cacheDir` to store the downloaded PDF (Android OS is free to delete this dir if required), and also provies helper method to delete the file
- Uses `coroutines` with appropriate `Dispatchers` internally

___

### Demo
<img width="466" alt="image" src="https://user-images.githubusercontent.com/8118918/213729760-432f689f-971f-41ce-bba5-ecd9aed7f60e.png">

<img width="462" alt="image" src="https://user-images.githubusercontent.com/8118918/213729956-caf63246-dadf-4b1a-8bed-99c2c864fddd.png">


___

### Usage

In project-level `build.gradle`
```gradle
allprojects {
  repositories {
    maven { url 'https://jitpack.io' }
  }
}
```

In app-level `build.gradle`
```gradle
dependencies {
    implementation "com.github.mumayank:airpdf:+"
}
```

#### Note: Most helper methods are suspended, so it is recommended to use `ViewModel` to call them.

To open a PDF file from asset
```kotlin
val filename = FileHelper.getFileName(cacheDir, assetManager, assetFileName)
```

To download a PDF file from URL
```kotlin
val filename = FileHelper.getFileName(cacheDir, url)
```

To get last index of the given PDF file
```kotlin
val lastIndex = PdfHelper.getLastIndex(cacheDir, filename)
```

#### Usually, a PDF is of `A4` size. So, it makes sense to set the height of your `ImageView` as `A4` size (i.e., make `width` = `match_parent` and then set `height` as `A4` ratio.

To get measured height and width of your `ImageView`
```kotlin
with(zoomageView) {
    post {
        // get `width` from here
    }
}

// then use the `width` here
val viewMeasurement = PdfHelper.getImageViewMeasurements(width)
```

To get `Bitmap` of a PDF page
```kotlin
val bitmap = PdfHelper.getBitmap(cacheDir, filename, viewMeasurement, index)
```

You may use the provided `zoomageView` (which is an `ImageView` that is zoomable) in your layout
```xml
<com.mumayank.airpdf.helpers.zoomage_view.ZoomageView
    android:id="@+id/zoomage_view"
    android:layout_width="match_parent"
    android:layout_height="0dp"
    android:layout_marginBottom="12dp"
    android:src="@drawable/rect_outline"
    app:layout_constraintBottom_toTopOf="@id/previousButton"
    app:layout_constraintEnd_toEndOf="parent"
    app:layout_constraintStart_toStartOf="parent"
    app:layout_constraintTop_toTopOf="parent"
    app:zoomage_animateOnReset="true"
    app:zoomage_autoCenter="true"
    app:zoomage_autoResetMode="UNDER"
    app:zoomage_maxScale="8"
    app:zoomage_minScale="0.6"
    app:zoomage_restrictBounds="false"
    app:zoomage_translatable="true"
    app:zoomage_zoomable="true" />
```
(credits to [`zoomageView`](https://github.com/jsibbold/zoomage))

That's all!
___

## Extras
___

### Problem Statement
In android, it is not very straightforward to render PDFs from 'within your app'.

___

### Existing solutions?

#### #1

Android devs use 

- Google drive URL (`https://drive.google.com/viewerng/viewer?embedded=true&url=<PDF_URL_HERE>`) 
or

- Google doc URL (`http://docs.google.com/gview?embedded=true&url=<PDF_URL_HERE>`)

to render PDFs in `WebView`. 

This approach is not recommended for the following reasons:

- Google may remove the URL access, so not future-safe
- Maxium limit of the PDF file size is 10mb
- These links have [usage-limits](https://stackoverflow.com/questions/2655972/how-can-i-display-a-pdf-document-into-a-webview#comment42182386_5296125)
- Devs can't control the UX for their app users (Google controls via Drive or Docs)

#2

There's a library [barteksc/AndroidPdfViewer](https://github.com/barteksc/AndroidPdfViewer) for this. Possible downsides:

- It [increases app size](https://github.com/barteksc/AndroidPdfViewer#why-resulting-apk-is-so-big) by a lot (~16MB)
- Integrating a 3rd party library needs extensive security review by auditors

#3

There is another paid library by [pspdfkit](https://pspdfkit.com/pdf-sdk/android/). Possible downsides:

- It is expensive
- Not easy to customize the use-case or UX

___


### Native android solution?

Android provides `PdfRenderer` (API 21+). 
This allows us to create a `Bitmap` from a page in a PDF document so that we can display it on the screen. 

Downsides:

- `PdfRenderer` class is not thread-safe.
- If we want to render a PDF, we first need to get a `ParcelFileDescriptorfrom` the file and then create a renderer instance.

___
