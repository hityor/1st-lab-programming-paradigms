package domain

import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*

class Order(
    val items: MutableList<OrderItem> = mutableListOf(),
    var comment: String = "",
    val createdAt: LocalDateTime = LocalDateTime.now(),
    var scheduledFor: LocalDateTime? = null,
    val id: UUID = UUID.randomUUID()
) : Printable {

    fun totalPrice(storage: DataStorage): Int = items.sumOf { it.calcPrice(storage) }

    override fun printInfo(storage: DataStorage) {
        println("Заказ: №$id")

        println("Создан: ${createdAt.format(DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm"))}")

        val scheduledForFormatted = scheduledFor?.format(DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm"))
        if (scheduledFor != null)
            println("Отложен: на ${scheduledForFormatted}")
        else
            println("Отложен: нет")
        println("Комментарий: $comment")

        items.forEach { item ->
            item.printInfo(storage)
        }

        println("Общая стоимость: ${totalPrice(storage)} руб.")
    }
}