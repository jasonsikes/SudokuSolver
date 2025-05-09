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

import java.util.HashSet;
import java.util.Set;

public class Board {
    private static final int BOARD_SIZE = 81;
    public  static final int ROW_SIZE = 9;
    public  static final int COL_SIZE = 9;
    public  static final char EMPTY = '_';
    private char[] board;
    private Set<Character>[] candidates;
    private int emptySpaces;

    /**
     * Creates a new empty Sudoku board
     */
    @SuppressWarnings("unchecked")
    public Board() {
        board = new char[BOARD_SIZE];
        candidates = new Set[BOARD_SIZE];
        emptySpaces = BOARD_SIZE;
        // Initialize with empty spaces and all possible candidates
        for (int i = 0; i < BOARD_SIZE; i++) {
            board[i] = EMPTY;
            candidates[i] = new HashSet<>();
            for (char c = '1'; c <= '9'; c++) {
                candidates[i].add(c);
            }
        }
    }

    /**
     * Gets the value at the specified position
     * @param row Row index (0-8)
     * @param col Column index (0-8)
     * @return The character at the specified position
     */
    public char getValue(int row, int col) {
        if (row < 0 || row >= ROW_SIZE || col < 0 || col >= COL_SIZE) {
            throw new IllegalArgumentException("Row and column indices must be between 0 and 8");
        }
        return board[row * ROW_SIZE + col];
    }

    /**
     * Gets the candidates for the specified position
     * @param row Row index (0-8)
     * @param col Column index (0-8)
     * @return Set of possible candidates for the position
     */
    public Set<Character> getCandidates(int row, int col) {
        if (row < 0 || row >= ROW_SIZE || col < 0 || col >= COL_SIZE) {
            throw new IllegalArgumentException("Row and column indices must be between 0 and 8");
        }
        return new HashSet<>(candidates[row * ROW_SIZE + col]);
    }

    /**
     * Sets the value at the specified position
     * @param row Row index (0-8)
     * @param col Column index (0-8)
     * @param value The value to set (1-9)
     */
    public void setValue(int row, int col, char value) {
        if (row < 0 || row >= ROW_SIZE || col < 0 || col >= COL_SIZE) {
            throw new IllegalArgumentException("Row and column indices must be between 0 and 8");
        }
        if (value < '1' || value > '9') {
            throw new IllegalArgumentException("Value must be between 1-9");
        }
        
        int index = row * ROW_SIZE + col;
        if (board[index] != EMPTY) {
            throw new IllegalArgumentException("Cell is already filled");
        }
        emptySpaces--;
        board[index] = value;
        // Clear candidates for this cell since it's now filled
        candidates[index].clear();
        
        // Remove value from candidates in the same row
        for (int c = 0; c < COL_SIZE; c++) {
            if (c != col) {
                candidates[row * ROW_SIZE + c].remove(value);
            }
        }
        
        // Remove value from candidates in the same column
        for (int r = 0; r < ROW_SIZE; r++) {
            if (r != row) {
                candidates[r * ROW_SIZE + col].remove(value);
            }
        }
        
        // Remove value from candidates in the same 3x3 box
        int boxRow = (row / 3) * 3;
        int boxCol = (col / 3) * 3;
        for (int r = boxRow; r < boxRow + 3; r++) {
            for (int c = boxCol; c < boxCol + 3; c++) {
                if (r != row || c != col) {
                    candidates[r * ROW_SIZE + c].remove(value);
                }
            }
        }
    }

    /**
     * Returns a string representation of the board
     * @return String representation of the board
     */
    @Override
    public String toString() {
        // Create a new string with a space between each row.
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < BOARD_SIZE; i++) {
            sb.append(board[i]);
            if ((i + 1) % ROW_SIZE == 0) {
                sb.append(" ");
            }
        }
        return sb.toString();
    }

    /**
     * Gets the number of empty spaces remaining on the board
     * @return The count of empty spaces
     */
    public int getEmptySpaces() {
        return emptySpaces;
    }

    /**
     * Creates a deep copy of the board
     * @return A new Board instance with the same state as this board
     */
    public Board copy() {
        Board copy = new Board();
        copy.emptySpaces = this.emptySpaces;
        System.arraycopy(this.board, 0, copy.board, 0, BOARD_SIZE);
        
        for (int i = 0; i < BOARD_SIZE; i++) {
            copy.candidates[i] = new HashSet<>(this.candidates[i]);
        }
        
        return copy;
    }
}
