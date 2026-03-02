package domain

import java.util.*

class CustomPizza(
    private val name: String,
    val baseId: UUID,
    private val ingredientsIds: List<UUID>,
) : PizzaOrder {
    override fun title(storage: DataStorage): String = "Кастом: $name"

    override fun printComposition(storage: DataStorage, doubleIngredients: Boolean) {
        val base = storage.bases.find { it.id == baseId }
            ?: error("Основа не найдена")

        println("  Основа ${base.name} +${base.price} руб.")
        println("  Ингридиенты:")

        ingredientsIds.forEach { id ->
            val ingr = storage.ingredients.find { it.id == id }
                ?: error("Ингридиент не найден")

            val suffix = if (doubleIngredients) "x2" else ""
            println("    - ${ingr.name} +${ingr.price}$suffix руб.")
        }
    }

    override fun basePrice(storage: DataStorage): Int {
        val base = storage.bases.find { it.id == baseId }
            ?: error("Основа не найдена")

        val ingredientsSum = ingredientsIds.sumOf { id ->
            storage.ingredients.find { it.id == id }?.price
                ?: error("Ингридиент не найден")
        }

        return base.price + ingredientsSum
    }

    fun getIngredientsIds(): List<UUID> = ingredientsIds
}