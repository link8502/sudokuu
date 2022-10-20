package sudokuframe;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Panel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.WindowEvent;
import javax.swing.BorderFactory;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.Timer;

@SuppressWarnings("serial")     //To avoid warnings
public final class SudokuFrame extends JFrame {

    private final JPanel buttonSelectionPanel;
    private final SudokuPanel sPanel;
    private Timer timer;
    private JMenu center;
    private JLabel label = new JLabel("Timer :00 : 00 : 00", JLabel.CENTER);
    private int counter = 0;

    public SudokuFrame() {
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setTitle("Sudoku");
        this.setMinimumSize(new Dimension(1024, 800));

        JMenuBar menuBar = new JMenuBar();//About Menu
        JButton sixBySixGame = new JButton("6 By 6 Game"); //6x6 Button
        sixBySixGame.addActionListener(new NewGameListener(SudokuPuzzleType.SIXBYSIX, 30));
        JButton nineByNineGame = new JButton("9 By 9 Game"); //9x9 Button
        nineByNineGame.addActionListener(new NewGameListener(SudokuPuzzleType.NINEBYNINE, 26));
        JButton twelveByTwelveGame = new JButton("12 By 12 Game");//12x12 Button
        twelveByTwelveGame.addActionListener(new NewGameListener(SudokuPuzzleType.TWELVEBYTWELVE, 20));
        JButton help = new JButton("How to Play");
        JButton finish = new JButton("Finish");
        JPanel timepanel = new JPanel();
        JButton exit = new JButton("Exit");
        
        help.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JOptionPane.showMessageDialog(null, "The goal of Sudoku is to fill in a chosen grid(6x6,9x9 and 12by12) "
                        + "\nwith digits so that each column, row, and section. At the beginning of the game, the chosen grid will have some of the "
                        + "\nsquares filled in. Your job is to use logic to fill in the missing digits and "
                        + "\ncomplete the grid. Don’t forget, a move is incorrect if: Any row contains more than one of the same number,"
                        + "\nAny column contains more than one of the same number,"
                        + "\nAny grid contains the same number ");
            }
        });

        exit.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JOptionPane.showMessageDialog(center, "Thank you for Playing");
                SudokuFrame.this.dispose();
            }
        });
        
        finish.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                
                Object [] options = {"No","YES"};
                int x = JOptionPane.showOptionDialog(null, "Want A New Game ?", "Finish", JOptionPane.DEFAULT_OPTION,JOptionPane.INFORMATION_MESSAGE, null, options, options[0]);
                
                if(x == 1){
                SudokuFrame.this.rebuildInterface(SudokuPuzzleType.NINEBYNINE, 26);
            }else{
                SudokuFrame.this.dispose();
                }
                
            }
        });
        

        
        timepanel.setBackground(Color.decode("#AABFFF"));
        timepanel.setBorder(BorderFactory.createLineBorder(Color.black, 6, true));
        timepanel.setLayout(new GridLayout(4, 3, 0, 20));


        ActionListener action = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                label.setText(TimeFormat(counter));
                counter++;
            }

        };
        timer = new Timer(1000, action);
        label.setForeground(Color.black);

        label.setFont(new Font(Font.DIALOG, Font.PLAIN, 16));
        timepanel.add(sixBySixGame);
        timepanel.add(nineByNineGame);
        timepanel.add(twelveByTwelveGame);          //Add buttons and menu in JPanel and JFrame
        timepanel.add(help);                
        timepanel.add(finish);
        
        timepanel.add(exit);
        this.setJMenuBar(menuBar);
        timepanel.add(label);
        this.add(timepanel, "South");
        JPanel windowPanel = new JPanel();
        windowPanel.setLayout(new FlowLayout());
        windowPanel.setPreferredSize(new Dimension(1024, 800));

        label.setFont(new Font("Verdana", Font.PLAIN, 30));
        buttonSelectionPanel = new JPanel();
        buttonSelectionPanel.setPreferredSize(new Dimension(90, 500));

        sPanel = new SudokuPanel();

        windowPanel.add(sPanel);
        windowPanel.add(buttonSelectionPanel);
        this.add(windowPanel);

        rebuildInterface(SudokuPuzzleType.NINEBYNINE, 26);
    }

    public void rebuildInterface(SudokuPuzzleType puzzleType, int fontSize) {
        SudokuPuzzle generatedPuzzle = new SudokuGenerator().generateRandomSudoku(puzzleType);
        sPanel.newSudokuPuzzle(generatedPuzzle);
        sPanel.setFontSize(fontSize);
        buttonSelectionPanel.removeAll();
        for (String value : generatedPuzzle.getValidValues()) {             //To generate button seletion based on the chosen grid
            JButton b = new JButton(value);
            b.setPreferredSize(new Dimension(50, 30));                                 
            b.addActionListener(sPanel.new NumActionListener());
            buttonSelectionPanel.add(b);
        }
        sPanel.repaint();
        buttonSelectionPanel.revalidate();
        buttonSelectionPanel.repaint();
    }

    public class NewGameListener implements ActionListener {

        private SudokuPuzzleType puzzleType;
        private int fontSize;

        public NewGameListener(SudokuPuzzleType puzzleType, int fontSize) {
            this.puzzleType = puzzleType;
            this.fontSize = fontSize;
        }

        @Override
        public void actionPerformed(ActionEvent e) {        //Listener to repaint the chosen grid
            rebuildInterface(puzzleType, fontSize);

            counter = 0;

            timer.start();

        }

    }

    private String TimeFormat(int count) {

        int hours = count / 3600;
        int minutes = (count - hours * 3600) / 60;          //Time format 
        int seconds = count - minutes * 60;

        return String.format("Timer :" + "%02d", hours) + " : " + String.format("%02d", minutes) + " : " + String.format("%02d", seconds);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                SudokuFrame frame = new SudokuFrame();
                frame.setVisible(true);     //To make the JFrame visible
                frame.timer.start();        //Timer will automatically start when the program runs 
            }
        });
    }
}
