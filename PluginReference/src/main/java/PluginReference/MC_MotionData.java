package PluginReference;

/** 
 * Motion data for an Entity
 */ 			
public class MC_MotionData
{
	public double xMotion = 0;
	public double yMotion = 0;
	public double zMotion = 0;
	public double fallDistance = 0;
	public boolean onGround = true;
	public boolean inWater = false;
	
	  public String toString()
	  {
		  return String.format("[motion=(%.2f, %.2f, %.2f), fallDist=%.2f, onGround=%s, inWater=%s]", xMotion, yMotion, zMotion, fallDistance, onGround+"", inWater+"");
	  }
	
}
