fun line() {
    println("---------------------------------------------------------------------------------------------------------")
}

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

    while (true) {
        println("0 - Выйти из программы")
        println("1 - Ингридиенты")
        println("2 - Основа")
        println("3 - Пицца")

        val output = readln().toInt()
        if (output == 0) {
            break
        } else if (output == 1) {
            ingredientsMenu(ingredients, pizzas)
        } else if (output == 2) {
            basesMenu(bases, pizzas)
        } else if (output == 3) {
            pizzaMenu(pizzas, ingredients, bases)
        }
    }
}