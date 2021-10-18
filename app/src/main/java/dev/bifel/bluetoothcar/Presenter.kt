package dev.bifel.bluetoothcar

import android.bluetooth.BluetoothDevice
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

/**
 * Date: 18.10.2021
 * Time: 17:48
 *
 * @author Bohdan Ishchenko
 */
class Presenter {

    private val helper = BluetoothHelper()
    private var view: MainView? = null
    private var compositeDisposable = CompositeDisposable()
    val bondedDevices
        get() = helper.bondedDevices

    fun sendData(data: Data) = helper.send(data)

    fun onResume(view: MainView) {
        this.view = view
        if (helper.isEnabled) {
            if (helper.isConnected) {
                compositeDisposable.add(
                    helper.receivedDataObservable
                        .subscribeOn(Schedulers.newThread())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(view::showReceivedData, view::showError)
                )
            } else {
                view.showConnectDialog()
            }
        } else {
            view.showBluetoothDisabled()
        }
    }

    fun onDestroy() = helper.closeConnection()

    fun onPause() {
        compositeDisposable.dispose()
        view = null
    }

    fun connectTo(device: BluetoothDevice) = try {
        compositeDisposable.add(
            helper.connectTo(device)
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