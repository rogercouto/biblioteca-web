package br.com.biblioteca.presentation.features.books

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import br.com.biblioteca.databinding.ItemBookBinding
import br.com.biblioteca.domain.entity.Book
import br.com.biblioteca.presentation.ext.visibleOrGone

class BookViewHolder(
    itemView : View,
    private val onItemClick: (Book)->Unit,
    private val onNextPage: ()->Unit
) : RecyclerView.ViewHolder(itemView) {

    private val tvItemTitle = ItemBookBinding.bind(itemView).tvItemTitle
    private val tvItemAuthors = ItemBookBinding.bind(itemView).tvItemAuthors
    private var ivItemImage = ItemBookBinding.bind(itemView).ivBook

    fun bind(item: Book){
        tvItemTitle.text = item.title
        tvItemAuthors.text = item.authors
        if (item.id  > 0){
            ivItemImage.visibleOrGone(true)
            itemView.setOnClickListener{
                onItemClick(item)
            }
        }else{
            ivItemImage.visibleOrGone(false)
            itemView.setOnClickListener {
                onNextPage()
            }
        }
    }
}