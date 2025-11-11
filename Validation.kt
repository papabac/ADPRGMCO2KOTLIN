import kotlin.collections.mutableListOf
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeParseException

class Validation {

    fun isParseDateValid(dateString: String): Boolean{
        val (month, day, year) = dateString.split("-")
        val monthInt = month.toIntOrNull() ?: 0
        val dayInt = day.toIntOrNull() ?: 0
        val yearInt = year.toIntOrNull() ?: 0

        if (monthInt == 0 || monthInt < 1 || monthInt > 12 || dayInt == 0 || dayInt < 1 || dayInt > 31 || yearInt == 0 )
            return false

        when (monthInt) {
            2 -> if (dayInt > 28) return false
            4 -> if (dayInt > 30) return false
            6 -> if (dayInt > 30) return false
            9 -> if (dayInt > 30) return false
            11 -> if (dayInt > 30) return false
            else -> null
        }
        return true
    }
    fun isValidDate(dateString: String, pattern: String = "yyyy-MM-dd"): Boolean {
        return try {
            val formatter = DateTimeFormatter.ofPattern(pattern)
            val parsedDate = LocalDate.parse(dateString, formatter)

            parsedDate.format(formatter) == dateString
        } catch (e: DateTimeParseException) {
            false
        }
    }

    fun countInvalidRows(dataTable: MutableList<List<String>>): Int{
        val incompleteRows = mutableListOf<Int>()
        var incompleteRowsCount = 0
        var i = 0
        while(i < dataTable.size){
            var checker = false
            if ((!isValidDate(dataTable[i][16]) && !isParseDateValid(dataTable[i][16])) || (!isValidDate(dataTable[i][13]) && !isParseDateValid(dataTable[i][13])) || (dataTable[i][9].toIntOrNull()
                ?: 0) == 0 ){
                    checker = true
            }
            else {
                for (string in dataTable[i]) {
                    if (string == "" || string == " ") {
                        incompleteRowsCount += 1
                        checker = true
                        break
                    }
                }
            }

            if (checker)
            incompleteRows.add(i) //add the line with incomplete row
            i += 1
        }
        return incompleteRowsCount
    }

    fun InvalidRows(dataTable: MutableList<List<String>>): MutableList<Int>{
        val incompleteRows = mutableListOf<Int>()
        var incompleteRowsCount = 0
        var i = dataTable.size - 1
        while(i >= 0){
            var checker = false
            if ((!isValidDate(dataTable[i][16]) && !isParseDateValid(dataTable[i][16])) || (!isValidDate(dataTable[i][13]) && !isParseDateValid(dataTable[i][13])) || (dataTable[i][9].toIntOrNull()
                    ?: 0) == 0 ){
                checker = true
            }
            else {
                // Check all elements except the fifth one (index 4) because multiplicity can be empty
                for (j in dataTable[i].indices) {
                    if (j == 4) continue  // Skip the fifth element

                    val string = dataTable[i][j]
                    if (string == "" || string == " ") {
                        checker = true
                        break
                    }
                }
            }

            if (checker){
                incompleteRows.add(i) //add the line with incomplete row
                dataTable.removeAt(i)
            }

            i -= 1
        }
        return incompleteRows
    }

    fun OutOfRangeRows(dataTable: MutableList<List<String>>): MutableList<Int>{
        var i = dataTable.size - 1
        var countRowsFiltered = 0
        var rowsFiltered = mutableListOf<Int>()
        while (i >= 0){
            var getYear = StringBuilder()
            getYear.append(dataTable[i][9][0])
            getYear.append(dataTable[i][9][1])
            getYear.append(dataTable[i][9][2])
            getYear.append(dataTable[i][9][3])

            var yearString: String = getYear.toString()
            var yearNum: Int = yearString.toIntOrNull() ?: 0

            if (yearNum !in 2021..2023){
                countRowsFiltered += 1
                rowsFiltered.add(i)
                dataTable.removeAt(i)
            }

            i -= 1
        }
        return rowsFiltered
    }

    fun countOutOfRangeRows(dataTable: MutableList<List<String>>): Int{
        var i = 0
        var countRowsFiltered = 0
        var rowsFiltered = mutableListOf<Int>()
        while (i < dataTable.size){
            var getYear = StringBuilder()
            getYear.append(dataTable[i][9][0])
            getYear.append(dataTable[i][9][1])
            getYear.append(dataTable[i][9][2])
            getYear.append(dataTable[i][9][3])

            var yearString: String = getYear.toString()
            var yearNum: Int = yearString.toIntOrNull() ?: 0
            if (yearNum == 0){
                println(i)
            }

            if (yearNum !in 2021..2023){
                countRowsFiltered += 1
                rowsFiltered.add(i)
            }

            i += 1
        }
        return countRowsFiltered
    }

    fun isDouble(stringNum: String): Boolean{

        return ((stringNum.toDoubleOrNull() ?: -1) == -1)

    }

    fun isInt(stringNum: String): Boolean{

        return ((stringNum.toIntOrNull() ?: -1) == -1)

    }

    fun checkIfDouble(dataTable: MutableList<List<String>>): MutableList<Int> {
        val incompleteRows = mutableListOf<Int>()
        var incompleteRowsCount = 0
        var i = dataTable.size - 1
        while(i >= 0){
            if(isDouble(dataTable[i][11]) || isDouble(dataTable[i][12]) || isDouble(dataTable[i][17])
                || isDouble(dataTable[i][18]) || isDouble(dataTable[i][20]) || isDouble(dataTable[i][21])){
                incompleteRows.add(i)
                incompleteRowsCount += 1
                dataTable.removeAt(i)
            }
            i -= 1
        }
        println("$incompleteRowsCount rows are not double")
        return incompleteRows
    }

    fun checkIfInt(dataTable: MutableList<List<String>>): MutableList<Int> {
        val incompleteRows = mutableListOf<Int>()
        var incompleteRowsCount = 0
        var i = dataTable.size - 1
        while(i >= 0){
            if(isInt(dataTable[i][15])){
                incompleteRows.add(i)
                incompleteRowsCount += 1
                dataTable.removeAt(i)
            }
            i -= 1
        }
        println("$incompleteRowsCount rows are not int")
        return incompleteRows
    }


}