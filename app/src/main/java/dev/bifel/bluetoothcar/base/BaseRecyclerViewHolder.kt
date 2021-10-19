package dev.bifel.bluetoothcar.base

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.IdRes
import androidx.annotation.LayoutRes
import androidx.annotation.StringRes
import androidx.recyclerview.widget.RecyclerView

abstract class BaseRecyclerViewHolder<DATA>(itemView: View) : RecyclerView.ViewHolder(itemView) {

    abstract fun bind(data: DATA)

    private var lastTap = 0L

    fun setOnItemClickListener(onItemClickListener: (Int) -> Unit) {
        itemView.setOnClickListener {
            val now = System.currentTimeMillis()
            if (now - lastTap > 500) {
                lastTap = now
                onItemClickListener(adapterPosition)
            }
        }
    }

    protected fun getString(@StringRes resId: Int, vararg formatArgs: Any?): String {
        return itemView.context.getString(resId, *formatArgs)
    }

    protected fun getString(@StringRes resId: Int): String {
        return itemView.context.getString(resId)
    }

    protected fun <T : View> findViewById(@IdRes resId: Int): T = itemView.findViewById(resId)

    companion object {
        @JvmStatic
        fun inflate(
            @LayoutRes resource: Int,
            root: ViewGroup,
            attachToRoot: Boolean
        ): View {
            return LayoutInflater.from(root.context).inflate(resource, root, attachToRoot)
        }
    }
}