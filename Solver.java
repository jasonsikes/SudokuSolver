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

import java.util.Set;

public class Solver {
    private final Board board;
    private static int iteration = 0;

    /**
     * The number of "steps" that the Solver has taken.
     */
    private static int counter = 0;

    /**
     * Creates a new Solver instance with the given Sudoku board
     * @param board The Sudoku board to solve
     */
    public Solver(Board board) {
        this.board = board;
    }

    public int getCounter() {
        return counter;
    }

    public void resetCounter() {
        counter = 0;
    }

    /**
     * Attempts to solve the Sudoku puzzle, recursively.
     * @return The solved board, or null if the puzzle is unsolvable
     * @discussion: This method will modify that board that is passed in.
     *              If recursion needs to occur, a copy of the board is made
     *              before the recursive call.
     */
    public Board solve() {
        // First step: Find all the cells that have a single candidate
        // and set their value to that candidate.
        // Repeat as long as there are cells that have a single candidate.
        for (int i = 0; i < Board.ROW_SIZE; i++) {
            for (int j = 0; j < Board.COL_SIZE; j++) {
                Set<Character> candidates = board.getCandidates(i, j);
                if (candidates.size() == 1) {
                    counter++;
                    board.setValue(i, j, candidates.iterator().next());
                    // Every time a cell is given a value, we start over.
                    i = 0;
                    j = 0;
                }
            }
        }
        // When we reach here, we have set the value of every cell that has a single candidate.
        // If there are no empty spaces, we solved it.
        if (board.getEmptySpaces() == 0) {
            return board;
        }

        // Second step: Find the first cell that has multiple candidates.
        // Try each candidate.
        // If a candidate solves the puzzle, return the solved board.
        // If a candidate does not solve the puzzle, try the next candidate.
        // If all candidates have been tried and no solution is found, return null.
        int myIteration = iteration;
        for (int i = 0; i < Board.ROW_SIZE; i++) {
            for (int j = 0; j < Board.COL_SIZE; j++) {
                Set<Character> candidates = board.getCandidates(i, j);
                if (candidates.size() > 1) {
                    // Try each candidate.
                    for (char candidate : candidates) {
                        counter++;
                        iteration = myIteration + 1;
                        Board copy = board.copy();
                        copy.setValue(i, j, candidate);
                        Solver solver = new Solver(copy);
                        Board solution = solver.solve();
                        if (solution != null) {
                            return solution;
                        }
                    }
                    // We have tried all of the candidates for this space and none of them worked.
                    return null;
                }
            }
        }
        // None of the spaces have any candidates left.
        return null;
    }

}
