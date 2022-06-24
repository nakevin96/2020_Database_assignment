
import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.swing.JOptionPane;




public class Music {
	Connection admincon;
	
	public static void StartMusic(String n) {
		try {
			String musicLocation =n+".wav";
			File musicPath = new File(musicLocation);
			if(musicPath.exists()) {
				AudioInputStream audioInput = AudioSystem.getAudioInputStream(musicPath);
				Clip clip = AudioSystem.getClip();
				clip.open(audioInput);
				clip.start();
				
				JOptionPane.showMessageDialog(null, "Press OK to stop playing");
				clip.stop();

			}else {
				System.out.println("cant find file");
			}
		}catch(Exception ex){
			ex.printStackTrace();
		}
	}
	
	public static void ShowAllMusicSimple() {
		try {
			Connection admincon = DriverManager.getConnection("jdbc:mariadb://localhost:3307/musicstreaming"
					,"musicuser","musicstreaming");
			Statement st = admincon.createStatement();
			String sql = "SELECT 음악이름, 가수 , 음원관리번호 FROM 음원 ORDER BY 음악이름";
			ResultSet rs = st.executeQuery(sql);
			int i=1;
			while(rs.next()) {
				String title = rs.getString(1);
				String singer = rs.getString(2);
				int musicNum = rs.getInt(3);
				System.out.println("[음원"+i+"]");
				i+=1;
				System.out.println("타이틀:"+title);
				System.out.println("가수:"+singer);
				System.out.println("관리번호:"+musicNum);
				System.out.println();
			}
			rs.close();
			st.close();
			admincon.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static void SearchMusic(char select, String searchingWord) {
		String sql;
		switch(select) {
		case '1'://음악이름
			sql = "SELECT 음악이름, 가수, 장르, count, 앨범이름, 음원관리번호  FROM 음원  WHERE 음악이름 LIKE '%"+searchingWord+"%'";
			break;
		case '2'://가수명
			sql = "SELECT 음악이름, 가수, 장르, count, 앨범이름, 음원관리번호  FROM 음원  WHERE 가수 LIKE '%"+searchingWord+"%' ORDER BY 음악이름";
			break;
		case '3'://장르명
			sql = "SELECT 음악이름, 가수, 장르, count, 앨범이름, 음원관리번호  FROM 음원  WHERE 장르 LIKE '%"+searchingWord+"%' ORDER BY 음악이름";
			break;
		case '4'://앨범명
			sql = "SELECT 음악이름, 가수, 장르, count, 앨범이름, 음원관리번호  FROM 음원  WHERE 앨범명 LIKE'%"+searchingWord+"%' ORDER BY 음악이름";
			break;
		case '7':
			sql  = "SELECT 음악이름, 가수, 장르, count, 앨범이름, 음원관리번호  FROM 음원  ORDER BY 음악이름";
			break;
		case '8':
			sql  = "SELECT 음악이름, 가수, 장르, count, 앨범이름, 음원관리번호  FROM 음원  ORDER BY count DESC";
			break;
		default:
			System.out.println("잘못 입력하셨습니다.");
			return;
		}
		try {
			Connection admincon = DriverManager.getConnection("jdbc:mariadb://localhost:3307/musicstreaming"
					,"musicuser","musicstreaming");
			Statement st = admincon.createStatement();
			ResultSet rs = st.executeQuery(sql);
			int i=1;
			while(rs.next()) {
				String title = rs.getString(1);
				String singer = rs.getString(2);
				String genre = rs.getString(3);
				int play_count = rs.getInt(4);
				String albumName = rs.getString(5);
				int musicNum = rs.getInt(6);
				System.out.println("[음원"+i+"]");
				i+=1;
				System.out.println("타이틀:"+title);
				System.out.println("가수:"+singer);
				System.out.println("장르:"+genre);
				System.out.println("앨범:"+albumName);
				System.out.println("플레이횟수:"+play_count);
				System.out.println("관리번호:"+musicNum);
				System.out.println();
			}
			rs.close();
			st.close();
			admincon.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static void WithdrawlMusic(String control) {
		try {
			Connection admincon = DriverManager.getConnection("jdbc:mariadb://localhost:3307/musicstreaming"
					,"musicuser","musicstreaming");
			Statement st = admincon.createStatement();
			String sql = "DELETE FROM musicstreaming.음원 WHERE 음원관리번호='"+control+"'";
			st.executeUpdate(sql);
			System.out.println("삭제되었습니다.");
			st.close();
			admincon.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	@SuppressWarnings("resource")
	public static void PlayMusic(int num, String id) {
		try {
			Connection admincon = DriverManager.getConnection("jdbc:mariadb://localhost:3307/musicstreaming"
					,"musicuser","musicstreaming");
			Statement st = admincon.createStatement();
			Statement st2 = admincon.createStatement();
			String sql_music_count = "SELECT count FROM musicstreaming.음원 WHERE 음원관리번호 = '"+num+"'";
			String sql_music_count_UPDATE = "UPDATE 음원 set count = count+1 WHERE 음원관리번호 = '"+num+"'";
			String select_playlist = "SELECT ID, play_음원관리번호, p_count"
					+ " FROM musicstreaming.play, musicstreaming.스트리밍구독자"
					+ " WHERE play_구독자주민번호 = 구독자주민번호 AND ID = '"+id+"' AND play_음원관리번호='"+num+"'";
			
			String ssn=null;
			String ssn_search="SELECT 구독자주민번호 FROM musicstreaming.스트리밍구독자 WHERE ID='"+id+"';";
			ResultSet rs = st.executeQuery(ssn_search);
			while(rs.next()) {
				ssn=rs.getString(1);
			}
			
			String make_play="INSERT INTO musicstreaming.play (play_음원관리번호,play_구독자주민번호,p_count)"
					+ " VALUES ("+num+", '"+ssn+"', 1)"; 
			String update_pCount="UPDATE musicstreaming.play set p_count = p_count+1"
					+ " WHERE play_구독자주민번호='"+ssn+"' AND play_음원관리번호='"+num+"'";
			
			
			rs = st.executeQuery(sql_music_count);
			ResultSet rs2 = st2.executeQuery(select_playlist);
			if(rs.next()) {
				rs=st.executeQuery(sql_music_count_UPDATE);
				if(rs2.next()==false) {
					//play에 해당사항이 없으니 create
					rs2 =st2.executeQuery(make_play);
				}else {
					//play에 해당사항이 있으니 update
					rs2 = st2.executeQuery(update_pCount);
				}
				String n= Integer.toString(num);
				StartMusic(n);
				System.out.println("성공적 플레이");
			}else {
				System.out.println("노래번호를 잘못 입력하셨습니다.");
				rs2.close();
				st2.close();
				rs.close();
				st.close();
				admincon.close();
				return;
			}
			rs2.close();
			st2.close();
			rs.close();
			st.close();
			admincon.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	@SuppressWarnings("resource")
	public static void PlayMusicPlaylist(int num, String id, String listName) {
		try {
			Connection admincon = DriverManager.getConnection("jdbc:mariadb://localhost:3307/musicstreaming"
					,"musicuser","musicstreaming");
			Statement st = admincon.createStatement();
			Statement st2 = admincon.createStatement();
			String isInPlayList = "SELECT a_음원관리번호 FROM 추가 WHERE a_플레이리스트명='"+listName+"' AND a_음원관리번호='"+num+"'";
			ResultSet rs =st.executeQuery(isInPlayList);
			if(rs.next()==false) {
				System.out.println("플레이리스트에 해당곡이 추가되어있지 않습니다.");
				rs.close();
				st.close();
				st2.close();
				admincon.close();
				return;
			}
			
			String sql_music_count = "SELECT count FROM musicstreaming.음원 WHERE 음원관리번호 = '"+num+"'";
			String sql_music_count_UPDATE = "UPDATE musicstreaming.음원 set count = count+1 WHERE 음원관리번호 = '"+num+"'";
			String select_playlist = "SELECT ID, play_음원관리번호, p_count"
					+ " FROM musicstreaming.play, musicstreaming.스트리밍구독자"
					+ " WHERE play_구독자주민번호 = 구독자주민번호 AND ID = '"+id+"' AND play_음원관리번호='"+num+"'";
			
			String ssn=null;
			String ssn_search="SELECT 구독자주민번호 FROM musicstreaming.스트리밍구독자 WHERE ID='"+id+"';";
			rs = st.executeQuery(ssn_search);
			while(rs.next()) {
				ssn=rs.getString(1);
			}
			
			String make_play="INSERT INTO musicstreaming.play (play_음원관리번호,play_구독자주민번호,p_count)"
					+ " VALUES ("+num+", '"+ssn+"', 1)"; 
			String update_pCount="UPDATE musicstreaming.play set p_count = p_count+1"
					+ " WHERE play_구독자주민번호='"+ssn+"' AND play_음원관리번호='"+num+"'";
			
			
			rs = st.executeQuery(sql_music_count);
			ResultSet rs2 = st2.executeQuery(select_playlist);
			if(rs.next()) {
				rs=st.executeQuery(sql_music_count_UPDATE);
				if(rs2.next()==false) {
					//play에 해당사항이 없으니 create
					rs2 =st2.executeQuery(make_play);
				}else {
					//play에 해당사항이 있으니 update
					rs2 = st2.executeQuery(update_pCount);
				}
				String n= Integer.toString(num);
				StartMusic(n);
				System.out.println("성공적 플레이");
			}else {
				System.out.println("노래번호를 잘못 입력하셨습니다.");
				rs2.close();
				st2.close();
				rs.close();
				st.close();
				admincon.close();
				return;
			}
			rs2.close();
			st2.close();
			rs.close();
			st.close();
			admincon.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static void UploadMusic(String title,String singer,String genre,String Alubum) {
		try {
			Connection admincon = DriverManager.getConnection("jdbc:mariadb://localhost:3307/musicstreaming"
					,"musicuser","musicstreaming");
			int next=0;
			Statement st = admincon.createStatement();
			Statement musicNum = admincon.createStatement();
			String getMusicNum = "SELECT COUNT(*) FROM musicstreaming.음원";
			ResultSet rsMusic = musicNum.executeQuery(getMusicNum);
			while(rsMusic.next()) {
				next=rsMusic.getInt(1);
			}
			next+=1;
			String admin = Admin.GetAdminSsn();
			
			String sqlIsUser="INSERT INTO musicstreaming.음원(음악이름,가수,장르,count,앨범이름,음원관리번호,음원관리자주민번호)"
					+ "VALUES('"+title+"','"+singer+"','"+genre+"',0,'"+Alubum+"','"+next+"','"+admin+"')";
			
			st.executeUpdate(sqlIsUser);
			System.out.println("성공적으로 노래를 등록했습니다.");
			
			st.close();
			admincon.close();
		}catch(SQLException e) {
			System.out.println("노래등록과정중 오류가 발생하였습니다. 시스템 관리자에게 문의해주세요.");
		}
	}
	
}
