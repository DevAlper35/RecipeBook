package com.mudurlu.recipebook.adaptor

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import com.mudurlu.recipebook.databinding.RecyclerRowBinding
import com.mudurlu.recipebook.model.Tarif
import com.mudurlu.recipebook.view.ListeFragmentDirections
import com.mudurlu.recipebook.view.TarifFragmentDirections

class TarifAdapter ( val tarifListesi : List<Tarif>) : RecyclerView.Adapter<TarifAdapter.TarifHolder>() {

    class TarifHolder(val binding:RecyclerRowBinding) : RecyclerView.ViewHolder(binding.root){

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TarifHolder {
        val recyclerRowBinding = RecyclerRowBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return TarifHolder(recyclerRowBinding)
    }

    override fun getItemCount(): Int {
        return tarifListesi.size
    }

    override fun onBindViewHolder(holder: TarifHolder, position: Int) {
        holder.binding.textView.text = tarifListesi[position].yemek_adi
        holder.itemView.setOnClickListener {
            val action =ListeFragmentDirections.actionListeFragmentToTarifFragment(bilgi = "eski", id = tarifListesi[position].id)
            Navigation.findNavController(it).navigate(action)
        }
    }
}