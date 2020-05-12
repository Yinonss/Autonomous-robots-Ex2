
public class runSimulation {

	public static void main(String[] args)  {
		// TODO Auto-generated method stub

		Spacecraft beresheet = new Spacecraft();
		Moon moon = new Moon();
		Landing_simulation land=new Landing_simulation(beresheet ,moon);
		land.start();
	}

}
