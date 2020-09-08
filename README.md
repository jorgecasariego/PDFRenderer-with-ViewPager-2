# PDFRenderer-with-ViewPager-2

PDFRenderer class enables rendering a PDF document and this class is not thread safe.

If you want to render a PDF, you create a renderer and for every page you want to render, you open 
the page, render it, and close the page. After you are done with rendering, you close the renderer. 

After the renderer is closed it should not be used anymore. Note that the pages are rendered one 
by one, i.e. you can have only a single page opened at any given time.

A typical use of the APIs to render a PDF looks like this:

```
// create a new renderer
 PdfRenderer renderer = new PdfRenderer(getSeekableFileDescriptor());

 // let us just render all pages
 final int pageCount = renderer.getPageCount();
 for (int i = 0; i < pageCount; i++) {
     Page page = renderer.openPage(i);

     // say we render for showing on the screen
     page.render(mBitmap, null, null, Page.RENDER_MODE_FOR_DISPLAY);

     // do stuff with the bitmap

     // close the page
     page.close();
 }

 // close the renderer
 renderer.close();
```

## Pool of Bitmaps

Instead of lazily loading the pages one by one, we can have a bitmap pool that, whenever a page is 
selected, silently preloads the adjacent pages of the current one, so when the user swipes to the 
next (or previous) page, we just need to return the previously rendered bitmap, ready to be shown 
to the user.


## Other Libraries used in this Project:

- **PhotoView**: a custom implementation of ImageView that handles smoothly pinch-to-zoom and double-tap to zoom

- **ViewPager 2**: is now using the RecyclerView components and also brings in a lot of interesting features and improvements, such as:
    * Right-to-Left (RTL) support
    *  Support for vertical paging
    *  Ability to programmatically scroll the page
    *  Added MarginPageTransformer and CompositePageTransformer, which allows you to achieve beautiful custom animations for your pages

## Demo
<img src="https://github.com/jorgecasariego/PDFRenderer-with-ViewPager-2/blob/master/demo/test.gif" alt="alt text" width="400" height="790">
