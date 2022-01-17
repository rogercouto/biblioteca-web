package br.com.biblioteca.presentation.features.books

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import br.com.biblioteca.R
import br.com.biblioteca.databinding.FragmentBookListBinding
import br.com.biblioteca.presentation.ext.visibleOrGone
import org.koin.androidx.viewmodel.ext.android.sharedViewModel
import android.widget.ArrayAdapter

class BookListFragment : Fragment() {

    private lateinit var binding : FragmentBookListBinding

    private val viewModel: BookViewModel by sharedViewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentBookListBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpActions()
        setUpViewModel()
    }

    private fun setUpActions(){
        binding.btnFind.setOnClickListener{
            val text = binding.etFind.text.toString()
            viewModel.fetchBooks(text)
        }
    }

    private fun setUpViewModel(){
        viewModel.suggestions.observe(viewLifecycleOwner){ suggestions->
            val adapter: ArrayAdapter<String> = ArrayAdapter<String>(
                requireContext(),
                android.R.layout.simple_dropdown_item_1line,
                suggestions
            )
            binding.etFind.setAdapter(adapter)
        }
        viewModel.fetchSuggestions()
        viewModel.books.observe(viewLifecycleOwner){ books ->
            binding.rvBooks.apply {
                //addItemDecoration(DividerItemDecoration(context, DividerItemDecoration.VERTICAL))
                adapter = BookAdapter(books,
                    onItemClick = { navigateToDetail(it.id) },
                    onNextPage = { viewModel.fetchMoreBooks() }
                )
            }
        }
        viewModel.loading.observe(viewLifecycleOwner){ isLoading->
            binding.rvBooks.visibleOrGone(!isLoading)
            binding.viewLoading.root.visibleOrGone(isLoading)
        }
    }

    private fun navigateToDetail(id: Long) {
        findNavController().navigate(
            R.id.bookDetailFragment,
            bundleOf(Pair(BOOK_ID, id))
        )
    }
}