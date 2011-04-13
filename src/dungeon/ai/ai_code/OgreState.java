package dungeon.ai.ai_code;

public class OgreState {
	private double ogreHealth;
	private double ogreEnergy;
	private double enemyHealth;
	private double enemyDistance;
	
	public double getOgreHealth() {
		return ogreHealth;
	}
	public void setOgreHealth(double ogreHealth) {
		this.ogreHealth = ogreHealth/100;
	}
	public double getOgreEnergy() {
		return ogreEnergy;
	}
	public void setOgreEnergy(double ogreEnergy) {
		this.ogreEnergy = ogreEnergy/100;
	}
	public double getEnemyHealth() {
		return enemyHealth;
	}
	public void setEnemyHealth(double enemyHealth) {
		this.enemyHealth = enemyHealth/100;
	}
	public double getEnemyDistance() {
		return enemyDistance;
	}
	public void setEnemyDistance(double enemyDistance) {
		this.enemyDistance = enemyDistance;
	}
	public void print() {
		System.out.println("oldState="+
		getOgreHealth()+" "+
		getOgreEnergy()+" "+
		getEnemyHealth()+" "+
		getEnemyDistance()+" ");
	}
	public OgreState(double ogreHealth, double ogreEnergy, double enemyHealth,
			double enemyDistance) {
		this.ogreHealth = ogreHealth;
		this.ogreEnergy = ogreEnergy;
		this.enemyHealth = enemyHealth;
		this.enemyDistance = enemyDistance;
	}
	public OgreState(double ogreEnergy, double enemyDistance) {
		this.ogreHealth = 0;
		this.ogreEnergy = ogreEnergy;
		this.enemyHealth = 0;
		this.enemyDistance = enemyDistance;
	}	
	public double[] toArray(){
		double[] array = new double[4];
		array[0] = ogreHealth;
		array[1] = ogreEnergy;
		array[2] = enemyHealth;
		array[3] = enemyDistance;
		return array;
	}
}
