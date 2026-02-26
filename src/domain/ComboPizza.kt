package domain

import java.util.UUID
import kotlin.math.roundToInt

class ComboPizza(
    private val leftPizzaId: UUID,
    private val rightPizzaId: UUID,
    private val baseId: UUID
) : PizzaOrder {

    override fun title(storage: DataStorage): String {
        val left = storage.pizzas.find { it.id == leftPizzaId }?.name ?: "?"
        val right = storage.pizzas.find { it.id == rightPizzaId }?.name ?: "?"
        return "1/2 $left + 1/2 $right"
    }

    override fun basePrice(storage: DataStorage): Int {
        val left = storage.pizzas.find { it.id == leftPizzaId }
            ?: error("Лвевая пицца не найдена")
        val right = storage.pizzas.find { it.id == rightPizzaId }
            ?: error("Правая пицца не найдена")

        val base = storage.bases.find { it.id == baseId }
            ?: error("Основа не найдена")

        val leftSum = left.ingredientsIds.sumOf { id ->
            storage.ingredients.find { it.id == id }?.price
                ?: error("Ингридиент не найден")
        }

        val rightSum = right.ingredientsIds.sumOf { id ->
            storage.ingredients.find { it.id == id }?.price
                ?: error("Ингридиент не найден")
        }

        return (0.5 * leftSum).roundToInt() + (0.5 * rightSum).roundToInt() + base.price
    }

    fun halfIngredients(storage: DataStorage): Pair<List<UUID>, List<UUID>> {
        val left = storage.pizzas.find { it.id == leftPizzaId }
            ?: error("Левая пицца не найдена")

        val right = storage.pizzas.find { it.id == rightPizzaId }
            ?: error("Правая пицца не надена")

        return left.ingredientsIds to right.ingredientsIds
    }
}