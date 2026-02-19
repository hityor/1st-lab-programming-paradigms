package domain

import java.util.UUID

class Ingredient(
    name: String,
    price: Int,
    val id: UUID = UUID.randomUUID()
) {
    var name: String = name
        private set

    var price: Int = price
        private set

    fun changeName(newName: String) {
        require(newName.isNotBlank()) { "Название не может быть пустым" }
        name = newName.trim()
    }

    fun changePrice(newPrice: Int) {
        require(newPrice > 0) { "Цена должна быть > 0" }
        price = newPrice
    }
}
