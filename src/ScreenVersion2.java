import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Calendar;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

//import de.javasoft.plaf.synthetica.SyntheticaPlainLookAndFeel;

/**
 * Created by NoobSquad on 10/15/2016.
 * 
 * @author Nick Fordyce, Bobby Frydell, Mark Gelfius, Cameron Glass, and Nirali
 *         Rai
 * @version 10162016
 */

public class ScreenVersion2 extends JFrame implements ActionListener {
	private JRadioButton[] courts = new JRadioButton[5];
	private JRadioButton[] times = new JRadioButton[3];
	private JCheckBox[] rests = new JCheckBox[4];
	private ButtonGroup bcourts = new ButtonGroup();
	private ButtonGroup bmeals = new ButtonGroup();
	private String JSONResponse;
	static private String date;
	private static String day;
	private String currentDiningCourt, currentMealTime;
	private ArrayList<String> currentRestrict = new ArrayList<String>();
	private JPanel rightPanel, leftPanel, courtPanel, mealPanel, restPanel;
	private JLabel courtsHeader = new JLabel("Dining Courts", SwingConstants.CENTER);
	private JLabel timesHeader = new JLabel("Meal Times", SwingConstants.CENTER);
	private JLabel restHeader = new JLabel("Restrictions", SwingConstants.CENTER);
	static private JLabel dateHeader = new JLabel("Date:", SwingConstants.CENTER);
	private Icon cl = new ImageIcon("Images/closed_sign.jpg");
	private JLabel closed = new JLabel(cl);
	public final String[] COURT_NAMES = { "Earhart", "Windsor", "Wiley", "Hillenbrand", "Ford" };
	public final String[] MEAL_TIMES = { "Breakfast", "Lunch", "Dinner" };
	private final String[] DIET_RES = { "Vegetarian", "Gluten", "Milk", "Peanuts" };
	private final String USER_AGENT = "Chrome/5.0";
	private static int dayInt;
	private static int yearInt;
	private static int monthInt;
	private static JPanel datePanel = new JPanel();
	private static JPanel dayButtons = new JPanel();
	private static JLabel dayText;
	private static JLabel time;
	private static DayOfWeek dayOfWeek;
	private ArrayList<JPanel> panels = new ArrayList<JPanel>();
	private ArrayList<Food> foodInPanels = new ArrayList<Food>();

	public ScreenVersion2() {

//		try {
//			UIManager.setLookAndFeel(new SyntheticaPlainLookAndFeel());
//		} catch (Exception e) {
//			e.printStackTrace();
//		}

		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		Container contentPane = this.getContentPane();
		contentPane.setLayout(new BorderLayout());

		JButton okButton = new JButton("OK");
		leftPanel = new JPanel(new GridLayout(4, 1));
		courtPanel = new JPanel(new GridLayout(6, 1));
		mealPanel = new JPanel(new GridLayout(4, 1));
		restPanel = new JPanel(new GridLayout(5, 1));
		datePanel = new JPanel(new GridLayout(4, 1));
		rightPanel = new JPanel(new GridLayout(0, 1));
		JScrollPane scrollPane = new JScrollPane(rightPanel);
		courtsHeader.setFont(new Font(courtsHeader.getFont().getName(), Font.BOLD, 14));
		courtPanel.add(courtsHeader);

		for (int x = 0; x < 5; x++) {
			courts[x] = new JRadioButton(COURT_NAMES[x]);
			courts[x].addActionListener(this);
			courtPanel.add(courts[x]);
			bcourts.add(courts[x]);
		}
		timesHeader.setFont(new Font(courtsHeader.getFont().getName(), Font.BOLD, 14));
		mealPanel.add(timesHeader);

		for (int x = 0; x < 3; x++) {
			times[x] = new JRadioButton(MEAL_TIMES[x]);
			times[x].addActionListener(this);
			mealPanel.add(times[x]);
			bmeals.add(times[x]);
		}
		restHeader.setFont(new Font(courtsHeader.getFont().getName(), Font.BOLD, 14));
		restPanel.add(restHeader);
		for (int x = 0; x < 4; x++) {
			rests[x] = new JCheckBox(DIET_RES[x]);
			rests[x].addActionListener(this);
			restPanel.add(rests[x]);
		}

		dateHeader.setFont(new Font(courtsHeader.getFont().getName(), Font.BOLD, 14));
		datePanel.add(dateHeader);

		DateFormat dateFormat = new SimpleDateFormat("MM-dd-yyyy");
		Calendar cal = Calendar.getInstance();
		cal.setFirstDayOfWeek(Calendar.MONDAY);
		date = dateFormat.format(cal.getTime());
		time = new JLabel(date, SwingConstants.CENTER);

		yearInt = Integer.parseInt(date.substring(6, date.length()));
		dayInt = Integer.parseInt(date.substring(3, 5));
		monthInt = Integer.parseInt(date.substring(0, 2));
		dayOfWeek = LocalDate.of(yearInt, monthInt, dayInt).getDayOfWeek();
		day = dayOfWeek.toString();
		dayText = new JLabel(day, SwingConstants.CENTER);

		datePanel.add(dayText);
		datePanel.add(time);

		JButton back = new JButton("<--");
		dayButtons.add(back);

		JButton forward = new JButton("-->");
		dayButtons.add(forward);
		forward.addActionListener(new ActionForwardOneDay());
		back.addActionListener(new ActionBackOneDay());

		datePanel.add(dayButtons);

		courtPanel.setBorder(BorderFactory.createTitledBorder(""));
		mealPanel.setBorder(BorderFactory.createTitledBorder(""));
		restPanel.setBorder(BorderFactory.createTitledBorder(""));
		datePanel.setBorder(BorderFactory.createTitledBorder(""));

		Icon logo = new ImageIcon("Images/logo.png");
		JLabel logoLabel = new JLabel(logo);

		leftPanel.add(logoLabel);
		leftPanel.add(datePanel);
		leftPanel.add(courtPanel);
		leftPanel.add(mealPanel);
		leftPanel.add(restPanel);
		leftPanel.add(okButton);
		contentPane.add(leftPanel, BorderLayout.WEST);
		contentPane.add(scrollPane, BorderLayout.CENTER);
		contentPane.setName("Options");
		okButton.addActionListener(this);
		this.pack();
		this.setSize(1000, 800);
		this.setVisible(true);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}

	public static void main(String[] args) {
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				ScreenVersion2 program = new ScreenVersion2();
			}
		});
	}

	public void actionPerformed(ActionEvent z) {
		Object obj = z.getSource();
		if (obj instanceof JRadioButton) {
			for (int x = 0; x < courts.length; x++) {
				if (courts[x].isSelected()) {
					currentDiningCourt = courts[x].getText();
				}
			}

			for (int x = 0; x < times.length; x++) {
				if (times[x].isSelected()) {
					currentMealTime = times[x].getText();
				}
			}
		}
		if (obj instanceof JCheckBox) {
			for (int x = 0; x < rests.length; x++) {
				if (rests[x].isSelected() && !(currentRestrict.contains(rests[x].getText()))) {
					currentRestrict.add(rests[x].getText());
				} else if (rests[x].isSelected() && (currentRestrict.contains(rests[x].getText()))) {

				} else {
					currentRestrict.remove(rests[x].getText());
				}
			}
		}

		if (obj instanceof JButton) {
			JButton selectButton = (JButton) obj;
			if (selectButton.getText().equals("OK")) {
				try {
					this.sendGet();
				} catch (Exception e) {
					e.printStackTrace();
				}
				try {
					rightPanel.removeAll();
					rightPanel.updateUI();
					boolean checkDietary = true;
					JSONArray meals, stations, items, restricts;
					JSONObject foodObj, stationObj, mealObj, restrictObj;
					JSONObject json = new JSONObject(JSONResponse);
					System.out.println(json);
					meals = json.getJSONArray("Meals");
					for (int a = 0; a < meals.length(); a++) {
						mealObj = meals.getJSONObject(a);
						if (!mealObj.getString("Name").equals(currentMealTime))
							continue;
						stations = mealObj.getJSONArray("Stations");
						for (int b = 0; b < stations.length(); b++) {
							stationObj = stations.getJSONObject(b);
							items = stationObj.getJSONArray("Items");
							for (int c = 0; c < items.length(); c++) {
								foodObj = items.getJSONObject(c);
								if (currentRestrict.contains("Vegetarian") && foodObj != null
										&& foodObj.has("IsVegetarian") && !foodObj.getBoolean("IsVegetarian"))
									checkDietary = false;
								if (!currentRestrict.isEmpty() && foodObj != null && foodObj.has("Allergens")) {
									restricts = foodObj.getJSONArray("Allergens");
									if (!restricts.isNull(c)) {
										for (int d = 0; d < restricts.length(); d++) {
											restrictObj = restricts.getJSONObject(d);
											if (currentRestrict.contains(restrictObj.getString("Name"))
													&& restrictObj.getBoolean("Value"))
												checkDietary = false;
										}
									}
								}
								if (foodObj != null && checkDietary)
									addFoodItem(foodObj.getString("Name"));
								checkDietary = true;
							}
						}
					}
					if (rightPanel.getComponents().length == 0) {
						rightPanel.add(closed);
					}
					this.setSize(1000, 800);
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
		}
	}

	private void addFoodItem(String name) {
		JPanel foodPanel = new JPanel(new GridLayout(2, 2));
		foodPanel.setBorder(BorderFactory.createTitledBorder(""));
		Food item = new Food(name, 0);
		JLabel foodName = new JLabel(item.getName());
		JLabel empty = new JLabel("");
		JLabel foodRating;
		JPanel ratingPanel = new JPanel(new GridLayout(1, 5));
		for (int x = 1; x < 6; x++) {
			ratingPanel.add(new JButton("" + x));
			ratingPanel.getComponent(x - 1);
		}
		if (item.getRate() == 0.0) {
			foodRating = new JLabel("Rating: None");
		} else {
			foodRating = new JLabel("Rating:" + item.getRate());
		}
		foodPanel.add(foodName);
		foodPanel.add(empty);
		foodPanel.add(foodRating);
		foodPanel.add(ratingPanel);
		panels.add(foodPanel);
		rightPanel.add(foodPanel);
		System.out.println(item.getName());
		this.pack();
	}

	private void sendGet() throws Exception {

		String url = "https://api.hfs.purdue.edu/menus/v2/locations/" + currentDiningCourt + "/" + date;

		URL obj = new URL(url);
		HttpURLConnection con = (HttpURLConnection) obj.openConnection();

		// optional default is GET
		con.setRequestMethod("GET");

		// add request header
		con.setRequestProperty("User-Agent", USER_AGENT);

		con.setRequestProperty("Accept", "application/json");

		int responseCode = con.getResponseCode();
		System.out.println("\nSending 'GET' request to URL : " + url);
		System.out.println("Response Code : " + responseCode);

		BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
		String inputLine;
		StringBuffer response = new StringBuffer();

		while ((inputLine = in.readLine()) != null) {
			response.append(inputLine);
		}
		in.close();

		JSONResponse = response.toString();

		// print result
		System.out.println("Response: " + response.toString());

	}

	static class ActionBackOneDay implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			dayInt--;
			date = monthInt + "-" + dayInt + "-" + yearInt;
			time = new JLabel(date, SwingConstants.CENTER);
			dayOfWeek = LocalDate.of(yearInt, monthInt, dayInt).getDayOfWeek();
			day = dayOfWeek.toString();
			dayText = new JLabel(day, SwingConstants.CENTER);
			datePanel.removeAll();
			datePanel.updateUI();
			datePanel.add(dateHeader);
			datePanel.add(dayText);
			datePanel.add(time);
			datePanel.add(dayButtons);
		}
	}

	static class ActionForwardOneDay implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			dayInt++;
			date = monthInt + "-" + dayInt + "-" + yearInt;
			time = new JLabel(date, SwingConstants.CENTER);
			dayOfWeek = LocalDate.of(yearInt, monthInt, dayInt).getDayOfWeek();
			day = dayOfWeek.toString();
			dayText = new JLabel(day, SwingConstants.CENTER);
			datePanel.removeAll();
			datePanel.updateUI();
			datePanel.add(dateHeader);
			datePanel.add(dayText);
			datePanel.add(time);
			datePanel.add(dayButtons);
		}
	}

}