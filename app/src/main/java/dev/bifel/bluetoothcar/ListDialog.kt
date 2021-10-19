package dev.bifel.bluetoothcar

import android.bluetooth.BluetoothDevice
import android.os.Bundle
import android.view.View
import androidx.fragment.app.DialogFragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import dev.bifel.bluetoothcar.base.BaseRecyclerAdapter

/**
 * Date: 20.10.2021
 * Time: 0:10
 *
 * @author Bohdan Ishchenko
 */
class ListDialog : DialogFragment(R.layout.list_dialog) {

    private val adapter = BaseRecyclerAdapter { viewGroup -> DeviceViewHolder(viewGroup) }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val recycler = view as RecyclerView
        recycler.adapter = adapter
        recycler.layoutManager = LinearLayoutManager(context)
        adapter.addAll(BluetoothHelper.bondedDevices)
        adapter.setOnItemClickListener {
            (context as? OnListItemSelectListener)?.onListItemSelected(it)
            dismiss()
        }
    }

    interface OnListItemSelectListener {
        fun onListItemSelected(device: BluetoothDevice)
    }

    companion object {
        fun newInstance() = ListDialog()
    }
}