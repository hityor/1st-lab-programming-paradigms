package domain

import java.util.UUID

class Ingredient(
    name: String,
    price: Int,
    id: UUID = UUID.randomUUID()
) : CatalogItem(name, id) {
    var price: Int = price
        private set

    fun changePrice(newPrice: Int) {
        require(newPrice > 0) { "Цена должна быть > 0" }
        price = newPrice
    }

    override fun printInfo(storage: DataStorage) {
        println("Ингидиент: $name, цена: $price")
    }
}
