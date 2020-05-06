
public class Spacecraft {

	// Weights:
	
	public static final double FUEL_WEIGHT = 420;
	public static final double SPACECRAFT_WEIGHT = 165;
	public static final double TOTAL_WEIGHT = FUEL_WEIGHT + SPACECRAFT_WEIGHT; 
	
	// Engines:
	public static final double MAIN_ENGINE = 430; //N
	public static final double SECOND_ENGINE = 25;
	public static final double ENGINES_FORCE = MAIN_ENGINE + SECOND_ENGINE*8;
	
	// Burn:
	public static final double MAIN_BURN = 0.15; 
	public static final double SECOND_BURN = 0.009;

	// Velocity:
	private double vertical_velocity;
	private double horizontal_velocity;
	private double angular_velocity;
	
}
