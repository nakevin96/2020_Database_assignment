
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;


public class Node{

	/*
	 * @param keys
	 * Key(key,value)를 저장할 배열
	 * 
	 * @param childNodes
	 * 자식노드들을 저장할 배열
	 * 
	 * 만약 자식노드의 개수가 1개라면 그것은 leafNode
	 * nonLeafNode에서는 키 index 왼쪽 값이 좌측 노드 제일 오른쪽이 마지막 노드
	 * 예를 들어 nonLeafNode의 키값이 1이라면 childNode의 갯수는 무조건 2이기 때문.
	 * 
	 * @param n
	 * 이노드에 포함된 키의 갯수
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
