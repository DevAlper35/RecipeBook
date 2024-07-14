package com.mudurlu.recipebook.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.room.Room
import com.mudurlu.recipebook.adaptor.TarifAdapter
import com.mudurlu.recipebook.databinding.FragmentListeBinding
import com.mudurlu.recipebook.model.Tarif
import com.mudurlu.recipebook.roomdb.TarifDAO
import com.mudurlu.recipebook.roomdb.TarifDB
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.schedulers.Schedulers


class ListeFragment : Fragment() {
    private var _binding: FragmentListeBinding? = null
    private val binding get() = _binding!!
    private val mDisposable = CompositeDisposable()

    private lateinit var db : TarifDB
    private lateinit var dao : TarifDAO

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        db = Room.databaseBuilder(requireContext(),TarifDB::class.java,"Tarifler").build()
        dao = db.tarifDAO()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentListeBinding.inflate(inflater, container, false)
        val view = binding.root
        return view

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.floatingActionButton.setOnClickListener { yeniTarif(it) }
        binding.ListeRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        verileriAl()
    }

    private fun verileriAl() {
        mDisposable.add(
            dao.tumTarifler()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::handleResponse)
        )
    }

    private fun handleResponse(tariflerListesi : List<Tarif>){
        val adapter = TarifAdapter(tariflerListesi)
        binding.ListeRecyclerView.adapter = adapter
    }

    fun yeniTarif(view : View){
        val action = ListeFragmentDirections.actionListeFragmentToTarifFragment("yeni",id = -1)
        Navigation.findNavController(view).navigate(action)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        mDisposable.clear()
    }
}