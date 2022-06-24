


public class Key{
	/*
	 * @param key
	 * BplusTree�� �� Key�� (INTEGER)
	 * 
	 * @param value
	 * Key�� ������ value�� ���⼭�� integer������ �����δ� key���� �����Ǿ��ִ� �������ּҰ�����ִ�.
	 */
	
	private int key;
	private int value;
	
	//Key class�� ������
	public Key(int key,int value) {
		this.key=key;
		this.value=value;
	}
	//nonLeafNode���� ����� ������
	public Key(int key) {
		this.key=key;
	}
	//getter setter
	public int getKey() {
		return key;
	}
	public void setKey(int ikey) {
		key=ikey;
	}
	
	public int getValue() {
		return value;
	}
	public void setValue(int ivalue) {
		value=ivalue;
	}
	
	//String
	public String toString() {
		return "("+key+" , "+value+")";
	}

}
