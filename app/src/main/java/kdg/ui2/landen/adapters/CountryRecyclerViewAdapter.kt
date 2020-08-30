package kdg.ui2.landen.adapters

import android.content.Context
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import kdg.ui2.landen.R
import kdg.ui2.landen.model.Country
//import kdg.ui2.landen.country.getCountries

class CountryRecyclerViewAdapter(
    private val context: Context,
    private val onSelectListener: OnSelectionListener
) : RecyclerView.Adapter<CountryRecyclerViewAdapter.ViewHolder>() {

    var countries: Array<Country> = arrayOf()
        set(countries) {
            field = countries
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.fragment_country, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = countries[position]
        holder.flagImageView.setImageBitmap(item.flagBitmap)
        holder.nameView.text = item.name
        holder.fullnameView.text = item.fullname
        holder.itemView.setOnClickListener {
            onSelectListener.onSelected(position)
        }

    }

    override fun getItemCount(): Int = countries.size

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val flagImageView: ImageView = view.findViewById(R.id.item_flag)
        val nameView: TextView = view.findViewById(R.id.item_name)
        val fullnameView: TextView = view.findViewById(R.id.item_fullname)

        override fun toString(): String {
            return super.toString() + " '" + fullnameView.text + "'"
        }
    }

    interface OnSelectionListener {
        fun onSelected(position: Int)
    }
}