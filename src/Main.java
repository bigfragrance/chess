import chess.modules.Chess;
import chess.modules.ChessMain;
import chess.modules.Screen;

import javax.swing.*;
import java.awt.*;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    public static void main(String[] args) {
        //TIP Press <shortcut actionId="ShowIntentionActions"/> with your caret at the highlighted text
        // to see how IntelliJ IDEA suggests fixing it.
        Screen.frame = new JFrame("lol");
        Screen panel = new Screen();
        panel.setBackground(new Color(50,200,20));
        Screen.frame.add(panel);
        Screen.frame.setSize(1000, 1000);
        Screen.frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        Screen.frame.setVisible(true);
        Thread thread = new Thread(panel);

        new ChessMain();
        new Thread(()->{
            ChessMain.cs.run();
        }).start();
        //cm.run();
        thread.start();
    }
}