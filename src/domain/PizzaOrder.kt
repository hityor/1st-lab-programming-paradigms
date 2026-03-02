package domain

interface PizzaOrder {
    fun title(storage: DataStorage): String
    fun printComposition(storage: DataStorage, doubleIngredients: Boolean)
    fun basePrice(storage: DataStorage): Int
}