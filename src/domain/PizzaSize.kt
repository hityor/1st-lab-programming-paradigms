package domain

enum class PizzaSize(val multiplier: Double, val slices: Int) {
    SMALL(0.8, 6),
    MEDIUM(1.0, 8),
    LARGE(1.3, 12)
}