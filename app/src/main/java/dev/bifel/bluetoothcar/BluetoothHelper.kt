package dev.bifel.bluetoothcar

import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothSocket
import io.reactivex.subjects.PublishSubject
import java.io.InputStream
import java.io.OutputStream
import java.util.*

/**
 * Date: 18.10.2021
 * Time: 17:48
 *
 * @author Bohdan Ishchenko
 */
class BluetoothHelper {
    private val adapter = BluetoothAdapter.getDefaultAdapter()
    private var socket: BluetoothSocket? = null

    val isConnected: Boolean
        get() = socket?.isConnected == true
    val bondedDevices: Set<BluetoothDevice>
        get() = adapter.bondedDevices
    private val inputStream: InputStream?
        get() = socket?.inputStream
    private val outputStream: OutputStream?
        get() = socket?.outputStream
    val isEnabled: Boolean
        get() = adapter.isEnabled

    val receivedDataObservable = PublishSubject.create<String> {
        try {
            val packetBytes = ByteArray(6)
            while (!it.isDisposed) {
                inputStream?.read(packetBytes)
                val data = String(packetBytes, Charsets.US_ASCII)
                println(data)
                it.onNext(data)
            }
        } catch (e: Exception) {
            it.onError(e)
        }
    }

    fun connectTo(device: BluetoothDevice) {
        val uuid =
            UUID.fromString("00001101-0000-1000-8000-00805F9B34FB") //Standard SerialPortService ID
        socket = device.createRfcommSocketToServiceRecord(uuid).apply { connect() }
    }

    fun closeConnection() {
        socket?.close()
    }

    fun send(data: Data) {
        outputStream?.write((String.format("%03d", data.strength - 255) + data.turn).toByteArray())
    }


}