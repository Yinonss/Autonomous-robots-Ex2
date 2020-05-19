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
			    // Your database code h ere
				seconds++;
				
			  }
			};
	}

	

	public void start() {
    // Parameters:
	double engine_power = 0.8; // [0,1] engine use
	double angle =70;
	boolean final_maneuver = false;
	System.out.println("time[sec] : "+seconds+ " , alt : "+altitude + " , vv : "+beresheet.getVV()+" , vh : "+beresheet.getHV());
	while(altitude > 0) {
		timer_task.run();
	// We separate the landing process into two phases. 
		//first - altitude above 2000m , second - below 2000m
		// we will maintain speed of 20-25 m/s
		if(altitude > 1000 && angle != 90 && !empty_tank) {
			if(beresheet.getVV() > 15 && engine_power < 1)
				engine_power += 0.005;
			if(beresheet.getVV() < 10 && engine_power > 0)
				engine_power -= 0.005;	
			
			if(angle < 90 && beresheet.getHV() < 0)
				angle += 10;
				
		}
		
	//	else if(altitude > 1900 && altitude < 2000)
		//	engine_power = 0.97;
		
		// close to reach the surface 
		else if((altitude < 1000 || angle == 90) && altitude > 100 ){
			/*if(angle > 0 )
				angle--;*/
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
		
		//TODO Fix angle change - temporary using boaz' method.
		
		//engine power
		double engine_force = engine_power * beresheet.ENGINES_FORCE; // N
		if(empty_tank) {
			engine_force = 0;
			engine_power = 0;
		}
				
		//current fuel mass
		beresheet.setGas_tank(beresheet.getGas_tank() - engine_power * beresheet.TOTAL_BURN); //kg
		
		// current mass
		beresheet.setCurrent_weight(beresheet.SPACECRAFT_WEIGHT + beresheet.getGas_tank() ); //kg
		
		// *acceleration*
		double mass = beresheet.getCurrent_weight() / moon.GRAVITY;   // W = m * g
		double engine_acc = engine_force / mass;                      // F = m * a
		double rad_angle = Math.toRadians(angle);

		// Gravity force
		double gravity_force = mass * moon.GRAVITY; // (mass * gravity = Fg
		
		//forces :
		//Y axis:
		double y_force = gravity_force - (engine_force * Math.sin(rad_angle)); // Forces equation
		
		double acc_y = y_force / mass;
		
		beresheet.setVV(beresheet.getVV() + acc_y);
		
		// X axis:
		double x_force = engine_force * Math.cos(rad_angle);
		
		double acc_x = x_force / mass;
		
		beresheet.setHV(beresheet.getHV() - acc_x);
		
		// current altitude
		altitude = altitude - beresheet.getVV();    
		
		//angle change
		if(altitude > 1000)
		  angle -= 0.01;
		
		// HV = 0 maneuver 
		if(beresheet.getHV() < 2.5 && beresheet.getHV() > -2.5 && acc_x < 1) {
			angle = 90;
			if(!final_maneuver)
			   engine_power-=0.1;
			final_maneuver = true;
	}
		
		//if(acc_y < 0 && angle == 90)
			//engine_power-=0.1;
		
		//Output:
		if(seconds%10 == 0) {
			System.out.println("time[sec] : "+seconds+ " , alt : "+altitude + " , vv : "+beresheet.getVV()+" , vh : "+beresheet.getHV());
		    System.out.println("engine power: "+engine_power + " , acc y :"+acc_y+" , acc x : "+acc_x);
		    System.out.println("mass : "+mass+" , engine_acc : "+engine_acc + " , angle : "+angle);
		    System.out.println("gravity force : "+gravity_force+" , engine force : "+engine_force);
		    System.out.println();
		}
		synchronized(this) {
		try {
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


