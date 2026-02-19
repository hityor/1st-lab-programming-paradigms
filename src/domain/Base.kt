package domain

import java.util.UUID

class Base(
    name: String,
    price: Int,
    val isClassic: Boolean,
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

    fun changePrice(newPrice: Int, classicBasePrice: Int) {
        require(!isClassic) { "Нельзя менять цену классической основы" }
        require(newPrice > 0) { "Цена должна быть > 0" }
        require(newPrice <= (classicBasePrice * 1.2).toInt()) {
            "Цена превышает предел: максимум +20% от классической"
        }
        price = newPrice
    }
}

