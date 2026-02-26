package domain

import java.util.*
import kotlin.math.roundToInt

class OrderItem(
    val pizza: PizzaOrder,
    val pizzaSize: PizzaSize,
    val doubleIngredients: Boolean,
    val borderId: UUID? = null
) {
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

        if (borderId != null) {
            val border  = storage.borders.find { it.id == borderId }
                ?: error("Бортик не найден")
            price += border.calcPrice(storage.ingredients)
        }

        return (price * pizzaSize.multiplier).roundToInt()
    }

    fun printInfo(storage: DataStorage) {
        println("${pizza.title(storage)} - ${calcPrice(storage)} руб.")
    }
}