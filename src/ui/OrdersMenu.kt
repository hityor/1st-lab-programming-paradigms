package ui

import domain.BorderChoice
import domain.CatalogPizza
import domain.ComboPizza
import domain.CommonBorder
import domain.CustomPizza
import domain.DataStorage
import domain.Order
import domain.OrderItem
import domain.Pizza
import domain.PizzaSize
import domain.SplitBorder
import utils.line
import utils.readDate
import utils.readDateAndTime
import utils.readIndex
import utils.readNonBlank
import java.util.UUID

fun createOrder(storage: DataStorage) {
    val order = Order()

    fun chooseSize(): PizzaSize {
        println("Выберите размер: 0 - SMALL, 1 - MEDIUM, 2 - LARGE")
        return PizzaSize.entries[readIndex("Выбор: ", 3)]

    }

    fun chooseDouble(): Boolean {
        println("Удвоить ингридиенты? 0 - нет, 1 - да")
        return readIndex("Выбор", 2) == 1
    }

    fun chooseBorderForCatalogPizzaOrNull(pizzaId: UUID): BorderChoice? {
        if (storage.borders.isEmpty()) return null
        println("Добавить бортик? 0 - нет, 1 - да")
        if (readIndex("Выбор: ", 2) == 0) return null

        val allowedBorders = storage.borders.filter { it.isAllowedFor(pizzaId) }
        if (allowedBorders.isEmpty()) {
            println("Для этой пиццы нет доступных бортиков")
            return null
        }

        allowedBorders.forEachIndexed { i, b -> println("$i - ${b.name}")}
        val idx = readIndex("Выберите бортик: ", allowedBorders.size)
        return CommonBorder(allowedBorders[idx].id)
    }

    fun chooseBorderForCustomOrNull(): BorderChoice? {
        if (storage.borders.isEmpty()) return null
        println("Добавить бортик? 0 - нет, 1 - да")
        if (readIndex("Выбор: ", 2) == 0) return null

        storage.borders.forEachIndexed { i, b -> println("$i - ${b.name}")}
        val idx = readIndex("Выбор", storage.borders.size)
        return CommonBorder(storage.borders[idx].id)
    }

    fun chooseBorderForComboOrNull(leftPizzaId: UUID, rightPizzaId: UUID): BorderChoice? {
        if (storage.borders.isEmpty()) return null
        println("Добавить бортик? 0 - нет, 1 - да")
        if (readIndex("Выбор: ", 2) == 0) return null

        println("Общий или раздельный бортик? 0 - общий, 1 - раздельный")
        when (readIndex("Выбор: ", 2)) {
            0 -> {
                val allowedBorders = storage.borders.filter { it.isAllowedFor(leftPizzaId) && it.isAllowedFor(rightPizzaId) }
                if (allowedBorders.isEmpty()) {
                    println("Для этих половинок нет общего бортика")
                    return null
                }
                allowedBorders.forEachIndexed { i, b -> println("$i - ${b.name}")}
                val idx = readIndex("Выбор", allowedBorders.size)
                return CommonBorder(allowedBorders[idx].id)
            }
            else -> {
                var leftBorderId: UUID? = null
                val allowedBorderForLeft = storage.borders.filter {it.isAllowedFor(leftPizzaId)}
                if (allowedBorderForLeft.isEmpty()) {
                    println("Бортиков для левой пиццы нет")
                } else {
                    allowedBorderForLeft.forEachIndexed { i, b -> println("$i - ${b.name}")}
                    val idxLeft = readIndex("Выбор", allowedBorderForLeft.size)
                    leftBorderId = allowedBorderForLeft[idxLeft].id
                }

                var rightBorderId: UUID? = null
                val allowedBorderForRight = storage.borders.filter {it.isAllowedFor(rightPizzaId)}
                if (allowedBorderForRight.isEmpty()) {
                    println("Бортиков для правой пиццы нет")
                } else {
                    allowedBorderForRight.forEachIndexed { i, b -> println("$i - ${b.name}")}
                    val idxRight = readIndex("Выбор", allowedBorderForRight.size)
                    rightBorderId = allowedBorderForRight[idxRight].id
                }

                if (leftBorderId == null && rightBorderId == null) {
                    return null
                }
                return SplitBorder(leftBorderId, rightBorderId)
            }
        }
    }

    fun chooseCatalogPizzaId(prompt: String): UUID {
        println(prompt)
        storage.pizzas.forEachIndexed { i, p ->  println("$i - ${p.name}")}
        val idx = readIndex("Выберите пиццу: ", storage.pizzas.size)
        return storage.pizzas[idx].id
    }

    fun chooseBaseId(): UUID {
        if (storage.bases.isEmpty()) error("В системе нет основ")
        storage.bases.forEachIndexed { i, b -> println("$i - ${b.name}") }
        val idx = readIndex("Выберите основу: ", storage.bases.size)
        return storage.bases[idx].id
    }

    while (true) {
        println("0 - Закончить выбор")
        println("1 - Пицца из каталога")
        println("2 - Кастомная пицца")
        println("3 - Комбо (1/2 + 1/2)")

        when (readIndex("Выбор: ", 4)) {
            0 -> break

            1 -> {
                val pizzaId = chooseCatalogPizzaId("Выберите пиццу")
                val size = chooseSize()
                val doubleIng = chooseDouble()
                val border = chooseBorderForCatalogPizzaOrNull(pizzaId)

                order.items.add(
                    OrderItem(
                        pizza = CatalogPizza(pizzaId),
                        doubleIngredients = doubleIng,
                        pizzaSize = size,
                        border = border
                    )
                )
            }

            2 -> {
                val name = readNonBlank("Название кастомной пиццы:")
                val baseId = chooseBaseId()
                val ingIds = chooseIngredients(storage)
                val size = chooseSize()
                val doubleIng = chooseDouble()
                val border = chooseBorderForCustomOrNull()

                order.items.add(
                    OrderItem(
                        pizza = CustomPizza(name, baseId, ingIds),
                        doubleIngredients = doubleIng,
                        pizzaSize = size,
                        border = border
                    )
                )
            }
            3 -> {
                if (storage.pizzas.size < 2) {
                    println("Для комбо нужно минимум 2 пиццы в каталоге")
                    continue
                }
                val leftId = chooseCatalogPizzaId("Выберите пиццу для левой половины")
                val rightId = chooseCatalogPizzaId("Выберите пиццу для правой половины")
                val baseId = chooseBaseId()
                val size = chooseSize()
                val doubleIng = chooseDouble()
                val border = chooseBorderForComboOrNull(leftId, rightId)

                order.items.add(
                    OrderItem(
                        pizza = ComboPizza(leftId, rightId, baseId),
                        doubleIngredients = doubleIng,
                        pizzaSize = size,
                        border = border
                    )
                )
            }
        }
    }

    println("Хотите оставить комментарий для заказа? 0 - нет, 1 - да")
    if (readIndex("Выбор: ", 2) == 1) {
        order.comment = readNonBlank("Введите комментарий")
    }

    println("Сделать заказ отложенным? 0 - нет, 1 - да")
    if (readIndex("Выбор: ", 2) == 1) {
        order.scheduledFor = readDateAndTime()
    }

    storage.orders.add(order)
}

fun printOrders(storage: DataStorage) {
    storage.orders.forEach { o ->
        o.printInfo(storage)
    }
}

fun filterOrdersByDate(storage: DataStorage): List<Order> {
    val dateFilter = readDate()

    return storage.orders.filter { it.createdAt.toLocalDate() == dateFilter }
}

fun ordersMenu(storage: DataStorage) {
    while (true) {
        println("0 - Выйти из меню")
        println("1 - Создать заказ")
        println("2 - Вывести заказы")
        println("3 - Фильтр заказов по дате")

        when (readIndex("Выбор: ", 4)) {
            0 -> break
            1 -> createOrder(storage)
            2 -> printOrders(storage)
            3 -> {
                val filteredOrders = filterOrdersByDate(storage)

                if (filteredOrders.isEmpty()) println("Ничего не найдено")
                else filteredOrders.forEach { o -> o.printInfo(storage) }

            }
        }

        line()
    }
}
