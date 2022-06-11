package lishui.service.data.pref

import android.content.Context
import android.lib.base.log.LogUtils
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.*
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.map
import lishui.service.core.AppDemo
import java.io.IOException

// At the top level to init global DataStore
private const val DATA_STORE_PREF_NAME = "demo_pref_store"
private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = DATA_STORE_PREF_NAME)

object PrefDataStore {

    private val prefDataStore: DataStore<Preferences> by lazy { AppDemo.appContext.dataStore }

    /* DataStore 写操作 */
    
    suspend fun putBooleanData(key: String, value: Boolean) {
        prefDataStore.edit { mutablePreferences ->
            mutablePreferences[booleanPreferencesKey(key)] = value
        }
    }

    suspend fun putIntData(key: String, value: Int) {
        prefDataStore.edit { mutablePreferences ->
            mutablePreferences[intPreferencesKey(key)] = value
        }
    }

    suspend fun putFloatData(key: String, value: Float) {
        prefDataStore.edit { mutablePreferences ->
            mutablePreferences[floatPreferencesKey(key)] = value
        }
    }

    suspend fun putLongData(key: String, value: Long) {
        prefDataStore.edit { mutablePreferences ->
            mutablePreferences[longPreferencesKey(key)] = value
        }
    }

    suspend fun putStringData(key: String, value: String) {
        prefDataStore.edit { mutablePreferences ->
            mutablePreferences[stringPreferencesKey(key)] = value
        }
    }

    suspend fun putStringSetData(key: String, value: Set<String>) {
        prefDataStore.edit { mutablePreferences ->
            mutablePreferences[stringSetPreferencesKey(key)] = value
        }
    }

    
    /* DataStore 读操作 */
    
    fun getBooleanFlow(key: String, default: Boolean = false): Flow<Boolean> =
        prefDataStore.data
            .catch {
                // 当读取数据遇到错误时，如果是 `IOException` 异常，发送一个 emptyPreferences 来重新使用
                // 但是如果是其他的异常，最好将它抛出去，不要隐藏问题
                if (it is IOException) {
                    emit(emptyPreferences())
                } else {
                    throw it
                }
            }.map {
                it[booleanPreferencesKey(key)] ?: default
            }

    fun getIntFlow(key: String, default: Int = 0): Flow<Int> =
        prefDataStore.data
            .catch {
                if (it is IOException) {
                    emit(emptyPreferences())
                } else {
                    throw it
                }
            }.map {
                it[intPreferencesKey(key)] ?: default
            }

    fun getFloatFlow(key: String, default: Float = 0f): Flow<Float> =
        prefDataStore.data
            .catch {
                if (it is IOException) {
                    emit(emptyPreferences())
                } else {
                    throw it
                }
            }.map {
                it[floatPreferencesKey(key)] ?: default
            }

    fun getLongFlow(key: String, default: Long = 0L): Flow<Long> =
        prefDataStore.data
            .catch {
                if (it is IOException) {
                    emit(emptyPreferences())
                } else {
                    throw it
                }
            }.map {
                it[longPreferencesKey(key)] ?: default
            }

    fun getStringFlow(key: String, default: String = ""): Flow<String> =
        prefDataStore.data
            .catch {
                if (it is IOException) {
                    emit(emptyPreferences())
                } else {
                    throw it
                }
            }.map {
                it[stringPreferencesKey(key)] ?: default
            }

    fun getStringSetFlow(key: String, default: Set<String> = emptySet()): Flow<Set<String>> =
        prefDataStore.data
            .catch {
                if (it is IOException) {
                    emit(emptyPreferences())
                } else {
                    throw it
                }
            }.map {
                it[stringSetPreferencesKey(key)] ?: default
            }

    suspend fun printPrefStoreData() {
        prefDataStore.data.collect {
            LogUtils.d("$DATA_STORE_PREF_NAME file data $it")
        }
    }

}