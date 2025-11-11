import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit

class ComputeField {

    fun computeCostSavings(dataTable: MutableList<List<String>>, row: Int): Double {
        val contractBudget = dataTable[row][11].toDouble()
        val contractCost = dataTable[row][12].toDouble()

        return contractBudget - contractCost
    }

    fun completionDelayDays(dateStr1: String, dateStr2: String, pattern: String = "yyyy-MM-dd"): Long {
        val formatter = DateTimeFormatter.ofPattern(pattern)
        val date1 = LocalDate.parse(dateStr1, formatter)
        val date2 = LocalDate.parse(dateStr2, formatter)

        return ChronoUnit.DAYS.between(date1, date2)

    }
}