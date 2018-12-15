package com.bappvn.mai.googlebooks

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.RecyclerView
import android.support.design.widget.Snackbar
import android.view.LayoutInflater
import android.view.Menu
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.SearchView
import android.widget.TextView

import com.bappvn.mai.googlebooks.model.Volume
import com.bappvn.mai.googlebooks.model.VolumeList
import com.bumptech.glide.annotation.GlideModule
import com.bumptech.glide.module.AppGlideModule
import kotlinx.android.synthetic.main.activity_book_list.*
import kotlinx.android.synthetic.main.book_list_content.view.*
import kotlinx.android.synthetic.main.book_list.*


@GlideModule
class MyAppGlideModule: AppGlideModule()

/**
 * An activity representing a list of Pings. This activity
 * has different presentations for handset and tablet-size devices. On
 * handsets, the activity presents a list of items, which when touched,
 * lead to a [BookDetailActivity] representing
 * item details. On tablets, the activity presents the list of items and
 * item details side-by-side using two vertical panes.
 */
class BookListActivity : AppCompatActivity() {

    private lateinit var viewModel: BookListViewModel
    /**
     * Whether or not the activity is in two-pane mode, i.e. running on a tablet
     * device.
     */
    private var twoPane: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_book_list)

        setSupportActionBar(toolbar)
        toolbar.title = title

        if (book_detail_container != null) {
            // The detail container view will be present only in the
            // large-screen layouts (res/values-w900dp).
            // If this view is present, then the
            // activity should be in two-pane mode.
            twoPane = true
        }

        setupViewModel()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.options_menu, menu)

        (menu?.findItem(R.id.app_bar_search)?.actionView as SearchView).apply {
            setOnQueryTextListener(object: SearchView.OnQueryTextListener {
                override fun onQueryTextChange(newText: String?): Boolean {
                    return false
                }

                override fun onQueryTextSubmit(query: String?): Boolean {
                    if (query != null) {
                        book_list.adapter = SimpleItemRecyclerViewAdapter(this@BookListActivity, emptyList(), twoPane)
                        empty_view.visibility = View.GONE
                        shimmer_view_container.startShimmer()
                        shimmer_view_container.visibility = View.VISIBLE
                        viewModel.search(query)
                    }
                    return false
                }
            })
        }
        return true
    }

    override fun onStart() {
        super.onStart()
        shimmer_view_container.visibility = View.INVISIBLE
    }

    private val onBookSearchObserver = Observer<VolumeList> { results ->
        shimmer_view_container.stopShimmer()
        shimmer_view_container.visibility = View.INVISIBLE
        val books: List<Volume> = when(results) {
            checkNotNull(results) -> results.items
            else -> emptyList()
        }
        book_list.adapter = SimpleItemRecyclerViewAdapter(this, books, twoPane)
        empty_view.visibility = if (books.isEmpty()) View.VISIBLE else View.GONE
    }

    private fun setupViewModel() {
        viewModel = ViewModelProviders.of(this).get(BookListViewModel::class.java)
        viewModel.bookList.observe(this, onBookSearchObserver)
    }

    class SimpleItemRecyclerViewAdapter(
        private val parentActivity: BookListActivity,
        private val books: List<Volume>,
        private val twoPane: Boolean
    ) :
        RecyclerView.Adapter<SimpleItemRecyclerViewAdapter.ViewHolder>() {

        private val onClickListener: View.OnClickListener

        init {
            onClickListener = View.OnClickListener { v ->
                val item = v.tag as Volume
                if (twoPane) {
                    val fragment = BookDetailFragment().apply {
                        arguments = Bundle().apply {
                            putSerializable(BookDetailFragment.ARG_ITEM, item)
                        }
                    }
                    parentActivity.supportFragmentManager
                        .beginTransaction()
                        .replace(R.id.book_detail_container, fragment)
                        .commit()
                } else {
                    val intent = Intent(v.context, BookDetailActivity::class.java).apply {
                        putExtra(BookDetailFragment.ARG_ITEM, item)
                    }
                    v.context.startActivity(intent)
                }
            }
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.book_list_content, parent, false)
            return ViewHolder(view)
        }

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            val item = books[position]
            holder.titleTextView.text = item.volumeInfo.title
            holder.subtitleTextView.text = item.volumeInfo.subtitle

            GlideApp
                .with(parentActivity)
                .load(item.volumeInfo.imageLinks?.thumbnail?.toString())
                .placeholder(ColorDrawable(Color.BLACK))
                .into(holder.thumbnailImageView)

            with(holder.itemView) {
                tag = item
                setOnClickListener(onClickListener)
            }
        }

        override fun getItemCount() = books.size

        inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
            val titleTextView: TextView = view.book_title
            val subtitleTextView: TextView = view.book_subtitle
            val thumbnailImageView: ImageView = view.book_thumbnail_image
        }
    }
}
