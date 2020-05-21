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
	double angle =35;
	boolean final_maneuver = false;
	System.out.println("Time:          Alt :                HV:               VV:                Angle:                      Mass:                Engine power:               Fuel:");
	System.out.println(seconds+"  ,  "+altitude+"  ,  "+beresheet.getHV() +"  ,  "+beresheet.getVV()+"  ,  "+angle+"  ,  "+beresheet.getCurrent_weight()/moon.GRAVITY+"  ,  "+engine_power+"  ,  " + beresheet.getGas_tank());
	
	while(altitude > 0) {
		timer_task.run();
	
	
	
		// ---- Control section  -----
		
		//engine power
		double engine_force = engine_power * beresheet.ENGINES_FORCE; // N
		
		if(beresheet.getGas_tank() <= 0) {                                            // If there is no fuel left - shut down the engine.
			//System.out.println("Empty tank");
			empty_tank = true;
			engine_force = 0;
			engine_power = 0;
		}
				
		//current fuel mass
		beresheet.setGas_tank(beresheet.getGas_tank() - engine_power * beresheet.TOTAL_BURN); //kg
		
		// current mass
		beresheet.setCurrent_weight(beresheet.SPACECRAFT_WEIGHT + beresheet.getGas_tank() ); //kg
		
		// *acceleration*
		double mass = moons_physics.calculate_mass(beresheet.getCurrent_weight()); // W = m * g         
		double engine_acc =  moons_physics.calculate_acceleration(engine_force , mass);// a = f / m
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
		if(altitude > 1000 || angle != 90) {
			if(beresheet.getHV() > 0) 
				angle += 0.03;

		}
		  
		
		// HV = 0 maneuver   -  When horizontal velocity reach to [2.5 , -2.5] m/s there is no need to reduce the horizontal anymore.
		//Therefore we will turn the spacecraft 90 degree so the engine will break the vertical speed only.
		if(beresheet.getHV() < 2.5 && beresheet.getHV() > -2.5) {
			angle = 90;
			if(!final_maneuver)
			   engine_power -= 0.2;
			final_maneuver = true;
	}
		
		// So the engine won't push to hard on the vertical axis.
		if(beresheet.getVV() < 0) {
			engine_power -= 0.1;
		}
		
		
		
		// We separate the landing process into three phases. 
		//first - reduce the horizontal speed to approximately  0 m/s , second - reduce altitude from 17000m , third - below 1500m
		// we will maintain acceleration of 1.9 m/s^2 on the horizontal axis.
		
		if(beresheet.getHV() > 2.5 && angle != 90 && !empty_tank) {
			if(acc_x < 1.9 &&  acc_y > 0 && engine_power < 1)
				engine_power += 0.003;
			if(acc_x > 1.9 && engine_power > 0)
				if(beresheet.getVV() < 0)
				  engine_power -= 0.003;	
			
				
		}
	
		//phase 2
		// close to reach the surface 
		else if((altitude < 17000 || angle == 90) && altitude > 1500 ){ //18k
			if(!empty_tank)
			{
				if(beresheet.getVV() > 50 && engine_power < 1)
					engine_power += 0.005;
				if(beresheet.getVV() < 30 && engine_power > 0)
					engine_power -= 0.005;	
			}
			
		}
		//phase 3 
		
		else {
			if(acc_y > 0 && beresheet.getVV() > 5 && angle == 90 && engine_power < 1)
				engine_power += 0.035; 	
			else if(acc_y < 0 && beresheet.getVV() < 0 && angle == 90 && engine_power > 0 )
				engine_power -= 0.035 ;
		}
		

		
		//Output :
		if(seconds%10 == 0) {
			System.out.println(seconds+"  ,  "+altitude+"  ,  "+beresheet.getHV() +"  ,  "+beresheet.getVV()+"  ,  "+angle+"  ,  "+mass+"  ,  "+engine_power + "  ,  " + beresheet.getGas_tank());
		    System.out.println();
		}
		if(altitude < 0) {
			System.out.println(seconds+"  ,  "+altitude+"  ,  "+beresheet.getHV() +"  ,  "+beresheet.getVV()+"  ,  "+angle+"  ,  "+mass+"  ,  "+engine_power+"  ,  " + beresheet.getGas_tank());
		    break;
		}
			synchronized(this) {
		try {
			// *If you wish to run the simulation faster please reduce the wait delay*
			wait(1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		}
	}
	
	System.out.println("Mission complete");
	timer_task.cancel();

	}
	
	 
}


