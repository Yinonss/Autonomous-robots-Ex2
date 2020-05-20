import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

public class Landing_simulation {
    
	private double altitude; //m
	private Timer timer;
	private TimerTask timer_task;
	private Spacecraft beresheet;
	private Moon moon;
	private int seconds;
	private boolean empty_tank;
	
	public Landing_simulation(Spacecraft _beresheet , Moon _moon ) {
		
		this.beresheet = _beresheet;
		this.beresheet.setHV(1701.6);
		this.beresheet.setVV(43);
		this.moon = _moon;
		this.altitude = 30000;
		this.seconds = 0;
		this.empty_tank = false;
		timer = new Timer();
		timer_task = new TimerTask() {
			public void run() {
				seconds++;
				
			  }
			};
	}

	

	public void start() {
	Physics moons_physics = new Physics(moon.GRAVITY);
    // Parameters:
	double engine_power = 0.8; // [0,1] engine use
	double angle =70;
	boolean final_maneuver = false;
	System.out.println("time[sec] : "+seconds+ " , alt : "+altitude + " , vv : "+beresheet.getVV()+" , vh : "+beresheet.getHV());
	while(altitude > 0) {
		timer_task.run();
	
		// We separate the landing process into three phases. 
		//first - altitude above 1000m , second - below 1000m , third - below 100m
		// we will maintain speed of 10-15 m/s
		
		if(altitude > 1000 && angle != 90 && !empty_tank) {
			if(beresheet.getVV() > 15 && engine_power < 1)
				engine_power += 0.005;
			if(beresheet.getVV() < 10 && engine_power > 0)
				engine_power -= 0.005;	
			
			if(angle < 90 && beresheet.getHV() < 0)
				angle += 10;
				
		}
		
		
		// close to reach the surface 
		else if((altitude < 1000 || angle == 90) && altitude > 100 ){
			if(!empty_tank)
			{
				if(beresheet.getVV() > 10 && engine_power < 1)
					engine_power += 0.002;
				if(beresheet.getVV() < 0 && engine_power > 0)
					engine_power -= 0.002;	
			}
			
		}
		
		else {
			if(beresheet.getVV() > 2.5 && beresheet.getVV() > 0 && angle == 90 && engine_power < 1)
				engine_power += 0.32; 	
			else if(beresheet.getVV() < -2.5 && beresheet.getVV() < 0 && angle == 90 && engine_power > 0 )
				engine_power -= 0.32;
		}
		
	
		// ---- Control section  -----
		
		//engine power
		double engine_force = engine_power * beresheet.ENGINES_FORCE; // N
		
		if(empty_tank) {                                            // If there is no fuel left - shut down the engine.
			System.out.println("Empty tank");
			engine_force = 0;
			engine_power = 0;
		}
				
		//current fuel mass
		beresheet.setGas_tank(beresheet.getGas_tank() - engine_power * beresheet.TOTAL_BURN); //kg
		
		// current mass
		beresheet.setCurrent_weight(beresheet.SPACECRAFT_WEIGHT + beresheet.getGas_tank() ); //kg
		
		// *acceleration*
		double mass = moons_physics.calculate_mass(beresheet.getCurrent_weight()); // W = m * g         
		double engine_acc =  moons_physics.calculate_acceleration(engine_force , mass);// F = m * a
		double rad_angle = Math.toRadians(angle);

		// Gravity force
		double gravity_force = moons_physics.calculate_gravity_force(mass); //mass * gravity = Fg
		
		//forces :
		//Y axis:
		double y_force = moons_physics.forces_equation_y(engine_force * Math.sin(rad_angle) , gravity_force , false);
		double acc_y = moons_physics.calculate_acceleration(y_force, mass);
		
		beresheet.setVV(beresheet.getVV() + acc_y);                           // New vertcial velocity
		
		// X axis:
		double x_force = moons_physics.forces_equation_x(0, engine_force * Math.cos(rad_angle), true);
		double acc_x = moons_physics.calculate_acceleration(x_force , mass);
		
		beresheet.setHV(beresheet.getHV() - acc_x);                           //New horizontal velocity
		
		// current altitude
		altitude = altitude - beresheet.getVV();    
		
		//angle change
		if(altitude > 1000)
		  angle -= 0.01;
		
		// HV = 0 maneuver   -  When horizontal velocity reach to [2.5 , -2.5] m/s there is no need to reduce the horizontal anymore.
		//Therefore we will turn the spacecraft 90 degree so the engine will break the vertical speed only.
		if(beresheet.getHV() < 2.5 && beresheet.getHV() > -2.5 && acc_x < 1) {
			angle = 90;
			if(!final_maneuver)
			   engine_power-=0.1;
			final_maneuver = true;
	}
		

		
		//Output:
		if(seconds%10 == 0) {
			System.out.println("time: "+seconds+ " , alt : "+altitude + " , vv : "+beresheet.getVV()+" , vh : "+beresheet.getHV());
		    System.out.println("engine power: "+engine_power + " , acc y :"+acc_y+" , acc x : "+acc_x);
		    System.out.println("mass : "+mass+" , fuel"+beresheet.getGas_tank()+" , engine_acc : "+engine_acc + " , angle : "+angle);
		    System.out.println("gravity force : "+gravity_force+" , engine force : "+engine_force);
		    System.out.println();
		}
		synchronized(this) {
		try {
			// *If you wish to run the simulation faster please reduce the wait delay*
			wait(10);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		}
	}
	System.out.println("time[sec] : "+seconds+ " , alt : "+altitude + " , vv : "+beresheet.getVV()+" , vh : "+beresheet.getHV());
	System.out.println("Mission complete");
	timer_task.cancel();

	}
	
	 
}


