package com.bappvn.mai.googlebooks

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bappvn.mai.googlebooks.model.Volume
import kotlinx.android.synthetic.main.activity_book_detail.*
import kotlinx.android.synthetic.main.book_detail.view.*

/**
 * A fragment representing a single Book detail screen.
 * This fragment is either contained in a [BookListActivity]
 * in two-pane mode (on tablets) or a [BookDetailActivity]
 * on handsets.
 */
class BookDetailFragment : Fragment() {

    private var book: Volume? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        arguments?.let {
            if (it.containsKey(ARG_ITEM)) {
                book = it.getSerializable(ARG_ITEM) as Volume
                activity?.toolbar_layout?.title = book?.volumeInfo?.title
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val rootView = inflater.inflate(R.layout.book_detail, container, false)
        book?.let {
            rootView.book_title.text = it.volumeInfo.title
            rootView.book_detail_author.text = it.volumeInfo.authors.joinToString(", ")
            rootView.book_description.text = it.volumeInfo.description
            GlideApp
                .with(this)
                .load(it.volumeInfo.imageLinks.thumbnail.toString())
                .into(rootView.cover_image)
        }

        return rootView
    }

    companion object {
        /**
         * The fragment argument representing the item that this fragment
         * represents.
         */
        const val ARG_ITEM = "item"
    }
}
