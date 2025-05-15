/*
 * Copyright (c) 2025, All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 * 1. Redistributions of source code must retain the above copyright notice, this
 *    list of conditions and the following disclaimer.
 *
 * 2. Redistributions in binary form must reproduce the above copyright notice,
 *    this list of conditions and the following disclaimer in the documentation
 *    and/or other materials provided with the distribution.
 *
 * 3. Neither the name of the copyright holder nor the names of its
 *    contributors may be used to endorse or promote products derived from
 *    this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE
 * FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL
 * DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
 * SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER
 * CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
 * OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE
 * OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.*;

/**
 * This is the main class and user interface for the Sudoku Solver.
 */

public class SudokuSolver {
    private static Boolean isSolved = false;
    private static JButton solveButton;
    private static JLabel messageLabel;
    private static JButton clearButton;
    private static SudokuCellPanel[][] cells;
    private static Color lightBackground = new Color(255, 255, 255);
    private static Color darkBackground = new Color(240, 240, 240);
    private static Color focusedBackground = new Color(255, 255, 200);
    private static Color entryColor = new Color(0, 0, 0);
    private static Font entryFont = new Font("Arial", Font.PLAIN, 24);
    private static Font givenSpaceFont = new Font("Arial", Font.ITALIC, 24);
    private static Font derivedSpaceFont = new Font("Arial", Font.BOLD, 36);
    private static Color givenFontColor = new Color(128, 128, 128); // Gray
    private static Color derivedFontColor = new Color(0, 0, 128); // Dark blue
    private static String instructionsMessage = "Use mouse or arrow keys to navigate and type numbers 1-9 to set values.";

    private static class SudokuCellPanel extends JPanel {
        private String value = "";
        private boolean isFocused = false;
        private CellState state = CellState.ENTRY;
        private final int row;
        private final int col;

        private enum CellState {
            ENTRY,      // User input before solving
            GIVEN,      // Initial values
            DERIVED    // Solved values
        }

        public SudokuCellPanel(int row, int col) {
            this.row = row;
            this.col = col;
            setPreferredSize(new Dimension(50, 50));
            setFocusable(true);
            setBorder(BorderFactory.createLineBorder(Color.BLACK));
            
            addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    if (state == CellState.ENTRY) {
                        requestFocusInWindow();
                    }
                }
            });

            addKeyListener(new KeyAdapter() {
                @Override
                public void keyPressed(KeyEvent e) {
                    if (isSolved || state != CellState.ENTRY) return;

                    switch (e.getKeyCode()) {
                        case KeyEvent.VK_UP:
                            if (row > 0) {
                                cells[row][col].setFocused(false);
                                cells[row-1][col].requestFocusInWindow();
                            }
                            break;
                        case KeyEvent.VK_DOWN:
                            if (row < 8) {
                                cells[row][col].setFocused(false);
                                cells[row+1][col].requestFocusInWindow();
                            }
                            break;
                        case KeyEvent.VK_LEFT:
                            if (col > 0) {
                                cells[row][col].setFocused(false);
                                cells[row][col-1].requestFocusInWindow();
                            }
                            break;
                        case KeyEvent.VK_RIGHT:
                            if (col < 8) {
                                cells[row][col].setFocused(false);
                                cells[row][col+1].requestFocusInWindow();
                            }
                            break;
                        case KeyEvent.VK_BACK_SPACE:
                        case KeyEvent.VK_DELETE:
                            setValue("");
                            break;
                        default:
                            if (e.getKeyChar() >= '1' && e.getKeyChar() <= '9') {
                                setValue(String.valueOf(e.getKeyChar()));
                            }
                            break;
                    }
                }
            });

            addFocusListener(new FocusAdapter() {
                @Override
                public void focusGained(FocusEvent e) {
                    if (state == CellState.ENTRY) {
                        setFocused(true);
                    }
                }

                @Override
                public void focusLost(FocusEvent e) {
                    setFocused(false);
                }
            });
        }

        public void setValue(String value) {
            this.value = value;
            repaint();
        }

        public String getValue() {
            return value;
        }

        public void setFocused(boolean focused) {
            isFocused = focused;
            repaint();
        }

        public void setState(CellState newState) {
            state = newState;
            repaint();
        }

        public CellState getState() {
            return state;
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2d = (Graphics2D) g;
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            // Draw background
            if (isFocused && state == CellState.ENTRY) {
                setBackground(focusedBackground);
            } else if ((row / 3 + col / 3) % 2 == 0) {
                setBackground(darkBackground);
            } else {
                setBackground(lightBackground);
            }

            // Draw value
            if (!value.isEmpty()) {
                switch (state) {
                    case ENTRY:
                        g2d.setFont(entryFont);
                        g2d.setColor(entryColor);
                        break;
                    case GIVEN:
                        g2d.setFont(givenSpaceFont);
                        g2d.setColor(givenFontColor);
                        break;
                    case DERIVED:
                        g2d.setFont(derivedSpaceFont);
                        g2d.setColor(derivedFontColor);
                        break;
                }
                
                FontMetrics fm = g2d.getFontMetrics();
                int x = (getWidth() - fm.stringWidth(value)) / 2;
                int y = ((getHeight() - fm.getHeight()) / 2) + fm.getAscent();
                g2d.drawString(value, x, y);
            }
        }
    }

    private static void setIsSolved(Boolean aIsSolved) {
        if (isSolved != aIsSolved) {
            isSolved = aIsSolved;
            solveButton.setEnabled(!isSolved);
        }
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("Sudoku Solver");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(500, 600);

        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        
        JPanel gridPanel = new JPanel(new GridLayout(9, 9, 1, 1));
        gridPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        cells = new SudokuCellPanel[9][9];
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                cells[i][j] = new SudokuCellPanel(i, j);
                gridPanel.add(cells[i][j]);
            }
        }
        cells[0][0].requestFocusInWindow();

        // Create a wrapper panel to maintain square aspect ratio
        JPanel wrapperPanel = new JPanel() {
            @Override
            public Dimension getPreferredSize() {
                Dimension d = super.getPreferredSize();
                int size = Math.min(d.width, d.height);
                return new Dimension(size, size);
            }
        };
        wrapperPanel.setLayout(new BorderLayout());
        wrapperPanel.add(gridPanel, BorderLayout.CENTER);

        mainPanel.add(wrapperPanel);
        mainPanel.add(Box.createVerticalStrut(10));
        
        messageLabel = new JLabel(instructionsMessage);
        messageLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        mainPanel.add(messageLabel);
        mainPanel.add(Box.createVerticalStrut(5));
        
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 10, 0));
        buttonPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        clearButton = new JButton("Clear board");
        clearButton.addActionListener(e -> {
            for (int i = 0; i < 9; i++) {
                for (int j = 0; j < 9; j++) {
                    cells[i][j].setValue("");
                    cells[i][j].setState(SudokuCellPanel.CellState.ENTRY);
                }
            }
            setIsSolved(false);
            messageLabel.setText(instructionsMessage);
        });
        
        solveButton = new JButton("Solve Sudoku");
        solveButton.addActionListener(e -> {
            // Create and show the calculating dialog
            JDialog calculatingDialog = new JDialog(frame, "Calculating", true);
            calculatingDialog.setLayout(new BorderLayout());
            
            JLabel calculatingLabel = new JLabel("Calculating solution...", SwingConstants.CENTER);
            calculatingLabel.setFont(new Font("Arial", Font.PLAIN, 14));
            calculatingLabel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
            calculatingDialog.add(calculatingLabel, BorderLayout.CENTER);
            
            calculatingDialog.pack();
            calculatingDialog.setLocationRelativeTo(frame);
            
            new Thread(() -> {
                SwingUtilities.invokeLater(() -> calculatingDialog.setVisible(true));
                
                solveSudoku();
                
                SwingUtilities.invokeLater(() -> calculatingDialog.dispose());
            }).start();
        });
        
        buttonPanel.add(clearButton);
        buttonPanel.add(solveButton);
        mainPanel.add(buttonPanel);
        
        frame.add(mainPanel);
        frame.setVisible(true);
    }

    private static void solveSudoku() {
        Board board = new Board();

        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                String value = cells[i][j].getValue();
                if (!value.isEmpty()) {
                    board.setValue(i, j, value.charAt(0));
                    cells[i][j].setState(SudokuCellPanel.CellState.GIVEN);
                }
            }
        }
        Solver solver = new Solver(board.copy());
        solver.resetCounter();
        Board solution = solver.solve();
        if (solution != null) {
            for (int i = 0; i < 9; i++) {
                for (int j = 0; j < 9; j++) {
                    cells[i][j].setValue(String.valueOf(solution.getValue(i, j)));
                    if (cells[i][j].getState() != SudokuCellPanel.CellState.GIVEN) {
                        cells[i][j].setState(SudokuCellPanel.CellState.DERIVED);
                    }
                }
            }
            messageLabel.setText("Solution found in " + solver.getCounter() + " iterations.");
            setIsSolved(true);
        } else {
            messageLabel.setText("No solution found.");
        }
    }
}
