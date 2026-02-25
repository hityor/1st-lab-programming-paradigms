package ui

import domain.Base
import domain.DataStorage
import utils.*

fun printBases(storage: DataStorage) {
    val sortedBases = storage.bases.sortedWith(Comparator<Base> { a, b ->
        a.price.compareTo(b.price)
    })
    printItems(storage, sortedBases)
}

fun chooseBase(storage: DataStorage, prompt: String? = null): Base {
    val sortedBases = storage.bases.sortedBy { it.price }

    if (prompt != null) println(prompt)
    sortedBases.forEachIndexed { index, base ->
        println("$index - Название: ${base.name}, цена: ${base.price}, классическая: ${base.isClassic}")
    }

    val idx = readIndex("Введите номер элемента", sortedBases.size)
    return sortedBases[idx]
}

fun addBase(storage: DataStorage) {
    val baseName = readNonBlank("Введите название основы")

    val basePrice = readPositiveInt("Введите цену")

    val classicBasePrice = storage.bases.find { it.isClassic }?.price
    if (classicBasePrice != null) {
        if (basePrice <= classicBasePrice * 1.2) {
            println("Классическая основа уже есть, будет создана неклассическая основа")
            storage.bases.add(Base(name = baseName, price = basePrice, isClassic = false))
        } else {
            println("Цена более чем на 20 процентов больше, чем цена классической основы - так нельзя")
        }
    } else {
        println("Вы хотите, чтобы эта основа была классической. Если нет, то создать основу нельзя будет")

        if (readIndex("0 - хотите, 1 - не хотите", 2) == 0) {
            storage.bases.add(Base(name = baseName, price = basePrice, isClassic = true))
        } else {
            println("Неклассическую основу нельзя создать, не создав классическую")
        }
    }
}

fun editBase(storage: DataStorage) {
    val base = chooseBase(storage, "Выберите основу, которую хотите удалить")
    val classicBasePrice = storage.bases.find { it.isClassic }?.price

    if (classicBasePrice == null) {
        println("Классической основы нет => ничего изменять тоже нельзя, на самом деле и основ нет")
        return
    }

    val newName = readOptionalNonBlank("Введите новое название (enter = не менять)")
    if (newName != null) base.changeName(newName)

    if (base.isClassic) {
        println("Менять цену классической пиццы нельзя")
        return
    }

    val newPrice = readOptionalPositiveInt("Введите новую цену (enter = не менять)")
    if (newPrice != null) {
        base.changePrice(newPrice, classicBasePrice)
    }
}

fun deleteBase(storage: DataStorage) {
    val baseToDelete = chooseBase(storage, "Выберите основу, которую хотите удалить")
    if (storage.pizzas.any { it.baseId == baseToDelete.id }) {
        println("Нельзя удалить основу, которая используется в какой то пицце")
        return
    }
    if (baseToDelete.isClassic && storage.bases.any { !it.isClassic }) println("Удалять классическую основу нельзя, если уже есть неклассическая")
    else storage.bases.removeIf { it.id == baseToDelete.id }
}

fun filterBases(storage: DataStorage): List<Base> {
    println("0 - Сортировать по имени")
    println("1 - Сортировать по диапазону цены")

    val userChoice = readIndex("Выбор: ", 2)

    when (userChoice) {
        0 -> {
            val filterName = readNonBlank("Введите название: ")

            return storage.bases.filter {
                it.name.contains(
                    filterName,
                    ignoreCase = true
                )
            }
        }

        else -> {
            val start = readPositiveInt("Введите начальный диапазон")
            val end = readPositiveInt("Введите конечный диапазон")

            return storage.bases.filter {
                it.price in start..end
            }
        }
    }
}

fun basesMenu(dataStorage: DataStorage) {
    while (true) {
        println("0 - Выйти из меню")
        println("1 - Вывести список основ")
        println("2 - Добавить основу")
        println("3 - Редактировать основу")
        println("4 - Удалить основу")
        println("5 - Фильтрация")

        val userOutput = readIndex("Выберите номер (0...5)", 6)

        when (userOutput) {
            0 -> break
            1 -> printBases(dataStorage)
            2 -> addBase(dataStorage)
            3 -> editBase(dataStorage)
            4 -> deleteBase(dataStorage)
            5 -> {
                val filteredBases = filterBases(dataStorage)

                if (filteredBases.isEmpty()) println("Ничего не найдено")
                else filteredBases.forEach { base -> base.printInfo(dataStorage) }
            }
        }

        line()
    }
}