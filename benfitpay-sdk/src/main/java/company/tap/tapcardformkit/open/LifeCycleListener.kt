package company.tap.tapbenefitpay.open
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent


class AppLifecycleObserver : LifecycleObserver {


    @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
    fun onEnterForeground() {
        BenefitPayDataConfiguration.getAppLifeCycle()?.onEnterForeground()
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
    fun onEnterBackground() {
        BenefitPayDataConfiguration.getAppLifeCycle()?.onEnterBackground()
    }
}
