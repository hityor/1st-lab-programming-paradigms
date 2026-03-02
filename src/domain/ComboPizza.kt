package domain

import java.util.UUID
import kotlin.math.roundToInt

class ComboPizza(
    val leftPizzaId: UUID,
    val rightPizzaId: UUID,
    val baseId: UUID
) : PizzaOrder {

    override fun title(storage: DataStorage): String {
        val left = storage.pizzas.find { it.id == leftPizzaId }?.name ?: "?"
        val right = storage.pizzas.find { it.id == rightPizzaId }?.name ?: "?"
        return "1/2 $left + 1/2 $right"
    }

    override fun printComposition(storage: DataStorage, doubleIngredients: Boolean) {
        val base = storage.bases.find { it.id == baseId }
            ?: error("Основа не найдена")

        println("  Основа ${base.name} +${base.price} руб.")

        val (leftIds, rightIds) = halfIngredients(storage)

        println("  Левая половина:")
        leftIds.forEach {id ->
            val ingr  = storage.ingredients.find { it.id == id }
                ?: error("Ингридиент не найден")

            val suffix = if (doubleIngredients) "x2" else ""
            println("    - ${ingr.name} +${ingr.price}$suffix руб.")
        }

        println("  Правая половина:")
        rightIds.forEach {id ->
            val ingr  = storage.ingredients.find { it.id == id }
                ?: error("Ингридиент не найден")

            val suffix = if (doubleIngredients) "x2" else ""
            println("    - ${ingr.name} +${ingr.price}$suffix руб.")
        }
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