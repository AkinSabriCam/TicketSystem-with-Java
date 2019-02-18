package application;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class SQLSorgulari {

	public ResultSet Select(String baslangic, String bitis, String tarih) throws SQLException {
		String sorgu = "SELECT S.seferID,O.otobusMarka, S.kalkisSaati, S.seferUcreti,S.rotaID "
				+ "FROM tblSefer S INNER JOIN tblRota R ON S.rotaID = R.rotaID "
				+ "INNER JOIN tblOtobus O ON S.otobusID = O.otobusID "
				+ "WHERE R.baslangic = '" + baslangic + "' AND R.bitis = '" + bitis + "' AND S.kalkisTarihi = '" + tarih + "'";
		Statement statement = Main.connection.createStatement();
		ResultSet resultSet = statement.executeQuery(sorgu);
		return resultSet;
		// test edildi.
	}

	/*public ResultSet GetRota(String rotaId) throws SQLException {
		String Sorgu="Select * from tblRota where rotaID="+rotaId+"";
		Statement statement=Main.connection.createStatement();
		ResultSet resultset=statement.executeQuery(Sorgu);

		return resultset;
	}
	public ResultSet GetSefer(String seferid) throws SQLException {
		String Sorgu="SELECT * FROM tblSefer where seferID="+seferid+"";
		Statement statement = Main.connection.createStatement();
	    ResultSet resultset=statement.executeQuery("Sorgu");
		return resultset;
	}
	 */
	public ResultSet TicketControlForSeat(String seferId) throws SQLException {
		String Sorgu="Select  y.koltukNumarasi from tblSefer s INNER JOIN tblBilet b on s.seferID=b.seferID "
				+" INNER JOIN tblYolcu y on y.yolcuID=b.yolcuID  where s.seferID="+Integer.parseInt(seferId)+"";
		Statement statement =Main.connection.createStatement();
		ResultSet resultset = statement.executeQuery(Sorgu);
		return resultset;
	}
	public void InsertYolcu(String Ad,String Soyad,String cinsiyet,String tckn,String KoltukNo) throws SQLException {
		String adsoyad=Ad +" "+ Soyad; 
		String Sorgu="INSERT INTO tblYolcu (adSoyad,TCKN,cinsiyet,koltukNumarasi)"
				+ " values('"+adsoyad+"','"+tckn+"','"+cinsiyet+"',"+Integer.parseInt(KoltukNo)+")";
		System.out.println(Sorgu);
		Statement statement = Main.connection.createStatement();
		statement.execute(Sorgu);
		//  test edildi !!
		System.out.println("Yolcular tablosuna eklendi");
	}
	public ResultSet GetBilet(String seferId) throws SQLException {
		String Sorgu="SELECT s.kalkisSaati,s.kalkisTarihi,r.Baslangic,r.Bitis,s.seferUcreti"
				+ " FROM  tblSefer s INNER JOIN tblRota r on(s.rotaID=r.rotaID) where"
				+ " s.seferID='"+seferId+"'";
		Statement statement =Main.connection.createStatement();
		ResultSet resultset= statement.executeQuery(Sorgu);
		System.out.println(Sorgu);
		return resultset;
	}
	public ResultSet SelectYolcu(String tckn) throws SQLException {
		String SelectSorgu="SELECT yolcuID  FROM tblYolcu  where TCKN='"+tckn+"' ";
		Statement statement =Main.connection.createStatement();
		ResultSet  resultset=statement.executeQuery(SelectSorgu);
		return resultset;
	}

	public void InsertBilet(String yolcuId,String seferId) throws SQLException {
		String Sorgu="INSERT INTO tblBilet (yolcuID,seferID) values("+Integer.parseInt(yolcuId)+","+Integer.parseInt(seferId)+")";

		Statement statement = Main.connection.createStatement();
		statement.execute(Sorgu);
		System.out.println(Sorgu);
	}

	public void InsertBus(String Plaka,String Marka) throws SQLException {
		String Sorgu="INSERT INTO tblOtobus (otobusMarka,otobusPlaka) values ('"+Marka+"','"+Plaka+"')";
		Statement statement =Main.connection.createStatement();
		statement.execute(Sorgu);
	}
	public ResultSet GetAllBus() throws SQLException {
		String Sorgu="Select * FROM tblOtobus";
		Statement statement =Main.connection.createStatement();
		ResultSet resultset=statement.executeQuery(Sorgu);
		return  resultset;
	}

	public ResultSet GetAllJourneyByID(String busID) throws SQLException {
		String sorgu = "SELECT S.kalkisSaati, S.kalkisTarihi, R.baslangic, R.bitis "
				+ "FROM tblSefer S INNER JOIN tblRota R ON S.rotaID = R.rotaID WHERE S.otobusID = " + busID;
		Statement statement = Main.connection.createStatement();
		ResultSet resultset=statement.executeQuery(sorgu);
		return  resultset;
	}

	public ResultSet GetBiletByTCKN(String TCKN) throws SQLException {
		String query = "SELECT b.biletID,Y.adSoyad, Y.koltukNumarasi, S.kalkisSaati, S.kalkisTarihi, R.Baslangic, R.Bitis, S.seferUcreti "
				+ "FROM tblBilet B INNER JOIN tblYolcu Y ON B.yolcuID = Y.yolcuID "
				+ "INNER JOIN tblSefer S ON B.seferID = S.seferID "
				+ "INNER JOIN tblRota R ON S.rotaID = R.rotaID "
				+ "WHERE Y.TCKN = '" + TCKN +"'";
		Statement statement = Main.connection.createStatement();
		ResultSet resultset=statement.executeQuery(query);
		return resultset;
	}
	public void DeleteBilet(String biletId) throws SQLException {
    
	 String Sorgu="DELETE FROM tblBilet where biletID="+biletId+"";
	 Statement statement =Main.connection.createStatement();
	 statement.execute(Sorgu);
     
	}
   public void DeleteBusandOthers(String busID) throws SQLException {
	   String Sorgu = "UPDATE tblOtobus SET isActive="+0+" where otobusID="+busID+"";
	   Statement  statement = Main.connection.createStatement();
	   statement.execute(Sorgu);
	   
	   String seferid="";
	   String SeferSorgu="Select seferID From tblSefer where otobusID="+busID+"";
	   
	   ResultSet resultset=statement.executeQuery(SeferSorgu);
	   while(resultset.next()) {
		   seferid=resultset.getString("seferID");
	   }
	   
	   String DeleteBilet="DELETE  FROM tblBilet where seferID="+seferid+"";
	   statement.execute(DeleteBilet);
	   
	   String DeleteSefer = "DELETE  FROM tblSefer where otobusID="+busID+"";
	   statement.execute(DeleteSefer);
	   
	  
	   
	   System.out.println("bus silindi yeaaa");
   }
}
