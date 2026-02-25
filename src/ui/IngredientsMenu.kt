package ui


import domain.DataStorage
import domain.Ingredient
import utils.*


fun chooseIngredient(storage: DataStorage, prompt: String? = null): Ingredient {
    val sortedIngredients = storage.ingredients.sortedBy { it.price }

    if (prompt != null) println(prompt)
    sortedIngredients.forEachIndexed { index, ingredient ->
        println("$index - Название: ${ingredient.name}, цена - ${ingredient.price}")
    }

    val idx = readIndex("Выберите номер элемента", sortedIngredients.size)
    return sortedIngredients[idx]
}

fun addIngredient(storage: DataStorage) {
    val name = readNonBlank("Введите название ингредиента")

    val price = readPositiveInt("Введите цену ингредиента")

    storage.ingredients.add(Ingredient(name, price))
}

fun printIngredients(storage: DataStorage) {
    val sortedIngredients = storage.ingredients.sortedBy { it.price }
    printItems(storage, sortedIngredients)
}

fun editIngredient(storage: DataStorage) {
    val ingredient = chooseIngredient(storage, "Выберите ингридиент, который хотите изменить")

    val newName = readOptionalNonBlank("Введите новое название (enter = не менять)")
    if (newName != null) ingredient.changeName(newName)

    val newPrice = readOptionalPositiveInt("Введите новую цену (enter = не менять)")
    if (newPrice != null) ingredient.changePrice(newPrice)
}

fun deleteIngredient(storage: DataStorage) {
    val ingredientToDelete = chooseIngredient(storage, "Выберите ингридиент, который хотите удалить")

    var usedInPizza = false
    storage.pizzas.forEach { pizza ->
        if (pizza.ingredientsIds.any { it == ingredientToDelete.id }) {
            usedInPizza = true
        }
    }
    if (usedInPizza) {
        println("Нельзя удалить ингридиент, который используется в какой то пицце")
        return
    }

    var usedInBorder = false
    storage.borders.forEach { border ->
        if (border.ingredientsIds.any { it == ingredientToDelete.id }) {
            usedInBorder = true
        }
    }
    if (usedInBorder) {
        println("Нельзя удалить ингридиент, который используется в каком то бортике")
        return
    }

    storage.ingredients.removeIf { it.id == ingredientToDelete.id }
}

fun filterIngredients(storage: DataStorage): List<Ingredient> {
    println("0 - Сортировать по имени")
    println("1 - Сортировать по диапазону цены")

    val userChoice = readIndex("Выбор: ", 2)

    when (userChoice) {
        0 -> {
            val filterName = readNonBlank("Введите название: ")

            return storage.ingredients.filter {
                it.name.contains(
                    filterName,
                    ignoreCase = true
                )
            }
        }

        else -> {
            val start = readPositiveInt("Введите начальный диапазон")
            val end = readPositiveInt("Введите конечный диапазон")

            return storage.ingredients.filter {
                it.price in start..end
            }
        }
    }
}


fun ingredientsMenu(dataStorage: DataStorage) {
    while (true) {
        println("0 - Выйти из меню")
        println("1 - Создать ингридиент")
        println("2 - Вывести список ингридиентов")
        println("3 - Редактировать ингридиент")
        println("4 - Удалить ингридиент")
        println("5 - Фильтрация")

        val userOutput = readIndex("Выберите номер (0...5)", 6)

        when (userOutput) {
            0 -> break
            1 -> addIngredient(dataStorage)
            2 -> printIngredients(dataStorage)
            3 -> editIngredient(dataStorage)
            4 -> deleteIngredient(dataStorage)
            5 -> {
                val filteredIngredients = filterIngredients(dataStorage)

                filteredIngredients.forEach { ingredient -> ingredient.printInfo(dataStorage) }
            }
        }

        line()
    }
}