package com.bappvn.mai.googlebooks

import android.content.Intent
import android.net.Uri
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
        book?.let { volume ->
            rootView.book_title.text = volume.volumeInfo.title
            rootView.book_detail_author.text = volume.volumeInfo.authors.joinToString(", ")
            rootView.book_description.text = volume.volumeInfo.description
            GlideApp
                .with(this)
                .load(volume.volumeInfo.imageLinks?.thumbnail?.toString())
                .into(rootView.cover_image)

            rootView.buy_button.setOnClickListener {
                startActivity(
                    Intent(
                        Intent.ACTION_VIEW,
                        Uri.parse(volume.saleInfo.buyLink.toString())
                    )
                )
            }
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
