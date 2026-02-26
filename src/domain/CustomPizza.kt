package domain

import java.util.*

class CustomPizza(
    private val name: String,
    private val baseId: UUID,
    private val ingredientsIds: List<UUID>,
) : PizzaOrder {
    override fun title(storage: DataStorage): String = "Кастом: $name"

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