/*******************************************************************************
 * Copyright (c) 2013 See AUTHORS file.
 *
 * This file is part of SleepFighter.
 *
 * SleepFighter is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * SleepFighter is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * See the GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with SleepFighter. If not, see <http://www.gnu.org/licenses/>.
 ******************************************************************************/

#ifndef ANDROID_LOGGING_H_
#define ANDROID_LOGGING_H_

#include <string>
#include <sstream>
#include <android/log.h>

#include "logging.h"

using namespace std;

/**
 * android_logger is a logger that logs to logcat.
 */
class android_logger : public logger {
public:
	android_logger( android_LogPriority& lvl, string& tag ) : lvl(lvl), tag(tag) {
	}

	void set_level( android_LogPriority& lvl ) {
		this -> lvl = lvl;
	}

	void set_tag( string& tag ) {
		this -> tag = tag;
	}

public:
	logger& operator<<(const char* c) {
		this -> log( c );
		return *this;
	}

	logger& operator<<(std::string str) {
		this -> log( str.c_str() );
		return *this;
	}

	logger& operator<<(bool b) {
		this -> log(b ? "true" : "false");
		return *this;
	}

	logger& operator<<(char c) {
		this -> log( &c );
		return *this;
	}

	logger& operator<<(int i) {
		this -> log( i );
		return *this;
	}

	logger& operator<<(long l) {
		this -> log( l );
		return *this;
	}

	logger& operator<<(double d) {
		this -> log( d );
		return *this;
	}

	logger& operator<<(unsigned int i) {
		this -> log( i );
		return *this;
	}

	logger& operator<<(long unsigned int i) {
		this -> log( i );
		return *this;
	}

	logger& operator<<(std::ostream& ostream) {
		this -> log<std::ostream&>( ostream );
		return *this;
	}

	logger& operator<<(logger::special spec) {
		switch (spec) {
		case endl:
			// don't do anything for logcat.
			break;

		default:
			break;
		}
		return *this;
	}

private:
	void log( const char* str ) {
#ifdef ANDROID_LOGGING
		__android_log_write( this -> lvl, this -> tag.c_str(),  str );
#endif
	}

	template<typename T> void log( T t ) {
		stringstream ss;
		ss << t;
		this -> log( ss.str().c_str() );
	}

private:
	android_LogPriority& lvl;
	string& tag;
};

#endif /* ANDROID_LOGGING_H_ */
