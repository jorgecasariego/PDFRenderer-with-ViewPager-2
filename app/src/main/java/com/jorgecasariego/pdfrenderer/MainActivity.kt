package com.jorgecasariego.pdfrenderer

import android.os.Bundle
import android.os.ParcelFileDescriptor
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.viewpager2.widget.ViewPager2
import androidx.viewpager2.widget.ViewPager2.ORIENTATION_HORIZONTAL
import kotlinx.android.synthetic.main.pdf_activity.*
import java.io.File
import java.io.InputStream

class MainActivity : AppCompatActivity() {
    lateinit var pageViewPager: ViewPager2
    var parcelFileDescriptor: ParcelFileDescriptor? = null
    var pdfAdapter: PDFAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.pdf_activity)

        pageViewPager = findViewById(R.id.pageViewPager)

        with(pageViewPager) {
            clipToPadding = false
            clipChildren = false
            offscreenPageLimit = 3
        }


        val pageMarginPx = resources.getDimensionPixelOffset(R.dimen.pageMargin)
        val offsetPx = resources.getDimensionPixelOffset(R.dimen.offset)

        pageViewPager.setPageTransformer { page, position ->
            val viewPager = page.parent.parent as ViewPager2
            val offset = position * -(2 * offsetPx + pageMarginPx)
            if (viewPager.orientation == ORIENTATION_HORIZONTAL) {
                if (ViewCompat.getLayoutDirection(viewPager) == ViewCompat.LAYOUT_DIRECTION_RTL) {
                    page.translationX = -offset
                } else {
                    page.translationX = offset
                }
            } else {
                page.translationY = offset
            }
        }

        initPdfViewer(getFile())

    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId) {
            android.R.id.home -> onBackPressed()
        }
        return true
    }

    fun initPdfViewer(pdfFile : File){
        try {
            pageViewPager.visibility = View.VISIBLE
            baseProgressBar.visibility = View.GONE

            parcelFileDescriptor = ParcelFileDescriptor.open(pdfFile, ParcelFileDescriptor.MODE_READ_ONLY)
            pdfAdapter = PDFAdapter(parcelFileDescriptor!!, this@MainActivity)
            pageViewPager.adapter = pdfAdapter

        }catch (e: Exception){
            pdfFile.delete()
        }
    }

    fun getFile() : File{
        val inputStream = assets.open("dc_comics.pdf")
        return File(filesDir.absolutePath + "dc_comics.pdf").apply {
            copyInputStreamToFile(inputStream)
        }
    }

    fun File.copyInputStreamToFile(inputStream: InputStream) {
        this.outputStream().use { fileOut ->
            inputStream.copyTo(fileOut)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        parcelFileDescriptor?.close()
        pdfAdapter?.clear()

    }
}