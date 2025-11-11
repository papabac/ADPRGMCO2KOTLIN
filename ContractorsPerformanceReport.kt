import java.io.File
import java.util.Locale

data class contractorInformation(
    val totalContractCost: Double,
    val numProjects: Int,
    val averageCompletionDelayDays: Double,
    val totalCostSavings: Double,
    val reliabilityIndex: Double,
    val riskFlag: String
)

class ContractorsPerformanceReport {

    fun printReport(sortedList: List<Pair<String, contractorInformation>>){
        println(
            "%-10s | %-250s | %-20s | %15s | %20s | %20s | %20s | %20s".format("Rank",
                "Contractor", "Total Contract Cost", "Num of Projects", "Avg Completion Delay",
                "Total Cost Savings", "Reliability Index", "Flag"
            )
        )


        println("-".repeat(150))

        var i = 1
        for ((contractor, info) in sortedList) {
            if (i == 16)
                break

            println(
                "%-10d | %-250s | %-20.2f | %15d | %20.2f | %20.2f | %20.2f | %20s".format(
                    i,
                    contractor,
                    info.totalContractCost,
                    info.numProjects,
                    info.averageCompletionDelayDays,
                    info.totalCostSavings,
                    info.reliabilityIndex,
                    info.riskFlag
                )
            )
            i += 1
        }
    }

    fun printCSVReport(sortedList: List<Pair<String, contractorInformation>>){
        val csvLines = mutableListOf(
            "Rank,Contractor,Total ContractCost,NumberOfProjects,AvgCompletionDelay,TotalCostSavings,ReliabilityIndex,Flag"
        )
        var i = 1

        for ((contractor, info) in sortedList) {

            if (i == 16)
                break

            csvLines.add(
                listOf(
                    "%d".format(i),
                    "\"$contractor\"",
                    "\"${"%,.2f".format(Locale.US, info.totalContractCost)}\"",
                    "%d".format(info.numProjects),
                    "\"${"%,.2f".format(Locale.US, info.averageCompletionDelayDays)}\"",
                    "\"${"%,.2f".format(Locale.US, info.totalCostSavings)}\"",
                    "\"${"%,.2f".format(Locale.US, info.reliabilityIndex)}\"",
                    info.riskFlag
                ).joinToString(",")
            )
            i += 1
        }
        File("contractor_report2.csv").printWriter().use { out ->
            csvLines.forEach { out.println(it) }
        }


    }

    fun generateReportTwo(projectData: MutableList<Project>){
        var contractorCategories: Map<String, List<Project>> = projectData.groupBy { it.Contractor }
        var contractorReport: MutableMap<String, contractorInformation> = mutableMapOf()

        var totalContractCost: Double
        var numProjects: Int
        var totalCostSavings: Double

        var averageCompletionDelayDays: Double
        var sortedAverageCompletionDelayDays: MutableList<Int> = mutableListOf()

        var flag: String

        for ((contractor, projects) in contractorCategories) {

            var i = 0
            totalContractCost = 0.0
            numProjects = 0
            totalCostSavings = 0.0
            sortedAverageCompletionDelayDays.clear()

            while (i < projects.size){

                totalContractCost += projects[i].ContractCost
                totalCostSavings += projects[i].CostSavings

                numProjects += 1

                sortedAverageCompletionDelayDays.add(projects[i].completionDelayDays.toInt())

                i+=1

            }

            if (numProjects >= 5) {

                averageCompletionDelayDays = sortedAverageCompletionDelayDays.sum().toDouble() / sortedAverageCompletionDelayDays.size.toDouble()

                var index = (1.0 - (averageCompletionDelayDays / 90)) * (totalCostSavings / totalContractCost) * 100
                val cappedIndex = index.coerceAtMost(100.0)

                if (cappedIndex < 50) {
                    flag = "High Risk"
                }
                else{
                    flag = "Low Risk"
                }


                contractorReport[contractor] = contractorInformation(
                    totalContractCost = totalContractCost,
                    numProjects = numProjects,
                    averageCompletionDelayDays = averageCompletionDelayDays,
                    totalCostSavings = totalCostSavings,
                    reliabilityIndex = cappedIndex,
                    riskFlag = flag
                )
            }
        }

        val list = contractorReport.toList()
        val sortedList = list.sortedByDescending { it.second.totalContractCost }

        printReport(sortedList)
        printCSVReport(sortedList)

    }
}

