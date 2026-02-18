import java.util.UUID

class Base(
    name: String,
    price: Int,
    val isClassic: Boolean,
    val id: UUID = UUID.randomUUID()
) {
    var name: String = name
        private set

    var price: Int = price
        private set

    fun changeName(newName: String) {
        if (newName.isNotBlank())
            name = newName
    }

    fun changePrice(newPrice: Int, classicBasePrice: Int) {
        if (!isClassic) {
            require(newPrice <= classicBasePrice * 1.2) {
                "Цена превышает предел: максимум +20% от стоимости классической основы"
            }
            price = newPrice
        }
    }
}

fun printBases(bases: List<Base>) {
    bases.forEach { base ->
        println("Название: ${base.name}, цена: ${base.price}, классическая: ${base.isClassic}")
    }
}

fun chooseBase(bases: MutableList<Base>): Base {
    println("Введите номер элемента")
    bases.forEachIndexed { index, base ->
        println("$index - Название: ${base.name}, цена: ${base.price}, классическая: ${base.isClassic}")
    }

    return bases[readln().toInt()]
}

fun addBase(bases: MutableList<Base>) {
    println("Введите название основы")
    val baseName = readln()

    println("Введите цену")
    val basePrice = readln().toInt()

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
        println("0 - хотите, 1 - не хотите")

        if (readln().toInt() == 0) {
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

    println("Введите новое название (enter = не менять)")
    val newName = readln()
    if (newName != "") base.changeName(newName)

    if (base.isClassic) {
        println("Менять цену классической пиццы нельзя")
        return
    }

    println("Введите новую цену (enter = не менять)")
    val newPrice = readln()
    if (newPrice != "") {

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

        val userOutput = readln().toInt()

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