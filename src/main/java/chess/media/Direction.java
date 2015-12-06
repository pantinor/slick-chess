package chess.media;

public enum Direction {

    NORTH(2),
    NORTHEAST(3),
    EAST(4),
    SOUTHEAST(5),
    SOUTH(6),
    SOUTHWEST(7),
    WEST(0),
    NORTHWEST(1);

    private int index;

    private Direction(int index) {
        this.index = index;
    }

    public int getIndex() {
        return index;
    }

    public static Direction forIndex(int index) {

        Direction direction = NORTH;
        for (Direction d : Direction.values()) {
            if (d.getIndex() == index) {
                direction = d;
            }
        }

		//System.out.println("DIR="+direction+" : "+index);
        return direction;
    }

}
