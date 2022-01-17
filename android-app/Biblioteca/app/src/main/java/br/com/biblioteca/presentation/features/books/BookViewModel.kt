package br.com.biblioteca.presentation.features.books

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import br.com.biblioteca.domain.entity.Book
import br.com.biblioteca.domain.entity.Exemplar
import br.com.biblioteca.domain.repository.BookRepository
import br.com.biblioteca.domain.repository.SuggestionRepository
import br.com.biblioteca.presentation.base.BaseViewModel

class BookViewModel(
    private val bookRepository: BookRepository,
    private val suggestionRepository: SuggestionRepository
) : BaseViewModel(){

    private val _books = MutableLiveData<List<Book>>()
    val books: LiveData<List<Book>> = _books

    private val _book = MutableLiveData<Book>()
    val book: LiveData<Book> = _book

    private val _message = MutableLiveData<String>()
    val message: LiveData<String> = _message

    private val _suggestions = MutableLiveData<List<String>>()
    val suggestions: LiveData<List<String>> = _suggestions

    var token : String = ""
    var userId : Long = 0

    private var currentFind = ""
    private var currentPage = 0

    fun fetchSuggestions(){
        _suggestions.postValue(suggestionRepository.findSuggestions())
    }

    fun fetchBooks(text : String){
        launch {
            if (text.trim().isNotEmpty()){
                val test = _suggestions.value?.contains(text)
                test?.let{ contains ->
                    if (!contains){
                        suggestionRepository.addSuggestion(text)
                    }
                }
            }
            currentFind = text
            currentPage = 0
            val tBooks : MutableList<Book> = bookRepository.findBooks(text) as MutableList<Book>
            if (tBooks.size > 9){
                tBooks.add(Book(0, "Carregar mais ..", "", "", "", "", ArrayList<Exemplar>()))
            }
            _books.postValue(tBooks)
        }
    }

    /**
     * Carrega mais uma pagina de livros e adiciona a lista atual
     */
    fun fetchMoreBooks(){
        launch {
            val tBooks = books.value?.filter { it.id > 0 } as MutableList<Book>
            val sizeBefore = tBooks.size
            tBooks.addAll(bookRepository.findBooks(currentFind, ++currentPage))
            val sizeAfter = tBooks.size
            if (sizeAfter > sizeBefore){
                tBooks.add(Book(0, "Carregar mais ..", "", "", "", "", ArrayList<Exemplar>()))
            }
            _books.postValue(tBooks)
        }
    }

    fun fetchBook(id: Long){
        launch {
            val book = bookRepository.findBookById(id)
            _book.postValue(book)
        }
    }

    fun reserveBook(){
        launch {
            book.value?.let{
                val numRegistro = it.getLastNumRegistro()
                bookRepository.reserveExemplar(numRegistro, userId, "Bearer "+token)
                val tBook = book.value
                tBook?.run{
                    this.setLastReserved()
                    _book.postValue(this)
                }
                _message.postValue("Exemplar reservado!")
            }
        }
    }

    fun clearMessage(){
        _message.postValue("")
    }

}