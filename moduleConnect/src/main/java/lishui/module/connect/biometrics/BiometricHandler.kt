package lishui.module.connect.biometrics

import android.app.KeyguardManager
import android.content.Context
import android.hardware.biometrics.BiometricManager
import android.hardware.biometrics.BiometricPrompt
import android.lib.base.executor.GlobalTaskExecutor
import android.lib.base.log.LogUtils
import android.lib.base.util.EnvironmentUtils
import android.os.Build
import android.os.CancellationSignal
import androidx.annotation.RequiresApi

object BiometricHandler {

    private const val TAG = "BiometricHandler"

    fun showBiometricPromptDialog(context: Context) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.P) {
            LogUtils.d(TAG, "Lower than Android P, can't not show biometric dialog.")
            return
        }
        val keyguardManager = context.getSystemService(Context.KEYGUARD_SERVICE) as KeyguardManager
        if (keyguardManager.isKeyguardSecure) {
            val authenticationCallback = @RequiresApi(Build.VERSION_CODES.P)
            object : BiometricPrompt.AuthenticationCallback() {

                override fun onAuthenticationError(errorCode: Int, errString: CharSequence?) {
                    super.onAuthenticationError(errorCode, errString)
                    LogUtils.d(
                        TAG,
                        "onAuthenticationError errorCode=$errorCode, errString=$errString"
                    )
                }

                override fun onAuthenticationFailed() {
                    super.onAuthenticationFailed()
                    LogUtils.d(TAG, "onAuthenticationFailed...")
                }

                override fun onAuthenticationSucceeded(result: BiometricPrompt.AuthenticationResult?) {
                    super.onAuthenticationSucceeded(result)
                    LogUtils.d(TAG, "onAuthenticationSucceeded...")
                }
            }

            val biometricBuilder = BiometricPrompt.Builder(context)
                .setTitle("Test biometric")
                .setNegativeButton(
                    "Cancel",
                    {
                        LogUtils.d(TAG, "run cancel runnable")
                    },
                    { _, _ ->
                        LogUtils.d(TAG, "cancel it for biometric")
                    })

            if (EnvironmentUtils.isAtLeastR() && keyguardManager.isDeviceSecure) {
                biometricBuilder.setAllowedAuthenticators(
                    BiometricManager.Authenticators.BIOMETRIC_WEAK
                )
            }

            val biometricPrompt: BiometricPrompt = biometricBuilder.build()
            biometricPrompt.authenticate(
                CancellationSignal(),
                GlobalTaskExecutor.getComputationThreadExecutor(),
                authenticationCallback
            )

        } else {
            LogUtils.d(TAG, "PIN, pattern or password is not set or a SIM card is unlocked.")
        }
    }
}