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

    fun changeName(newName: String) {
        name = newName.trim()
    }

    fun setIngredientsIds(newIngredientsIds: List<UUID>) {
        ingredientsIdsMutable.clear()
        ingredientsIdsMutable.addAll(newIngredientsIds)
    }

    fun calcPrice(ingredients: List<Ingredient>): Int {
        var price = 0
        ingredientsIdsMutable.forEach {ingredientId ->
            price += ingredients.find { it.id == ingredientId }?.price ?: error("Ингридиент бортика не найден: $name")
        }

        return price
    }
}