#ifndef _MINESWEEPER_GAME
#define _MINESWEEPER_GAME

#include "logging.h"
#include <cstdlib>

// RNG definition.
typedef void(seeder);
typedef int(*randomizer)(int);

// In every board game square there should be a mine and they can be EMPTY, 1-8 or a MINE.
#define NA -1
#define EMPTY 0
#define MINE 9

enum GameState {
	PROGRESS, WON, LOST
};

enum ClickType {
	NORMAL, FLAG, QUESTION, EXPAND
};

enum SquareState {
	NOT_CLICKED, FLAG_CLICKED, QUESTION_CLICKED, CLICKED
};

// The prototypes of the classes so that people know they exist and are waiting to happen.
class Position;
class Dimensions;
class Move;
class Square;
class Board;
class Game;

/**
 * \brief This class represents dimensions.
 *
 * Please note that it is immutable.
 */
class Dimensions {
public:
	Dimensions(int width, int height) {
		this->width = width;
		this->height = height;
	}

	int getWidth() const {
		return width;
	}

	int getHeight() const {
		return height;
	}

	int size() const {
		return width * height;
	}

private:
	int width, height;
};

class Position {
public:
	Position() {
		this->x = this->y = 0;
	}

	Position(int x, int y) {
		this->x = x;
		this->y = y;
	}

	int getX() const {
		return x;
	}

	int getY() const {
		return y;
	}

	bool isAdjacent(Position other) const {
		return abs(x - other.x) <= 1 && abs(y - other.y) <= 1;
	}

	Position translated( const int dx, const int dy ) const {
		return Position( x + dx, y + dy );
	}

	inline Position translated( const int& delta[2] ) const {
		return translated( delta[0], delta[1] );
	}

private:
	int x, y;
};

class Square {
public:
	int value;
	SquareState state;

	bool isMine() const {
		return value == MINE;
	}

	bool isEmpty() const {
		return value == EMPTY;
	}
};

class Board {
public:
	Board( const Dimensions dim, const int mineCount, randomizer rng, logger* log);
	~Board();

	void print() const;

	GameState clickSquare( const Move& move);

	Dimensions getDimensions() const;
	Square* getGrid();
	const Square& getSquare( const int row, const int col ) const;

	bool isGenerated() const;

	Position posLoc( const int position ) const;

	int locPos( const Move& move) const;
	int locPos( const Position& pos) const;
	int locPos( const int col, const int row) const;
	bool isValidPos( const Position& pos) const;
	bool isValidPos( const int col, const int row) const;

	GameState resetToGenerated( const Move& initial );

private:
	void generateGrid( const Move& move);
	int openEmptySquares( const Position& position);
	GameState expandSquares( const Position& position);

	Square* grid;

	Dimensions dim;
	int mines, squaresLeft;
	bool generated;

	randomizer rng;

	logger* log;
};

// TODO it is possible that only Game and Move need to be in the final interface. Think more on
// this.

/**
 * \brief This class represents a move that the user can make in a game of minesweeper. 
 *
 * When the user wants to make a move then they can just use this to make it happen. This way we can
 * pass moves into the game whether they come from an AI or a computer. Please note that this class
 * should be immutable.
 */
class Move {
public:
	Move(Position pos, ClickType clickType) :
			position(pos), clickType(clickType) {
	}

	Position getPosition() const {
		return position;
	}
	ClickType getClickType() const {
		return clickType;
	}

private:
	Position position;
	ClickType clickType;
};

class Game {
public:
	Game( const Dimensions dim, const int mineCount, randomizer gen, logger* log);
	~Game();

	void print();
	Board* getBoard();
	GameState getState();
	logger* getLogger();
	void resetToGenerated( const Move& initial );
	const Move firstMove(const Position& pos );

	// facade methods:
	inline Dimensions getDimensions() const;
	const Square& Game::getSquare( const int row, const int col ) const;
	void acceptMove(Move& m);

private:
	void generateBoard(int rows, int cols);

	Board board;
	GameState state;

	logger* log;
};

#endif
