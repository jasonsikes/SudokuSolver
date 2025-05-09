# Readme for SudokuSolver

SudokuSolver is a Java application that solves a given Sudoku puzzle. It features a graphical user interface that allows users to input their Sudoku puzzles and solve them with a single click.

## Features

- Interactive 9x9 Sudoku grid interface
- Keyboard navigation (arrow keys) and mouse input support
- One-click solution generation

## Requirements

- Java Runtime Environment (JRE) 8 or higher
- Java Development Kit (JDK) 8 or higher (for development)

## How to Use

1. **Compile the Program**
   ```bash
   javac SudokuSolver.java
   ```

2. **Run the Program**
   ```bash
   java SudokuSolver
   ```

3. **Using the Interface**
   - Click on any cell or use arrow keys to navigate
   - Type numbers 1-9 to input values
   - Use Backspace or Delete to clear a cell
   - Click "Solve" to solve the puzzle
   - Click "Clear" to reset the grid

## Controls

- **Mouse**: Click on cells to select them
- **Arrow Keys**: Navigate between cells
- **Numbers 1-9**: Input values
- **Backspace/Delete**: Clear cell value
- **Solve Button**: Solve the current puzzle
- **Clear Button**: Reset the grid

## Project Structure

- `SudokuSolver.java`: Main application file containing the GUI implementation
- `Board.java`: Contains the Sudoku board logic
- `Solver.java`: Implements the Sudoku solving algorithm

## Development

The project is written in Java using Swing for the graphical user interface. The solver implements an efficient backtracking algorithm to find solutions to valid Sudoku puzzles.

## License

This project is open source and available for personal and educational use under the BSD License. This means you are free to use, modify, and distribute this software, subject to the following conditions:

1. Redistributions of source code must retain the above copyright notice
2. Redistributions in binary form must reproduce the above copyright notice
3. Neither the name of the copyright holder nor the names of its contributors may be used to endorse or promote products derived from this software without specific prior written permission

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.

