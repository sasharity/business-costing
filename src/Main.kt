//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
import java.util.Locale

/**
 * Main entry point for the Business Costing application.
 *
 * Displays the menu and manages user interaction.
 */
fun main() {
    println("========================================")
    println("         BUSINESS COSTING APP")
    println("========================================")
    println("Welcome! Let's calculate your product costs.")

    var running = true

    while (running) {
        println()
        println("============== MAIN MENU ==============")
        println("1. Calculate product cost")
        println("2. View saved costing records")
        println("3. Exit")
        println("=======================================")

        print("Choose an option: ")

        when (readlnOrNull()?.trim()) {
            "1" -> createProductCosting()
            "2" -> ProductCosting.viewSavedRecords()
            "3" -> {
                println("Thank you for using Business Costing App!")
                running = false
            }
            else -> println("Invalid option. Please enter 1, 2, or 3.")
        }
    }
}

/**
 * Collects product information and creates a costing calculation.
 */
fun createProductCosting() {
    println()
    println("========== NEW PRODUCT COSTING ==========")

    val productName = readNonEmptyText("Enter product name: ")
    val unitsProduced = readPositiveInt("How many units will you produce? ")
    val profitMargin = readProfitMargin()

    val costing = ProductCosting(
        productName = productName,
        unitsProduced = unitsProduced,
        profitMarginPercent = profitMargin
    )

    println()
    println("Enter the ingredients or materials for your product.")
    println("When you finish, enter DONE as the item name.")

    var addingItems = true

    while (addingItems) {
        print("Ingredient/material name (or DONE): ")
        val itemName = readlnOrNull()?.trim() ?: ""

        if (itemName.equals("DONE", ignoreCase = true)) {
            addingItems = false
        } else if (itemName.isBlank()) {
            println("Item name cannot be empty.")
        } else {
            val quantity = readPositiveDouble("Enter quantity used: ")
            val unitCost = readNonNegativeDouble("Enter cost per unit (₦): ")

            val item = CostItem(
                name = itemName,
                quantity = quantity,
                unitCost = unitCost
            )

            costing.addItem(item)

            println(
                "$itemName added. Total item cost: " +
                        formatMoney(item.calculateTotalCost())
            )
        }
    }

    println()
    println("========== ADDITIONAL EXPENSES ==========")

    costing.labourCost =
        readNonNegativeDouble("Enter total labour cost (₦): ")

    costing.packagingCost =
        readNonNegativeDouble("Enter total packaging cost (₦): ")

    costing.otherExpenses =
        readNonNegativeDouble("Enter other expenses (₦): ")

    costing.displaySummary()

    println()
    print("Save this costing record? (Y/N): ")

    when (readlnOrNull()?.trim()?.uppercase(Locale.ROOT)) {
        "Y", "YES" -> costing.saveToFile()
        else -> println("Costing record was not saved.")
    }
}

/**
 * Reads a non-empty text value from the user.
 */
fun readNonEmptyText(prompt: String): String {
    while (true) {
        print(prompt)
        val input = readlnOrNull()?.trim()

        if (!input.isNullOrBlank()) {
            return input
        }

        println("This field cannot be empty. Please try again.")
    }
}

/**
 * Reads a positive integer, such as the number of units produced.
 */
fun readPositiveInt(prompt: String): Int {
    while (true) {
        print(prompt)
        val input = readlnOrNull()?.trim()
        val number = input?.toIntOrNull()

        if (number != null && number > 0) {
            return number
        }

        println("Enter a whole number greater than zero.")
    }
}

/**
 * Reads a positive decimal number.
 */
fun readPositiveDouble(prompt: String): Double {
    while (true) {
        print(prompt)
        val input = readlnOrNull()?.trim()
        val number = input?.toDoubleOrNull()

        if (number != null && number.isFinite() && number > 0.0) {
            return number
        }

        println("Enter a valid number greater than zero.")
    }
}

/**
 * Reads a number that can be zero or greater.
 */
fun readNonNegativeDouble(prompt: String): Double {
    while (true) {
        print(prompt)
        val input = readlnOrNull()?.trim()
        val number = input?.toDoubleOrNull()

        if (number != null && number.isFinite() && number >= 0.0) {
            return number
        }

        println("Enter a valid number that is zero or greater.")
    }
}

/**
 * Reads a valid profit margin from zero up to, but not including, 100%.
 */
fun readProfitMargin(): Double {
    while (true) {
        print("Enter desired profit margin (0 to less than 100%): ")
        val input = readlnOrNull()?.trim()
        val margin = input?.toDoubleOrNull()

        if (
            margin != null &&
            margin.isFinite() &&
            margin >= 0.0 &&
            margin < 100.0
        ) {
            return margin
        }

        println("Enter a margin from 0 up to, but not including, 100.")
    }
}