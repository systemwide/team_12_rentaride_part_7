import edu.uga.cs.rentaride.RARException;
import edu.uga.cs.rentaride.entity.*;

public class HourlyPriceImpl
	implements HourlyPrice
{

	private long		id;
	private int		maxHrs;
	private int		price;
	VehicleType		vehicleType;
	public HourlyPriceImpl()
	{
		super();
		this.id =		-1;
		this.maxHrs =	-1;
		this.price =		-1;
		vehicleType =	null;
		
	}
	
	public HourlyPriceImpl(long id, int maxHrs, int price, VehicleType vehicleType)
	{
		this.id = id;
		this.maxHrs = maxHrs;
		this.price = price;
		this.vehicleType = vehicleType;
	}
	
	@Override
	public long getId() {
		// TODO Auto-generated method stub
		return id;
	}

	@Override
	public void setId(long id) {
		// TODO Auto-generated method stub
		this.id = id;
	}

	@Override
	public boolean isPersistent() {
		// TODO Auto-generated method stub
		return id == -1;
	}

	@Override
	public int getMaxHours() {
		// TODO Auto-generated method stub
		return maxHrs;
	}

	@Override
	public void setMaxHours(int maxHours) throws RARException {
		// TODO Auto-generated method stub
		this.maxHrs = maxHours;
	}

	@Override
	public int getPrice() {
		// TODO Auto-generated method stub
		return price;
	}

	@Override
	public void setPrice(int price) throws RARException {
		// TODO Auto-generated method stub
		this.price = price;
	}

	@Override
	public VehicleType getVehicleType() {
		// TODO Auto-generated method stub
		return vehicleType;
	}

	@Override
	public void setVehicleType(VehicleType vehicleType) throws RARException {
		// TODO Auto-generated method stub
		this.vehicleType = vehicleType;
	}
	
}