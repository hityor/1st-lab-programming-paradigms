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

    override fun printComposition(storage: DataStorage, doubleIngredients: Boolean) {
        val pizza = storage.pizzas.find { it.id == pizzaId }
            ?: error("Пицца не найдена")

        val base = storage.bases.find { it.id == pizza.baseId }
            ?: error("Основа не найдена")

        println("  Основа ${base.name} +${base.price} руб.")
        println("  Ингридиенты:")

        pizza.ingredientsIds.forEach { id ->
            val ingr = storage.ingredients.find { it.id == id }
                ?: error("Ингридиент не найден")

            val suffix = if (doubleIngredients) "x2" else ""
            println("    - ${ingr.name} +${ingr.price}$suffix руб.")
        }
    }

    override fun basePrice(storage: DataStorage): Int {
        val pizza = storage.pizzas.find { it.id == pizzaId }
            ?: error("Пицца не найдена")
        return pizza.calcPrice(storage)
    }

    fun getIngredientsIds(storage: DataStorage) =
        storage.pizzas.find { it.id == pizzaId }!!.ingredientsIds
}