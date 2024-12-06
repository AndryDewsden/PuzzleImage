package com.example.puzzleimage

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.GridLayout
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import kotlin.random.Random

class MainActivity : AppCompatActivity() {

    private lateinit var gridLayout: GridLayout
    private lateinit var resetButton: Button
    private val tiles = Array(4) { IntArray(4) }
    private var emptyTileRow = 3
    private var emptyTileCol = 3
    private lateinit var originalBitmap: Bitmap
    private val tileSize = 200

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        gridLayout = findViewById(R.id.gridLayout)
        resetButton = findViewById(R.id.resetButton)

        resetButton.setOnClickListener { resetGame() }

        originalBitmap = BitmapFactory.decodeResource(resources, R.drawable.puzzle_image)
        initializeGame()
        setupTiles()
    }

    private fun initializeGame() {
        var number = 0
        for (i in 0 until 4) {
            for (j in 0 until 4) {
                if (number < 15) {
                    tiles[i][j] = number++
                } else {
                    tiles[i][j] = -1
                }
            }
        }
        shuffleTiles()
    }

    private fun shuffleTiles() {
        for (i in 0 until 100) {
            val direction = Random.nextInt(4)
            when (direction) {
                0 -> if (emptyTileRow > 0) moveTile(emptyTileRow - 1, emptyTileCol) // вверх
                1 -> if (emptyTileRow < 3) moveTile(emptyTileRow + 1, emptyTileCol) // вниз
                2 -> if (emptyTileCol > 0) moveTile(emptyTileRow, emptyTileCol - 1) // влево
                3 -> if (emptyTileCol < 3) moveTile(emptyTileRow, emptyTileCol + 1) // вправо
            }
        }
    }

    private fun setupTiles() {
        gridLayout.removeAllViews()
        for (i in 0 until 4) {
            for (j in 0 until 4) {
                val imageView = ImageView(this)
                if (tiles[i][j] != -1) {
                    val x = (tiles[i][j] % 4) * tileSize
                    val y = (tiles[i][j] / 4) * tileSize
                    val tileBitmap = Bitmap.createBitmap(originalBitmap, x, y, tileSize, tileSize)
                    imageView.setImageBitmap(tileBitmap)
                } else {
                    imageView.setImageBitmap(null)
                }

                imageView.setOnClickListener { onTileClick(i, j) }
                gridLayout.addView(imageView, GridLayout.LayoutParams().apply {
                    width = 0
                    height = 0
                    columnSpec = GridLayout.spec(j, 1f)
                    rowSpec = GridLayout.spec(i, 1f)
                })
            }
        }
    }

    private fun onTileClick(row: Int, col: Int) {
        if (isAdjacent(row, col)) {
            tiles[emptyTileRow][emptyTileCol] = tiles[row][col]
            tiles[row][col] = -1
            emptyTileRow = row
            emptyTileCol = col
            setupTiles()
        }
    }

    private fun isAdjacent(row: Int, col: Int): Boolean {
        return (row == emptyTileRow && Math.abs(col - emptyTileCol) == 1) ||
                (col == emptyTileCol && Math.abs(row - emptyTileRow) == 1)
    }

    private fun moveTile(newRow: Int, newCol: Int) {
        tiles[emptyTileRow][emptyTileCol] = tiles[newRow][newCol]
        tiles[newRow][newCol] = -1
        emptyTileRow = newRow
        emptyTileCol = newCol
    }

    private fun resetGame() {
        initializeGame()
        setupTiles()
    }
}
