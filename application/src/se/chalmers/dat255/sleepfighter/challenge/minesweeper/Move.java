package se.chalmers.dat255.sleepfighter.challenge.minesweeper;
import org.bridj.FlagSet;
import org.bridj.IntValuedEnum;
import org.bridj.Pointer;
import org.bridj.ann.Constructor;
import org.bridj.ann.Field;
import org.bridj.ann.Library;
import org.bridj.ann.Name;
import org.bridj.cpp.CPPObject;
import se.chalmers.dat255.sleepfighter.challenge.minesweeper.SeChalmersDat255SleepfighterChallengeMinesweeperLibrary.ClickType;
/**
 * \brief This class represents a move that the user can make in a game of minesweeper. <br>
 * * When the user wants to make a move then they can just use this to make it happen. This way we can<br>
 * pass moves into the game whether they come from an AI or a computer. Please note that this class<br>
 * should be immutable.<br>
 * <i>native declaration : line 237</i><br>
 * This file was autogenerated by <a href="http://jnaerator.googlecode.com/">JNAerator</a>,<br>
 * a tool written by <a href="http://ochafik.com/">Olivier Chafik</a> that <a href="http://code.google.com/p/jnaerator/wiki/CreditsAndLicense">uses a few opensource projects.</a>.<br>
 * For help, please visit <a href="http://nativelibs4java.googlecode.com/">NativeLibs4Java</a> or <a href="http://bridj.googlecode.com/">BridJ</a> .
 */
@Library("se.chalmers.dat255.sleepfighter.challenge.minesweeper") 
public class Move extends CPPObject {
	/** Original signature : <code>Move(Position, ClickType)</code> */
	@Name("Move") 
	@Constructor(0) 
	public move(Position pos, IntValuedEnum<ClickType > clickType) {
		super((Void)null, 0, pos, clickType);
	}
	/**
	 * Original signature : <code>Position getPosition()</code><br>
	 * <i>native declaration : line 243</i>
	 */
	native public Position getPosition();
	/**
	 * Original signature : <code>ClickType getClickType()</code><br>
	 * <i>native declaration : line 246</i>
	 */
	public IntValuedEnum<ClickType > getClickType() {
		return FlagSet.fromValue(getClickType$2(), ClickType.class);
	}
	@Name("getClickType") 
	protected native int getClickType$2();
	/** C type : Position */
	@Field(0) 
	public Position position() {
		return this.io.getNativeObjectField(this, 0);
	}
	/** C type : ClickType */
	@Field(1) 
	public IntValuedEnum<ClickType > clickType() {
		return this.io.getEnumField(this, 1);
	}
	public Move() {
		super();
	}
	public Move(Pointer pointer) {
		super(pointer);
	}
}
