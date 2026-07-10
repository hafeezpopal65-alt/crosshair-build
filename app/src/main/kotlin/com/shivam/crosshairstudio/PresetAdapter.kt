package com.shivam.crosshairstudio

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class PresetAdapter(
    private val presets: List<CrosshairPreset>,
    private val onSelect: (CrosshairPreset) -> Unit
) : RecyclerView.Adapter<PresetAdapter.VH>() {

    private var selectedPosition = -1

    inner class VH(view: View) : RecyclerView.ViewHolder(view) {
        val num: TextView = view.findViewById(R.id.presetNum)
        val name: TextView = view.findViewById(R.id.presetName)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        val v = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_preset, parent, false)
        return VH(v)
    }

    override fun onBindViewHolder(holder: VH, position: Int) {
        val p = presets[position]
        holder.num.text = "#${p.id}"
        holder.name.text = p.name

        if (position == selectedPosition) {
            holder.itemView.isSelected = true
            holder.name.setTextColor(Color.parseColor("#00E5FF"))
        } else {
            holder.itemView.isSelected = false
            holder.name.setTextColor(Color.parseColor("#E8EAF6"))
        }

        holder.itemView.setOnClickListener {
            val prev = selectedPosition
            selectedPosition = holder.adapterPosition
            notifyItemChanged(prev)
            notifyItemChanged(selectedPosition)
            onSelect(p)
        }
    }

    override fun getItemCount() = presets.size
}
