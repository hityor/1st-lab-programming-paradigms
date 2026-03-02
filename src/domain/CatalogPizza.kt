package domain

import java.util.*

class CatalogPizza(
    val pizzaId: UUID
) : PizzaOrder {
    override fun title(storage: DataStorage): String {
        val pizza = storage.pizzas.find { it.id == pizzaId }
            ?: error("Пицца не найдена")
        return pizza.name
    }

    override fun basePrice(storage: DataStorage): Int {
        val pizza = storage.pizzas.find { it.id == pizzaId }
            ?: error("Пицца не найдена")
        return pizza.calcPrice(storage)
    }

    fun getIngredientsIds(storage: DataStorage) =
        storage.pizzas.find { it.id == pizzaId }!!.ingredientsIds
}