package de.htwg.mps.minesweeper.view.gui

import javax.swing.border.EmptyBorder

import de.htwg.mps.minesweeper.controller.PlayerUpdate
import de.htwg.mps.minesweeper.model.player.Player

import scala.swing.{BorderPanel, Dimension, Frame, Label}

class PlayerGui(guiController: GuiController) extends Frame {

  listenTo(guiController)

  reactions += {
    case PlayerUpdate(player) => redraw(player)
  }

  title = "Player Score"

  private val heading = new Label {
    text = "Score history"
    font = Constants.headingFont
  }
  private val panelBorder = new EmptyBorder(10, 10, 10, 10)

  contents = new BorderPanel {
    add(heading, BorderPanel.Position.North)
    add(new Label("No games finished yet"), BorderPanel.Position.Center)
    border = panelBorder
    minimumSize = new Dimension(200, 200)
  }

  private def redraw(player: Player): Unit = contents = new BorderPanel {
    add(heading, BorderPanel.Position.North)
    add(new Label {
      text = player.history.foldLeft("<html>")((string, gameResult) => string +
        (if (gameResult.win) "win" else "defeat") + ": <b>" + gameResult.getScore + "</b><br/>"
      ) + "</html>"
    }, BorderPanel.Position.Center)
    border = new EmptyBorder(10, 10, 10, 10)
    minimumSize = new Dimension(200, 200)
  }


}
