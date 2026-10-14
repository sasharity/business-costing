import java.io.File

/**
 * Represents the costing details for a product or production batch.
 *
 * The class stores ingredients, additional expenses, and the profit margin.
 * It also provides functions for calculating costs and selling prices.
 */
class ProductCosting(
    val productName: String,
    val unitsProduced: Int,
    val profitMarginPercent: Double
) {
    // A mutable collection allows ingredients to be added while the app runs.
    private val items = mutableListOf<CostItem>()

    var labourCost: Double = 0.0
    var packagingCost: Double = 0.0
    var otherExpenses: Double = 0.0

    /**
     * Adds an ingredient or material to the product.
     */
    fun addItem(item: CostItem) {
        items.add(item)
    }

    /**
     * Returns a read-only view of the ingredients and materials.
     */
    fun getItems(): List<CostItem> {
        return items.toList()
    }

    /**
     * Calculates the combined cost of all ingredients and materials.
     */
    fun calculateMaterialsCost(): Double {
        var total = 0.0

        for (item in items) {
            total += item.calculateTotalCost()
        }

        return total
    }

    /**
     * Calculates the total cost of producing the entire batch.
     */
    fun calculateTotalProductionCost(): Double {
        return calculateMaterialsCost() +
                labourCost +
                packagingCost +
                otherExpenses
    }

    /**
     * Calculates the cost of producing one unit.
     */
    fun calculateCostPerUnit(): Double {
        return calculateTotalProductionCost() / unitsProduced
    }

    /**
     * Calculates a selling price that achieves the selected profit margin.
     *
     * Margin is calculated as profit divided by selling price.
     */
    fun calculateSellingPricePerUnit(): Double {
        val margin = profitMarginPercent / 100.0

        return if (margin >= 0.0 && margin < 1.0) {
            calculateCostPerUnit() / (1.0 - margin)
        } else {
            calculateCostPerUnit()
        }
    }

    /**
     * Calculates the estimated profit for the entire batch.
     */
    fun calculateEstimatedProfit(): Double {
        val revenue = calculateSellingPricePerUnit() * unitsProduced
        return revenue - calculateTotalProductionCost()
    }

    /**
     * Displays the complete costing summary.
     */
    fun displaySummary() {
        println()
        println("========================================")
        println("           COSTING SUMMARY")
        println("========================================")
        println("Product: $productName")
        println("Units produced: $unitsProduced")
        println()
        println("INGREDIENTS AND MATERIALS")

        if (items.isEmpty()) {
            println("No ingredients or materials added.")
        } else {
            for (item in items) {
                println(
                    "${item.name}: ${formatMoney(item.calculateTotalCost())}"
                )
            }
        }

        println()
        println("Materials cost: ${formatMoney(calculateMaterialsCost())}")
        println("Labour cost: ${formatMoney(labourCost)}")
        println("Packaging cost: ${formatMoney(packagingCost)}")
        println("Other expenses: ${formatMoney(otherExpenses)}")
        println("----------------------------------------")
        println(
            "Total production cost: " +
                    formatMoney(calculateTotalProductionCost())
        )
        println("Cost per unit: ${formatMoney(calculateCostPerUnit())}")
        println("Profit margin: $profitMarginPercent%")
        println(
            "Suggested selling price per unit: " +
                    formatMoney(calculateSellingPricePerUnit())
        )
        println(
            "Estimated batch profit: " +
                    formatMoney(calculateEstimatedProfit())
        )
        println("========================================")
    }

    /**
     * Saves the costing summary to a text file.
     */
    fun saveToFile() {
        val directory = File("data")

        if (!directory.exists()) {
            directory.mkdirs()
        }

        val file = File(directory, "costing.txt")

        file.appendText(
            """
            Product: $productName
            Units produced: $unitsProduced
            Materials cost: ${formatMoney(calculateMaterialsCost())}
            Labour cost: ${formatMoney(labourCost)}
            Packaging cost: ${formatMoney(packagingCost)}
            Other expenses: ${formatMoney(otherExpenses)}
            Total production cost: ${formatMoney(calculateTotalProductionCost())}
            Cost per unit: ${formatMoney(calculateCostPerUnit())}
            Profit margin: $profitMarginPercent%
            Selling price per unit: ${formatMoney(calculateSellingPricePerUnit())}
            Estimated batch profit: ${formatMoney(calculateEstimatedProfit())}
            ----------------------------------------
            
            """.trimIndent() + "\n"
        )

        println("Costing record saved to ${file.path}")
    }

    /**
     * Displays previously saved costing records.
     */
    companion object {
        fun viewSavedRecords() {
            val file = File("data/costing.txt")

            if (!file.exists() || file.readText().isBlank()) {
                println("No saved costing records were found.")
                return
            }

            println()
            println("========== SAVED COSTING RECORDS ==========")
            println(file.readText())
        }
    }
}

/**
 * Formats a number as Nigerian naira.
 */
fun formatMoney(amount: Double): String {
    return "₦" + String.format("%,.2f", amount)
}