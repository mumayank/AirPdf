# AirPdf
### An android library to download and render large PDF from within your app.

[![](https://jitpack.io/v/mumayank/airpdf.svg)](https://jitpack.io/#mumayank/airpdf)

### Presenting AirPdf

- Helps in rendering PDF pages as `Bitmap`s
- Uses native `PdfRenderer` internally
- Works with PDFs in `assets` folder
- Also works with PDFs present online (via their URL). The lib downloads the PDF using Buffer (no file size limit) and then renders the PDF pages using `Bitmap`s
- Deletes the original PDF file after downloading and converting pages into `Bitmap`s
- Recommends using `cacheDir` for storing `Bitmap`s so that Android OS can remove the files later automatically
    - Developers are free to provide other dir to store `Bitmap`s permanently
    - Provides helper methods to delete `Bitmap`s after user has navigated away from PDF rendering screen (optional)
- Uses `coroutines` with appropriate `Dispatchers` internally

___

### Demo

(Loads GIF, may take a while)
![demo4](https://user-images.githubusercontent.com/8118918/213924820-b6325e82-048e-41af-b7f7-e5be064d72eb.gif)

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

For PDF from `assets` folder:
```kotlin
val bitmapFilenames = PdfHelper.getBitmapFilenames(
    cacheDir,       // or fileDir
    assetManager,
    assetFilename,
    width
)
```

For PDF from URL:
```kotlin
val bitmapFilenames = PdfHelper.getBitmapFilenames(
    cacheDir,      // or fileDir 
    url,
    width
)
```

`width` is the width of the `ImageView` that is going to host the bitmap(s)
We need this to render the given PDF pages in `A4` sizes (Height is set in the ratio of the width so as to show a `A4` page)
```
imageview.post {
  // get width from here
}
```

You can use the `bitmapFilename` to show `Bitmap` in `ImageView` using image loading libs like (Glide)[https://bumptech.github.io/glide/]
```kotlin
Glide.with(context)
    .load(File(dir, bitmapFilename))
    .into(imageView)
```

To manually delete `Bitmap`s, call this from another screen (when the user has navigated away from the PDF rendering screen:
```
PdfHelper.deleteBitmaps(
  cacheDir, 
  bitmapFilenames
)
```

If you want to zoom in/out of the `ImageView` use [`zoomageView`](https://github.com/jsibbold/zoomage)

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
