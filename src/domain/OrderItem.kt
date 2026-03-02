package domain

import java.util.*
import kotlin.math.roundToInt

class OrderItem(
    val pizza: PizzaOrder,
    val pizzaSize: PizzaSize,
    val doubleIngredients: Boolean,
    val border: BorderChoice? = null
)  {
    fun calcPrice(storage: DataStorage): Int {
        var price = pizza.basePrice(storage)

        if (doubleIngredients) {
            val extra = when (pizza) {
                is CatalogPizza -> {
                    pizza.getIngredientsIds(storage).sumOf { id ->
                        storage.ingredients.find { it.id == id }?.price
                            ?: error("Ингридиент не найден")
                    }
                }

                is CustomPizza -> {
                    pizza.getIngredientsIds().sumOf { id ->
                        storage.ingredients.find { it.id == id }?.price
                            ?: error("Ингридиент не найден")
                    }
                }

                is ComboPizza -> {
                    val (leftIds, rightIds) = pizza.halfIngredients(storage)

                    val leftSum = leftIds.sumOf { id ->
                        storage.ingredients.find { it.id == id }?.price
                            ?: error("Ингридиент не найден")
                    }

                    val rightSum = rightIds.sumOf { id ->
                        storage.ingredients.find { it.id == id }?.price
                            ?: error("Ингридиент не найден")
                    }

                    (0.5 * leftSum).roundToInt() + (0.5 * rightSum).roundToInt()
                }

                else -> error("Неизвестный тип pizza")
            }

            price += extra
        }

        if (border != null) {
            price += border.calcPrice(storage)
        }

        return (price * pizzaSize.multiplier).roundToInt()
    }

    fun printInfo(storage: DataStorage) {
        println("Позиция: ${pizza.title(storage)}")
        println("  Размер: $pizzaSize (x${pizzaSize.multiplier})")
        println("  Удовение игридиентов: ${if (doubleIngredients) "да" else "нет"}")

        if (border != null) {
            println("  Бортик: ${border.title(storage)} +${border.calcPrice(storage)} руб.")
        } else {
            println("  Бортик: нет")
        }

        pizza.printComposition(storage, doubleIngredients)

        println("  Итого по позиции: ${calcPrice(storage)} руб.")
        println()
    }
}