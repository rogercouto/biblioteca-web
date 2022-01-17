package br.com.biblioteca.presentation.features.books

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import br.com.biblioteca.databinding.FragmentBookDetailBinding
import br.com.biblioteca.presentation.ext.setTextAndFit
import org.koin.androidx.viewmodel.ext.android.sharedViewModel

const val BOOK_ID = "BOOK_ID"

class BookDetailFragment : Fragment() {

    private val viewModel : BookViewModel by sharedViewModel()

    private lateinit var binding : FragmentBookDetailBinding

    private val bookId by lazy {
        arguments?.getLong(BOOK_ID)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentBookDetailBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpViewModel()
        setUpActions()
    }

    private fun setUpViewModel(){
        binding.btnReserve.isEnabled = viewModel.token != null && viewModel.userId > 0
        viewModel.book.observe(viewLifecycleOwner){ book->
            binding.etTitle.setTextAndFit(book.title)
            if (book.abstract != null){
                binding.etAbstract.setTextAndFit(book.abstract.toString())
            }else{
                binding.etAbstract.setTextAndFit("(nenhum resumo)")
                binding.etAbstract.isEnabled = false
            }
            if (book.publisher != null){
                binding.etPublisher.setTextAndFit(book.publisher.toString())
            }else{
                binding.etPublisher.setTextAndFit("(sem editora)")
                binding.etPublisher.isEnabled = false
            }
            binding.etSubject.setText(book.subject)
            binding.etAuthors.setTextAndFit(book.authors)
            val disp = book.getAvaliableExemplars().toString() +" / "+book.exemplars.count().toString()
            binding.etExemplars.setText(disp)
        }
        viewModel.message.observe(viewLifecycleOwner){ message ->
            if (message.trim().isNotEmpty()){
                AlertDialog.Builder(requireContext())
                    .setTitle("Confirmação")
                    .setMessage(message)
                    .show()
                viewModel.clearMessage()
            }
        }
        viewModel.error.observe(viewLifecycleOwner){ error->
            if (error?.code == 400){
                AlertDialog.Builder(requireContext())
                    .setTitle("Atenção")
                    .setMessage("Você já reservou o limite de livros permitido!")
                    .show()
                viewModel.clearError()
            }
        }
        bookId?.let { viewModel.fetchBook(it) }
    }

    private fun setUpActions(){
        binding.btnReserve.setOnClickListener {
            viewModel.reserveBook()
        }
    }
}