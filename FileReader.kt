import java.io.File

class FileReader {

    fun parseLine(line: String): List<String> {
        val valueList = mutableListOf<String>()
        var currentString = StringBuilder()
        var insideQuotes = false

        for (char in line) {
            when {
                char == '"' -> {
                    insideQuotes = !insideQuotes
                }
                char == ',' && !insideQuotes -> {
                    valueList.add(currentString.toString())
                    currentString = StringBuilder()
                }
                else -> {
                    currentString.append(char)
                }
            }
        }

        valueList.add(currentString.toString())
        return valueList
    }

    fun readFile(path: String): MutableList<List<String>>{
        val dataLines: List<String> = File(path).readLines()
        val dataTable: MutableList<List<String>> = mutableListOf()

        for (line in dataLines) {
            var addValue = parseLine(line)
            dataTable.add(addValue)
        }

        dataTable.remove(dataTable[0])

        return dataTable
    }




}