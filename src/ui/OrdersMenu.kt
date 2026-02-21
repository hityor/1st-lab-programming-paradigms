package ui

import domain.DataStorage
import domain.Order
import domain.OrderItem
import domain.PizzaSize
import utils.readIndex
import java.util.UUID

fun createOrder(storage: DataStorage) {
    val order = Order()

    while (true) {
        if (readIndex("Добавить пиццу в заказ? 0 - нет, 1 - да", 2) == 0)
            break

        if (storage.pizzas.isEmpty()) {
            println("В системе нет пицц")
            return
        }

        storage.pizzas.forEachIndexed { i, p ->
            println("$i - ${p.name}")
        }

        val pizzaIdx = readIndex("Выберите пиццу: ", storage.pizzas.size)
        val pizza = storage.pizzas[pizzaIdx]

        println("Выберите размер: 0 - SMALL, 1 - MEDIUM, 2 - LARGE")
        val size = PizzaSize.entries[readIndex("Выберите номер: ", 3)]

        println("Удвоить ингредиенты? 0 - нет, 1 - да ")
        val doubleIng = readIndex("Выбор: ", 2) == 1

        var borderId: UUID? = null
        if (storage.borders.isNotEmpty()) {
            println("Добавить бортик? 0 - нет, 1 - да")
            if (readIndex("Выбор: ", 2) == 1) {
                storage.borders.forEachIndexed { i, b ->
                    println("$i - ${b.name}")
                }

                val brdIdx = readIndex("Выберите бортик: ", storage.borders.size)
                borderId = storage.borders[brdIdx].id
            }
        }

        order.items.add(
            OrderItem(
                pizzaId = pizza.id,
                pizzaSize = size,
                doubleIngredients = doubleIng,
                borderId = borderId
            )
        )
    }

    storage.orders.add(order)
}

fun ordersMenu(storage: DataStorage) {
    while (true) {
        println("0 - Выйти из меню")
        println("1 - Создать заказ")
        println("2 - Вывести все заказы")

        when (readIndex("Введите номер (0...2)", 3)) {
            0 -> return
            1 -> createOrder(storage)
            2 -> storage.orders.forEach { order -> order.printInfo(storage) }
        }
    }
}