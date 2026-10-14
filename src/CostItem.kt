/**
 * Represents one ingredient or material used to make a product.
 *
 * @property name the name of the ingredient or material
 * @property quantity the quantity used
 * @property unitCost the cost of one unit
 */
data class CostItem(
    val name: String,
    val quantity: Double,
    val unitCost: Double
) {
    /**
     * Calculates the total cost of this ingredient or material.
     */
    fun calculateTotalCost(): Double {
        return quantity * unitCost
    }
}
