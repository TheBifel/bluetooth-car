package dev.bifel.bluetoothcar

import android.bluetooth.BluetoothDevice
import android.view.ViewGroup
import android.widget.TextView
import dev.bifel.bluetoothcar.base.BaseRecyclerViewHolder

/**
 * Date: 20.10.2021
 * Time: 0:18
 *
 * @author Bohdan Ishchenko
 */
class DeviceViewHolder(parent: ViewGroup) : BaseRecyclerViewHolder<BluetoothDevice>(
    inflate(R.layout.item_device, parent, false)
) {
    override fun bind(data: BluetoothDevice) {
        findViewById<TextView>(R.id.txt).text = data.name
    }
}