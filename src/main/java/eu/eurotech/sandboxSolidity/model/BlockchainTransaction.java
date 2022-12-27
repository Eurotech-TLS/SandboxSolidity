package eu.eurotech.sandboxSolidity.model;

public class BlockchainTransaction {
	
	private String id;
	private int fromId;
	private int toId;
	private long value;
	private boolean accepted;
	
	
	public BlockchainTransaction() {
		super();
	}


	public BlockchainTransaction(int formId, int toId, long value) {
		//super();
		System.out.println("====================================");
		this.fromId = formId;
		this.toId = toId;
		this.value = value;
	}


	public String getId() {
		return id;
	}


	public void setId(String id) {
		this.id = id;
	}


	public int getFromId() {
		return fromId;
	}


	public void setFromId(int formId) {
		this.fromId = formId;
	}


	public int getToId() {
		return toId;
	}


	public void setToId(int toId) {
		this.toId = toId;
	}


	public long getValue() {
		return value;
	}


	public void setValue(long value) {
		this.value = value;
	}


	public boolean isAccepted() {
		return accepted;
	}


	public void setAccepted(boolean accepted) {
		this.accepted = accepted;
	}


	@Override
	public String toString() {
		return "BlockchainTransaction [id=" + id + ", formId=" + fromId + ", toId=" + toId + ", value=" + value
				+ ", accepted=" + accepted + "]";
	}
	
	

}
