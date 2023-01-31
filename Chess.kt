package chess

const val BoardSize = 8

enum class Field() {
    WHITE,
    BLACK,
    BLANK
}

data class Player(val name: String, val turn: Boolean, val pawn: Field)

object Board {
    val board = MutableList(BoardSize) { MutableList<Field>(BoardSize) { Field.BLANK } }

    init {
        board.forEachIndexed { index, fields ->
            if (index == 1) fields.forEachIndexed { i, _ -> fields[i] = Field.BLACK }
            if (index == 6) fields.forEachIndexed { i, _ -> fields[i] = Field.WHITE }
        }
    }
}

class ChessGame() {
    val board = Board.board

    init {
        run()
    }

    fun makeMove() {

    }

    private fun run() {
        println("Pawns-Only Chess")
        println("First Player's name:")
        val whitePlayer = Player(readln(), true, Field.WHITE)
        println("Second Player's name:")
        val blackPlayer = Player(readln(), false, Field.BLACK)

        printBoard()
        /*while (true) {

        }*/
    }

    fun printBoard() {
        repeat(BoardSize) {
            println("  +---+---+---+---+---+---+---+---+")
            print("${BoardSize - it} |")

            repeat(BoardSize) { i ->
                when (board[it][i]) {
                    Field.BLANK -> print("   |")
                    Field.WHITE -> print(" W |")
                    Field.BLACK -> print(" B |")
                }
            }
            println()
        }
        println("  +---+---+---+---+---+---+---+---+")
        println("    a   b   c   d   e   f   g   h  ")
    }
}

fun main() {
    ChessGame()
}