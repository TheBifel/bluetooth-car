package dev.bifel.bluetoothcar.base

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

@Suppress("MemberVisibilityCanBePrivate", "unused")
open class BaseRecyclerAdapter<DATA, VH : BaseRecyclerViewHolder<DATA>>
    : RecyclerView.Adapter<VH> {

    private val viewHolderSupplier: (ViewGroup, Int) -> VH

    constructor(viewHolderSupplier: (ViewGroup, Int) -> VH) : super() {
        this.viewHolderSupplier = viewHolderSupplier
    }

    constructor(viewHolderSupplier: (ViewGroup) -> VH) : super() {
        this.viewHolderSupplier = { group, _ -> viewHolderSupplier(group) }
    }

    val data: MutableList<DATA> = ArrayList()
    private var onItemClickListener: ((DATA) -> Unit)? = null
    var onEmptyDataListener: ((Boolean) -> Unit)? = null

    var lastLoadedListener: (Int) -> Unit = {}

    override fun onCreateViewHolder(viewGroup: ViewGroup, type: Int): VH {
        return viewHolderSupplier(viewGroup, type)
    }

    override fun getItemCount(): Int {
        return data.size
    }

    fun add(dataType: DATA) {
        data.add(dataType)
        notifyItemInserted(data.size - 1)
    }

    fun add(position: Int, dataType: DATA) {
        data.add(position, dataType)
        notifyItemInserted(position)
    }

    open fun addAll(list: Collection<DATA>) {
        val insertPosition = data.size
        data.addAll(list)
        notifyItemRangeInserted(insertPosition, list.size)
    }

    fun addAll(position: Int, list: Collection<DATA>) {
        data.addAll(position, list)
        notifyItemRangeInserted(position, list.size)
    }

    fun delete(position: Int) {
        data.removeAt(position)
        notifyItemRemoved(position)
    }

    fun delete(data: DATA) = delete(this.data.indexOf(data))

    fun update(adapterPosition: Int, newData: DATA) {
        data[adapterPosition] = newData
        notifyItemChanged(adapterPosition)
    }

    fun setData(list: Collection<DATA>) {
        data.clear()
        data.addAll(list)
        notifyDataSetChanged()
        onEmptyDataListener?.invoke(data.isEmpty())
    }

    fun clear() {
        val count = data.size
        data.clear()
        notifyItemRangeRemoved(0, count)
        onEmptyDataListener?.invoke(true)
    }

    override fun onBindViewHolder(viewHolder: VH, i: Int) {
        viewHolder.bind(data[i])
        onItemClickListener?.let {
            viewHolder.setOnItemClickListener { position: Int -> it(data[position]) }
        }
        if (i == data.size - 1) {
            lastLoadedListener(i)
        }
    }

    fun setOnItemClickListener(onItemClickListener: ((DATA) -> Unit)?) {
        this.onItemClickListener = onItemClickListener
    }

    fun getItemAt(position: Int): DATA {
        return data[position]
    }
}