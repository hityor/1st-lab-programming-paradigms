package ui

import domain.Border
import domain.DataStorage
import domain.Ingredient
import domain.Pizza
import utils.readIndex
import utils.readInt
import utils.readNonBlank
import utils.readOptionalNonBlank

fun printBorders(storage: DataStorage) {
    storage.borders.forEach { border ->
        border.printInfo(storage.ingredients, storage.pizzas)
    }
}

fun chooseForbiddenPizzas(border: Border, storage: DataStorage) {
    while (true) {
        println("Выберите пиццу которую хотите запретить для этого бортика: 0...${storage.pizzas.size - 1}, или -1 чтобы закончить")
        storage.pizzas.forEachIndexed { index, pizza ->
            println("${index} - ${pizza.name}")
        }

        val n = readInt("Номер:")
        if (n == -1) break

        if (n !in 0..<storage.pizzas.size) {
            println("Неверный номер")
            continue
        }

        val pizzaIdToForbid = storage.pizzas[n].id
        if (border.isAllowedFor(pizzaIdToForbid)) border.forbidPizza(pizzaIdToForbid)
        else println("Эта пицца уже запрещена")
    }
}

fun addBorder(storage: DataStorage) {
    val name = readNonBlank("Введите название бортика")
    val ingredientsIds = chooseIngredients(storage)

    val newBorder = Border(name, ingredientsIds)
    storage.borders.add(newBorder)

    chooseForbiddenPizzas(newBorder, storage)
}

fun editBorder(storage: DataStorage) {
    storage.borders.forEachIndexed { index, border ->
        println("$index - ${border.name}")
    }
    val borderIdx = readIndex("Введите номер бортика который хотите изменить", storage.borders.size)
    val border = storage.borders[borderIdx]

    println("${border.name} - название бортика")
    val newName = readOptionalNonBlank("Введите новое название бортика (enter = не менять название)")
    if (newName != null) border.changeName(newName)

    println("Текущий список ингридиентов")
    border.ingredientsIds.forEach { ingredientId ->
        println("${storage.ingredients.find { it.id == ingredientId }?.name}")
    }
    val changeIngredients = readIndex("0 - не менять ингредиенты, 1 - пересобрать ингредиенты", 2)
    if (changeIngredients == 1) {
        val newIngredientsIds = chooseIngredients(storage)
        border.setIngredients(newIngredientsIds)
    }
}

fun deleteBorder(storage: DataStorage) {
    storage.borders.forEachIndexed { index, border ->
        println("$index - ${border.name}")
    }

    val chosenPizzaIdx = readIndex("Выберитие номер бортика который хотите удалить", storage.borders.size)
    val chosenPizzaId = storage.borders[chosenPizzaIdx].id

    storage.borders.removeIf { it.id == chosenPizzaId }
}

fun bordersMenu(dataStorage: DataStorage) {
    while (true) {
        println("0 - Выйти из меню")
        println("1 - Вывести список бортиков")
        println("2 - Добавить бортик")
        println("3 - Редактировать бортик")
        println("4 - Удалить бортик")

        val userOutput = readIndex("Выберите номер (0...4)", 5)

        when (userOutput) {
            0 -> break
            1 -> printBorders(dataStorage)
            2 -> addBorder(dataStorage)
            3 -> editBorder(dataStorage)
            4 -> deleteBorder(dataStorage)
        }
    }


}