package ui

import domain.CatalogItem
import domain.DataStorage

fun printItems(storage: DataStorage, items: List<CatalogItem>) {
    items.forEach { it.printInfo(storage) }
}