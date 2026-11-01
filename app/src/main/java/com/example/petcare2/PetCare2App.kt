package com.example.petcare2

import android.app.Application
import com.example.petcare2.core.di.coreModule
import com.example.petcare2.detail.di.detailModule
import com.example.petcare2.favorite.di.favoriteModule
import com.example.petcare2.home.di.homeModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.component.KoinComponent
import org.koin.core.context.startKoin

class PetCare2App : Application(), KoinComponent {
    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidLogger()
            androidContext(this@PetCare2App)
            modules(
                coreModule,
                homeModule,
                detailModule,
                favoriteModule
            )
        }
    }
}