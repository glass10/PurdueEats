import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import javax.net.ssl.HttpsURLConnection;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
* Created by Nicholas on 10/15/2016.
*/
public class temp extends JFrame implements ActionListener {
   private JRadioButton[] courts = new JRadioButton[5];
   private JRadioButton[] times = new JRadioButton[3];
   public final String[] COURT_NAMES = {"Earhart", "Windsor", "Wiley", "Hillenbrand", "Ford"};
   public final String[] MEAL_TIMES = {"Breakfast", "Lunch", "Dinner"};
   private final String USER_AGENT = "Mozilla/5.0";
   private String JSONResponse;
   private JPanel rightPanel, leftPanel, courtPanel, mealPanel;


   public temp() {
       Container contentPane = this.getContentPane();
       contentPane.setLayout(new BorderLayout());

       JButton okButton = new JButton("OK");
       leftPanel = new JPanel(new GridLayout(3, 1));
       courtPanel = new JPanel(new GridLayout(5, 1));
       mealPanel = new JPanel(new GridLayout(3, 1));
       rightPanel = new JPanel(new GridLayout(0,1));
       JScrollPane scrollPane = new JScrollPane(rightPanel);
       for (int x = 0; x < 5; x++) {
           courts[x] = new JRadioButton(COURT_NAMES[x]);
           courts[x].addActionListener(this);
           courtPanel.add(courts[x]);
       }
       for (int x = 0; x < 3; x++) {
           times[x] = new JRadioButton(MEAL_TIMES[x]);
           times[x].addActionListener(this);
           mealPanel.add(times[x]);
       }
       /*for (int x = 0; x < 300; x++) {
           rightPanel.add(new JLabel("Label No." + x));
       }*/
       courtPanel.setBorder(BorderFactory.createTitledBorder(""));
       mealPanel.setBorder(BorderFactory.createTitledBorder(""));
       leftPanel.add(courtPanel);
       leftPanel.add(mealPanel);
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
               temp program = new temp();
           }
       });
   }

   public void actionPerformed(ActionEvent z) {
       Object obj = z.getSource();
       if (obj instanceof JRadioButton) {

       }
       if (obj instanceof JButton) {
           try {
               this.sendGet();
           } catch (Exception e) {
               e.printStackTrace();
           }
           try {
               JSONArray meals,stations,items;
               JSONObject foodObj,stationObj,mealObj;
               JSONObject json = new JSONObject(JSONResponse);
               System.out.println(json);
               meals = json.getJSONArray("Meals");
               for(int a = 0;a < meals.length();a++){
                   mealObj = meals.getJSONObject(a);
                   stations = mealObj.getJSONArray("Stations");
                   for(int b = 0;b < stations.length();b++){
                       stationObj = stations.getJSONObject(b);
                       items = stationObj.getJSONArray("Items");
                       for(int c = 0;c < items.length();c++){
                           foodObj = items.getJSONObject(c);
                           addFoodItem(foodObj.getString("Name"));
                       }
                   }
               }
           } catch (JSONException e) {
               e.printStackTrace();
           }
       }

   }

   private void addFoodItem(String name){
       JPanel foodPanel = new JPanel(new GridLayout(2,2));
       Food item = new Food(name,0);
       JLabel foodName = new JLabel(item.getName());
       JLabel empty = new JLabel("");
       JLabel foodRating = new JLabel("" + item.getRate());
       foodPanel.add(foodName);
       foodPanel.add(empty);
       foodPanel.add(foodRating);
       rightPanel.add(foodPanel);
       System.out.println(item.getName());
       this.pack();
   }

   private void sendGet() throws Exception {

       String url = "https://api.hfs.purdue.edu/menus/v2/locations/Earhart/10-16-2016";

       URL obj = new URL(url);
       HttpURLConnection con = (HttpURLConnection) obj.openConnection();

       // optional default is GET
       con.setRequestMethod("GET");

       //add request header
       con.setRequestProperty("User-Agent", USER_AGENT);

       con.setRequestProperty("Accept","application/json");

       int responseCode = con.getResponseCode();
       System.out.println("\nSending 'GET' request to URL : " + url);
       System.out.println("Response Code : " + responseCode);

       BufferedReader in = new BufferedReader(
               new InputStreamReader(con.getInputStream()));
       String inputLine;
       StringBuffer response = new StringBuffer();

       while ((inputLine = in.readLine()) != null) {
           response.append(inputLine);
       }
       in.close();

       JSONResponse = response.toString();

       //print result
       System.out.println("Response: " + response.toString());

   }

}

