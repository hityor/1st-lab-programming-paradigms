package domain

class DataStorage(
    val ingredients: MutableList<Ingredient>,
    val bases: MutableList<Base>,
    val pizzas: MutableList<Pizza>,
    val borders: MutableList<Border>
)