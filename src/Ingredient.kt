import java.util.UUID
import kotlin.collections.forEach

class Ingredient(
    name: String,
    price: Int,
    val id: UUID = UUID.randomUUID()
) {
    var name: String = name
        private set

    var price: Int = price
        private set

    fun changeName(newName: String) {
        require(newName.isNotBlank()) { "Название не может быть пустым" }
        name = newName.trim()
    }

    fun changePrice(newPrice: Int) {
        require(newPrice > 0) { "Цена должна быть > 0" }
        price = newPrice
    }
}

fun chooseIngredient(ingredients: List<Ingredient>): UUID {
    println("Выберите номер элемента")
    ingredients.forEachIndexed { index, ingredient ->
        println("$index - Название: ${ingredient.name}, цена - ${ingredient.price}")
    }
    return ingredients[readln().toInt()].id
}

fun addIngredient(ingredients: MutableList<Ingredient>) {
    val name = readNonBlank("Введите название ингредиента")

    val price = readPositiveInt("Введите цену ингредиента")

    ingredients.add(Ingredient(name, price))
}

fun printIngredients(ingredients: List<Ingredient>) {
    ingredients.forEach { ingredient ->
        println("Название ${ingredient.name}, цена - ${ingredient.price}")
    }
}

fun editIngredient(ingredients: List<Ingredient>) {
    val idIngredient = chooseIngredient(ingredients)
    val ingredient = ingredients.find { it.id == idIngredient }

    val newName = readOptionalNonBlank("Введите новое название (enter = не менять)")
    if (newName != null) ingredient?.changeName(newName)

    val newPrice = readOptionalNonBlank("Введите новую цену (enter = не менять)")
    if (newPrice != null) ingredient?.changePrice(newPrice.toInt())
}

fun deleteIngredient(ingredients: MutableList<Ingredient>, pizzas: List<Pizza>) {
    val idIngredient = chooseIngredient(ingredients)
    var used = false
    pizzas.forEach { pizza ->
        if (pizza.ingredientsIds.any { it == idIngredient }) {
            used = true
        }
    }
    if (used) {
        println("Нельзя удалить ингридиент, который используется в какой то пицце")
        return
    }
    ingredients.removeIf { it.id == idIngredient }
}

fun ingredientsMenu(ingredients: MutableList<Ingredient>, pizzas: List<Pizza>) {
    while (true) {
        println("0 - Выйти из меню")
        println("1 - Создать ингридиент")
        println("2 - Вывести список ингридиентов")
        println("3 - Редактировать ингридиент")
        println("4 - Удалить ингридиент")

        val userOutput = readln().toInt()

        if (userOutput == 0) {
            break
        } else if (userOutput == 1) {
            addIngredient(ingredients)
        } else if (userOutput == 2) {
            printIngredients(ingredients)
        } else if (userOutput == 3) {
            editIngredient(ingredients)
        } else if (userOutput == 4) {
            deleteIngredient(ingredients, pizzas)
        }

        line()
    }
}