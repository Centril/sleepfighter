#ifndef _MINESWEEPER_GAME
#define _MINESWEEPER_GAME

#include <cstdlib>

#include "logging/logging.h"
#include "position.h"
#include "dimensions.h"

// RNG definition.
typedef void(seeder);
typedef int(*randomizer)(int);

enum GameState {
	PROGRESS, WON, LOST
};

enum ClickType {
	NORMAL, FLAG, QUESTION, EXPAND
};

enum SquareState {
	NOT_CLICKED, FLAG_CLICKED, QUESTION_CLICKED, CLICKED
};

// In every board game square there should be a mine and they can be EMPTY, 1-8 or a MINE.

class Square {
public:
	typedef int minevalue;

	minevalue value;
	SquareState state;

	minevalue getValue() {
		return value;
	}

	bool isMine() const {
		return value == MINE;
	}

	bool isEmpty() const {
		return value == EMPTY;
	}

	SquareState getState() {
		return state;
	}

private:
	static const minevalue NA = -1;
	static const minevalue EMPTY = 0;
	static const minevalue MINE = 9;
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
