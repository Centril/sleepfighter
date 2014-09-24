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

package se.toxbee.commons.collect;

import java.util.AbstractCollection;
import java.util.Collection;
import java.util.NoSuchElementException;

import se.toxbee.commons.geom.Dimension;
import se.toxbee.commons.geom.FinalPosition;
import se.toxbee.commons.geom.Position;

/**
 *
 * @author Centril<twingoow@gmail.com> / Mazdak Farrokhzad.
 * @version 1.0
 * @since Feb, 18, 2014
 */
public abstract class AbstractGrid<T> extends AbstractCollection<T> implements Grid<T> {
	abstract protected int firstEmptyIndex();
	abstract protected int lastEmptyIndex();

	protected final Dimension dim;

	/**
	 * Constructs an Grid with dimension.
	 *
	 * @param w the width part of dimension.
	 * @param h the height part of dimension.
	 * @throws java.lang.IllegalArgumentException
	 * If the cross product of the 2 first dimension axes lower than 1.
	 */
	public AbstractGrid( int w, int h ) {
		this( new FinalPosition( w, h ) );
	}

	/**
	 * Constructs a Grid with dimension.
	 *
	 * @param dimension the dimension of the grid.
	 * @throws java.lang.IllegalArgumentException
	 * If the cross product of the 2 first dimension axes lower than 1.
	 */
	public AbstractGrid( Dimension dimension ) {
		super();

		if ( dimension.cross( 1 ) < 1 ) {
			// require at least 1 cell.
			throw new IllegalArgumentException( "A grid requires at least 1 cell." );
		}

		this.dim = dimension;
	}

	@Override
	public Dimension dim() {
		return this.dim;
	}

	@Override
	public Position position( int index ) {
		int y = index / dim.width();
		int x = index % dim.width();
		return new FinalPosition( x, y );
	}

	@Override
	public T get( int x, int y ) {
		return this.get( this.index( x, y ) );
	}

	@Override
	public T set( int x, int y, T val ) {
		return this.set( this.index( x, y ), val );
	}

	@Override
	public int index( int x, int y ) {
		return y * this.dim.width() + x;
	}

	@Override
	public boolean inBounds( int x, int y ) {
		return this.inBounds( new FinalPosition( x, y ) );
	}

	@Override
	public int index( Position p ) {
		return this.index( p.x(), p.y() );
	}

	@Override
	public T get( Position p ) {
		return this.get( this.index( p ) );
	}

	@Override
	public T set( Position p, T val ) {
		return this.set( this.index( p ), val );
	}

	@Override
	public boolean inBounds( Position pos ) {
		return this.dim.contains( pos );
	}

	@Override
	public boolean setAll( int index, Collection<? extends T> c ) {
		return setAll( index, this.size(), c );
	}

	@Override
	public boolean add( T t ) {
		if ( t == null ) {
			throw new IllegalArgumentException( "Grid.add() does not accept null valued elements." );
		}

		if ( this.isFull() ) {
			throw new IllegalStateException( "Grid.add() can't add the element, the grid is full." );
		}

		// Fill the first "empty" slot.
		this.set( this.firstEmptyIndex(), t );
		return true;
	}

	@Override
	public boolean isEmpty() {
		return this.firstEmptyIndex() == 0 && this.lastEmptyIndex() == this.size() - 1;
	}

	@Override
	public boolean isFull() {
		return this.firstEmptyIndex() == this.size();
	}

	@Override
	public int indexOf( Object o ) {
		GridIterator<T> it = iterator();

		if ( o == null ) {
			while ( it.hasNext() ) {
				if ( it.next() == null ) {
					return it.previousIndex();
				}
			}
		} else {
			while ( it.hasNext() ) {
				if ( o.equals( it.next() ) ) {
					return it.previousIndex();
				}
			}
		}

		return -1;
	}

	@Override
	public int lastIndexOf( Object o ) {
		GridIterator<T> it = iterator( size() );
		if ( o == null ) {
			while ( it.hasPrevious() ) {
				if ( it.previous() == null ) {
					return it.nextIndex();
				}
			}
		} else {
			while ( it.hasPrevious() ) {
				if ( o.equals( it.previous() ) ) {
					return it.nextIndex();
				}
			}
		}
		return -1;
	}

	@Override
	public GridIterator<T> iterator() {
		return new Itr<T>( this );
	}

	@Override
	public GridIterator<T> iterator( int start ) {
		return new Itr<T>( this, start );
	}

	protected int indexCheck( int index ) {
		if ( !this.isValid( index ) ) {
			throw new IndexOutOfBoundsException( "Index: " + index + ", Size: " + this.size() );
		}

		return index;
	}

	protected boolean isValid( int index ) {
		return 0 <= index && index < this.size();
	}

	protected boolean setAll( int index, int size, Collection<? extends T> c ) {
		indexCheck( index );

		if ( c.size() == 0 || size - index == 0 ) {
			return false;
		}

		for ( T e : c ) {
			if ( index == size ) {
				break;
			}

			set( index++, e );
		}

		return true;
	}

	protected static class Itr<T> implements GridIterator<T> {
		protected final Grid<T> grid;
		protected int cursor;

		/**
		 * Constructs the grid iterator, starting at index 0.
		 *
		 * @param grid the grid to iterate.
		 */
		public Itr( Grid<T> grid ) {
			this( grid, 0 );
		}

		/**
		 * Constructs the grid iterator, starting at start.
		 *
		 * @param grid the grid to iterate.
		 * @param start the index to start at.
		 */
		public Itr( Grid<T> grid, int start ) {
			this.grid = grid;
			this.cursor = start;
		}

		@Override
		public boolean hasNext() {
			return this.cursor != this.grid.size();
		}

		@Override
		public boolean hasPrevious() {
			return this.cursor != 0;
		}

		@Override
		public int nextIndex() {
			return this.cursor;
		}

		@Override
		public int previousIndex() {
			return this.cursor - 1;
		}

		@Override
		public T next() {
			int i = this.nextIndex();
			return this.go( i >= this.grid.size(), i );
		}

		@Override
		public T previous() {
			int i = this.previousIndex();
			return this.go( i < 0, i );
		}

		@Override
		public void set( T e ) {
			this.grid.set( this.cursor, e );
		}

		@Override
		public void add( T e ) {
			this.set( e );
		}

		@Override
		public void remove() {
			this.grid.remove( this.cursor );
		}

		private T go( boolean fail, int index ) {
			if ( fail ) {
				throw new NoSuchElementException();
			} else {
				return this.grid.get( this.cursor = index );
			}
		}
	}
}
