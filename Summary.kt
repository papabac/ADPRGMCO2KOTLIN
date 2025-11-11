import java.io.File

class Summary {

    fun generateSummary(projectData: MutableList<Project>){

        val totalProjects = projectData.size
        val totalContractors = projectData.map { it.Contractor }.distinct().size
        val totalProvinceProjects = projectData.map { it.Province }.distinct().size
        val globalAverageDelay = projectData.map {it.completionDelayDays}.average()
        val totalSavings = projectData.sumOf { it.CostSavings }


        val jsonContent = """
        {
            "totalProjects": $totalProjects,
            "totalContractors": $totalContractors,
            "totalProvinces": $totalProvinceProjects,
            "globalAverageDelay": $globalAverageDelay,
            "totalSavings": $totalSavings
        }
        """.trimIndent()

        File("summary.json").writeText(jsonContent)
        println("Summary report saved to summary.json")

    }
}