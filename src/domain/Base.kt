package domain

import java.util.*

class Base(
    name: String,
    price: Int,
    val isClassic: Boolean,
    id: UUID = UUID.randomUUID()
) : CatalogItem(name, id) {
    var price: Int = price
        private set

    fun changePrice(newPrice: Int, classicBasePrice: Int) {
        require(!isClassic) { "Нельзя менять цену классической основы" }
        require(newPrice > 0) { "Цена должна быть > 0" }
        require(newPrice <= (classicBasePrice * 1.2).toInt()) {
            "Цена превышает предел: максимум +20% от классической"
        }
        price = newPrice
    }

    override fun printInfo(storage: DataStorage) {
        println("Основа: $name, цена $price, классическая: ${if (isClassic) "да" else "нет"}")
    }
}

