import java.util.UUID

class Pizza(
    val name: String,
    val baseId: UUID,
    ingredientsIds: List<UUID>,
    val id: UUID = UUID.randomUUID()
) {
    private val ingredientsIdsMutable = ingredientsIds.toMutableList()

    val ingredientsIds: List<UUID>
        get() = ingredientsIdsMutable.toList()

    fun calcPrice(ingredients: List<Ingredient>, bases: List<Base>): Int {
        var price = 0;

        price += bases.find { it.id == baseId }?.price ?: 0

        ingredientsIds.forEach { ingredientId ->
            price += ingredients.find { it.id == ingredientId }?.price ?: 0
        }
        return price
    }

    fun printInfo(ingredients: List<Ingredient>, bases: List<Base>) {
        println("Название: $name")

        println("Основа: ${bases.find { it.id == baseId }?.name}")

        println("Ингридиенты")
        ingredientsIds.forEach { ingredientId ->
            val ingredient = ingredients.find { it.id == ingredientId }
            println(ingredient?.name ?: "Не найден")
        }
        println("Общая цена: ${calcPrice(ingredients, bases)}")
    }

    fun addIngredient(ingredientId: UUID): Boolean {
        if (!ingredientsIds.contains(ingredientId)) {
            ingredientsIdsMutable.add(ingredientId)
            return true
        } else {
            return false
        }
    }
}

fun addIngredientsToPizza(pizza: Pizza, ingredients: List<Ingredient>) {
    while (true) {
        println("Выберите номер ингридиента, который хотите добавить (-1 - прекратить выбор ингридиентов)")
        ingredients.forEachIndexed { index, ingredient ->
            println("$index - ${ingredient.name}, цена: ${ingredient.price}")
        }

        val ingredientNumber = readln().toInt()
        if (ingredientNumber != -1 && ingredients.isNotEmpty()) {
            if (!pizza.addIngredient(ingredients[ingredientNumber].id)) {
                println("Нельзя 2 рааз добавлять один и тот же ингридиент")
            }
        } else {
            break
        }
    }
}

fun addPizza(pizzas: MutableList<Pizza>, bases: List<Base>, ingredients: List<Ingredient>) {
    if (bases.isEmpty()) {
        println("Основ нет, пиццу создать нельзя")
        return
    }

    val pizzaName = readNonBlank("Введите название пиццы")

    println("Выберите номер основы")
    bases.forEachIndexed { index, base ->
        println("$index - ${base.name}")
    }
    val pizzaBaseId = bases[readln().toInt()].id

    val newPizza = Pizza(pizzaName, pizzaBaseId, mutableListOf())

    addIngredientsToPizza(newPizza, ingredients)

    pizzas.add(newPizza)
}

fun deletePizza(pizzas: MutableList<Pizza>) {
    println("Выберитие номер пиццы которую хотите удалить")
    pizzas.forEachIndexed { index, pizza ->
        println("$index - ${pizza.name}")
    }

    val chosenPizzaNumber = readln().toInt()
    val chosenPizzaId = pizzas[chosenPizzaNumber].id

    pizzas.removeIf { it.id == chosenPizzaId }
}

fun editPizza(pizzas: MutableList<Pizza>, ingredients: List<Ingredient>, bases: List<Base>) {
    println("Введите номер пиццы которую хотите изменить")
    pizzas.forEachIndexed { index, pizza ->
        println("$index - ${pizza.name}")
    }

    val pizzaNumber = readln().toInt()
    val pizza = pizzas[pizzaNumber]

    println("${pizza.name} - название пиццы")
    println("Введите новое название пиццы (enter = не менять название)")
    val newName = readln().ifEmpty { pizza.name }

    println("${bases.find { it.id == pizza.baseId }?.name} - основа пиццы")
    println("0 - не меняем основу, 1 - меняем основу")
    val newBaseId: UUID
    val changeBase = readln().toInt()
    if (changeBase == 1) {
        println("Выберите номер новой основы")
        bases.forEachIndexed { index, base ->
            println("$index - ${base.name}")
        }
        newBaseId = bases[readln().toInt()].id
    } else {
        newBaseId = pizza.baseId
    }

    println("Текущий список ингридиентов")
    pizza.ingredientsIds.forEachIndexed { index, ingredientId ->
        println("$index - ${ingredients.find { it.id == ingredientId }?.name}")
    }
    println("0 - не менять ингредиенты, 1 - пересобрать ингредиенты")
    val changeIngredients = readln().toInt()
    val editedPizza: Pizza
    val oldPizzaId = pizza.id
    if (changeIngredients == 1) {
        editedPizza = Pizza(
            name = newName,
            baseId = newBaseId,
            ingredientsIds = mutableListOf(),
            id = oldPizzaId
        )
        addIngredientsToPizza(editedPizza, ingredients)
    } else {
        editedPizza = Pizza(
            name = newName,
            baseId = newBaseId,
            ingredientsIds = pizza.ingredientsIds.toMutableList(),
            id = oldPizzaId
        )
    }

    pizzas[pizzaNumber] = editedPizza
}

fun filterPizzasByIngredient(pizzas: List<Pizza>, ingredients: List<Ingredient>): List<Pizza> {
    println("Выберите номер ингридиента, по которому хотите сделать фильтрацию")
    ingredients.forEachIndexed { index, ingredient ->
        println("$index - ${ingredient.name}")
    }
    val ingredientIdToFilterBy = ingredients[readln().toInt()].id

    val filteredPizzas = pizzas.filter { it.ingredientsIds.contains(ingredientIdToFilterBy) }

    return filteredPizzas
}

fun pizzaMenu(pizzas: MutableList<Pizza>, ingredients: MutableList<Ingredient>, bases: List<Base>) {
    while (true) {
        println("0 - Выйти из меню")
        println("1 - Вывести список пицц")
        println("2 - Создать пиццу")
        println("3 - Удалить пиццу")
        println("4 - Редактировать пиццу")
        println("5 - Фильтр пицц по ингридиенту")

        val userChoice = readln().toInt()

        if (userChoice == 0) {
            break
        } else if (userChoice == 1) {
            pizzas.forEach { pizza ->
                pizza.printInfo(ingredients, bases)
            }
        } else if (userChoice == 2) {
            addPizza(pizzas, bases, ingredients)
        } else if (userChoice == 3) {
            deletePizza(pizzas)
        } else if (userChoice == 4) {
            editPizza(pizzas, ingredients, bases)
        } else if (userChoice == 5) {
            val filteredPizzas = filterPizzasByIngredient(pizzas, ingredients)
            filteredPizzas.forEach { pizza ->
                pizza.printInfo(ingredients, bases)
            }
        }

        line()
    }
}