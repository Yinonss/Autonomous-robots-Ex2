
public class Spacecraft {

	// Weights:
	
	public static final double FUEL_WEIGHT = 420;
	public static final double SPACECRAFT_WEIGHT = 165;
	public static final double TOTAL_WEIGHT = FUEL_WEIGHT + SPACECRAFT_WEIGHT; 
	private double current_weight = TOTAL_WEIGHT;
	
	// Engines:
	public static final double MAIN_ENGINE = 430; //N
	public static final double SECOND_ENGINE = 25;
	public static final double ENGINES_FORCE = MAIN_ENGINE + SECOND_ENGINE*8;
	
	// Burn:
	public static final double MAIN_BURN = 0.15; 
	public static final double SECOND_BURN = 0.009;
	public static final double TOTAL_BURN = MAIN_BURN + SECOND_BURN * 8;
	private double gas_tank = 420;
	
	// Velocity:
	private double vertical_velocity;
	private double horizontal_velocity;
	private double angular_velocity;
	
	public double getVV() {
		return this.vertical_velocity;
	}
	public void setVV(double vv) {
		this.vertical_velocity = vv;
	}
	public double getHV() {
		return this.horizontal_velocity;
	}
	public void setHV(double hv) {
		this.horizontal_velocity = hv;
	}
	public double getGas_tank() {
		return gas_tank;
	}
	public void setGas_tank(double gas_tank) {
		this.gas_tank = gas_tank;
	}
	public double getCurrent_weight() {
		return current_weight;
	}
	public void setCurrent_weight(double current_weight) {
		this.current_weight = current_weight;
	}
	
}
