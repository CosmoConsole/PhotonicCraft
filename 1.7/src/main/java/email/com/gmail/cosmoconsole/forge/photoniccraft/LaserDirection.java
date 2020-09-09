package email.com.gmail.cosmoconsole.forge.photoniccraft;

public enum LaserDirection {
	UP, DOWN, NORTH, SOUTH, WEST, EAST;
	
	public LaserDirection inverse() {
		return new LaserDirection[]{DOWN, UP, SOUTH, NORTH, EAST, WEST}[this.ordinal()];
	}
	public LaserDirection left() {
		return new LaserDirection[]{WEST, EAST, UP, DOWN, NORTH, SOUTH}[this.ordinal()];
	}
	public LaserDirection right() {
		return this.left().inverse();
	}
}
