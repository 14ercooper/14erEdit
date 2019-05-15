package fourteener.worldeditor.worldeditor.selection;

public class SelectionManager {
	private double positionOne[] = {-1.0, -1.0, -1.0};
	private double positionTwo[] = {-1.0, -1.0, -1.0};
	
	// These two variables make the world editing math a bit easier
	private double mostNegativeCorner[] = new double[3];
	private double mostPositiveCorner[] = new double[3];
	
	public boolean updatePositionOne (double x, double y, double z) {
		positionOne[0] = x;
		// Use nested ternary operators to clamp y between 0 and 255
		positionOne[1] = y < 0 ? 0 : y > 255 ? 255 : y;
		positionOne[2] = z;
		recalculateCorners();
		return true;
	}
	
	public boolean updatePositionTwo (double x, double y, double z) {
		positionTwo[0] = x;
		// Use nested ternary operators to clamp y between 0 and 255
		positionTwo[1] = y < 0 ? 0 : y > 255 ? 255 : y;
		positionTwo[2] = z;
		recalculateCorners();
		return true;
	}
	
	// Check both selection positions have been defined (create a valid region)
	public boolean regionDefined () {
		if (positionOne[0] == -1.0 && positionOne[1] == -1.0 && positionOne[2] == -1.0)
			return false;
		if (positionTwo[0] == -1.0 && positionTwo[1] == -1.0 && positionTwo[2] == -1.0)
			return false;
		return true;
	}
	
	// This recalculates the two corners to help make the math a bit easier
	private void recalculateCorners () {
		// Check both selection positions have been defined first
		if (!regionDefined())
			return;
		
		// Set the X, then Y, then Z
		if (positionOne[0] <= positionTwo[0]) {
			mostNegativeCorner[0] = positionOne[0];
			mostPositiveCorner[0] = positionTwo[0];
		} else {
			mostNegativeCorner[0] = positionTwo[0];
			mostPositiveCorner[0] = positionOne[0];
		}
		
		if (positionOne[1] <= positionTwo[1]) {
			mostNegativeCorner[1] = positionOne[1];
			mostPositiveCorner[1] = positionTwo[1];
		} else {
			mostNegativeCorner[1] = positionTwo[1];
			mostPositiveCorner[1] = positionOne[1];
		}
		
		if (positionOne[2] <= positionTwo[2]) {
			mostNegativeCorner[2] = positionOne[2];
			mostPositiveCorner[2] = positionTwo[2];
		} else {
			mostNegativeCorner[2] = positionTwo[2];
			mostPositiveCorner[2] = positionOne[2];
		}
	}
}
