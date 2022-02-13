package sorting
import java.io.File
import java.util.Scanner


class Sorting(private val args: Array<String>) {
    private var sortingType = ""
    private var dataEntry = ""
    private var isArgCorrect = false
    private var fileName = ""
    private var outFile = ""

    private fun getArg() {
        val map = emptyMap<String, String>().toMutableMap()
        val validInput = listOf("long", "line", "word", "byCount", "natural", "-dataType", "-sortingType", "-inputFile", "-outputFile")
        for ( i in 0 until args.lastIndex step 2) {
            map[args[i]] = args[i + 1]
        }

        if (args.contains("-sortingType") && map["-sortingType"] == null) {
            println("No sorting type defined!")
            return
        }
        if (args.contains("-dataType") && map["-dataType"] == null) {
            println("No data type defined!")
            return
        }
        if (args.contains("-inputFile")) {
            fileName = map["-inputFile"]!!
        }
        if (args.contains("-outputFile")) {
            outFile = map["-outputFile"]!!
        }
        for (value in args) {
            if (value !in validInput) {
                println("\"$value\" is not a valid parameter. It will be skipped.")
            }
        }
        sortingType = when (map["-sortingType"]) {
            "byCount" -> "byCount"
            else -> "natural"
        }

        dataEntry = when (map["-dataType"]) {
            "long" -> "numbers"
            "line" -> "lines"
            else -> "words"
        }
        isArgCorrect = true
    }

    private var inList = ArrayList<String>()

    init {
        getArg()
        if (isArgCorrect && fileName == "") getList()
        else if (isArgCorrect && fileName != "") getListFromFile()
    }

    private fun convertList(list: ArrayList<String>): ArrayList<String> {
        val outList = ArrayList<String>()
        for (e in list) {
            if (e.toIntOrNull() != null) {
                outList.add(e)
            } else {
                println("\"$e\" is not a long. It will be skipped")
            }
        }
        return outList
    }

    private fun getList(){
        val scanner = Scanner(System.`in`)
        while (scanner.hasNext()) {
            if (dataEntry == "lines") {
                inList += scanner.nextLine()
            } else {
                inList += scanner.nextLine().split(Regex(" +"))
            }
        }
        if (dataEntry == "numbers") {
            inList = convertList(inList)
        }
    }

    private fun getListFromFile() {
        val lines = ArrayList(File(fileName).readLines())
        if (dataEntry == "lines") {
            inList = lines
        } else {
            for (line in lines) {
                inList += line.split(Regex(" +"))
            }
        }
        if (dataEntry == "numbers") {
            inList = convertList(inList)
        }
    }

    fun print() {
        val res = "Total $dataEntry: ${inList.size}.\n${getSortedData()}"
        if (isArgCorrect && outFile == "") {
            println(res)
        }
        if (isArgCorrect && outFile != "") {
            File(outFile).writeText(res)
        }
    }

    private fun getSortedData(): String {
        return if (dataEntry == "numbers" && sortingType == "natural") {
            "Sorted data: " + inList.sortedBy { it.toInt() }.joinToString(" ")
        } else if (sortingType == "byCount") {
            sortByCount(inList)
        } else if (dataEntry == "words" && sortingType == "natural") {
            "Sorted data: " + inList.sorted().joinToString(" ")
        } else if (dataEntry == "lines") {
            "Sorted data:\n" + inList.sorted().joinToString("\n")
        } else {
            ""
        }
    }

    private fun sortByCount(list: ArrayList<String>): String {
        val countMap = emptyMap<String, Int>().toMutableMap()
        var sortedData = ""
        val newList = if (dataEntry == "numbers") list.sortedBy { it.toInt() } else list.sorted()
        for (item in newList) {
            if (countMap.containsKey(item)) {
                countMap[item] = (countMap[item] ?: 0) + 1
            } else {
                countMap[item] = 1
            }
        }
        val sortedMap = countMap.toList().sortedBy { (_, value) -> value }.toMap()
        for ((k, v) in sortedMap) {
            val percent = String.format("%.0f", v.toDouble() / list.size * 100)
            sortedData += "$k: $v time(s), $percent% \n"
        }
        return sortedData
    }

}

fun main(args: Array<String>) {
    val sort = Sorting(args)
    sort.print()

}

