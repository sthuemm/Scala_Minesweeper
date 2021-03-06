package de.htwg.mps.minesweeper.core.model

import com.github.nscala_time.time.Imports._
import de.htwg.mps.minesweeper.api.{Game, GameResult, Grid}
import de.htwg.mps.minesweeper.core.model.grid.MinesweeperGrid

case class MinesweeperGame(start: DateTime,
                           end: DateTime,
                           running: Boolean,
                           grid: Grid,
                           gameResult: Option[GameResult]) extends Game {
  override def startGame(): Game = copy(start = DateTime.now, running = true)

  override def finishGame(): Game = {
    val now = DateTime.now
    copy(end = now, running = false,
      gameResult = Some(
        GameResult(
          checkWin,
          correctlyFlaggedBombs,
          grid.bombs,
          start.to(now).toDuration.getStandardSeconds,
          grid.coordinates.size
        )
      )
    )
  }

  override def updateGrid(grid: Grid): Game = copy(grid = grid)

  override def isRunning: Boolean = running

  override def isFinished: Boolean = running && !start.equals(end)

  override def checkWin: Boolean = detectedNonBombFields == nonBombFields && correctlyFlaggedBombs == grid.bombs

  override def checkLost: Boolean = openedBombFields > 0

  def nonBombFields: Int = grid.getFieldCount - grid.bombs

  def correctlyFlaggedBombs: Int = grid.fields.count(f => f.isFlagged && f.isBomb)

  def detectedNonBombFields: Int = grid.fields.count(field => !field.isBomb && field.isShown)

  def openedBombFields: Int = grid.fields.count(field => field.isBomb && field.isShown)

}

object MinesweeperGame {
  def apply(): MinesweeperGame = MinesweeperGame(
    DateTime.now,
    DateTime.now,
    running = false,
    MinesweeperGrid(1, 1, 0),
    None
  )

  def apply(grid: Grid): MinesweeperGame = MinesweeperGame(
    DateTime.now,
    DateTime.now,
    running = false,
    grid,
    None
  )
}