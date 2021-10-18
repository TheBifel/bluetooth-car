package dev.bifel.bluetoothcar

/**
 * Date: 18.10.2021
 * Time: 18:08
 *
 * @author Bohdan Ishchenko
 */
interface MainView {
    fun showBluetoothDisabled()
    fun showConnectDialog()
    fun showReceivedData(data: String)
    fun showError(throwable: Throwable)
}