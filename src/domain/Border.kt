package domain

import java.util.*

class Border(
    name: String,
    ingredientsIds: List<UUID>,
    val id: UUID = UUID.randomUUID()
) {
    var name: String = name
        private set

    private val ingredientsIdsMutable: MutableList<UUID> = ingredientsIds.toMutableList()
    val ingredientsIds: List<UUID> get() = ingredientsIdsMutable.toList()

    private val forbiddenPizzaIdsMutable = mutableSetOf<UUID>()
    val forbiddenPizzaIds: List<UUID> get() = forbiddenPizzaIdsMutable.toList()

    fun changeName(newName: String) {
        require(newName.isNotBlank()) { "Название не может быть пустым" }
        name = newName.trim()
    }

    fun setIngredients(newIngredientsIds: List<UUID>) {
        ingredientsIdsMutable.clear()
        ingredientsIdsMutable.addAll(newIngredientsIds)
    }

    fun printInfo(ingredients: List<Ingredient>, pizzas: List<Pizza>) {
        println("Название: $name")

        println("Ингредиенты")
        ingredientsIds.forEach { ingredientId ->
            val ingr = ingredients.find { it.id == ingredientId }?.name ?: error("Ингридиент бортика $name не найден")
            println(ingr)
        }

        println("Пиццы, с которыми запрещено использовать этот бортик:")
        forbiddenPizzaIds.forEach { frbdnId ->
            println(pizzas.find { it.id == frbdnId }?.name)
        }

        println("Цена бортика: ${calcPrice(ingredients)}")
    }

    fun calcPrice(ingredients: List<Ingredient>): Int =
        ingredientsIds.sumOf { ingredientId ->
            ingredients.find { it.id == ingredientId }?.price ?: error("Ингридиент бортика $name не найден")
        }

    fun forbidPizza(pizzaId: UUID) {
        forbiddenPizzaIdsMutable.add(pizzaId)
    }

    fun allowPizza(pizzaId: UUID) {
        forbiddenPizzaIdsMutable.remove(pizzaId)
    }

    fun isAllowedFor(pizzaId: UUID): Boolean = pizzaId !in forbiddenPizzaIdsMutable
}