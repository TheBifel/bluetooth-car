package dev.bifel.bluetoothcar

import android.bluetooth.BluetoothDevice
import android.os.Handler
import android.os.Looper
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.plugins.RxJavaPlugins
import io.reactivex.schedulers.Schedulers

/**
 * Date: 18.10.2021
 * Time: 17:48
 *
 * @author Bohdan Ishchenko
 */
class Presenter(private val cache: Cache) {
    private var view: MainView? = null
    private var compositeDisposable = CompositeDisposable()

    init {
        val handler = Handler(Looper.getMainLooper())
        RxJavaPlugins.setErrorHandler {
            handler.post {
                view?.showError(it)
                view?.showConnecting(false)
            }
        }
    }

    fun sendData(data: Data) = BluetoothHelper.send(data)

    fun onResume(view: MainView) {
        this.view = view
        if (BluetoothHelper.isEnabled) {
            if (BluetoothHelper.isConnected) {
                compositeDisposable.add(
                    BluetoothHelper.receivedDataObservable
                        .subscribeOn(Schedulers.newThread())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(view::showReceivedData, view::showError)
                )
            } else if (cache.lastConnection != null) {
                val lastConnection = cache.lastConnection
                val device =
                    BluetoothHelper.bondedDevices.firstOrNull { it.address == lastConnection }
                if (device == null) {
                    view.showConnectDialog()
                } else {
                    connectTo(device)
                }
            } else {
                view.showConnectDialog()
            }
        } else {
            view.showBluetoothDisabled()
        }
    }

    fun onDestroy() = BluetoothHelper.closeConnection()

    fun onPause() {
        compositeDisposable.dispose()
        view = null
    }

    fun connectTo(device: BluetoothDevice) = try {
        compositeDisposable.add(
            BluetoothHelper.connectTo(device)
                .doOnComplete { cache.lastConnection = device.address }
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe {
                    view?.showConnecting(device.name)
                    view?.showConnecting(true)
                }
                .doAfterTerminate { view?.showConnecting(false) }
                .subscribe({ view?.showConnected(device.name) }, { view?.showError(it) })
        )
    } catch (t: Throwable) {
        view?.showError(t)
    }
}