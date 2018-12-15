package com.bappvn.mai.googlebooks

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity
import android.support.v4.app.NavUtils
import android.view.MenuItem
import com.bappvn.mai.googlebooks.model.Volume
import kotlinx.android.synthetic.main.activity_book_detail.*
import java.net.URI

/**
 * An activity representing a single Book detail screen. This
 * activity is only used on narrow width devices. On tablet-size devices,
 * item details are presented side-by-side with a list of items
 * in a [BookListActivity].
 */
class BookDetailActivity : AppCompatActivity() {

    private lateinit var book: Volume

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_book_detail)
        setSupportActionBar(detail_toolbar)

        book = intent.getSerializableExtra(BookDetailFragment.ARG_ITEM) as Volume

        // Show the Up button in the action bar.
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        // savedInstanceState is non-null when there is fragment state
        // saved from previous configurations of this activity
        // (e.g. when rotating the screen from portrait to landscape).
        // In this case, the fragment will automatically be re-added
        // to its container so we don't need to manually add it.
        // For more information, see the Fragments API guide at:
        //
        // http://developer.android.com/guide/components/fragments.html
        //
        if (savedInstanceState == null) {
            // Create the detail fragment and add it to the activity
            // using a fragment transaction.
            val fragment = BookDetailFragment().apply {
                arguments = Bundle().apply {
                    putSerializable(BookDetailFragment.ARG_ITEM, book)
                }
            }

            supportFragmentManager.beginTransaction()
                .add(R.id.book_detail_container, fragment)
                .commit()
        }
    }

    override fun onOptionsItemSelected(item: MenuItem) =
        when (item.itemId) {
            android.R.id.home -> {
                // This ID represents the Home or Up button. In the case of this
                // activity, the Up button is shown. Use NavUtils to allow users
                // to navigate up one level in the application structure. For
                // more details, see the Navigation pattern on Android Design:
                //
                // http://developer.android.com/design/patterns/navigation.html#up-vs-back

                NavUtils.navigateUpTo(this, Intent(this, BookListActivity::class.java))
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
}
