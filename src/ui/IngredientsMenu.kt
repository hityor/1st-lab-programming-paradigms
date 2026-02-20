package ui

import domain.Ingredient
import domain.Pizza
import domain.DataStorage
import utils.*


import java.util.*


fun chooseIngredient(ingredients: List<Ingredient>): UUID {
    ingredients.forEachIndexed { index, ingredient ->
        println("$index - Название: ${ingredient.name}, цена - ${ingredient.price}")
    }

    val idx = readIndex("Выберите номер элемента", ingredients.size)
    return ingredients[idx].id
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

    val newPrice = readOptionalPositiveInt("Введите новую цену (enter = не менять)")
    if (newPrice != null) ingredient?.changePrice(newPrice)
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
            1 -> addIngredient(dataStorage.ingredients)
            2 -> printIngredients(dataStorage.ingredients)
            3 -> editIngredient(dataStorage.ingredients)
            4 -> deleteIngredient(dataStorage.ingredients, dataStorage.pizzas)
        }

        line()
    }
}