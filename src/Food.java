
public class Food {

	private String name;
	private double rate;

	public Food(String name, double rate) {
		this.name = name;
		this.rate = Math.random() * 5;
		this.rate = Math.round(this.rate * 100) / 100.0;
	}

	public String getName() {
		return name;
	}

	public double getRate() {
		return rate;
	}
}
