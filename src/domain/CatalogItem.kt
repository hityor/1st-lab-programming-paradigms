package domain

import java.util.*

abstract class CatalogItem(
    name: String,
    val id: UUID = UUID.randomUUID()
) {
    var name: String = name
        private set

    fun changeName(newName: String) {
        require(newName.isNotBlank()) { "Название не может быть пустым" }
        name = newName.trim()
    }

    abstract fun printInfo(storage: DataStorage)
}