import java.io.File
import java.util.Locale

data class ProjectTypeTrend(
    val totalProjects: Int,
    val averageCostSavings: Double,
    val overrunRate: Double,
    val savingsYearChangePercentage: Double
)

class AnnualReport {

    fun printReport(sortedList: List<Pair<Pair<String, Int>, ProjectTypeTrend>>){

        println(
            "%-20s | %-100s | %15s | %20s | %20s | %20s".format(
                "Funding Year", "Type Of Work", "Total Projects", "Avg Savings",
                "Overrun Rate", "YoY Change"
            )
        )


        println("-".repeat(150))


        for ((work, info) in sortedList) {
            println(
                "%-20d | %-100s | %15d | %20.2f | %20.2f | %20.2f".format(
                    work.second,
                    work.first,
                    info.totalProjects,
                    info.averageCostSavings,
                    info.overrunRate,
                    info.savingsYearChangePercentage
                )
            )
        }

    }

    fun printCSVReport(sortedList: List<Pair<Pair<String, Int>, ProjectTypeTrend>>){
        val csvLines = mutableListOf(
            "Funding Year,TypeOfWork,TotalProjects,AvgSavings,OverrunRate,YoYChange"
        )

        for ((work, info) in sortedList) {
            csvLines.add(
                listOf(
                    work.second,
                    work.first,
                    "%d".format(info.totalProjects),
                    "\"${"%,.2f".format(Locale.US, info.averageCostSavings)}\"",
                    "\"${"%,.2f".format(Locale.US, info.overrunRate)}\"",
                    "\"${"%,.2f".format(Locale.US, info.savingsYearChangePercentage)}\"",
                ).joinToString(",")
            )
        }
        File("annual_report3.csv").printWriter().use { out ->
            csvLines.forEach { out.println(it) }
        }

    }

    fun generateReportThree(projectData: MutableList<Project>){
        var workCategories: Map<Pair<String, Int>, List<Project>> = projectData.groupBy { Pair(it.TypeOfWork, it.FundingYear) }
        var workReport: MutableMap<Pair<String, Int>, ProjectTypeTrend> = mutableMapOf()

        var averageCostSavings: Double
        var sortedAverageCostSavings: MutableList<Double> = mutableListOf()
        var negativeSaving: Int
        var overrunRate: Double
        var yearChangePercentage: Double

        for ((work, projects) in workCategories) {
            if (work.second == 2021) {
                var i = 0
                var numProjects = 0
                negativeSaving = 0
                sortedAverageCostSavings.clear()

                while (i < projects.size) {
                    numProjects += 1
                    sortedAverageCostSavings.add(projects[i].CostSavings)
                    if (projects[i].CostSavings < 0)
                        negativeSaving += 1
                    i += 1
                }

                averageCostSavings = sortedAverageCostSavings.sum() / sortedAverageCostSavings.size.toDouble()
                overrunRate = negativeSaving.toDouble() / numProjects.toDouble() * 100

                workReport[work] = ProjectTypeTrend(
                    totalProjects = numProjects,
                    averageCostSavings = averageCostSavings,
                    overrunRate = overrunRate,
                    savingsYearChangePercentage = 0.0,
                )
            }
        }

        for ((work, projects) in workCategories) {
            if (work.second != 2021) {
                var i = 0
                var numProjects = 0
                negativeSaving = 0
                sortedAverageCostSavings.clear()

                while (i < projects.size) {
                    numProjects += 1
                    sortedAverageCostSavings.add(projects[i].CostSavings)
                    if (projects[i].CostSavings < 0)
                        negativeSaving += 1
                    i += 1
                }

                averageCostSavings = sortedAverageCostSavings.sum() / sortedAverageCostSavings.size.toDouble()
                overrunRate = negativeSaving.toDouble() / numProjects.toDouble() * 100


                val baseline = workReport[Pair(work.first, 2021)]?.averageCostSavings ?: 0.0

                yearChangePercentage = if (baseline != 0.0) {
                    ((averageCostSavings - baseline) / baseline) * 100
                } else {
                    0.0
                }

                workReport[work] = ProjectTypeTrend(
                    totalProjects = numProjects,
                    averageCostSavings = averageCostSavings,
                    overrunRate = overrunRate,
                    savingsYearChangePercentage = yearChangePercentage,
                )
            }
        }


        val list = workReport.toList()
        val sortedList = list.sortedWith(
            compareBy<Pair<Pair<String, Int>, ProjectTypeTrend>> { it.first.second }
                .thenByDescending { it.second.averageCostSavings }
        )

        printReport(sortedList)
        printCSVReport(sortedList)

    }
}