package domain

import java.util.*

sealed class BorderChoice {
    abstract fun calcPrice(storage: DataStorage): Int
}

class CommonBorder(val borderId: UUID) : BorderChoice() {
    override fun calcPrice(storage: DataStorage): Int {
        val border = storage.borders.find { it.id == borderId }
            ?: error("Не найден бортик")
        return border.calcPrice(storage.ingredients)
    }
}

class SplitBorder(val leftBorderId: UUID?, val rightBorderId: UUID?) : BorderChoice() {
    override fun calcPrice(storage: DataStorage): Int {
        val leftPrice = leftBorderId?.let { leftId ->
            val b = storage.borders.find { it.id == leftId } ?: error("Не найден левый бортик")
            b.calcPrice(storage.ingredients)
        } ?: 0

        val rightPrice = rightBorderId?.let { rightId ->
            val b = storage.borders.find { it.id == rightId } ?: error("Не найден правый бортик")
            b.calcPrice(storage.ingredients)
        } ?: 0

        return leftPrice + rightPrice
    }
}