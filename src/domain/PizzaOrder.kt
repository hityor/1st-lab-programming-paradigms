package domain

interface PizzaOrder {
    fun title(storage: DataStorage): String
    fun basePrice(storage: DataStorage): Int
}