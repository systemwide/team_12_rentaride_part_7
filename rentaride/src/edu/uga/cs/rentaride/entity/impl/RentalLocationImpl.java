import edu.uga.cs.rentaride.RARException;
import edu.uga.cs.rentaride.entity.RentalLocation;

public class RentalLocationImpl
	implements RentalLocation
{
	private long 		id;
	private String 		name;
	private String 		address;
	private int			capacity;	
	
	
	public RentalLocationImpl()
	{
		super();
		this.id =		-1;
		this.name = 		null;
		this.address = 	null;
		this.capacity =	-1;
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
		return id != -1;
	}

	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return name;
	}

	@Override
	public void setName(String name) throws RARException {
		// TODO Auto-generated method stub
		this.name = name;
	}

	@Override
	public String getAddress() {
		// TODO Auto-generated method stub
		return address;
	}

	@Override
	public void setAddress(String address) {
		// TODO Auto-generated method stub
		this.address = address;
	}

	@Override
	public int getCapacity() {
		// TODO Auto-generated method stub
		return capacity;
	}

	@Override
	public void setCapacity(int capacity) throws RARException {
		// TODO Auto-generated method stub
		this.capacity = capacity;
	}
	
}