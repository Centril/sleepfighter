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

import java.util.Arrays;
import java.util.Collection;

import se.toxbee.commons.geom.Dimension;
import se.toxbee.commons.reflect.ReflectionUtil;

/**
 * ArrayGrid implements a flat array based {@link Grid}.
 *
 * @author Centril<twingoow@gmail.com> / Mazdak Farrokhzad.
 * @version 1.0
 * @since Nov 5, 2013
 */
public class ArrayGrid<T> extends AbstractGrid<T> {
	protected T[] g;
	protected Dimension dim;

	protected transient int firstEmptyIndex;
	protected transient int lastEmptyIndex;

	/**
	 * Constructs an ArrayGrid with dimension.
	 *
	 * @param w the width part of dimension.
	 * @param h the height part of dimension.
	 * @throws java.lang.IllegalArgumentException
	 * If the cross product of the 2 first dimension axes lower than 1.
	 */
	public ArrayGrid( int w, int h ) {
		super( w, h );
	}

	/**
	 * Constructs an ArrayGrid with dimension.
	 *
	 * @param dimension the dimension of the grid.
	 * @throws java.lang.IllegalArgumentException
	 * If the cross product of the 2 first dimension axes lower than 1.
	 */
	public ArrayGrid( Dimension dimension ) {
		super( dimension );
	}

	/**
	 * Constructs an ArrayGrid of width & height size.<br/>
	 * The cells will be set to grid, make sure that <code>size(grid) = width * height</code>
	 *
	 * @param grid the cells to use, not copied.
	 * @param dim the Dimension containing width & height information.
	 */
	public ArrayGrid( T[] grid, Dimension dim ) {
		this ( dim );
		this.g = grid;

		this.findEmptyIndex();
	}

	/**
	 * Constructs a ArrayGrid of width & height size.
	 *
	 * @param clazz the Class object for type T.
	 * @param dim the Dimension containing width & height information.
	 */
	public ArrayGrid( Class<T> clazz, Dimension dim ) {
		this( ReflectionUtil.makeArray( clazz, dim.cross( 1 ) ), dim );
		this.initEmptyIndexes();
	}

	/**
	 * Copy constructor.
	 *
	 * @param rhs the other ArrayGrid.
	 */
	public ArrayGrid( ArrayGrid<T> rhs ) {
		this ( rhs.dim() );

		this.g = rhs.g.clone();
		this.firstEmptyIndex = rhs.firstEmptyIndex;
		this.lastEmptyIndex = rhs.lastEmptyIndex;
	}

	/**
	 * Makes a copy of rhs.
	 *
	 * @param rhs the other grid.
	 * @param cpyArr copy the contents of grid, or just dimensions?
	 * @return the new grid.
	 */
	public static <T> ArrayGrid<T> cpy( ArrayGrid<T> rhs, boolean cpyArr ) {
		return cpyArr ? new ArrayGrid<T>( rhs ) : new ArrayGrid<T>( ReflectionUtil.arrayClass( rhs.g ), rhs.dim );
	}

	@Override
	public int size() {
		return this.g.length;
	}

	@Override
	public T get( int index ) {
		indexCheck( index );
		return elemData( index );
	}

	@Override
	public T set( int index, T val ) {
		if ( val == null ) {
			return this.remove( index );
		}

		T old = setElem( index, val );

		if ( old == null && index == this.firstEmptyIndex ) {
			if ( index == this.lastEmptyIndex ) {
				this.firstEmptyIndex = this.lastEmptyIndex = this.size();
			} else {
				// find first empty slot.
				for ( int i = index + 1; i <= this.lastEmptyIndex; ++i ) {
					if ( this.indexEmpty( i ) ) {
						this.firstEmptyIndex = i;
					}
				}
			}
		}

		return old;
	}

	@Override
	public T remove( int index ) {
		T old = setElem( index, null );

		if ( old == null ) {
			if ( index < this.firstEmptyIndex ) {
				this.firstEmptyIndex = index;

				if ( this.isFull() ) {
					this.lastEmptyIndex = index;
				}
			} else if ( index > this.lastEmptyIndex ) {
				this.lastEmptyIndex = index;
			}
		}

		return old;
	}

	@Override
	public Object[] toArray() {
		return this.g.clone();
	}

	@Override
	@SuppressWarnings( "unchecked" )
	public <E> E[] toArray( E[] a ) {
		int size = this.size();

		if ( a.length < size ) {
			// Make a new array of a's runtime type, but my contents:
			return (E[]) Arrays.copyOf( this.g, size, a.getClass() );
		}

		System.arraycopy( this.g, 0, a, 0, size );
		if ( a.length > size ) {
			a[size] = null;
		}
		return a;
	}

	@Override
	public boolean removeAll( Collection<?> c ) {
		return remove( c, true );
	}

	@Override
	public boolean retainAll( Collection<?> c ) {
		return remove( c, false );
	}

	@Override
	public void clear() {
		for ( int i = this.size() - 1; i >= 0; --i ) {
			setData( i, null );
		}

		this.initEmptyIndexes();
	}

	@Override
	protected int firstEmptyIndex() {
		return this.firstEmptyIndex;
	}

	@Override
	protected int lastEmptyIndex() {
		return this.lastEmptyIndex;
	}

	protected void initEmptyIndexes() {
		this.firstEmptyIndex = 0;
		this.lastEmptyIndex = this.size() - 1;
	}

	protected boolean remove( Collection<?> c, boolean whenRemove ) {
		boolean modified = false;

		int size = this.size();
		for ( int i = 0; i < size; i++) {
			if ( c.contains( elemData( i ) ) == whenRemove ) {
				this.remove( i );
				modified = true;
			}
		}

		return modified;
	}

	protected void findEmptyIndex() {
		int size = this.size();

		// Find first empty index.
		this.firstEmptyIndex = size;
		for ( int i = 0; i < size; ++i ) {
			if ( indexEmpty( i ) ) {
				this.firstEmptyIndex = i;
				break;
			}
		}

		// Find last empty index (unless there wasn't a first).
		if ( this.firstEmptyIndex < size ) {
			for ( int i = size - 1; i <= this.firstEmptyIndex; ++i ) {
				if ( indexEmpty( i ) ) {
					this.lastEmptyIndex = i;
					break;
				}
			}
		}
	}

	protected boolean indexEmpty( int index ) {
		return elemData( index ) == null;
	}

	protected T setElem( int index, T data ) {
		indexCheck( index );
		T old = elemData( index );
		setData( index, data );
		return old;
	}

	protected void setData( int index, T data ) {
		this.g[index] = data;
	}

	protected T elemData( int index ) {
		return this.g[index];
	}

}