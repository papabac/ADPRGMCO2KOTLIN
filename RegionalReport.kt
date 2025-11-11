import java.io.File
import java.util.Locale

data class regionalInformation(
    val mainIsland: String,
    val totalBudget: Double,
    val medianCostSavings: Double,
    val averageCompletionDelayDays: Double,
    val delayPercentage: Double,
    val efficiencyScore: Double
)

class RegionalReport{

    fun printReport(sortedList: List<Pair<String, regionalInformation>>){
        println(
            "%-35s | %-20s | %15s | %20s | %20s | %20s | %20s".format(
                "Region", "Main Island", "Total Budget", "Median Cost Savings",
                "Avg Completion Delay", "Delay Percentage", "Efficiency Score"
            )
        )


        println("-".repeat(150))


        for ((region, info) in sortedList) {
            println(
                "%-35s | %-20s | %15.2f | %20.2f | %20.2f | %20.2f | %20.2f".format(
                    region,
                    info.mainIsland,
                    info.totalBudget,
                    info.medianCostSavings,
                    info.averageCompletionDelayDays,
                    info.delayPercentage,
                    info.efficiencyScore
                )
            )
        }



    }

    fun printCSVReport(sortedList: List<Pair<String, regionalInformation>>){
        val csvLines = mutableListOf(
            "Region,MainIsland,TotalBudget,MedianCostSavings,AverageCompletionDelay,DelayPercentage,EfficiencyScore"
        )

        for ((region, info) in sortedList) {
            csvLines.add(
                listOf(
                    region,
                    info.mainIsland,
                    "\"${"%,.2f".format(Locale.US, info.totalBudget)}\"",
                    "\"${"%,.2f".format(Locale.US, info.medianCostSavings)}\"",
                    "\"${"%,.2f".format(Locale.US, info.averageCompletionDelayDays)}\"",
                    "\"${"%,.2f".format(Locale.US, info.delayPercentage)}\"",
                    "\"${"%,.2f".format(Locale.US, info.efficiencyScore)}\""
                ).joinToString(",")
            )
        }
        File("regional_report1.csv").printWriter().use { out ->
            csvLines.forEach { out.println(it) }
        }

    }

    fun generateReportOne(projectData: MutableList<Project>){

        var regionalCategories: Map<String, List<Project>> = projectData.groupBy { it.Region }
        var regionalReport: MutableMap<String, regionalInformation> = mutableMapOf()

        var totalBudget: Double
        var medianCostSavings: Double
        var sortedMedianCostSavings: MutableList<Double> = mutableListOf()

        var averageCompletionDelayDays: Double
        var sortedAverageCompletionDelayDays: MutableList<Int> = mutableListOf()

        var delayPercentage: Double
        var countGreaterThan30: Int
        var efficiencyScore: Double

        for ((region, projects) in regionalCategories) {

            var i = 0
            totalBudget = 0.0
            sortedMedianCostSavings.clear()
            sortedAverageCompletionDelayDays.clear()
            countGreaterThan30 = 0


            while (i < projects.size){

                totalBudget += projects[i].ApprovedBudgetForContract

                sortedMedianCostSavings.add(projects[i].CostSavings)
                sortedAverageCompletionDelayDays.add(projects[i].completionDelayDays.toInt())

                if (projects[i].completionDelayDays.toInt() > 30)
                    countGreaterThan30 +=1

                i+=1

            }

            sortedMedianCostSavings.sort()

            if (sortedMedianCostSavings.size % 2 == 1)
                medianCostSavings = sortedMedianCostSavings[(sortedMedianCostSavings.size) / 2]
            else {
                medianCostSavings = (sortedMedianCostSavings[(sortedMedianCostSavings.size -1) / 2] + sortedMedianCostSavings[(sortedMedianCostSavings.size-1) / 2 + 1]) / 2
            }

            averageCompletionDelayDays = sortedAverageCompletionDelayDays.sum().toDouble()/ sortedAverageCompletionDelayDays.size.toDouble()

            delayPercentage = countGreaterThan30.toDouble() / sortedAverageCompletionDelayDays.size.toDouble() * 100

            efficiencyScore = (medianCostSavings / averageCompletionDelayDays) * 100

            regionalReport[region] = regionalInformation(
                mainIsland = projects[0].mainIsland,
                totalBudget = totalBudget,
                medianCostSavings = medianCostSavings,
                averageCompletionDelayDays = averageCompletionDelayDays,
                delayPercentage = delayPercentage,
                efficiencyScore = efficiencyScore
            )
        }

        val efficiencies = regionalReport.values.map { it.efficiencyScore }
        val minEff = efficiencies.minOrNull() ?: 0.0
        val maxEff = efficiencies.maxOrNull() ?: 1.0
        for ((region, info) in regionalReport) {
            val normalized = ((info.efficiencyScore - minEff) / (maxEff - minEff) * 100).coerceIn(0.0, 100.0)
            regionalReport[region] = info.copy(efficiencyScore = normalized)
        }

        val list = regionalReport.toList()
        val sortedList = list.sortedByDescending { it.second.efficiencyScore }

        printReport(sortedList)
        printCSVReport(sortedList)

    }
}