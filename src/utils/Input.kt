package utils

import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.format.DateTimeFormatter

fun readInt(prompt: String): Int {
    while (true) {
        print(prompt)
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
        print(prompt)
        val s = readln()
        if (s.isNotBlank()) return s.trim()
        println("Пустое значение. Попробуйте ещё раз.")
    }
}

fun readOptionalNonBlank(prompt: String): String? {
    while (true) {
        print(prompt)
        val s = readln()
        if (s.isEmpty()) return null
        if (s.isNotBlank()) return s.trim()
        println("Значение не может состоять из пробелов")
    }
}

fun readPositiveInt(prompt: String): Int {
    while (true) {
        print(prompt)
        val n = readln().toIntOrNull()
        if (n != null && n > 0) return n
        println("Введите целое число > 0.")
    }
}

fun readOptionalPositiveInt(prompt: String): Int? {
    while (true) {
        print(prompt)
        val n = readln().toIntOrNull() ?: return null
        if (n > 0) return n
        println("Введите целое число > 0.")
    }
}

fun readDateAndTime(): LocalDateTime {
    print("Введите дату (дд.ММ.гггг):")
    val date = LocalDate.parse(readln(),
        DateTimeFormatter.ofPattern("dd.MM.yyyy"))

    print("Введите время (ЧЧ:мм):")
    val time = LocalTime.parse(readln(),
        DateTimeFormatter.ofPattern("HH:mm"))

    return LocalDateTime.of(date, time)
}

fun readDate(): LocalDate {
    print("Введите дату (дд.ММ.гггг):")
    val date = LocalDate.parse(readln(),
        DateTimeFormatter.ofPattern("dd.MM.yyyy"))

    return date
}