package domain

import java.util.*

class Pizza(
    name: String,
    baseId: UUID,
    ingredientsIds: List<UUID>,
    id: UUID = UUID.randomUUID()
) : CatalogItem(name, id) {
    var baseId: UUID = baseId
        private set

    private var ingredientsIdsMutable = ingredientsIds.toMutableList()

    val ingredientsIds: List<UUID>
        get() = ingredientsIdsMutable.toList()

    fun changeBaseId(newId: UUID) {
        baseId = newId
    }

    fun calcPrice(ingredients: List<Ingredient>, bases: List<Base>): Int {
        var price = 0;

        price += bases.find { it.id == baseId }?.price ?: error("Основа не найдена для пиццы $name")

        ingredientsIds.forEach { ingredientId ->
            price += ingredients.find { it.id == ingredientId }?.price ?: error("Ингредиент не найден для пиццы $name")
        }
        return price
    }

    override fun printInfo(storage: DataStorage) {
        println("Название: $name")

        println("Основа: ${storage.bases.find { it.id == baseId }?.name}")

        println("Ингридиенты")
        ingredientsIds.forEach { ingredientId ->
            val ingredient = storage.ingredients.find { it.id == ingredientId }
            println(ingredient?.name ?: "Не найден")
        }
        println("Общая цена: ${calcPrice(storage.ingredients, storage.bases)}")
    }

    fun setIngredients(newIngredientIds: List<UUID>) {
        ingredientsIdsMutable = newIngredientIds.toMutableList()
    }
}