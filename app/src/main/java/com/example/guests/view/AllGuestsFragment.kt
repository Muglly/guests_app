package com.example.guests.view

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.guests.constants.DataBaseConstants
import com.example.guests.databinding.FragmentAllGuestsBinding
import com.example.guests.view.adapter.GuestsAdapter
import com.example.guests.view.listener.OnGuestListener
import com.example.guests.viewmodel.AllGuestsViewModel

class AllGuestsFragment : Fragment() {

    private var _binding: FragmentAllGuestsBinding? = null
    private val binding get() = _binding!!
    private lateinit var allGuestsViewModel: AllGuestsViewModel
    private val adapter = GuestsAdapter()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, b: Bundle?): View {
        allGuestsViewModel = ViewModelProvider(this).get(AllGuestsViewModel::class.java)
        _binding = FragmentAllGuestsBinding.inflate(inflater, container, false)

        // Layout
        binding.recycleAllGuests.layoutManager = LinearLayoutManager(context)

        // Adapter
        binding.recycleAllGuests.adapter = adapter

        val listener = object : OnGuestListener {
            override fun onClick(id: Int) {
                val intent = Intent(context, GuestFormActivity::class.java)
                val bundle = Bundle()
                bundle.putInt(DataBaseConstants.GUEST.ID, id)
                intent.putExtras(bundle)
                startActivity(intent)
            }

            override fun onDelete(id: Int) {
                allGuestsViewModel.delete(id)
                allGuestsViewModel.getAll()

            }
        }

        adapter.attachListener(listener)

        allGuestsViewModel.getAll()

        observe()

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    fun observe() {
        allGuestsViewModel.guestsAll.observe(viewLifecycleOwner) {
            adapter.updatedGuests(it)
        }
    }
}