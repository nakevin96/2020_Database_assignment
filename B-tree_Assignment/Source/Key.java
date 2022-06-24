


public class Key{
	/*
	 * @param key
	 * BplusTree에 들어갈 Key값 (INTEGER)
	 * 
	 * @param value
	 * Key와 연동된 value값 여기서는 integer이지만 실제로는 key값과 연동되어있는 데이터주소가들어있다.
	 */
	
	private int key;
	private int value;
	
	//Key class의 생성자
	public Key(int key,int value) {
		this.key=key;
		this.value=value;
	}
	//nonLeafNode에서 사용할 생성자
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
