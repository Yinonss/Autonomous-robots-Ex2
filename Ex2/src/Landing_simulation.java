import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

public class Landing_simulation {
    
	private double altitude; //m
	private double total_speed; // m/s
	private Timer timer;
	private TimerTask timer_task;
	private Spacecraft beresheet;
	private Moon moon;
	private double seconds;
	
	public Landing_simulation(Spacecraft _beresheet , Moon _moon ) {
		
		this.beresheet = _beresheet;
		this.moon = _moon;
		this.altitude = 30000;
		this.total_speed = 1700; 
		this.seconds = 0;
		timer = new Timer();
		timer_task = new TimerTask() {
			public void run() {
			    // Your database code here
				seconds++;
				System.out.println(seconds);
			  }
			};
	}
	/**This algorithm simulates the landing process. We use PID control algorithm to make sure the spacecraft
	 * will reach the moon's surface safely.
	 * 
	 * First we need to turn the main engine opposite to the falling direction.
	 * 
	 * Then we'll use the main engine to slow down the spacecraft and the others will balance the ship.
	 * 
	 * We assume that the spaceship is beginning the landing process with 90 degrees to the surface.
	 * 
	 * It will be slow down to 0 m/s horizontal velocity  and then it will free falling from rational height.
	 * 
	 */
	public void PID_algorithm() {
		ArrayList<Double> error = new ArrayList<Double>();
		double current_error = Double.MAX_VALUE;
		
	}
	 
	
}
