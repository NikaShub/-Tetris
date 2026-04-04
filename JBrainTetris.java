import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Random;

public class JBrainTetris extends JTetris{
    private JCheckBox brainMode;
    private JSlider adversary;
    private DefaultBrain defBrain;
    private JLabel status;
    private Brain.Move bestMove;
    private int brainCount = 0;

    /**
     * Creates a new JTetris where each tetris square
     * is drawn with the given number of pixels.
     *
     * @param pixels
     */
    JBrainTetris(int pixels) {
        super(pixels);
        defBrain = new DefaultBrain();
    }


    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception ignored) { }
        JBrainTetris tetris = new JBrainTetris(16);
        JFrame frame = JTetris.createFrame(tetris);
        frame.setVisible(true);
    }

    @Override
    public JComponent createControlPanel() {
        JPanel panel = (JPanel) super.createControlPanel();
        panel.add(new JLabel("Brain:"));
        brainMode = new JCheckBox("Brain active");
        brainMode.setSelected(false);
        panel.add(brainMode);
        panel.add(new JPanel());
        JPanel little = new JPanel();
        little.add(new JLabel("Adversary:"));
        adversary = new JSlider(0, 100, 0);
        adversary.setPreferredSize(new Dimension(100, 15));
        little.add(adversary);
        panel.add(little);
        status = new JLabel("ok");
        panel.add(status);
        return panel;
    }

    @Override
    public Piece pickNextPiece() {
        int randNum = (int) (Math.random() * 99) + 1;
        int adversaryNum = adversary.getValue();
        if (randNum >= adversaryNum) {
            status.setText("ok");
            return super.pickNextPiece();
        } else {
            status.setText("*ok*");
            double worstScore = 0;
            Piece worstPiece = null;
            Piece[] pieces = Piece.getPieces();
            for (Piece piece : pieces) {
                Brain.Move move = defBrain.bestMove(board, piece, board.getHeight(), null);
                if (move != null && move.score > worstScore) {
                    worstPiece = piece;
                    worstScore = move.score;
                }
            }
            if (worstPiece == null) return super.pickNextPiece();
            return worstPiece;
        }
    }


    @Override
    public void tick(int verb) {
        if (brainMode.isSelected() && verb == DOWN) {
            if (count != brainCount) {
                brainCount = count;
                board.undo();
                bestMove = defBrain.bestMove(board, currentPiece, board.getHeight(), bestMove);
            }
            if (currentPiece != null && bestMove != null) {
                if (!currentPiece.equals(bestMove.piece)) super.tick(ROTATE);
                if (currentX < bestMove.x) super.tick(RIGHT);
                else if (currentX > bestMove.x) super.tick(LEFT);
            }
        }
        super.tick(verb);
    }
}
