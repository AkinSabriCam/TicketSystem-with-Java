package application;

import java.awt.List;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import javafx.util.StringConverter;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;

public class MainPageController {
	SQLSorgulari SQL = new SQLSorgulari();
	Sefer sefer = new Sefer();
	String seferID,SilinecekBiletId="";
	String KoltukNumarasi,YolcuSecilenKoltuk;
	ObservableList<Bus> BusList=  FXCollections.observableArrayList();;
	Bus bus;

	@FXML
	private Button btn1,btn2,btn3,btn4,btn5,btn6,btn7,btn8,btn9,btn10,btn11,btn12,btn13,btn14,btn15,btn16,btn17,btn18,btn19,btn20
	,btn21,btn22,btn23,btn24,btn25,btn26,btn27,btn28,btn29,btn30,btn31,btn32,btn33,btn34,btn35,btn36,btn37,btn38;

	ArrayList<Button> Buttons = new ArrayList<Button>();

	@FXML
	private Button btnAddBus,btnDeleteBus;

	@FXML
	private CheckBox chcErkek,chcKadin;
	@FXML
	private TextField txtTCKN,txtSurname,txtName,txtPlate,txtModel, txtTicket;
	
	@FXML
	private Label lblTicketPrice,lblTicketName,lblTicketfto,lblTicketDate,lblTicketTime;

	@FXML
	private AnchorPane AnchorPanelYolcu;

	@FXML
	private Tab tabBusManagement;
	@FXML
	private AnchorPane ReservationPane;

	@FXML
	private ListView<String> lstJourney;

	@FXML
	private AnchorPane paneTickets;
	@FXML
	private TabPane MainTab;
	@FXML
	private Tab tabReservation;

	@FXML
	private Tab tabTickets;

	@FXML
	private AnchorPane paneRes;

	@FXML
	private AnchorPane paneBus;
	@FXML
	private ScrollPane SeatScrollPane;

	@FXML
	private AnchorPane paneAdmin;

	@FXML
	private Tab tabAdminPanel;

	@FXML
	private ComboBox<Sefer> cmbSearch;

	@FXML
	private ComboBox<Bus> cmbBus;

	@FXML
	void Reservation(Event event) {

	}


	@FXML
	void AdminPanel(Event event) {

	}

	@FXML
	void Tickets(Event event) {


	}
	@FXML
	void BusMSystem(Event event) throws SQLException {
		cmbBus.getItems().clear();
		lstJourney.getItems().clear();
		ResultSet resultset=SQL.GetAllBus();
		while(resultset.next()) {
			bus= new Bus();
            String OtobusID = resultset.getString("otobusID");
			String Marka=resultset.getString("otobusMarka");
			String Plaka=resultset.getString("otobusPlaka");
			String Aktif=resultset.getString("isActive");
			bus.OtobusID = OtobusID;
			bus.Marka=Marka;
			bus.Plaka=Plaka;
			
			if(Aktif.equals("1"))
				bus.isActive="Active";
			else
				bus.isActive="Inactive";


			BusList.add(bus);
		}
		cmbBus.setItems(BusList);
		cmbBus.setConverter(new StringConverter<Bus>() {
			@Override
			public Bus fromString(String string) {
				return cmbBus.getItems().stream().filter(ap -> 
				ap.OtobusID.equals(string)).findFirst().orElse(null);
			}
			@Override
			public String toString(Bus object) {
				// TODO Auto-generated method stub
				return object.Marka+"  "+object.Plaka + "  " + object.isActive;
			}
		});




	}
	@FXML
	void AddBus(ActionEvent event) throws SQLException {
		SQL.InsertBus(txtPlate.getText(), txtModel.getText());	
	}


	@FXML
	void cmbBusSelect (ActionEvent event) throws SQLException {

		String busID = cmbBus.getValue().OtobusID;
        btnDeleteBus.setDisable(false);
		ResultSet resultSet = SQL.GetAllJourneyByID(busID);

		while(resultSet.next()) {
			lstJourney.getItems().add("Journey: " + resultSet.getString("baslangic") + " - " + resultSet.getString("bitis")
			+ "  //  Date: " + resultSet.getString("kalkisTarihi") 
			+ "  //  Time: " + resultSet.getString("kalkisSaati"));
		}
	}

	@FXML
	void DeleteBus(ActionEvent event) throws SQLException {
		String busId=cmbBus.getValue().OtobusID;
		SQL.DeleteBusandOthers(busId);
		
		
	}
	@FXML
	void BuyClicked(ActionEvent event) throws SQLException, IOException {
		String sex;
		if(chcKadin.isSelected()) {
			sex="False";
		}
		else {
			sex="True";
		}	
		SQL.InsertYolcu(txtName.getText(), txtSurname.getText(),sex,txtTCKN.getText(),"7");
		System.out.println("insert yolcu ok");
		// YUKARIDA CÝNSÝYETÝN KAADIN VEYA ERKEK OLDUÐUNU BELÝRLEDÝK	

		// BÝLETÝ ALACAK OLAN YOLCUYU EKLEDÝK
		ResultSet resultset = SQL.SelectYolcu(txtTCKN.getText());
		System.out.println("select yolcu ok");
		// eklediðimiz yolcunun id sine ihtiyac var bu sebeble sql sorgusu çalýþtýrýyoruz
		String YolcuId=null;

		while(resultset.next()){
			YolcuId=resultset.getString("yolcuID");
			// burada gelen yolcunun id sini YolcuId 'ye atadýk
		}

		System.out.println("yolcu id = " + YolcuId);
		if(YolcuId!=null) {
			// eðer yolcu varsa if'e girecektir. burada bilet için gelecek olan pencere doldurulacaktýr.

			SQL.InsertBilet(YolcuId,seferID);//bilet ekledik..		
			System.out.println("insert bilet ok");

			ResultSet BiletresultSet=SQL.GetBilet(seferID);
			while(BiletresultSet.next()) {
				String sex_;
				if(chcErkek.isSelected())
					sex_ = "Erkek";
				else
					sex_ = "Kadýn";
				FXMLLoader Loader = new FXMLLoader();
				Loader.setLocation(getClass().getResource("Ticket.fxml"));
				try {
					Loader.load();
				}
				catch(Exception e) {
					System.out.println("ERROR");
				}
				TicketController tickCont= Loader.getController();

				Parent p = Loader.getRoot();
				Stage stage= new Stage();
				stage.setScene(new Scene(p));
				tickCont.SetText(txtTCKN.getText(),txtName.getText() +" "+txtSurname.getText(), sex_,
						BiletresultSet.getString("Baslangic"), BiletresultSet.getString("Bitis"), 
						BiletresultSet.getString("kalkisSaati"), BiletresultSet.getString("kalkisTarihi"),
						BiletresultSet.getString("seferUcreti"));
				stage.showAndWait();
			}
		}
		else {
			System.out.println("Ýþlem gerçekleþmedi //SQL YOLCU ERROR//");
		}
	}

	@FXML
	void TicketFind(ActionEvent event) throws SQLException {
		String ticketTCKN = txtTicket.getText();
		
		ResultSet resultSet = SQL.GetBiletByTCKN(ticketTCKN);
		
		while(resultSet.next()) {
		    SilinecekBiletId=resultSet.getString("biletID");
			lblTicketPrice.setText(resultSet.getString("seferUcreti"));
			lblTicketName.setText(resultSet.getString("adSoyad"));
			lblTicketfto.setText(resultSet.getString("Baslangic")+"-"+resultSet.getString("Bitis"));
			lblTicketDate.setText(resultSet.getString("kalkisTarihi"));
		    lblTicketTime.setText(resultSet.getString("kalkisSaati"));
		}

	}
	@FXML
	void DeleteTicket (ActionEvent event) throws SQLException {
	    SQL.DeleteBilet(SilinecekBiletId);
		lblTicketDate.setText("-");
		lblTicketfto.setText("-");
		lblTicketName.setText("-");
		lblTicketPrice.setText("-");
		lblTicketTime.setText("-");
	}
	@FXML
	void CancelClicked(ActionEvent event) {

	
	}
	@FXML
	void clicked(ActionEvent event) throws SQLException
	{

		SeatScrollPane.setVisible(false);
		AnchorPanelYolcu.setVisible(true);

	}
	@FXML
	public void cmbSelected(ActionEvent event) throws SQLException {
		seferID=cmbSearch.getValue().seferID; 
		SeatScrollPane.setVisible(true);


		Buttons.add(btn1); 
		Buttons.add(btn2);
		Buttons.add(btn3);
		Buttons.add(btn4);
		Buttons.add(btn5);
		Buttons.add(btn6);
		Buttons.add(btn7);
		Buttons.add(btn8);
		Buttons.add(btn9);
		Buttons.add(btn10);
		Buttons.add(btn11);
		Buttons.add(btn12);
		Buttons.add(btn13);
		Buttons.add(btn14);
		Buttons.add(btn15);
		Buttons.add(btn16);
		Buttons.add(btn17);
		Buttons.add(btn18);
		Buttons.add(btn19);
		Buttons.add(btn20);
		Buttons.add(btn21);
		Buttons.add(btn22);
		Buttons.add(btn23);
		Buttons.add(btn24);
		Buttons.add(btn25);
		Buttons.add(btn26);
		Buttons.add(btn27);
		Buttons.add(btn28);
		Buttons.add(btn29);
		Buttons.add(btn30);
		Buttons.add(btn31);
		Buttons.add(btn32);
		Buttons.add(btn33);
		Buttons.add(btn34);
		Buttons.add(btn35);
		Buttons.add(btn36);
		Buttons.add(btn37);
		Buttons.add(btn38);

		ResultSet resultset=SQL.TicketControlForSeat(seferID);
		while(resultset.next())
		{   
			for(int i=0;i<Buttons.size();i++) {
				if(Buttons.get(i).getText().equals(resultset.getString("koltukNumarasi")))
				{
					Button btn = new Button();
					btn=Buttons.get(i);
					KoltukNumarasi=btn.getText();
					btn.setStyle("-fx-background-color: red;");
					btn.setDisable(true);
					System.out.println(btn.getText());
				}
			}
		}
	}
	@FXML
	void search(ActionEvent event) throws SQLException
	{  
		System.out.println("OK");
		ResultSet resultSet = SQL.Select("Ýstanbul", "Kayseri", "19-12-2018");
		ObservableList<Sefer> Seferler = FXCollections.observableArrayList();
		while(resultSet.next()) 
		{

			String seferID = resultSet.getString("seferID");  		
			String otobusMarka = resultSet.getString("otobusMarka");
			String kalkisSaati = resultSet.getString("kalkisSaati");
			String seferUcreti = resultSet.getString("seferUcreti");
			String  rotaid=resultSet.getString("rotaID");

			sefer.seferID = seferID;
			sefer.otobusMarka = otobusMarka;
			sefer.kalkisSaati = kalkisSaati;
			sefer.seferUCreti = seferUcreti;
			sefer.rotaID=rotaid;

			Seferler.addAll(sefer);
		}
		cmbSearch.setItems(Seferler);
		cmbSearch.setConverter(new StringConverter<Sefer>() {
			@Override
			public Sefer fromString(String string) {
				return cmbSearch.getItems().stream().filter(ap -> 
				ap.otobusMarka.equals(string)).findFirst().orElse(null);
			}
			@Override
			public String toString(Sefer object) {
				// TODO Auto-generated method stub
				return object.otobusMarka+"  "+object.kalkisSaati + "  " + object.seferUCreti;
			}
		});

	}


}
