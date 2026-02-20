package app

import domain.Base
import domain.Ingredient
import domain.Pizza
import domain.DataStorage
import ui.basesMenu
import ui.ingredientsMenu
import ui.pizzaMenu
import utils.readIndex

fun main() {
    val ingredients = mutableListOf<Ingredient>(
        Ingredient(name = "Моцарелла", price = 115),
        Ingredient(name = "Нежный цыплёнок", price = 99),
        Ingredient(name = "Ветчина", price = 99),
        Ingredient(name = "Пепперони", price = 99),
        Ingredient(name = "Томаты", price = 79),
        Ingredient(name = "Маринованные огурчики", price = 79)
    )

    val bases = mutableListOf<Base>(
        Base(name = "классическая", price = 100, isClassic = true),
        Base(name = "толстая", price = 110, isClassic = false)
    )


    val pizzas = mutableListOf<Pizza>(
        Pizza(
            name = "Пепперони",
            baseId = bases[0].id,
            ingredientsIds = mutableListOf(ingredients[0].id, ingredients[4].id)
        ), Pizza(
            name = "какая та",
            baseId = bases[1].id,
            ingredientsIds = mutableListOf(ingredients[4].id, ingredients[5].id)
        ), Pizza(
            name = "еще какая та", baseId = bases[0].id, ingredientsIds = mutableListOf(ingredients[5].id)
        )
    )

    val storage = DataStorage(
        ingredients = ingredients,
        bases = bases,
        pizzas = pizzas
    )

    while (true) {
        println("0 - Выйти из программы")
        println("1 - Ингридиенты")
        println("2 - Основа")
        println("3 - Пицца")

        val output = readIndex("Выберите номер (0...3)", 4)
        when (output) {
            0 -> break
            1 -> ingredientsMenu(storage)
            2 -> basesMenu(storage)
            3 -> pizzaMenu(storage)
        }
    }
}