
import java.util.*;
import java.io.*;

public class Main {

	public static void main(String[] args) {
		//.csv���� ����
				List<String[]> list;
				
				List<String> order = new ArrayList<>();
				for(int i=0;i<args.length;i++) {
					order.add(args[i]);
				}
				
				try {
					switch(order.get(0)) {
					case "-c":
						if(order.size() ==3) {
							int tmpDegree= Integer.parseInt(order.get(2));
							BplusTree bplusTreeImplementation = new BplusTree(tmpDegree);
							writeBplusToFile(order.get(1),bplusTreeImplementation);
						}else {
							System.out.println("Wrong use with -c parameter");
							System.out.println("-c [The file has BplusTreeData] [(INTEGER)The degree of Tree]");
						}
						break;
					case "-i":
						if(order.size() ==3) {
							list = readFromCsvFile(order.get(2));
							BplusTree bplusI =makeBplusFromFile(order.get(1));
							int keyI,valueI;
							String[] tmpI;
							for(int i=0;i<list.size();i++) {
								tmpI=list.get(i);
								keyI=Integer.parseInt(tmpI[0]);
								valueI=Integer.parseInt(tmpI[1]);
								bplusI.insert(keyI, valueI);
							}
							writeBplusToFile(order.get(1),bplusI);
						}else {
							System.out.println("Wrong use with -i parameter");
							System.out.println("-i [The file, contain BplusTreeData] [The file, contain input data (.csv)only ]");
						}
						
						break;
					case "-d":
						if(order.size() ==3) {
							list = readFromCsvFile(order.get(2));
							BplusTree bplusD =makeBplusFromFile(order.get(1));
							int keyD;
							String[] tmpD;
							for(int i=0;i<list.size();i++) {
								tmpD=list.get(i);
								keyD=Integer.parseInt(tmpD[0]);
								bplusD.delete(keyD);
							}
							writeBplusToFile(order.get(1),bplusD);
						}else {
							System.out.println("Wrong use with -d parameter");
							System.out.println("-d [The file, contain BplusTreeData] [The file, contain delete data (.csv)only ]");
						}
						
						break;
					case "-s":
						if(order.size()==3) {
							long start = System.currentTimeMillis();
							int searchKey= Integer.parseInt(order.get(2));
							BplusTree bplusS = makeBplusFromFile(order.get(1));
						
							List<String> searchList = bplusS.search(searchKey);
							
							Iterator<String> searchIt = searchList.iterator();
							while(searchIt.hasNext()) {
								System.out.println(searchIt.next());
							}
							long end = System.currentTimeMillis();
							System.out.println("Ž�� �ڵ� ����ð� : "+(end-start)/1000.0);
						}else {
							System.out.println("Wrong use with -s parameter");
							System.out.println("-s [The file, contain BplusTreeData] [the key you want find (int type)]");
						}
						
						break;
					case "-r":
						if(order.size()==4) {
							int rangeKey1 = Integer.parseInt(order.get(2));
							int rangeKey2 = Integer.parseInt(order.get(3));
							if(rangeKey1 > rangeKey2) {
								int tmp = rangeKey2;
								rangeKey2 = rangeKey1;
								rangeKey1 = tmp;
							}
							BplusTree bplusR = makeBplusFromFile(order.get(1));
							List<Key> rangeList = bplusR.search(rangeKey1,rangeKey2);
							Iterator<Key> rangeIt = rangeList.iterator();
							while(rangeIt.hasNext()) {
								System.out.println(rangeIt.next().toString());
							}
						}else {
							System.out.println("Wrong use with -r parameter");
							System.out.println("-r [The file, contain BplusTreeData] [the key you want find start (int type)] [the key you want find end (int type)]");
						}
						
						break;
					default:
						System.out.printf("%s�� ���� ��ɾ �ƴմϴ�.", order);
						break;
					}
					
				}catch(Exception e) {
					e.printStackTrace();
				}	
			return;
	}
	private static List<String[]> readFromCsvFile(String fileName){
		try(BufferedReader reader = new BufferedReader(new FileReader(fileName))){
			List<String[]> list = new ArrayList<>();
			String line = "";
			while((line = reader.readLine())!=null) {
				String[] array = line.split(",");
				list.add(array);
			}
			return list;
		}catch(IOException e) {
			e.printStackTrace();
			return null;
		}
	}
	private static BplusTree makeBplusFromFile(String filename) {
		List<Node> mainNodeList = new ArrayList<>();
		List<Integer> numChilds = new ArrayList<>();
		List<String[]> list = new ArrayList<>();
		int degree=100;
		/*
		 * ���� �о�ͼ� mainNodeList�� numChilds�� ����
		 * degree
		 * * Ű����,Ű1,��1,Ű2,��2,..
			  ���ϵ��尹�� ������ �ݺ� �Է�
			  �Ǿ��ִ� ��������
			  ���ο� ��� ���� numofKey�� Űvalue�� newKey�̿��Ͽ� ����
			  ������ �ش� ��尡 ���� �ε����� ���� numChilds�� ��ġ�� childNode�� ���� ����
		 */
		//���������ִºκ�
		try(BufferedReader reader = new BufferedReader(new FileReader(filename))){
			String line = "";
			while((line = reader.readLine())!=null) {
				String[] array = line.split(",");
				list.add(array);
			}
			reader.close();
		}catch(IOException e) {
			e.printStackTrace();
			return null;
		}
		if(list.size()==0){
			System.out.println("Please make index.dat file by using -c argument");
			System.exit(1);
		}
		if(list.size()==1) {
			//�ƹ� ��尡 ���ٸ�
			degree=Integer.parseInt(list.get(0)[0]);
		}else {
			//��尡 �����Ѵٸ�
			degree=Integer.parseInt(list.get(0)[0]);
			for(int i=1;i<list.size();i++) {
				Node insertedNode = new Node();
				int j;
				
				insertedNode.setN(Integer.parseInt(list.get(i)[0]));
				
				for(j=1;j<=2*(insertedNode.getN());j=j+2) {
					insertedNode.getKeys().add(new Key(Integer.parseInt(list.get(i)[j]),Integer.parseInt(list.get(i)[j+1])));
				}
				mainNodeList.add(insertedNode);
				numChilds.add(Integer.parseInt(list.get(i)[j]));
			}
			list.clear();
		}
		//���������ִºκ�
		
		/*
		 * BplusTree �����κ�
		 */
		BplusTree bt = new BplusTree(degree);
		if(mainNodeList.size()!=0) {
			bt.setRoot(mainNodeList.get(0));
			Node tmpNode;
			int useIndex=1;
			int cCount;
			for(int i=0;i<mainNodeList.size()-1;i++) {
				cCount = numChilds.get(i);
				tmpNode = mainNodeList.get(i);
				for(int j=useIndex;j<useIndex+cCount;j++) {
					tmpNode.getChildNodes().add(mainNodeList.get(j));
				}
				if(cCount==0) {
					tmpNode.setR(mainNodeList.get(i+1));
				}
				useIndex +=cCount;
			}
		}
		return bt;
	}


	private static void writeBplusToFile(String filename,BplusTree b) {
		List<Node> pushNodeList = new ArrayList<>();
		Node currNode=b.getRoot();
		int indexForNode=1;
		if(currNode == null) {
			//�ƹ��͵� ���� ���¶�� index.dat�� degree���Է��ϱ�
			//���������ִºκ�
			 try (FileWriter writer = new FileWriter(filename,false)){
			        writer.append(Integer.toString(b.getDegree()));
			        writer.append(System.lineSeparator());
			        writer.flush();
			        writer.close();
			    } catch (IOException e) {
			        e.printStackTrace();
			    }
			//���������ִºκ�
		}
		else {
			pushNodeList.add(currNode);
			while(!currNode.getChildNodes().isEmpty()) {
				//currNode �߰��ϰ� �ڽĳ�� ������� �߰��ϱ�
				for(int i=0;i<currNode.getChildNodes().size();i++) {
					pushNodeList.add(currNode.getChildNodes().get(i));
				}
				//
				currNode = pushNodeList.get(indexForNode);
				indexForNode++;
			}
			/*
			 * pushNodeList�ϼ��Ͽ����� index.dat��
			 * degree�ְ�
			 * Ű����,Ű1,��1,Ű2,��2,..
			 * ���ϵ��尹�� ������ �Է�
			 */
			//���������ִºκ�
			try (FileWriter writer = new FileWriter(filename,false)){
		        writer.append(Integer.toString(b.getDegree()));
		        writer.append(System.lineSeparator());
		        for(int i=0;i<pushNodeList.size();i++) {
		        	writer.append(Integer.toString(pushNodeList.get(i).getN()));
		        	if(pushNodeList.get(i).getN()!=0) {
		        		 writer.append(",");
		        	}
		        	for(int j=0;j<pushNodeList.get(i).getN();j++) {
		        		writer.append(Integer.toString(pushNodeList.get(i).getKeys().get(j).getKey()));
		        		writer.append(",");
		        		writer.append(Integer.toString(pushNodeList.get(i).getKeys().get(j).getValue()));
		        		writer.append(",");
		        	}
		        	writer.append(Integer.toString(pushNodeList.get(i).getChildNodes().size()));
		        	writer.append(System.lineSeparator());
		        }
		        writer.flush();
		        writer.close();
		    } catch (IOException e) {
		        e.printStackTrace();
		    }
			//���������ִºκ�
		}
		return;
	}
	
}
