package domain

import com.sun.jdi.Mirror

class DataStorage(
    val ingredients: MutableList<Ingredient>,
    val bases: MutableList<Base>,
    val pizzas: MutableList<Pizza>,
    val borders: MutableList<Border>
)