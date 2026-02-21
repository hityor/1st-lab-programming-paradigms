package domain

import java.time.LocalDateTime
import java.util.UUID

class Order(
    val items: MutableList<OrderItem> = mutableListOf(),
    var comment: String = "",
    val createdAt: LocalDateTime = LocalDateTime.now(),
    val id: UUID = UUID.randomUUID()
) {

    fun totalPrice(storage: DataStorage): Int = items.sumOf { it.calcPrice(storage) }

    fun printInfo(storage: DataStorage) {
        println("Заказ: №$id")
        println("Создан: $createdAt")
        println("Комментарий: $comment")

        items.forEachIndexed { index, item ->
            println("Позиция $index: ${item.calcPrice(storage)} руб.")
        }

        println("Общая стоимость: ${totalPrice(storage)} руб.")
    }
}