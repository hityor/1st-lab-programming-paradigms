import java.util.UUID
import kotlin.collections.forEach

class Ingredient(
    var name: String, var price: Int, val id: UUID = UUID.randomUUID()
)

fun printIngredients(ingredients: List<Ingredient>) {
    ingredients.forEach { ingredient ->
        println("Название ${ingredient.name}, цена - ${ingredient.price}")
    }
}

fun chooseIngredient(ingredients: List<Ingredient>): UUID {
    println("Выберите номер элемента")
    ingredients.forEachIndexed { index, ingredient ->
        println("$index - Название: ${ingredient.name}, цена - ${ingredient.price}")
    }
    return ingredients[readln().toInt()].id
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
            println("Введите название ингридиента")
            val ingredientName = readln()
            println("Введите цену ингридиента")
            val ingredientPrice = readln().toInt()
            ingredients.add(Ingredient(name = ingredientName, price = ingredientPrice))
        } else if (userOutput == 2) {
            printIngredients(ingredients)
        } else if (userOutput == 3) {
            val idIngredient = chooseIngredient(ingredients)
            val ingredient = ingredients.find { it.id == idIngredient }

            println("Введите новое название (enter = не менять)")
            val newName = readln()
            if (newName != "") ingredient?.name = newName

            println("Введите новую цену (enter = не менять)")
            val newPrice = readln()
            if (newPrice != "") ingredient?.price = newPrice.toInt()
        } else if (userOutput == 4) {
            val idIngredient = chooseIngredient(ingredients)
            var used = false
            pizzas.forEach { pizza ->
                if (pizza.ingredientsIds.any { it == idIngredient }) {
                    used = true
                }
            }
            if (used) {
                println("Нельзя удаолить ингридиент, который используется в какой то пицце")
                continue
            }
            ingredients.removeIf { it.id == idIngredient }
        }

        line()
    }
}