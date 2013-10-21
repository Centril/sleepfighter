#include <cstdlib>
#include <cassert>
#include <algorithm>
#include <random>
#include <vector>

#include "game.h"

typedef Position::direction direction;

// Implementing the Game
Game::Game( const Dimensions dim, const int mineCount, randomizer rng, logger* log) :
		board(dim, mineCount, rng, log), log(log) {
	state = PROGRESS;
}

Game::~Game() {
}

const Move Game::firstMove(const Position& pos ) {
	Move move( pos, NORMAL );
	acceptMove(move);
	return move;
}

void Game::acceptMove(Move& m) {
	if (state == PROGRESS) {
		state = board.clickSquare(m);
	}
}

Dimensions Game::getDimensions() const {
	return board.getDimensions();
}

const Square& Game::getSquare( const int row, const int col ) const {
	return board.getSquare( row, col );
}

const Square& Board::getSquare( const int row, const int col ) const {
	return grid[locPos(col, row)];
}

void Game::resetToGenerated( const Move& initial ) {
	if ( !board.isGenerated() ) {
		(*log) << "Board has not been generated yet" << logger::endl;
	}

	// used to reset to generated after solver has been run.
	state = board.resetToGenerated( initial );
}

GameState Board::resetToGenerated( const Move& initial ) {
	// First reset the entire grid.
	squaresLeft = dim.size();
	for ( int i = 0; i < squaresLeft; ++i ) {
		grid[i].state = SquareState::NOT_CLICKED;
	}

	// Now re-do the inital move.
	clickSquare( initial );

	return GameState::PROGRESS;
}

void Game::print() {
	// TODO maybe show more game information
	board.print();
}

GameState Game::getState() {
	return state;
}

logger* Game::getLogger() {
	return this->log;
}

Board* Game::getBoard() {
	return &board;
}

//
// Implementing the Board
//
Board( const Dimensions dim, const int mineCount, randomizer rng, logger* log) :
		squaresLeft(0), dim(dim), rng(rng), log(log) {
	grid = NULL;
	generated = false;
	mines = mineCount;
}

Board::~Board() {
	if (grid != NULL) {
		delete[] grid;
	}
}

void Board::print() const {
	for (int row = 0; row < dim.getHeight(); ++row) {
		for (int col = 0; col < dim.getWidth(); ++col) {
			int position = locPos(col, row);
			int gridValue;
			switch (grid[position].state) {
			case CLICKED:
				gridValue = grid[position].value;
				switch (gridValue) {
				case MINE:
					(*log) << "M";
					break;

				case EMPTY:
					(*log) << " ";
					break;

				default:
					(*log) << gridValue;
					break;
				}
				break;

			case FLAG:
				(*log) << "F";
				break;

			case QUESTION:
				(*log) << "?";
				break;

			case NOT_CLICKED:
				(*log) << "#";
				break;

			default:
				(*log) << "E";
				break;
			}
		}

		(*log) << logger::endl;
	}
}

GameState Board::clickSquare( const Move& move) {
	if (!generated) {
		generateGrid(move);
	}

	GameState resultingState = PROGRESS;

	Position clickPosition = move.getPosition();
	if (isValidPos(clickPosition)) {
		int position = locPos(move);

		if (grid[position].state == NOT_CLICKED) {
			switch (move.getClickType()) {
			case NORMAL:
				squaresLeft -= openEmptySquares(clickPosition);

				if (grid[position].state == NOT_CLICKED) {
					grid[position].state = CLICKED;
					squaresLeft--;
				}

				// Check to see if you won or lost on this click
				if (grid[position].value == MINE) {
					// if you clicked a mine then you lost
					resultingState = LOST;
				} else if (squaresLeft == mines) {
					// if you did not click a mine and only mines are left on the board then
					// you won!
					resultingState = WON;
				}
				break;

			case FLAG:
				if (grid[position].state != CLICKED) {
					grid[position].state = FLAG_CLICKED;
				}
				break;

			case QUESTION:
				if (grid[position].state != CLICKED) {
					grid[position].state = QUESTION_CLICKED;
				}
				break;

			case EXPAND:
				if (grid[position].state != CLICKED) {
					resultingState = expandSquares(clickPosition);
				}
				break;
			}
		}
	} else {
		(*log)
				<< "ERROR: The position provided in the move did not exist on the board."
				<< logger::endl;
	}

	return resultingState;
}

int Board::openEmptySquares( const Position& position) {
	int pos = locPos(position);
	int clicked = 0;

	if (isValidPos(position)) {
		if (grid[pos].state != CLICKED) {
			grid[pos].state = CLICKED;
			clicked++;

			if (grid[pos].value == EMPTY) {
				for (direction dir = 0; dir < Position::directions_count; ++dir) {
					Position nextPos = position.translated( dir );
					clicked += openEmptySquares(nextPos);
				}
			}
		}
	}

	return clicked;
}

GameState Board::expandSquares( const Position& position) {
	int count = 0;

	// Count the number of adjacent flags
	for (direction dir = 0; dir < Position::directions_count; ++dir) {
		Position tempPos = position.translated( dir );
		if (isValidPos(tempPos)) {
			count += (grid[locPos(tempPos)].state == FLAG_CLICKED);
		}
	}

	GameState lastGameState = PROGRESS;
	// if you have clicked enough adjacent flags
	if (count == grid[locPos(position)].value) {
		// click each adjacent square normally
		for (direction dir = 0; dir < Position::directions_count; ++dir) {
			Position tempPos = position.translated( dir );
			Move move(tempPos, NORMAL);
			lastGameState = clickSquare(move);
		}
	}

	return lastGameState;
}

Dimensions Board::getDimensions() const {
	return dim;
}

Square* Board::getGrid() {
	// TODO is this function even required
	return grid;
}

Position Board::posLoc(int position) const {
	int row = position / dim.getWidth();
	int col = position % dim.getWidth();
	return Position(col, row);
}

int Board::locPos( const Move& move) const {
	Position pos = move.getPosition();
	return locPos(pos);
}

int Board::locPos( const Position& pos) const {
	return locPos(pos.getX(), pos.getY());
}

int Board::locPos(int col, int row) const {
	return row * dim.getWidth() + col;
}

bool Board::isValidPos( const Position& pos) const {
	return isValidPos(pos.getX(), pos.getY());
}

template<typename T>
inline static bool inBounds( const T& v, const T& b ) const {
	return v >= 0 && v < b;
}

bool Board::isValidPos(int col, int row) const {
	return inBounds( row, dim.getHeight() ) && inBounds( col, dim.getWidth() );
}

class IncGenerator {
public:
	IncGenerator(Dimensions dim) :
			dimensions(dim), current(Position(0, 0)) {
	}

	IncGenerator(Position initial, Dimensions dim) :
			dimensions(dim), current(initial) {
	}

	Position operator()() {
		Position last = current;
		if (current.getX() + 1 == dimensions.getWidth()) {
			current = Position(0, current.getY() + 1);
		} else {
			current = Position(current.getX() + 1, current.getY());
		}
		return last;
	}

private:
	Dimensions dimensions;
	Position current;
};

// We can assume that the move is valid by this stage.
void Board::generateGrid( const Move& move) {
	int totalSquares = dim.size();
	squaresLeft = totalSquares;
	assert(mines >= 0);
	assert(mines < totalSquares); // Cannot place more mines than there are squares.

	// Shuffle the positions so that we can pick the ones that we want to be the mines.
	typedef std::vector<Position> posvec;
	posvec squarePositions(totalSquares);
	IncGenerator gen(dim);
	std::generate(squarePositions.begin(), squarePositions.end(), gen);
	std::random_shuffle(squarePositions.begin(), squarePositions.end(), rng );

	// Generate the board
	grid = new Square[totalSquares];
	for (int i = 0; i < totalSquares; ++i) {
		grid[i].state = NOT_CLICKED;
		grid[i].value = EMPTY;
	}

	// Pick the Mines
	{
		int mineCount = 0;
		for (posvec::iterator positionIter = squarePositions.begin();
				mineCount < mines && positionIter != squarePositions.end();
				++positionIter) {
			if (!positionIter->isAdjacent(move.getPosition())) {
				grid[locPos(*positionIter)].value = MINE;
				++mineCount;
			}
		}
	}

	// Calculate the numbers of the squares
	for (int row = 0; row < dim.getHeight(); ++row) {
		for (int col = 0; col < dim.getWidth(); ++col) {
			int currentSquare = locPos(col, row);

			if (grid[currentSquare].value != MINE) {
				int count = EMPTY;

				Position testPos = Position(col, row );

				for (direction dir = 0; dir < Position::directions_count; ++dir) {
					testPos = testPos.translated(dir);
					if (isValidPos(testPos)) {
						count += (grid[locPos(testPos)].value == MINE);
					}
				}

				grid[currentSquare].value = count;
			}
		}
	}

	// Now finally state that the board has been generated
	generated = true;
}
