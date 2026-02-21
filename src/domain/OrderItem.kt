package domain

import java.util.UUID

class OrderItem(
    val pizzaId: UUID,
    val pizzaSize: PizzaSize,
    val doubleIngredients: Boolean,
    val borderId: UUID? = null
) {
    fun calcPrice(storage: DataStorage): Int {
        val pizza = storage.pizzas.find { it.id == pizzaId } ?: error("Пицца не найдена")

        var price = pizza.calcPrice(storage.ingredients, storage.bases)

        if (doubleIngredients) {
            val extra = pizza.ingredientsIds.sumOf { id ->
                storage.ingredients.find { it.id == id }?.price
                    ?: error("Ингредиент не найден")
            }
            price += extra
        }

        if (borderId != null) {
            val border = storage.borders.find { it.id == borderId } ?: error("Бортик не найден")

            if (!border.isAllowedFor(pizzaId))
                error("Этот бортик нельзя использовать с этой пиццей")

            price += border.calcPrice(storage.ingredients)
        }

        return (price * pizzaSize.multiplier).toInt()
    }
}