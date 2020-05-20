/**
 * This class represent a bunch of physics formulas regarding forces and free fall.
 * @author yinon
 *
 */
public class Physics {
	
	private double gravity;
	
	public Physics(double gravity) {
		this.gravity = gravity;
	}
	/**
	 * This function calculate the mass of the object.
	 * Mass = Weight / Gravity 
	 * @param weight The current weight of the object
	 * @return
	 */
	public double calculate_mass(double weight) {
		return weight / gravity;
	}
	/**
	 * Calculates object acceleration by using its applied force and mass.
	 * (Acceleraion = Force / Mass)
	 * @param force
	 * @param mass
	 * @return acceleration
	 */
	public double calculate_acceleration(double force, double mass) {
		return force / mass;
	}
	/**
	 * This function calculate the force that the plant / moon applied on object.
	 * Force (g) = Mass * Gravity
	 * @param mass
	 * @return gravity force
	 */
	public double calculate_gravity_force(double mass) {
		return mass * gravity;
	}
	/**
	 * 
	 * Force aquation - according to Newton second law the dirction which object is moving is a consequence 
	 * of the sum of the forces applied on it.
	 * https://en.wikipedia.org/wiki/Newton%27s_laws_of_motion
	 * 
	 * In this function we calculate the total force accounting the forces which applied on it.
	 * @param left_force
	 * @param right_force
	 * @param rightPositive If we define right to be the plus side of the graph.
	 * @return
	 */
	public double forces_equation_x(double left_force ,double right_force, boolean rightPositive) {
		if(rightPositive)
			return right_force - left_force;
		else
			return left_force - right_force;
	}
	
	/**
	 * Force aquation - according to Newton second law the dirction which object is moving is a consequence 
	 * of the sum of the forces applied on it.
	 * https://en.wikipedia.org/wiki/Newton%27s_laws_of_motion
	 * 
	 * In this function we calculate the total force accounting the forces which applied on it.
	 * @param up_force
	 * @param down_force
	 * @param upPositive If we define up to be the plus side of the graph.
	 * @return total force
	 */
	public double forces_equation_y(double up_force ,double down_force, boolean upPositive) {
		if(upPositive)
			return up_force - down_force;
		else
			return down_force - up_force;
	}

}
