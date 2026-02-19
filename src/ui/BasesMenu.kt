package ui

import domain.Base
import domain.Pizza
import utils.*

fun printBases(bases: List<Base>) {
    bases.forEach { base ->
        println("Название: ${base.name}, цена: ${base.price}, классическая: ${base.isClassic}")
    }
}

fun chooseBase(bases: MutableList<Base>): Base {
    bases.forEachIndexed { index, base ->
        println("$index - Название: ${base.name}, цена: ${base.price}, классическая: ${base.isClassic}")
    }

    val idx = readIndex("Введите номер элемента", bases.size)
    return bases[idx]
}

fun addBase(bases: MutableList<Base>) {
    val baseName = readNonBlank("Введите название основы")

    val basePrice = readPositiveInt("Введите цену")

    val classicBasePrice = bases.find { it.isClassic }?.price
    if (classicBasePrice != null) {
        if (basePrice <= classicBasePrice * 1.2) {
            println("Классическая основа уже есть, будет создана неклассическая основа")
            bases.add(Base(name = baseName, price = basePrice, isClassic = false))
        } else {
            println("Цена более чем на 20 процентов больше, чем цена классической основы - так нельзя")
        }
    } else {
        println("Вы хотите, чтобы эта основа была классической. Если нет, то создать основу нельзя будет")

        if (readIndex("0 - хотите, 1 - не хотите", 2) == 0) {
            bases.add(Base(name = baseName, price = basePrice, isClassic = true))
        } else {
            println("Неклассическую основу нельзя создать, не создав классическую")
        }
    }
}

fun editBase(bases: MutableList<Base>) {
    val base = chooseBase(bases)
    val classicBasePrice = bases.find { it.isClassic }?.price

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

    println()
    val newPrice = readOptionalPositiveInt("Введите новую цену (enter = не менять)")
    if (newPrice != null) {
        base.changePrice(newPrice, classicBasePrice)
    }
}

fun deleteBase(bases: MutableList<Base>, pizzas: List<Pizza>) {
    val baseToDelete = chooseBase(bases)
    if (pizzas.any { it.baseId == baseToDelete.id }) {
        println("Нельзя удалить основу, которая используется в какой то пицце")
        return
    }
    if (baseToDelete.isClassic && bases.any { !it.isClassic })
        println("Удалять классическую основу нельзя, если уже есть неклассическая")
    else
        bases.removeIf { it.id == baseToDelete.id }
}

fun basesMenu(bases: MutableList<Base>, pizzas: List<Pizza>) {
    while (true) {
        println("0 - Выйти из меню")
        println("1 - Вывести список основ")
        println("2 - Добавить основу")
        println("3 - Редактировать основу")
        println("4 - Удалить основу")

        val userOutput = readIndex("Выберите номер (0...4)", 5)

        if (userOutput == 0) {
            break
        } else if (userOutput == 1) {
            printBases(bases)
        } else if (userOutput == 2) {
            addBase(bases)
        } else if (userOutput == 3) {
            editBase(bases)
        } else if (userOutput == 4) {
            deleteBase(bases, pizzas)
        }

        line()
    }
}