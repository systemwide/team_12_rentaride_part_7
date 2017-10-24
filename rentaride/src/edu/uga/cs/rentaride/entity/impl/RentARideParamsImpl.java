import edu.uga.cs.rentaride.RARException;
import edu.uga.cs.rentaride.entity.RentARideParams;

public class RentARideParamsImpl
	implements RentARideParams
{
	private long		id;
	private int 		membershipPrice;
	private int		lateFee;
	
	
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
	public int getMembershipPrice() {
		// TODO Auto-generated method stub
		return membershipPrice;
	}

	@Override
	public void setMembershipPrice(int membershipPrice) throws RARException {
		// TODO Auto-generated method stub
		this.membershipPrice = membershipPrice;
	}

	@Override
	public int getLateFee() {
		// TODO Auto-generated method stub
		return lateFee;
	}

	@Override
	public void setLateFee(int lateFee) throws RARException {
		// TODO Auto-generated method stub
		this.lateFee = lateFee;
	}
	
}// end class RentARideParamsImpl