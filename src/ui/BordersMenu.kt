package ui

import domain.Border
import domain.DataStorage
import domain.Ingredient
import domain.Pizza
import utils.readIndex
import utils.readInt
import utils.readNonBlank
import utils.readOptionalNonBlank

fun printBorders(borders: List<Border>, ingredients: List<Ingredient>, pizzas: List<Pizza>) {
    borders.forEach { border ->
        border.printInfo(ingredients, pizzas)
    }
}

fun chooseForbiddenPizzas(border: Border, pizzas: List<Pizza>) {
    while (true) {
        println("Выберите пиццу которую хотите запретить для этого бортика: 0...${pizzas.size - 1}, или -1 чтобы закончить")
        pizzas.forEachIndexed { index, pizza ->
            println("${index} - ${pizza.name}")
        }

        val n = readInt("Номер:")
        if (n == -1) break

        if (n !in 0..<pizzas.size) {
            println("Неверный номер")
            continue
        }

        val pizzaIdToForbid = pizzas[n].id
        if (border.isAllowedFor(pizzaIdToForbid)) border.forbidPizza(pizzaIdToForbid)
        else println("Эта пицца уже запрещена")
    }
}

fun addBorder(borders: MutableList<Border>, pizzas: List<Pizza>, ingredients: List<Ingredient>) {
    val name = readNonBlank("Введите название бортика")
    val ingredientsIds = chooseIngredients(ingredients)

    val newBorder = Border(name, ingredientsIds)
    borders.add(newBorder)

    chooseForbiddenPizzas(newBorder, pizzas)
}

fun editBorder(borders: List<Border>, ingredients: List<Ingredient>) {
    borders.forEachIndexed { index, border ->
        println("$index - ${border.name}")
    }
    val borderIdx = readIndex("Введите номер бортика который хотите изменить", borders.size)
    val border = borders[borderIdx]

    println("${border.name} - название бортика")
    val newName = readOptionalNonBlank("Введите новое название бортика (enter = не менять название)")
    if (newName != null) border.changeName(newName)

    println("Текущий список ингридиентов")
    border.ingredientsIds.forEach { ingredientId ->
        println("${ingredients.find { it.id == ingredientId }?.name}")
    }
    val changeIngredients = readIndex("0 - не менять ингредиенты, 1 - пересобрать ингредиенты", 2)
    if (changeIngredients == 1) {
        val newIngredientsIds = chooseIngredients(ingredients)
        border.setIngredients(newIngredientsIds)
    }
}

fun deleteBorder(borders: MutableList<Border>) {
    borders.forEachIndexed { index, border ->
        println("$index - ${border.name}")
    }

    val chosenPizzaIdx = readIndex("Выберитие номер бортика который хотите удалить", borders.size)
    val chosenPizzaId = borders[chosenPizzaIdx].id

    borders.removeIf { it.id == chosenPizzaId }
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
            1 -> printBorders(dataStorage.borders, dataStorage.ingredients, dataStorage.pizzas)
            2 -> addBorder(dataStorage.borders, dataStorage.pizzas, dataStorage.ingredients)
            3 -> editBorder(dataStorage.borders, dataStorage.ingredients)
            4 -> deleteBorder(dataStorage.borders)
        }
    }


}