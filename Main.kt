import java.util.*

fun main() {

    val scanner = Scanner(System.`in`)
    var choice: Int
    var loaded = false

    val fileReader = FileReader()
    var dataTable: MutableList<List<String>>
    val validationData = Validation()
    val computeField = ComputeField()
    val finalTable = mutableListOf<Project>()
    val report = RegionalReport()
    val contractorReport = ContractorsPerformanceReport()
    val annualReport = AnnualReport()
    val summary = Summary()

    do {
        displayMainMenu()
        print("Enter choice: ")

        try {
            choice = scanner.nextInt()
            scanner.nextLine()

            when (choice) {
                1 -> {
                    dataTable = fileReader.readFile("dpwh_flood_control_projects.csv") //store table values in 2D list
                    validTableStats(dataTable, validationData)

                    var i = 0
                    while (i < dataTable.size) {
                        val projectData = Project(dataTable[i][0], dataTable[i][1], dataTable[i][2], dataTable[i][3],
                            dataTable[i][4], dataTable[i][5], dataTable[i][6], dataTable[i][7],
                            dataTable[i][8], dataTable[i][9].toInt(), dataTable[i][10], dataTable[i][11].toDouble(),
                            dataTable[i][12].toDouble(), dataTable[i][13], dataTable[i][14], dataTable[i][15].toInt(),
                            dataTable[i][16], dataTable[i][17].toDouble(), dataTable[i][18].toDouble(), dataTable[i][19],dataTable[i][20].toDouble(),
                            dataTable[i][21].toDouble(), computeField.computeCostSavings(dataTable, i),
                            computeField.completionDelayDays(dataTable[i][16], dataTable[i][13]))

                        finalTable.add(projectData)
                        i += 1
                    }
                    loaded = true
                }
                2 -> {
                    if (loaded) {
                        report.generateReportOne(finalTable)
                        println("\n")
                        contractorReport.generateReportTwo(finalTable)
                        println("\n")
                        annualReport.generateReportThree(finalTable)
                        println("\n")
                        summary.generateSummary(finalTable)
                    } else {
                        println("Please load the file first (option 1)")
                    }
                }
                else -> {
                    if (choice != 0) {
                        println("Invalid choice. Please try again.")
                    }
                }
            }
        } catch (e: InputMismatchException) {
            println("Please enter a valid number.")
            scanner.nextLine()
            choice = -1
        }

    } while (choice != 0)

    println("Exiting program...")
    scanner.close()
}

fun validTableStats(dataTable: MutableList<List<String>>, validationData: Validation) {
    println("Number of columns:")
    println(dataTable[0].size)

    println("Amount of rows:")
    println(dataTable.size)

    println("Amount of invalid rows (with multiplicity):")
    println(validationData.countInvalidRows(dataTable))
    println("Amount of Rows where out of 2021-2023")
    println(validationData.countOutOfRangeRows(dataTable))

    println("\nStart of cleanup\n")

    println("Rows not double")
    println(validationData.checkIfDouble(dataTable))

    println("Rows not int")
    println(validationData.checkIfInt(dataTable))

    println("Rows where values are incomplete/invalid (without multiplicity)")
    println(validationData.InvalidRows(dataTable))

    println("Rows where out of 2021-2023")
    println(validationData.OutOfRangeRows(dataTable))

    println("Amount of rows After cleanup:")
    println(dataTable.size)
}
fun displayMainMenu() {
    println("Select Language Implementation:")
    println("[1] Load the file")
    println("[2] Generate Reports")
    println("[0] Exit")
    println()
}