import java.sql.*;
import java.time.*;
import java.time.temporal.ChronoUnit;

public class User {
	private String inputname;
	private String inputpass;
	
	private String adminID = "root";
	private String adminPW ="with7742I@";
	private String url = "jdbc:mariadb://localhost:3307/musicstreaming";
	private String userID = "musicuser";
	private String userPW = "musicstreaming";
	
	Connection con = null;
	Statement st = null;
	
	static {
		try {Class.forName("org.mariadb.jdbc.Driver");
		}catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}
	
	
	@SuppressWarnings("unused")
	private User() {};
	
	public User(String name , String pass) {
		this.inputname=name;
		this.inputpass =pass;
	}
	
	
	public static void SignUp(String gender,String address,String Phone,String ssn,int subscribe, String pw,String name,String id) {
		try {
			Connection admincon = DriverManager.getConnection("jdbc:mariadb://localhost:3307/musicstreaming"
					,"root","with7742I@");
			
			Statement st = admincon.createStatement();
			//sha256
			String adminSsn=Admin.GetAdminSsn();
			pw=SimpleFunction.getUserPass(pw);
			LocalDate nowDate = LocalDate.now();
			String sqlIsUser="INSERT INTO 스트리밍구독자 (성별,주소,핸드폰번호,구독자주민번호,구독_관리자주민번호,구독개월수,구독시작날짜,passwd,성명,ID) "
					+ "VALUES ('"+gender+"','"+address+"','"+Phone+"','"+ssn+"','"+adminSsn+"','"+subscribe+"','"+nowDate+"','"+pw+"','"+name+"','"+id+"')";
			
			st.executeUpdate(sqlIsUser);
			System.out.println("성공적으로 회원가입 되셨습니다.");
			//ResultSet rs = st.executeQuery(sqlIsUser);
			
			//boolean isLogin = false;
			//while(rs.next()) isLogin =true;
			
			//rs.close();
			st.close();
			admincon.close();
		}catch(SQLException e) {
			System.out.println("이미 가입되어있거나 가입불가능한 상태입니다.");
		}
	}

	public boolean isInDatabase() {
		boolean success=false;
		try {
			Connection admincon = DriverManager.getConnection(this.url,this.adminID,this.adminPW);
			
			Statement st = admincon.createStatement();
			//sha256
			inputpass=SimpleFunction.getUserPass(inputpass);
			
			String sqlIsUser="select * from 스트리밍구독자 where ID='"+this.inputname+"'and passwd='"+this.inputpass+"'";
			
			st.executeUpdate(sqlIsUser);
			ResultSet rs = st.executeQuery(sqlIsUser);
			
			boolean isLogin = false;
			while(rs.next()) isLogin =true;
			if(isLogin) {
				System.out.println("로그인성공");
				success=true;
				userLogin();
			}else {
				System.out.println("로그인에 실패하였습니다. 관리자에게 문의해주세요.");
				rs.close();
				st.close();
				admincon.close();
				success=false;
			}
			rs.close();
			st.close();
			admincon.close();
		}catch(SQLException e) {
			e.printStackTrace();
		}
		return success;
	}
	
	public void showMyPlayList() {
		//id 존재
		try {
			
			String sql="SELECT 플레이리스트명  FROM 플레이리스트 "
					+ "WHERE 플레이리스트_구독자주민번호 = ALL (SELECT 구독자주민번호 FROM 스트리밍구독자 WHERE ID='"+this.inputname+"')";
			ResultSet rs = st.executeQuery(sql);
			if(rs.next()) {
				String pname= rs.getString(1);
				System.out.println("플레이리스트이름: "+pname);
				while(rs.next()) {
					pname= rs.getString(1);
					System.out.println("플레이리스트이름: "+pname);
				}
			}else {
				System.out.println("보유한 플레이리스트가 없습니다.");
			}
			
			rs.close();
		}catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void usePlayList(String p_name, ShowInterface s) {
		//id 존재
		try {
			String ssn=null;
			String ssn_search="SELECT 구독자주민번호 FROM 스트리밍구독자 WHERE ID='"+this.inputname+"'";
			ResultSet rs = st.executeQuery(ssn_search);
			while(rs.next()) {
				ssn=rs.getString(1);
			}
			String listExistsql = "SELECT 플레이리스트명 FROM 플레이리스트 "
					+ "WHERE 플레이리스트_구독자주민번호='"+ssn+"' AND 플레이리스트명='"+p_name+"'";
			rs=st.executeQuery(listExistsql);
			if(rs.next()==false) {
				System.out.println("플레이리스트가 없습니다.");
				rs.close();
				return;
			}
			
			String sql="SELECT 음악이름, 가수, 장르, 음원관리번호 FROM 추가, 음원 "
					+ "WHERE a_구독자주민번호 ='"+ssn+"' AND a_플레이리스트명='"+p_name+"' AND 음원관리번호=a_음원관리번호 ORDER BY 음악이름";
			
			rs = st.executeQuery(sql);
			int i=1;
			String sql2;
			int takePcount=0;
			if(rs.next()) {
				String title= rs.getString(1);
				String singer = rs.getString(2);
				String genre = rs.getString(3);
				int m_num = rs.getInt(4);
				sql2 = "SELECT p_count FROM play WHERE play_구독자주민번호='"+ssn+"' AND play_음원관리번호='"+m_num+"'";
				ResultSet rs2 = st.executeQuery(sql2);
				if(rs2.next()) takePcount = rs2.getInt(1);
				System.out.println("=====음악"+i+"=====");
				i++;
				System.out.println("타이틀 :"+title);
				System.out.println("가수 :"+singer);
				System.out.println("장르 :"+genre);
				System.out.println("관리번호 :"+m_num);
				System.out.println("내가 재생한 횟수 :"+takePcount);
				System.out.println();
				while(rs.next()) {
					title= rs.getString(1);
					singer = rs.getString(2);
					genre = rs.getString(3);
					m_num = rs.getInt(4);
					sql2 = "SELECT p_count FROM play WHERE play_구독자주민번호='"+ssn+"' AND play_음원관리번호='"+m_num+"'";
					rs2 = st.executeQuery(sql2);
					if(rs2.next()) takePcount = rs2.getInt(1);
					System.out.println("=====음악"+i+"=====");
					i++;
					System.out.println("타이틀 :"+title);
					System.out.println("가수 :"+singer);
					System.out.println("장르 :"+genre);
					System.out.println("관리번호 :"+m_num);
					System.out.println("내가 재생한 횟수 :"+takePcount);
					System.out.println();
				}
				s.playlistExist=true;
				rs2.close();
			}else {
				System.out.println("플레이리스트에 노래가 없습니다.");
				s.playlistExist=true;
			}
			rs.close();
		}catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void GetUserTitleRanking() {
		try {
			String ssn=null;
			String ssn_search="SELECT 구독자주민번호 FROM musicstreaming.스트리밍구독자 WHERE ID='"+this.inputname+"'";
			ResultSet rs = st.executeQuery(ssn_search);
			while(rs.next()) {
				ssn=rs.getString(1);
			}
			String sql="SELECT 음악이름, 가수, 장르, 음원관리번호 , p_count FROM play, 음원 "
					+ "WHERE play_구독자주민번호 ='"+ssn+"' AND 음원관리번호=play_음원관리번호 ORDER BY p_count DESC";
			
			rs = st.executeQuery(sql);
			int i=1;
			if(rs.next()) {
				String title= rs.getString(1);
				String singer = rs.getString(2);
				String genre = rs.getString(3);
				int m_num = rs.getInt(4);
				int p_num =rs.getInt(5);
				System.out.println("=====음악"+i+"=====");
				i++;
				System.out.println("타이틀 :"+title);
				System.out.println("가수 :"+singer);
				System.out.println("장르 :"+genre);
				System.out.println("관리번호 :"+m_num);
				System.out.println("내가 재생한 횟수 :"+p_num);
				System.out.println();
				while(rs.next()) {
					title= rs.getString(1);
					singer = rs.getString(2);
					genre = rs.getString(3);
					m_num = rs.getInt(4);
					p_num =rs.getInt(5);
					System.out.println("=====음악"+i+"=====");
					i++;
					System.out.println("타이틀 :"+title);
					System.out.println("가수 :"+singer);
					System.out.println("장르 :"+genre);
					System.out.println("관리번호 :"+m_num);
					System.out.println("내가 재생한 횟수 :"+p_num);
					System.out.println();
				}
			}else {
				System.out.println("보유한 플레이리스트가 없습니다.");
			}
			rs.close();
		}catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
	public void AddTiltleToPlayList(String p_name, int addnum) {
		//id 존재
		try {
			String checkExist = "SELECT 음악이름 FROM 음원 WHERE 음원관리번호='"+addnum+"'";
			ResultSet rs = st.executeQuery(checkExist);
			if(rs.next()==false) {
				System.out.println("해당 관리번호를 가진 음악은 존재하지 않습니다. 다시 확인해주세요.");
				rs.close();
				return;
			}
			String ssn=null;
			String ssn_search="SELECT 구독자주민번호 FROM musicstreaming.스트리밍구독자 WHERE ID='"+this.inputname+"'";
			rs = st.executeQuery(ssn_search);
			while(rs.next()) {
				ssn=rs.getString(1);
			}
			String sql="SELECT a_음원관리번호 FROM 추가 WHERE a_구독자주민번호='"+ssn+"' AND a_플레이리스트명='"+p_name+"' AND a_음원관리번호='"+addnum+"'";
			rs =st.executeQuery(sql);
			if(rs.next()==true) {
				System.out.println("이미 존재하는 음악입니다.");
				rs.close();
				return;
			}
			
			String addsql = "INSERT INTO 추가 (a_음원관리번호,a_구독자주민번호,a_플레이리스트명)"
					+ " VALUES ("+addnum+",'"+ssn+"','"+p_name+"')";
	
			st.executeQuery(addsql);
			System.out.println("성공적으로 추가했습니다.");
			rs.close();
		}catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void DelTiltleToPlayList(String p_name, int delnum) {
		//id 존재
		try {
			String checkExist = "SELECT 음악이름 FROM 음원 WHERE 음원관리번호='"+delnum+"'";
			ResultSet rs = st.executeQuery(checkExist);
			if(rs.next()==false) {
				System.out.println("해당 관리번호를 가진 음악은 존재하지 않습니다. 다시 확인해주세요.");
				rs.close();
				return;
			}
			String ssn=null;
			String ssn_search="SELECT 구독자주민번호 FROM musicstreaming.스트리밍구독자 WHERE ID='"+this.inputname+"'";
			rs = st.executeQuery(ssn_search);
			while(rs.next()) {
				ssn=rs.getString(1);
			}
			String sql="SELECT a_음원관리번호 FROM 추가 WHERE a_구독자주민번호='"+ssn+"' AND a_플레이리스트명='"+p_name+"' AND a_음원관리번호='"+delnum+"'";
			rs =st.executeQuery(sql);
			if(rs.next()==false) {
				System.out.println("플레이리스트에 존재하지 않는 음악입니다.");
				rs.close();
				return;
			}
			
			String delsql = "DELETE FROM 추가 WHERE a_음원관리번호='"+delnum+"' AND a_플레이리스트명='"+p_name+"' AND a_구독자주민번호='"+ssn+"'";
	
			st.executeQuery(delsql);
			System.out.println("성공적으로 삭제했습니다.");
			rs.close();
		}catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void DelPlayList(String p_name) {
		//id 존재
		try {
			
			String ssn=null;
			String ssn_search="SELECT 구독자주민번호 FROM musicstreaming.스트리밍구독자 WHERE ID='"+this.inputname+"'";
			ResultSet rs = st.executeQuery(ssn_search);
			while(rs.next()) {
				ssn=rs.getString(1);
			}
			String sql="SELECT 플레이리스트명 FROM 플레이리스트 WHERE 플레이리스트_구독자주민번호='"+ssn+"' AND 플레이리스트명='"+p_name+"'";
			rs =st.executeQuery(sql);
			if(rs.next()==false) {
				System.out.println("존재하지 않는 플레이리스트입니다.");
				rs.close();
				return;
			}
			
			String delsql = "DELETE FROM 플레이리스트 "
					+ "WHERE 플레이리스트_구독자주민번호='"+ssn+"' AND 플레이리스트명='"+p_name+"'";
	
			st.executeQuery(delsql);
			System.out.println("성공적으로 삭제했습니다.");
			rs.close();
		}catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void AddPlayList(String p_name) {
		//id 존재
		try {
			String ssn=null;
			String ssn_search="SELECT 구독자주민번호 FROM musicstreaming.스트리밍구독자 WHERE ID='"+this.inputname+"'";
			ResultSet rs = st.executeQuery(ssn_search);
			while(rs.next()) {
				ssn=rs.getString(1);
			}
			String sql="SELECT 플레이리스트명 FROM 플레이리스트 WHERE 플레이리스트_구독자주민번호='"+ssn+"' AND 플레이리스트명='"+p_name+"'";
			rs =st.executeQuery(sql);
			if(rs.next()==true) {
				System.out.println("이미 존재하는 플레이리스트 이름입니다.");
				rs.close();
				return;
			}
			
			String addsql = "INSERT INTO 플레이리스트 (플레이리스트_구독자주민번호,플레이리스트명) "
					+ "VALUES ('"+ssn+"','"+p_name+"')";
	
			st.executeQuery(addsql);
			System.out.println("성공적으로 생성했습니다.");
			rs.close();
		}catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void CheckMyInformation(String myId,String mypass) {
		try {
		//	st = con.createStatement();
			
			mypass = SimpleFunction.getUserPass(mypass);
			String sql = "SELECT 성별, 주소 , 핸드폰번호, 구독자주민번호, 구독개월수, 구독시작날짜, 성명, ID "
					+ "FROM 스트리밍구독자 WHERE ID='"+myId+"' AND passwd='"+mypass+"'";
			ResultSet rs = st.executeQuery(sql);
			if(rs.isAfterLast()!=false) {
				System.out.println("정보가일치하지 않습니다.");
				return;
			}else {
				if(rs.next()) {
					//String gender = rs.getString(1);
					String address = rs.getString(2);
					String PhoneNum = rs.getString(3);
					String ssn = rs.getString(4);
					int subscribeMonth = rs.getInt(5);
					String start = rs.getString(6);
					String name = rs.getString(7);
					String ID = rs.getString(8);
					String[] sstart = start.split("-");
					LocalDate date = LocalDate.of(Integer.parseInt(sstart[0]), Integer.parseInt(sstart[1]), Integer.parseInt(sstart[2]));
					LocalDate sdate = date.plusMonths(subscribeMonth);
					LocalDate ndate = LocalDate.now();
					long days = ChronoUnit.DAYS.between(ndate, sdate);
					System.out.println("=====MY INFO=====");
					System.out.println("이름:"+name);
					System.out.println("아이디:"+ID);
					System.out.println("주소:"+address);
					System.out.println("핸드폰번호:"+PhoneNum);
					System.out.println("주민등록번호:"+ssn);
					System.out.println("구독시작날짜:"+start);
					System.out.println("오늘날짜:"+ndate.toString()+ "(남은일수:"+days+")");
					System.out.println();
				}else {
					System.out.println("잘못입력하셨습니다. 아이디와 비밀번호를 확인해주세요.");
				}
			}
			rs.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private void userLogin() {
		try {
			this.con = DriverManager.getConnection(this.url,this.userID,this.userPW);
			this.st = con.createStatement();
			/*System.out.println("HELOOL");
			st.close();
			con.close();*/
		}catch(SQLException e) {
			e.printStackTrace();
		}	
	}
	public void userLogout() {
		try {
			st.close();
			con.close();
		}catch(SQLException e) {
			e.printStackTrace();
		}
	}
	public String getId() {
		return this.inputname;
	}
	
	
	
}
