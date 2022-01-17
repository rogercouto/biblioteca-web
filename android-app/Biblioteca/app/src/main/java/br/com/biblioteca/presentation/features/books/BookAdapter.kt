package br.com.biblioteca.presentation.features.books

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import br.com.biblioteca.R
import br.com.biblioteca.domain.entity.Book

class BookAdapter(
    private val list: List<Book>,
    private val onItemClick: (Book) -> Unit,
    private val onNextPage: () -> Unit
) : RecyclerView.Adapter<BookViewHolder>(){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BookViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_book, parent, false)
        return BookViewHolder(view, onItemClick, onNextPage)
    }

    override fun onBindViewHolder(holder: BookViewHolder, position: Int) {
        val item = list[position]
        holder.bind(item)
    }

    override fun getItemCount() = list.size
}