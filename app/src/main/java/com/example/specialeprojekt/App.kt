package com.example.specialeprojekt

import android.app.Application
import android.util.Log
import org.spongycastle.jce.provider.BouncyCastleProvider
import java.security.Security
class App : Application() {
    override fun onCreate() {
        super.onCreate()

        // when app is created, remove BC as a provider and add it again through spongycastle
        Security.removeProvider("BC")

        Security.insertProviderAt(BouncyCastleProvider(), 1)
        Log.d("PASSPORT", "Providers: ${Security.getProviders().map { it.name }}")
    }
}