package chess

import kotlin.math.absoluteValue

const val blank = "blank"
const val white = "white"
const val black = "black"
const val boardSize = 8

val characters = MutableList<Char>(boardSize) { ' ' }
val board = createChesboard(boardSize, boardSize)
val player = mutableListOf<MutableList<String>>()
var enPassant = false

fun createChesboard (x: Int, y: Int): MutableList<MutableList<String>> {
    var char = 'a'

    for (i in characters.indices) {
        characters[i] = char
        char++
    }
    return MutableList(x) { MutableList<String>(y) { blank } }
}

fun initializeChessboard (board: MutableList<MutableList<String>>) {
    for (i in board.indices) {
        board[1][i] = black
    }
    for (i in board.indices) {
        board[board.lastIndex - 1][i] = white
    }
}

fun initializePlayers (player: MutableList<MutableList<String>>) {
    println("First Player's name:")
    player.add(mutableListOf())
    player[0].add(readln())
    player[0].add(white)

    println("Second Player's name:")
    player.add(mutableListOf())
    player[1].add(readln())
    player[1].add(black)
}

fun enPassant(x1: Int, y1 :Int, x2: Int, y2: Int, board: MutableList<MutableList<String>>, player: String): Boolean {
    if (board[y1][x1 + 1] != blank && board[y1][x1 + 1] != player) {
        enPassant = true
        board[y1][x1] = blank
        board[y1][x1 + 1] = blank
        board[y2][x2] = player
        return true
    } else if (x1 == 0 || x1 == boardSize - 1) {
        println("Invalid Input")
        return false
    } else if (board[y1][x1 - 1] != blank && board[y1][x1 - 1] != player) {
        enPassant = true
        board[y1][x1] = blank
        board[y1][x1 - 1] = blank
        board[y2][x2] = player
        return true
    }
    println("Invalid Input")
    return false
}

fun checkMove(board: MutableList<MutableList<String>>, coordinates: MutableList<Int>,
              player: String): Boolean {
    if (coordinates.joinToString("") == "exit") {
        println("Bye!")
        return false
    }

    val x1 = coordinates[0]
    val x2 = coordinates[2]
    val y1 = coordinates[1]
    val y2 = coordinates[3]

    if(!(x1 in 0 until boardSize) || !(x2 in 0 until boardSize) || !(y1 in 0 until boardSize) ||
        !(y2 in 0 until boardSize)) {
        return false
    } else if (player == black) {
        if (y2 < y1) {
            return false
        } else if (!(y1 == 1) && (y2 - y1).absoluteValue == 2) {
            return false
        }
    } else if (player == white) {
        if (y2 > y1) {
            return false
        } else if (!(y1 == boardSize - 2) && (y2 - y1).absoluteValue == 2) {
            return false
        }
    }

    if (board[y1][x1] == player) {
        if (board[y2][x2] == blank) {
            if (x2 == x1 && (y2 - y1).absoluteValue == 1) {
                return true
            } else if (x2 == x1 && (y2 - y1).absoluteValue == 2) {
                return true
            } else if (enPassant) {
                return true
            }
        } else if(board[y2][x2] != player && board[y2][x2] != blank &&
            (x2 == x1 + 1 || x2 == x1 - 1) && (y2 - y1).absoluteValue == 1) {
            return true
        }
    }
    return false
}

fun makeMove(board: MutableList<MutableList<String>>, coordinates: MutableList<Int>,
             player: String) {

    val x1 = coordinates[0]
    val x2 = coordinates[2]
    val y1 = coordinates[1]
    val y2 = coordinates[3]

    if (x2 == x1 && (y2 - y1).absoluteValue == 1) {
        enPassant = false
        board[y1][x1] = blank
        board[y2][x2] = player
    } else if (x2 == x1 && (y2 - y1).absoluteValue == 2) {
        enPassant = true
        board[y1][x1] = blank
        board[y2][x2] = player
    } else if (board[y2][x2] != player && board[y2][x2] != blank &&
        (x2 == x1 + 1 || x2 == x1 - 1) && (y2 - y1).absoluteValue == 1) {
        enPassant = false
        board[y1][x1] = blank
        board[y2][x2] = player
    } else if (enPassant) {
        enPassant(x1, y1, x2, y2, board, player)
    }
}


fun isStalemate(board: MutableList<MutableList<String>>, player: String): Boolean {
    var stalemate = false

    loop@for (i in board.indices) {
        if (board[i].contains(player)) {
            for (j in board.indices) {
                val y = j
                val x = i

                if (x in 1 until boardSize && y in 1 until boardSize) {
                    val coordinates = mutableListOf(y, x, y, x + 1)
                    val coordinates2 = mutableListOf(y, x, y, x - 1)
                    val coordinates3 = mutableListOf(y, x, y + 1, x + 1)
                    val coordinates4 = mutableListOf(y, x, y + 1, x - 1)
                    val coordinates5 = mutableListOf(y, x, y - 1, x + 1)
                    val coordinates6 = mutableListOf(y, x, y - 1, x - 1)

                    when (true) {
                        checkMove(board, coordinates, player) -> {
                            stalemate = false
                            break@loop
                        }

                        checkMove(board, coordinates2, player) -> {
                            stalemate = false
                            break@loop
                        }

                        checkMove(board, coordinates3, player) -> {
                            stalemate = false
                            break@loop
                        }

                        checkMove(board, coordinates4, player) -> {
                            stalemate = false
                            break@loop
                        }

                        checkMove(board, coordinates5, player) -> {
                            stalemate = false
                            break@loop
                        }

                        checkMove(board, coordinates6, player) -> {
                            stalemate = false
                            break@loop
                        }

                        else ->
                            stalemate = true
                    }
                } else {
                    stalemate = true
                }
            }
        }
    }

    return stalemate
}

fun checkResult(board: MutableList<MutableList<String>>, player: String): Boolean {
    var black = false
    var white = false

    for (i in board.indices) {
        if (board[i].contains("black")) {
            black = true
        }
        if (board[i].contains("white")) {
            white = true
        }
    }
    if (black && !white || board[board.lastIndex].contains("black")) {
        println("Black Wins!\nBye!")
        return false
    } else if (!black && white || board[0].contains("white")) {
        println("White Wins!\nBye!")
        return false
    }


    if (isStalemate(board, "black") && isStalemate(board,"white")) {
        println("Stalemate!\n" +
                "Bye!")
        return false
    } else if (isStalemate(board, "black") && !isStalemate(board,"white")) {
        println("Stalemate!\n" +
                "Bye!")
        return false
    } else if (!isStalemate(board, "black") && isStalemate(board,"white")) {
        println("Stalemate!\n" +
                "Bye!")
        return false
    }

    return true
}

fun printChesboard(board: MutableList<MutableList<String>>) {
    var counter = board.lastIndex + 1

    for (i in board) {
        print("  +")
        println("---+".repeat(board.first().lastIndex + 1))
        print("$counter |")

        for (j in i) {
            if (j == blank) {
                print("   |")
            } else if (j == black) {
                print(" B |")
            } else if (j == white) {
                print(" W |")
            }
        }
        println("")
        counter--
    }

    print("  +")
    println("---+".repeat(board.first().lastIndex + 1))
    print("    ")

    repeat(board.first().lastIndex + 1){
        print("${characters[it]}   ")
    }
    println()
}

fun coordinates (coordinates: MutableList<String>): MutableList<Int> {
    val x1 = characters.indexOf(coordinates[1].toCharArray()[0])
    val x2 = characters.indexOf(coordinates[3].toCharArray()[0])
    val y1 = boardSize - coordinates[2].toInt()
    val y2 = boardSize - coordinates[4].toInt()

    return mutableListOf(x1, y1, x2, y2)
}

fun play() {
    println("Pawns-Only Chess")
    initializeChessboard(board)
    initializePlayers(player)
    printChesboard(board)
    var playerTurn = 0
    val name = 0
    val chessPawn = 1

    while(true) {
        println("${player[playerTurn][name]}'s turn:")
        val move = readln()

        if (move == "exit") {
            println("Bye!")
            break
        }
        val coordinates = coordinates(move.split("").toMutableList())

        if (!checkMove(board, coordinates, player[playerTurn][chessPawn])) {
            if (board[coordinates[0]][coordinates[1]] != player[playerTurn][chessPawn]) {
                println("No $player pawn at ${coordinates[0]}${coordinates[1]}")
                continue
            }
            println("Invalid Input")
            continue
        }
        makeMove(board, coordinates, player[playerTurn][chessPawn])
        printChesboard(board)

        if (!checkResult(board, player[playerTurn][chessPawn])){
            break
        }

        if (playerTurn == 0) {
            playerTurn++
        } else {
            playerTurn--
        }
    }
}

fun main() {
    play()
}
