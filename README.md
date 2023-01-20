# AirPdf
### An android library to download and render large PDF from within your app.

[![](https://jitpack.io/v/mumayank/airqr.svg)](https://jitpack.io/#mumayank/airqr)
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

[Video Link](https://drive.google.com/file/d/1O5M0sIrv3gql9nfrHJUZ9pUQUi9UDz39/view?usp=share_link)

___

### Usage




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
