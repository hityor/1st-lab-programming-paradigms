package ui

import domain.Ingredient
import domain.Pizza
import domain.DataStorage
import utils.*


import java.util.*


fun chooseIngredient(storage: DataStorage): UUID {
    storage.ingredients.forEachIndexed { index, ingredient ->
        println("$index - Название: ${ingredient.name}, цена - ${ingredient.price}")
    }

    val idx = readIndex("Выберите номер элемента", storage.ingredients.size)
    return storage.ingredients[idx].id
}

fun addIngredient(storage: DataStorage) {
    val name = readNonBlank("Введите название ингредиента")

    val price = readPositiveInt("Введите цену ингредиента")

    storage.ingredients.add(Ingredient(name, price))
}

fun printIngredients(storage: DataStorage) {
    printItems(storage, storage.ingredients)
}

fun editIngredient(storage: DataStorage) {
    val idIngredient = chooseIngredient(storage)
    val ingredient = storage.ingredients.find { it.id == idIngredient }

    val newName = readOptionalNonBlank("Введите новое название (enter = не менять)")
    if (newName != null) ingredient?.changeName(newName)

    val newPrice = readOptionalPositiveInt("Введите новую цену (enter = не менять)")
    if (newPrice != null) ingredient?.changePrice(newPrice)
}

fun deleteIngredient(storage: DataStorage) {
    val idIngredient = chooseIngredient(storage)
    var used = false
    storage.pizzas.forEach { pizza ->
        if (pizza.ingredientsIds.any { it == idIngredient }) {
            used = true
        }
    }
    if (used) {
        println("Нельзя удалить ингридиент, который используется в какой то пицце")
        return
    }
    storage.ingredients.removeIf { it.id == idIngredient }
}

fun ingredientsMenu(dataStorage: DataStorage) {
    while (true) {
        println("0 - Выйти из меню")
        println("1 - Создать ингридиент")
        println("2 - Вывести список ингридиентов")
        println("3 - Редактировать ингридиент")
        println("4 - Удалить ингридиент")

        val userOutput = readIndex("Выберите номер (0...4)", 5)

        when (userOutput) {
            0 -> break
            1 -> addIngredient(dataStorage)
            2 -> printIngredients(dataStorage)
            3 -> editIngredient(dataStorage)
            4 -> deleteIngredient(dataStorage)
        }

        line()
    }
}