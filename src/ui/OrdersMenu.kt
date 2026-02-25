package ui

import domain.DataStorage
import domain.Order
import domain.OrderItem
import domain.PizzaSize
import utils.line
import utils.readDate
import utils.readDateAndTime
import utils.readIndex
import utils.readNonBlank
import java.util.*

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

    println("Хотите оставить комментарий для заказа? (0 - нет, 1 - да)")
    val leaveComment = readIndex("Выбор: ", 2) == 1
    var comment = ""
    if (leaveComment) comment = readNonBlank("Введите комментарий")
    order.comment = comment

    println("Сделать заказ отложенным? (0 - нет, 1 - да)")
    val delayed = readIndex("Выбор:", 2)
    if (delayed == 1) {
        order.scheduledFor = readDateAndTime()
    }

    storage.orders.add(order)
}

fun filterOrdersByDate(storage: DataStorage): List<Order> {
    val dateFilter = readDate()

    return storage.orders.filter { it.createdAt.toLocalDate() == dateFilter }
}

fun ordersMenu(storage: DataStorage) {
    while (true) {
        println("0 - Выйти из меню")
        println("1 - Создать заказ")
        println("2 - Вывести все заказы")
        println("3 - Фильтр заказов по дате их создания")

        when (readIndex("Введите номер (0...3)", 4)) {
            0 -> return
            1 -> createOrder(storage)
            2 -> storage.orders.forEach { order -> order.printInfo(storage) }
            3 -> {
                val filteredOrders = filterOrdersByDate(storage)

                if (filteredOrders.isEmpty()) println("Ничего не найдено")
                else filteredOrders.forEach { order -> order.printInfo(storage) }
            }

        }

        line()
    }
}
