package dev.bifel.bluetoothcar

import android.annotation.SuppressLint
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.FragmentActivity

@SuppressLint("SetTextI18n")
class MainActivity : FragmentActivity(), MainView, ListDialog.OnListItemSelectListener {
    private lateinit var txtReceived: TextView
    private lateinit var progressConnecting: View
    private lateinit var presenter: Presenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        presenter = Presenter(Cache(this))
        setContentView(R.layout.activity_main)
        txtReceived = findViewById(R.id.lblInput)
        progressConnecting = findViewById(R.id.progress_connecting)
        findViewById<View>(R.id.btnReconnect).setOnClickListener { showConnectDialog() }
        val txtSent = findViewById<TextView>(R.id.lblOutput)
        findViewById<StickView>(R.id.stick).onPositionChangeListener = {
            txtSent.text = "Sent ${it.strength} ${it.turn}"
            presenter.sendData(it)
        }
    }

    override fun onResume() {
        super.onResume()
        presenter.onResume(this)
    }

    override fun onPause() {
        super.onPause()
        presenter.onPause()
    }

    override fun onDestroy() {
        super.onDestroy()
        presenter.onDestroy()
    }

    override fun showBluetoothDisabled() =
        startActivity(Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE))

    override fun showReceivedData(data: String) {
        txtReceived.text = data
    }

    override fun showConnected(name: String) =
        Toast.makeText(this, "Connected to $name", Toast.LENGTH_SHORT).show()

    override fun showConnecting(name: String) =
        Toast.makeText(this, "Connecting to $name", Toast.LENGTH_SHORT).show()

    override fun showConnecting(isConnecting: Boolean) {
        progressConnecting.visibility = if (isConnecting) View.VISIBLE else View.GONE
    }

    override fun showError(throwable: Throwable) =
        Toast.makeText(this, throwable.localizedMessage, Toast.LENGTH_SHORT).show()

    override fun showConnectDialog() =
        ListDialog.newInstance().show(supportFragmentManager, "ListDialog")

    override fun onListItemSelected(device: BluetoothDevice) {
        presenter.connectTo(device)
    }


}