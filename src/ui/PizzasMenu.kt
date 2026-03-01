package ui

import domain.DataStorage
import domain.Pizza
import utils.line
import utils.readIndex
import utils.readInt
import utils.readNonBlank
import utils.readOptionalNonBlank
import java.util.*

fun choosePizza(storage: DataStorage, prompt: String? = null): Pizza {
    val sortedPizzas = storage.pizzas.sortedBy { it.calcPrice(storage) }

    if (prompt != null) println(prompt)
    sortedPizzas.forEachIndexed { index, pizza ->
        println("$index - Название: ${pizza.name}, цена: ${pizza.calcPrice(storage)}")
    }

    val idx = readIndex("Введите номер элемента", sortedPizzas.size)
    return sortedPizzas[idx]
}

fun chooseIngredients(storage: DataStorage): List<UUID> {
    val chosen = mutableListOf<UUID>()

    while (true) {
        println("Выберите ингредиент: 0...${storage.ingredients.size - 1}, или -1 чтобы закончить")
        storage.ingredients.forEachIndexed { index, ingredient ->
            println("$index - ${ingredient.name}, цена: ${ingredient.price}")
        }

        val n = readInt("Номер:")
        if (n == -1) break

        if (n !in 0..<storage.ingredients.size) {
            println("Неверный номер")
            continue
        }

        val id = storage.ingredients[n].id
        if (id in chosen) {
            println("Этот ингредиент уже выбран")
        } else {
            chosen.add(id)
        }
    }

    return chosen
}

fun printPizzas(storage: DataStorage) {
    val sortedPizzas = storage.pizzas.sortedBy { it.calcPrice(storage) }
    printItems(storage, sortedPizzas)
}

fun addPizza(storage: DataStorage) {
    if (storage.bases.isEmpty()) {
        println("Основ нет, пиццу создать нельзя")
        return
    }

    val pizzaName = readNonBlank("Введите название пиццы")

    storage.bases.forEachIndexed { index, base ->
        println("$index - ${base.name}")
    }
    val pizzaIdx = readIndex("Выберите номер основы", storage.bases.size)
    val pizzaBaseId = storage.bases[pizzaIdx].id

    val newPizza = Pizza(pizzaName, pizzaBaseId, chooseIngredients(storage))

    storage.pizzas.add(newPizza)
}

fun deletePizza(storage: DataStorage) {
    val chosenPizza = choosePizza(storage, "Выберите пиццу, которую хотите удалить")

    storage.pizzas.removeIf { it.id == chosenPizza.id }
}

fun editPizza(storage: DataStorage) {
    val pizza = choosePizza(storage, "Выберите пиццу, которую хотите изменить")

    println("${pizza.name} - название пиццы")
    val newName = readOptionalNonBlank("Введите новое название пиццы (enter = не менять название)")
    if (newName != null) pizza.changeName(newName)

    println("${storage.bases.find { it.id == pizza.baseId }?.name} - основа пиццы")
    val changeBase = readIndex("0 - не меняем основу, 1 - меняем основу", 2)
    if (changeBase == 1) {
        storage.bases.forEachIndexed { index, base ->
            println("$index - ${base.name}")
        }
        val newBaseId = storage.bases[readIndex("Выберите номер новой основы", storage.bases.size)].id
        pizza.changeBaseId(newBaseId)
    }

    println("Текущий список ингридиентов")
    pizza.ingredientsIds.forEachIndexed { index, ingredientId ->
        println("$index - ${storage.ingredients.find { it.id == ingredientId }?.name}")
    }
    val changeIngredients = readIndex("0 - не менять ингредиенты, 1 - пересобрать ингредиенты", 2)
    if (changeIngredients == 1) {
        val newIngredientsIds = chooseIngredients(storage)
        pizza.setIngredients(newIngredientsIds)
    }
}

fun filterPizzasByIngredient(storage: DataStorage): List<Pizza> {
    storage.ingredients.forEachIndexed { index, ingredient ->
        println("$index - ${ingredient.name}")
    }
    val ingredientIdx = readIndex("Выберите номер ингридиента, по которому хотите сделать фильтрацию", storage.ingredients.size)
    val ingredientIdToFilterBy = storage.ingredients[ingredientIdx].id

    val filteredPizzas = storage.pizzas.filter { it.ingredientsIds.contains(ingredientIdToFilterBy) }

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
            1 -> printPizzas(dataStorage)
            2 -> addPizza(dataStorage)
            3 -> deletePizza(dataStorage)
            4 -> editPizza(dataStorage)
            5 -> {
                val filteredPizzas = filterPizzasByIngredient(dataStorage)

                if (filteredPizzas.isEmpty()) println("Ничего не найдено")
                else filteredPizzas.forEach { pizza -> pizza.printInfo(dataStorage)}
            }
        }

        line()
    }
}