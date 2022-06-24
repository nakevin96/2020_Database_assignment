
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;


public class Node{

	/*
	 * @param keys
	 * Key(key,value)�� ������ �迭
	 * 
	 * @param childNodes
	 * �ڽĳ����� ������ �迭
	 * 
	 * ���� �ڽĳ���� ������ 1����� �װ��� leafNode
	 * nonLeafNode������ Ű index ���� ���� ���� ��� ���� �������� ������ ���
	 * ���� ��� nonLeafNode�� Ű���� 1�̶�� childNode�� ������ ������ 2�̱� ����.
	 * 
	 * @param n
	 * �̳�忡 ���Ե� Ű�� ����
	 * 
	 *@param r
	 *right siblingnode;
	 */
	
	private List<Key> keys;
	private List<Node> childNodes;
	private Node r;
	private int n;
	
	public Node() {
		keys = new ArrayList<>();
		childNodes = new ArrayList<>();
		n=0;
		r=null;
	}
	
	//getter setter
	public List<Key> getKeys(){
		return this.keys;
	}
	public void setKeys(List<Key> ikeys) {
		Iterator<Key> it = ikeys.iterator();
		while(it.hasNext()) {
			keys.add(it.next());
		}
	}
	
	public List<Node> getChildNodes(){
		return childNodes;
	}
	public void setChildNodes(List<Node> iNodes) {
		Iterator<Node> it = iNodes.iterator();
		while(it.hasNext()) {
			childNodes.add(it.next());
		}
	}
	public Node getR() {
		return r;
	}
	public void setR(Node r1) {
		r=r1;
	}
	
	public int getN() {
		return n;
	}
	public void setN(int iN){
		 n=iN;
	}
}
