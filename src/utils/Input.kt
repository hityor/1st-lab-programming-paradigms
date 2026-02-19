package utils

fun readInt(prompt: String): Int {
    while (true) {
        println(prompt)
        val n = readln().toIntOrNull()
        if (n != null) return n
        println("Введите целое число.")
    }
}

fun readIndex(prompt: String, size: Int): Int {
    require(size > 0) { "Список пуст" }
    while (true) {
        val idx = readInt(prompt)
        if (idx in 0..<size) return idx
        println("Неверный номер. Допустимо: 0..${size - 1}")
    }
}

fun readNonBlank(prompt: String): String {
    while (true) {
        println(prompt)
        val s = readln()
        if (s.isNotBlank()) return s.trim()
        println("Пустое значение. Попробуйте ещё раз.")
    }
}

fun readOptionalNonBlank(prompt: String): String? {
    while (true) {
        println(prompt)
        val s = readln()
        if (s.isEmpty()) return null
        if (s.isNotBlank()) return s.trim()
        println("Значение не может состоять из пробелов")
    }
}

fun readPositiveInt(prompt: String): Int {
    while (true) {
        println(prompt)
        val n = readln().toIntOrNull()
        if (n != null && n > 0) return n
        println("Введите целое число > 0.")
    }
}

fun readOptionalPositiveInt(prompt: String): Int? {
    while (true) {
        println(prompt)
        val n = readln().toIntOrNull() ?: return null
        if (n > 0) return n
        println("Введите целое число > 0.")
    }
}