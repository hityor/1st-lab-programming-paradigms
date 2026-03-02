package ui

import domain.*
import utils.*
import java.util.*

fun createOrder(storage: DataStorage) {
    val order = Order()

    fun chooseSize(): PizzaSize {
        println("Выберите размер: 0 - SMALL, 1 - MEDIUM, 2 - LARGE")
        return PizzaSize.entries[readIndex("Выбор: ", 3)]

    }

    fun chooseDouble(): Boolean {
        println("Удвоить ингридиенты? 0 - нет, 1 - да")
        return readIndex("Выбор: ", 2) == 1
    }

    fun chooseBorderIdFrom(list: List<Border>, prompt: String): UUID? {
        if (list.isEmpty()) {
            println("Нет доступных вариантов")
            return null
        }
        println(prompt)
        list.forEachIndexed { i, b -> println("$i - ${b.name}") }
        val idx = readIndex("Выбор: ", list.size)
        return list[idx].id
    }

    fun chooseBorderForCatalogPizzaOrNull(pizzaId: UUID): BorderChoice? {
        println("Добавить бортик? 0 - нет, 1 - да")
        if (readIndex("Выбор: ", 2) == 0) return null

        val allowedBorders = storage.borders.filter { it.isAllowedFor(pizzaId) }
        return chooseBorderIdFrom(allowedBorders, "Выберите бортик")?.let { CommonBorder(it) }
    }

    fun chooseBorderForCustomOrNull(): BorderChoice? {
        println("Добавить бортик? 0 - нет, 1 - да")
        if (readIndex("Выбор: ", 2) == 0) return null

        return chooseBorderIdFrom(storage.borders, "Выберите бортик")?.let { CommonBorder(it) }
    }

    fun chooseBorderForComboOrNull(leftPizzaId: UUID, rightPizzaId: UUID): BorderChoice? {
        println("Добавить бортик? 0 - нет, 1 - да")
        if (readIndex("Выбор: ", 2) == 0) return null

        println("Общий или раздельный бортик? 0 - общий, 1 - раздельный")
        when (readIndex("Выбор: ", 2)) {
            0 -> {
                val allowedBorders =
                    storage.borders.filter { it.isAllowedFor(leftPizzaId) && it.isAllowedFor(rightPizzaId) }
                return chooseBorderIdFrom(allowedBorders, "Выберите бортик")?.let { CommonBorder(it) }
            }

            else -> {
                val allowedBorderForLeft = storage.borders.filter { it.isAllowedFor(leftPizzaId) }
                val leftBorderId = chooseBorderIdFrom(allowedBorderForLeft, "Выберите левый бортик")
                val allowedBorderForRight = storage.borders.filter { it.isAllowedFor(rightPizzaId) }
                val rightBorderId = chooseBorderIdFrom(allowedBorderForRight, "Выберите правый бортик")

                if (leftBorderId == null && rightBorderId == null) {
                    return null
                }
                return SplitBorder(leftBorderId, rightBorderId)
            }
        }
    }

    fun chooseCatalogPizzaId(prompt: String): UUID {
        println(prompt)
        storage.pizzas.forEachIndexed { i, p -> println("$i - ${p.name}") }
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
                val name = readNonBlank("Название кастомной пиццы: ")
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
        order.comment = readNonBlank("Введите комментарий: ")
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
        println("1 - Вывести заказы")
        println("2 - Создать заказ")
        println("3 - Фильтр заказов по дате")

        when (readIndex("Выбор: ", 4)) {
            0 -> break
            1 -> printOrders(storage)
            2 -> createOrder(storage)
            3 -> {
                val filteredOrders = filterOrdersByDate(storage)

                if (filteredOrders.isEmpty()) println("Ничего не найдено")
                else filteredOrders.forEach { o -> o.printInfo(storage) }

            }
        }

        line()
    }
}
