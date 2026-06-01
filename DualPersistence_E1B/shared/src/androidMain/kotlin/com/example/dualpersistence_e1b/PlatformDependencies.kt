package com.example.dualpersistence_e1b

import android.content.Context
import app.cash.sqldelight.driver.android.AndroidSqliteDriver
import com.example.dualpersistence_e1b.db.AppDatabase
import com.example.dualpersistence_e1b.model.Item
import com.example.dualpersistence_e1b.repository.DualRepositoryProvider
import com.example.dualpersistence_e1b.repository.NoSqlItemRepository
import com.example.dualpersistence_e1b.repository.SQLiteItemRepository
import com.example.dualpersistence_e1b.repository.StorageToggleManager
import com.example.dualpersistence_e1b.viewmodel.ItemViewModel
import io.github.xxfast.kstore.file.storeOf
import okio.Path.Companion.toPath
import java.io.File

fun createItemViewModel(context: Context): ItemViewModel {
    val database = AppDatabase(
        AndroidSqliteDriver(AppDatabase.Schema, context, "app.db")
    )
    val sqlRepository = SQLiteItemRepository(database)
    val noSqlRepository = NoSqlItemRepository(createItemStore(context))
    val toggleManager = StorageToggleManager()
    val provider = DualRepositoryProvider(sqlRepository, noSqlRepository, toggleManager)

    return ItemViewModel(provider, toggleManager)
}

private fun createItemStore(context: Context) = storeOf(
    file = File(context.filesDir, "items.json").absolutePath.toPath(),
    default = emptyList<Item>()
)
