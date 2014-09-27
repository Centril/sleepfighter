/*
 * Copyright 2014 toxbee.se
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package se.toxbee.commons.geom

import spock.lang.Specification

/**
 *
 * @author Centril < twingoow @ gmail.com >  / Mazdak Farrokhzad.
 * @version 1.0
 * @since Sep , 26, 2014
 */
class PositionTest extends Specification {
	// Helpers:

	private Position p( boolean mut, int x, int y ) {
		return mut ? new MutablePosition( x, y ) : new FinalPosition( x, y );
	}

	private Position3 p3() {
		return new FinalPosition3( f(), 0 );
	}

	private Position m( int x, int y ) {
		return p( true, x, y );
	}

	private Position m() {
		return m( 1,2 );
	}

	private Position f( int x, int y ) {
		return p( false, x, y );
	}

	private Position f() {
		 return f( 1, 2 )
	}

	private boolean checkFinal( Position a, Position b ) {
		return a.isMutable() ? (a == b) : (a != b)
	}

	private boolean is( Position p, int x, int y ) {
		return p.x() == x && p.y() == y
	}

	private boolean isFinal( Position p, int x, int y, Position p2 ) {
		assert is( p2, x, y )
		assert is( p, x, y ) == p.isMutable()
		assert checkFinal( p, p2 )

		return true
	}

	// Tests:

	def "test isMutable()"() {
		expect:
			m().isMutable()
			!f().isMutable()
	}

	def "test x()"() {
		given:
			def p = m();
		expect:
			p.x() == 1
			p.getX() == p.x()
	}

	def "test y()"() {
		given:
			def p = m();
		expect:
			p.y() == 2
			p.getY() == p.y()
	}

	def "test set( int x, int y )"() {
		expect:
			isFinal( p, x, y, p.set( x, y ) )
		where:
			p   | x | y
			m() | 3 | 4
			f() | 3 | 4
	}

	def "test set( Position pos )"() {
		expect:
			isFinal( p, x, y, p.set( f( x, y ) ) )
		where:
			p   | x | y
			m() | 3 | 4
			f() | 3 | 4
	}

	def "test setX( int x )"() {
		expect:
			isFinal( p, v, p.y(), p.setX( v ) )
		where:
			p   | v
			m() | 3
			f() | 3
	}

	def "test setY( int y )"() {
		expect:
			isFinal( p, p.x(), v, p.setY( v ) )
		where:
			p   | v
			m() | 3
			f() | 3
	}

	def "test add( int x, int y )"() {
		expect:
			isFinal( p, x + p.x(), y + p.y(), p.add( x, y ) )
		where:
			p   | x | y
			m() | 3 | 4
			f() | 3 | 4
	}

	def "test add( Position p )"() {
		expect:
			isFinal( p, x + p.x(), y + p.y(), p.add( f( x, y ) ) )
		where:
			p   | x | y
			m() | 3 | 4
			f() | 3 | 4
	}

	def "test addX( int x )"() {
		expect:
			isFinal( p, v + p.x(), p.y(), p.addX( v ) )
		where:
			p   | v
			m() | 3
			f() | 3
	}

	def "test addY( int y )"() {
		expect:
			isFinal( p, p.x(), v + p.y(), p.addY( v ) )
		where:
			p   | v
			m() | 3
			f() | 3
	}

	def "test sub( int x, int y )"() {
		expect:
			isFinal( p, p.x() - x, p.y() - y, p.sub( x, y ) )
		where:
			p   | x | y
			m() | 3 | 4
			f() | 3 | 4
	}

	def "test sub( Position p )"() {
		expect:
			isFinal( p, p.x() - x, p.y() - y, p.sub( f( x, y ) ) )
		where:
			p   | x | y
			m() | 3 | 4
			f() | 3 | 4
	}

	def "test subX( int x )"() {
		expect:
			isFinal( p, p.x() - v, p.y(), p.subX( v ) )
		where:
			p   | v
			m() | 3
			f() | 3
	}

	def "test subY( int y )"() {
		expect:
			isFinal( p, p.x(), p.y() - v, p.subY( v ) )
		where:
			p   | v
			m() | 3
			f() | 3
	}

	def "test mul( int factor )"() {
		expect:
			isFinal( p, v * p.x(), v * p.y(), p.mul( v ) )
		where:
			p   | v
			m() | 3
			f() | 3
	}

	def "test cpy()"() {
		given:
			def p2 = p.cpy()
		expect:
			!p.is( p2 )
			is( p2, p.x(), p.y() )
		where:
			p << [m(), f()]
	}

	def "test move( Direction direction )"() {
		Direction.values().each {
			def m = m()
			def f = f()
			isFinal( m, m.x() + it.dx(), m.y() + it.dy(), m.move( it ) )
			isFinal( f, f.x() + it.dx(), f.y() + it.dy(), f.move( it ) )
		}
	}

	def "test move( Direction direction, int scale )"() {
		Direction.values().each {
			def m = m()
			def f = f()
			isFinal( m, m.move( it, 3), m.x() + 3 * it.dx(), m.y() + 3 * it.dy() )
			isFinal( f, f.move( it, 3), f.x() + 3 * it.dx(), f.y() + 3 * it.dy() )
		}
	}

	def "test values()"() {
		given:
			def v = m().values()
		expect:
			v[0] == 1
			v[1] == 2
	}

	def "test compareTo"() {
		given:
			def a = f( 1, 2 )
			def b = f( 2, 3 )
			def c = f( 2, 4 )
		expect:
			a.compareTo( a ) == 0
			b.compareTo( b ) == 0
			c.compareTo( c ) == 0
			a.compareTo( b ) < 0
			a.compareTo( c ) < 0
			b.compareTo( a ) > 0
			b.compareTo( c ) < 0
			c.compareTo( a ) > 0
			c.compareTo( b ) > 0
	}

	def "test containedIn"() {
		expect:
			p.containedIn( f( 5, 5 ) ) == t
		where:
			p           | t
			f( -1, -1 ) | false
			f( -1, 1 )  | false
			f( 1, -1 )  | false
			f( 0, 0 )   | true
			f( 4, 4 )   | true
			f( 5, 5 )   | false
			f( 6, 5 )   | false
			f( 5, 6 ) | false
	}

	def "test isAdjacent"() {
		expect:
			f( 1, 2 ).isAdjacent( b ) == t
		where:
			b        | t
			f(1,2)   | true
			f(0, 2)  | true
			f(1, 1)  | true
			f(0, 3)  | true
			f(3, 4)  | false
			f(3, 3)  | false
			f(2, 4)  | false
	}

	def "test clone"() {
		given:
			def f = f()
		expect:
			is( (Position) f.clone(), f.x(), f.y() )
	}

	def "test equals"() {
		expect:
			a.equals(b) == t
			b.equals(a) == t
		where:
			a     | b       | t
			"a"   | f()     | false
			f()   | f()     | true
			f()   | m()     | true
			m()   | m()     | true
			f()   | f(1,3)  | false
			f()   | f(0,2)  | false
			f()   | f(0,0)  | false
			m()   | p3()    | false

	}

	def "test width"() {
		given:
			def f = f()
		expect:
			f.x() == f.width()
			f.width() == f.getWidth()
	}

	def "test height"() {
		given:
			def f = f()
		expect:
			f.y() == f.height()
			f.height() == f.getHeight()
	}

	def "test size"() {
		given:
			def p = f()
			def v = p.values()
			def p3 = p3()
			def v3 = p3.values()
		expect:
			p.size( 0 ) == v[0]
			p.size( 1 ) == v[1]
			p3.size( 0 ) == v3[0]
			p3.size( 1 ) == v3[1]
			p3.size( 2 ) == v3[2]
	}

	def "test n"() {
		expect:
			f().n() == 1
			p3().n() == 2
	}

	def "test cross"() {
		given:
			def p = f()
			def p3 = p3()
		expect:
			p.cross() == p.x() * p.y()
			p3.cross() == p3.x() * p3.y() * p3.z()
	}

	def "test cross( int n )"() {
		given:
			def p = f(2, 3)
			def p3 = p3()
		expect:
			p.cross( 0 ) == p.x()
			p.cross( 1 ) == p.x() * p.y()
			p3.cross( 0 ) == p3.x()
			p3.cross( 1 ) == p3.x() * p3.y()
			p3.cross( 2 ) == p3.x() * p3.y() * p3.z()
	}

	def "test lowest"() {
		expect:
			v.lowest() == 1
		where:
			v << [f(1,2), f(2,1)]
	}

	def "test highest"() {
		expect:
			v.highest() == 2
		where:
			v << [f( 1, 2 ), f( 2, 1 )]
	}
}
