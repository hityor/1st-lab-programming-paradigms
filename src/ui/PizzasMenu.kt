package ui

import domain.Base
import domain.DataStorage
import domain.Ingredient
import domain.Pizza
import utils.readIndex
import utils.readInt
import utils.readNonBlank
import utils.readOptionalNonBlank
import java.util.*

fun chooseIngredients(ingredients: List<Ingredient>): List<UUID> {
    val chosen = mutableListOf<UUID>()

    while (true) {
        println("Выберите ингредиент: 0...${ingredients.size - 1}, или -1 чтобы закончить")
        ingredients.forEachIndexed { index, ingredient ->
            println("$index - ${ingredient.name}, цена: ${ingredient.price}")
        }

        val n = readInt("Номер:")
        if (n == -1) break

        if (n !in 0..<ingredients.size) {
            println("Неверный номер")
            continue
        }

        val id = ingredients[n].id
        if (id in chosen) {
            println("Этот ингредиент уже выбран")
        } else {
            chosen.add(id)
        }
    }

    return chosen
}

fun printPizzas(pizzas: List<Pizza>, ingredients: List<Ingredient>, bases: List<Base>) {
    pizzas.forEach { pizza ->
        pizza.printInfo(ingredients, bases)
    }
}

fun addPizza(pizzas: MutableList<Pizza>, ingredients: List<Ingredient>, bases: List<Base>) {
    if (bases.isEmpty()) {
        println("Основ нет, пиццу создать нельзя")
        return
    }

    val pizzaName = readNonBlank("Введите название пиццы")

    bases.forEachIndexed { index, base ->
        println("$index - ${base.name}")
    }
    val pizzaIdx = readIndex("Выберите номер основы", bases.size)
    val pizzaBaseId = bases[pizzaIdx].id

    val newPizza = Pizza(pizzaName, pizzaBaseId, chooseIngredients(ingredients))

    pizzas.add(newPizza)
}

fun deletePizza(pizzas: MutableList<Pizza>) {
    pizzas.forEachIndexed { index, pizza ->
        println("$index - ${pizza.name}")
    }

    val chosenPizzaIdx = readIndex("Выберитие номер пиццы которую хотите удалить", pizzas.size)
    val chosenPizzaId = pizzas[chosenPizzaIdx].id

    pizzas.removeIf { it.id == chosenPizzaId }
}

fun editPizza(pizzas: MutableList<Pizza>, ingredients: List<Ingredient>, bases: List<Base>) {
    pizzas.forEachIndexed { index, pizza ->
        println("$index - ${pizza.name}")
    }

    val pizzaIdx = readIndex("Введите номер пиццы которую хотите изменить", pizzas.size)
    val pizza = pizzas[pizzaIdx]

    println("${pizza.name} - название пиццы")
    val newName = readOptionalNonBlank("Введите новое название пиццы (enter = не менять название)")
    if (newName != null) pizza.changeName(newName)

    println("${bases.find { it.id == pizza.baseId }?.name} - основа пиццы")
    val changeBase = readIndex("0 - не меняем основу, 1 - меняем основу", 2)
    if (changeBase == 1) {
        bases.forEachIndexed { index, base ->
            println("$index - ${base.name}")
        }
        val newBaseId = bases[readIndex("Выберите номер новой основы", bases.size)].id
        pizza.changeBaseId(newBaseId)
    }

    println("Текущий список ингридиентов")
    pizza.ingredientsIds.forEachIndexed { index, ingredientId ->
        println("$index - ${ingredients.find { it.id == ingredientId }?.name}")
    }
    val changeIngredients = readIndex("0 - не менять ингредиенты, 1 - пересобрать ингредиенты", 2)
    if (changeIngredients == 1) {
        val newIngredientsIds = chooseIngredients(ingredients)
        pizza.setIngredients(newIngredientsIds)
    }
}

fun filterPizzasByIngredient(pizzas: List<Pizza>, ingredients: List<Ingredient>): List<Pizza> {
    ingredients.forEachIndexed { index, ingredient ->
        println("$index - ${ingredient.name}")
    }
    val ingredientIdx = readIndex("Выберите номер ингридиента, по которому хотите сделать фильтрацию", ingredients.size)
    val ingredientIdToFilterBy = ingredients[ingredientIdx].id

    val filteredPizzas = pizzas.filter { it.ingredientsIds.contains(ingredientIdToFilterBy) }

    return filteredPizzas
}

fun pizzaMenu(dataStorage: DataStorage) {
    while (true) {
        println("0 - Выйти из меню")
        println("1 - Вывести список пицц")
        println("2 - Создать пиццу")
        println("3 - Удалить пиццу")
        println("4 - Редактировать пиццу")
        println("5 - Фильтр пицц по ингридиенту")

        val userChoice = readIndex("Выберите номер (0...5)", 6)

        when (userChoice) {
            0 -> break
            1 -> printPizzas(dataStorage.pizzas, dataStorage.ingredients, dataStorage.bases)
            2 -> addPizza(dataStorage.pizzas, dataStorage.ingredients, dataStorage.bases)
            3 -> deletePizza(dataStorage.pizzas)
            4 -> editPizza(dataStorage.pizzas, dataStorage.ingredients, dataStorage.bases)
            5 -> {
                val filteredPizzas = filterPizzasByIngredient(dataStorage.pizzas, dataStorage.ingredients)
                filteredPizzas.forEach { pizza ->
                    pizza.printInfo(dataStorage.ingredients, dataStorage.bases)
                }
            }
        }
    }
}