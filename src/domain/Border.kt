package domain

import java.util.*

class Border(
    name: String,
    ingredientsIds: List<UUID>,
    id: UUID = UUID.randomUUID()
) : CatalogItem(name, id) {

    private val ingredientsIdsMutable: MutableList<UUID> = ingredientsIds.toMutableList()
    val ingredientsIds: List<UUID> get() = ingredientsIdsMutable.toList()

    private val forbiddenPizzaIdsMutable = mutableSetOf<UUID>()
    val forbiddenPizzaIds: List<UUID> get() = forbiddenPizzaIdsMutable.toList()

    fun setIngredients(newIngredientsIds: List<UUID>) {
        ingredientsIdsMutable.clear()
        ingredientsIdsMutable.addAll(newIngredientsIds)
    }

    override fun printInfo(storage: DataStorage) {
        println("Название: $name")

        println("Ингредиенты")
        ingredientsIds.forEach { ingredientId ->
            val ingredient = storage.ingredients.find { it.id == ingredientId }?.name ?: error("Ингридиент бортика $name не найден")
            println(ingredient)
        }

        println("Пиццы, с которыми запрещено использовать этот бортик:")
        forbiddenPizzaIds.forEach { frbdnId ->
            println(storage.pizzas.find { it.id == frbdnId }?.name)
        }

        println("Цена бортика: ${calcPrice(storage.ingredients)}")
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