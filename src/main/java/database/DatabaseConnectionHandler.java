package database;

import model.*;

import java.sql.*;
import java.util.ArrayList;


/**
 * This class handles all database related transactions
 */
public class DatabaseConnectionHandler {
    private static final String ORACLE_URL = "jdbc:oracle:thin:@localhost:1522:stu";
    private static final String EXCEPTION_TAG = "[EXCEPTION]";
    private static final String WARNING_TAG = "[WARNING]";

    private Connection connection = null;

    public DatabaseConnectionHandler() {
        try {
            // Load the Oracle JDBC driver
            // Note that the path could change for new drivers
            DriverManager.registerDriver(new oracle.jdbc.driver.OracleDriver());
        } catch (SQLException e) {
            System.out.println(EXCEPTION_TAG + " " + e.getMessage());
        }
    }

    public void close() {
        try {
            if (connection != null) {
                connection.close();
            }
        } catch (SQLException e) {
            System.out.println(EXCEPTION_TAG + " " + e.getMessage());
        }
    }


    public boolean login(String username, String password) {
        try {
            if (connection != null) {
                connection.close();
            }

            connection = DriverManager.getConnection(ORACLE_URL, username, password);
            connection.setAutoCommit(false);

            System.out.println("\nConnected to Oracle!");
            return true;
        } catch (SQLException e) {
            System.out.println(EXCEPTION_TAG + " " + e.getMessage());
            return false;
        }
    }

    private void rollbackConnection() {
        try {
            connection.rollback();
        } catch (SQLException e) {
            System.out.println(EXCEPTION_TAG + " " + e.getMessage());
        }
    }

    // Gets all vehicles that are available
    public VehicleModel[] getVehicleInfo() {
        ArrayList<VehicleModel> result = new ArrayList<VehicleModel>();

        try {
            Statement stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM Vehicle where STATUS = 'available'");

//    		// get info on ResultSet
//    		ResultSetMetaData rsmd = rs.getMetaData();
//
//    		System.out.println(" ");
//
//    		// display column names;
//    		for (int i = 0; i < rsmd.getColumnCount(); i++) {
//    			// get column name and print it
//    			System.out.printf("%-15s", rsmd.getColumnName(i + 1));
//    		}

            while (rs.next()) {
                VehicleModel model = new VehicleModel(rs.getInt("vid"),
                        rs.getString("vlicense"),
                        rs.getString("make"), rs.getString("model"), rs.getString("year"),
                        rs.getString("color"), rs.getDouble("odometer"), rs.getString("status"),
                        rs.getString("vtname"), rs.getString("location"), rs.getString("city"));
                result.add(model);
            }

            rs.close();
            stmt.close();
        } catch (SQLException e) {
            System.out.println(EXCEPTION_TAG + " " + e.getMessage());
        }

        return result.toArray(new VehicleModel[result.size()]);
    }

    // EFFECTS: returns the array of result sets for the query of getting available vehicles based on the params
    // note: you can use the size of the array to show the number of vehicles first!!
    public VehicleModel[] getVehicleInfo(String carType, String location, String city, String startTime, String endTime) {
        ArrayList<VehicleModel> result = new ArrayList<VehicleModel>();

        // Build up filter list based on input
        String cTypeFilter = "";
        String locFilter = "";
        String cityFilter = "";
        String timeIntFilter = "";
        if (carType != null && carType.length() > 0) {
            cTypeFilter = "AND v.vtname = '" + carType + "'";
        }
        if (location != null && location.length() > 0) {
            locFilter = "AND v.location = '" + location + "'";
        }
        if (city != null && city.length() > 0) {
            cityFilter = "AND v.city = '" + city + "'";
        }
        // Checks to see if a reservation has already been made from that interval
        if (startTime != null && endTime != null && startTime.length() > 0 && endTime.length() > 0) {
            //  not exists( Select * from reservation where timeInterval between (FROMDATETIME, TODATETIME)
            timeIntFilter = "and v.vid not in (select r.vid " +
                    "from reservation r, vehicle v1 " +
                    "where v1.vid = r.vid and r.fromDateTime <= to_timestamp('" + startTime + "','YYYY-MM-DD:HH24:MI')" +
                    "and r.toDateTime >= to_timestamp('" + endTime + "','YYYY-MM-DD:HH24:MI'))";
        }


        try {
            Statement stmt = connection.createStatement();
            String query = "SELECT distinct * FROM Vehicle v where status <> 'maintenance'"; // rented is okay because the current state does not matter
            // Add filter list to query
            query = query + cTypeFilter + locFilter + cityFilter + timeIntFilter;
            ResultSet rs = stmt.executeQuery(query);

//    		// get info on ResultSet
//    		ResultSetMetaData rsmd = rs.getMetaData();
//
//    		System.out.println(" ");
//
//    		// display column names;
//    		for (int i = 0; i < rsmd.getColumnCount(); i++) {
//    			// get column name and print it
//    			System.out.printf("%-15s", rsmd.getColumnName(i + 1));
//    		}

            // Reads sql result into vehicle array
            while (rs.next()) {
                VehicleModel model = new VehicleModel(rs.getInt("vid"),
                        rs.getString("vlicense"),
                        rs.getString("make"), rs.getString("model"), rs.getString("year"),
                        rs.getString("color"), rs.getDouble("odometer"), rs.getString("status"),
                        rs.getString("vtname"), rs.getString("location"), rs.getString("city"));
                result.add(model);
            }

            rs.close();
            stmt.close();
        } catch (SQLException e) {
            System.out.println(EXCEPTION_TAG + " " + e.getMessage());
        }

        return result.toArray(new VehicleModel[result.size()]);
    }

    // EFFECTS: returns the number of available vehicles based on params
    // this is redundant, but leaves here anyway...
    public int getAvailableNumOfVehicle(String carType, String location, String city, String startTime, String endTime) {
        ArrayList<VehicleModel> result = new ArrayList<VehicleModel>();

        // Build up filter list based on input
        String cTypeFilter = "";
        String locFilter = "";
        String cityFilter = "";
        String timeIntFilter = "";
        if (carType != null && carType.length() > 0) {
            cTypeFilter = "AND v.vtname = '" + carType + "'";
        }
        if (location != null && location.length() > 0) {
            locFilter = "AND v.location = '" + location + "'";
        }
        if (city != null && city.length() > 0) {
            cityFilter = "AND v.city = '" + city + "'";
        }
        // Checks to see if a reservation has already been made from that interval
        if (startTime != null && endTime != null && startTime.length() > 0 && endTime.length() > 0) {
            //  not exists( Select * from reservation where timeInterval between (FROMDATETIME, TODATETIME)
            timeIntFilter = "and v.vid not in (select r.vid " +
                    "from reservation r, vehicle v1 " +
                    "where v1.vid = r.vid and r.fromDateTime <= to_timestamp('" + startTime + "','YYYY-MM-DD:HH24:MI')" +
                    "and r.toDateTime >= to_timestamp('" + endTime + "','YYYY-MM-DD:HH24:MI'))";
        }

        /*
        select distinct * from vehicle v where v.vtname = 'Compact' and v.location = 'UBC' and v.city = 'Vancouver'
and v.vid not in (select r.vid from reservation r, vehicle v1 where v1.vid = r.vid and r.fromDateTime <= to_timestamp('2019-01-02:00:00','YYYY-MM-DD:HH24:MI')
and r.toDateTime >= to_timestamp('2019-01-03','YYYY-MM-DD'))
         */
        int resultNum = 0;

        try {
            Statement stmt = connection.createStatement();
            String query = "SELECT distinct * FROM Vehicle v where status <> 'maintenance'"; // rented is okay because the current state does not matter
            // Add filter list to query
            query = query + cTypeFilter + locFilter + cityFilter + timeIntFilter;
            ResultSet rs = stmt.executeQuery(query);

//    		// get info on ResultSet
//    		ResultSetMetaData rsmd = rs.getMetaData();
//
//    		System.out.println(" ");
//
//    		// display column names;
//    		for (int i = 0; i < rsmd.getColumnCount(); i++) {
//    			// get column name and print it
//    			System.out.printf("%-15s", rsmd.getColumnName(i + 1));
//    		}

            // Reads sql result into vehicle array
            while (rs.next()) {
                resultNum++;
            }

            rs.close();
            stmt.close();
        } catch (SQLException e) {
            System.out.println(EXCEPTION_TAG + " " + e.getMessage());
        }

        return resultNum;
    }


//tested
    // REQUIRES: all the inputs to be non-empty
    // EFFECTS: Makes a reservation and returns confirmation number
    //          if a reservation cannot be made for some reason, it returns -1
    public int makeReservation(String phoneNumber, String name, String address, String dlicense, String vid,
                               String fromDateTime, String toDateTime) {
        int confNo = -1;
        if (!isCustomerMember(phoneNumber)) {
            boolean status = addNewCustomer(phoneNumber, name, address, dlicense);
        }

        if (isOverBooked(vid, fromDateTime, toDateTime)) {
            return confNo;
        }

        PreparedStatement ps = null;
        try {
            ps = connection.prepareStatement("insert into reservation (vid, cellphone, fromDateTime, toDateTime) values ( " +
                    "?, ?, to_timestamp(?, 'YYYY-MM-DD:HH24:MI'), " +
                    "to_timestamp(?, 'YYYY-MM-DD:HH24:MI'))");
            ps.setInt(1, Integer.parseInt(vid));
            ps.setString(2, phoneNumber);
            ps.setString(3, fromDateTime);
            ps.setString(4, toDateTime);

            ps.executeUpdate();
            connection.commit();

            ps.close();

        } catch (SQLException e) {
            e.printStackTrace();
            rollbackConnection();
        }


        // get confirmation number
        try {
            Statement stmt = connection.createStatement();
            String query = "select confNo from reservation where confNo = " +
                    "(select max(confNo) from reservation)";
            ResultSet rs = stmt.executeQuery(query);

            rs.next();
            confNo = rs.getInt("confNo");

            rs.close();
            stmt.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return confNo;
    }

// tested
    // EFFECTS: returns the Reservation detail based on the confirmation number
    public ReservationModel getReservation(int confNo) {
        ReservationModel model = null;
        try {
            Statement stmt = connection.createStatement();
            String query = "select * from reservation where confNo = " + confNo;
            ResultSet rs = stmt.executeQuery(query);

            rs.next();
            model = new ReservationModel(rs.getInt("confNo"), rs.getInt("vid"),
                    rs.getString("cellphone"), rs.getTimestamp("fromDateTime"), rs.getTimestamp("toDateTime"));

            rs.close();
            stmt.close();

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return model;
    }

//tested
    // REQUIRES: all the inputs are in the valid format
    // EFFECTS: Rents a vehicle and returns confirmation number (rid)
    public int rentVehicle(String vid, String cellphone, String fromDateTime, String toDateTime, String odometer,
                           String confNo, String cardName, String cardNo, String expDate) {

        boolean isSuccessful = false;
        int rid = -1;
        try {
            PreparedStatement ps = connection.prepareStatement("insert into rent (vid, cellphone, fromDateTime, " +
                    "toDateTime, odometer, cardName, cardNo, expDate, confNo) values (" +
                    "?, ?, to_timestamp(?,'YYYY-MM-DD:HH24:MI'), to_timestamp(?,'YYYY-MM-DD:HH24:MI'), " +
                    "?, ?, ?, to_date(?, 'YYYY-MM-DD'), ?)");
            ps.setInt(1, Integer.parseInt(vid));
            ps.setString(2, cellphone);
            ps.setString(3, fromDateTime);
            ps.setString(4, toDateTime);
            ps.setDouble(5, Double.parseDouble(odometer));
            ps.setString(6, cardName);
            ps.setString(7, cardNo);
            ps.setString(8, expDate);
            ps.setInt(9, Integer.parseInt(confNo));

            ps.executeUpdate();
            connection.commit();
            isSuccessful = true;

            ps.close();

        } catch (SQLException e) {
            e.printStackTrace();
            rollbackConnection();
        }

        if (isSuccessful) {
            try {
                PreparedStatement ps = connection.prepareStatement("update vehicle set status = 'rented' where vid = ?");
                ps.setInt(1, Integer.parseInt(vid));
                ps.executeUpdate();
                connection.commit();

                ps.close();

            } catch (SQLException e) {
                e.printStackTrace();
                rollbackConnection();
            }

            rid = getRidForRent(Integer.parseInt(confNo), Integer.parseInt(vid));

        }
        return rid;

    }

    //tested
    // EFFECTS: returns the rid (confirmation of the rent) based on the confNo of reservation and vid
    public int getRidForRent(int confNo, int vid) {
        int rid = -1;
        try {
            Statement stmt = connection.createStatement();
            String query = "SELECT rid from Rent where confNo = " + confNo + " AND vid = " + vid;
            ResultSet rs = stmt.executeQuery(query);

            rs.next();
            rid = rs.getInt("rid");

            rs.close();
            stmt.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return rid;

    }


//tested
    // REQUIRES: all inputs must be in the valid format
    // EFFECTS: returns the confirmation message upon the successful return of vehicle
    public ReturnConfirmationMessageModel returnVehicle(String rid, String returnDateTime, String odometer, String fulltank) {
        ReturnConfirmationMessageModel confMessage = null;

        // double afterOdometer = Double.parseDouble(odometer);

        // calculate the amount
        VehicleTypeModel rateInfo = getRateInfo(Integer.parseInt(rid));
        TimeIntervalOdometerModel timeIntOdometer = getTimeIntervalAndOdometer(Integer.parseInt(rid));

        Timestamp fromDateTime = timeIntOdometer.getFromDateTime();
        Timestamp toDateTime = timeIntOdometer.getToDateTime();
        // double originalOdometer = timeIntOdometer.getOdometer();
        long startTime = fromDateTime.getTime();
        long endTime = toDateTime.getTime();
        long timeIntervalInHour = (endTime - startTime) / 3600000;

        long weeks = timeIntervalInHour / 168;
        timeIntervalInHour = timeIntervalInHour % 168;
        long days = timeIntervalInHour / 24;
        long hours = timeIntervalInHour % 24;

        double regularCost = weeks * rateInfo.getWrate() + days * rateInfo.getDrate() + hours * rateInfo.getHrate();
        double insuranceCost = weeks * rateInfo.getWirate() + days * rateInfo.getDirate() + hours * rateInfo.getHirate();
        double value = regularCost + insuranceCost;

        String regularCalDetail = "Payment Rate for this Vehicle:" + rateInfo.toString() + "￿￿\n"
                + "Regular Cost -> " + weeks + " week(s) * " + rateInfo.getWrate() + " + "
                + days + " day(s) * " + rateInfo.getDrate() + " + "
                + hours + " hour(s) * " + rateInfo.getHrate() + " = " + regularCost + "￿￿\n";
        String insCalDetail = "Insurance Cost -> " + weeks + " week(s) * " + rateInfo.getWirate() + " + "
                + days + " day(s) * " + rateInfo.getDirate() + " + "
                + hours + " hour(s) * " + rateInfo.getHirate() + " = " + insuranceCost + "￿￿\n"
                + "Total: " + value;

        String calculationDetail = regularCalDetail + insCalDetail;

        boolean success = false;
        try {
            PreparedStatement ps = connection.prepareStatement("insert into return values " +
                    "(?, to_timestamp(?,'YYYY-MM-DD:HH24:MI'), ?, ?, ?)");
            ps.setInt(1, Integer.parseInt(rid));
            ps.setString(2, returnDateTime);
            ps.setDouble(3, Double.parseDouble(odometer));
            ps.setString(4, fulltank);
            ps.setDouble(5, value);

            ps.executeUpdate();
            connection.commit();

            /*
            insert into return values (13, to_date('2200-03-19','YYYY-MM-DD'), 999999999, 'T', 1000)
/
update vehicle set status = 'available' where vid = ANY (select v.vid from vehicle v, rent r where v.vid = r.vid and r.rid = 13)
/
             */
            ps.close();
            success = true;

        } catch (SQLException e) {
            e.printStackTrace();
            rollbackConnection();
        }

        boolean success2 = false;

        try {
            PreparedStatement ps = connection.prepareStatement("update vehicle set status = 'available'" +
                    " where vid = ANY (select v.vid from vehicle v, rent r where v.vid = r.vid and r.rid = ?)");
            ps.setInt(1, Integer.parseInt(rid));

            ps.executeUpdate();
            connection.commit();


            ps.close();
            success2 = true;

        } catch (SQLException e) {
            e.printStackTrace();
            rollbackConnection();
        }

        if (success && success2) {
            confMessage = new ReturnConfirmationMessageModel(Integer.parseInt(rid), returnDateTime, value, calculationDetail);
            return confMessage;

        } else {
            return null;
        }
    }

//todo test
    // EFFECTS: returns the vehicles rented our on that day
    public VehicleModel[] generateReportDailyRentalsAllVehicleInfo(String date) {
        ArrayList<VehicleModel> result = new ArrayList<>();

        try {
            Statement stmt = connection.createStatement();
            String query = "SELECT v.vid, v.vlicense, v.make, v.model, v.year, " +
                    "v.color, v.odometer, v.status, v.vtname, v.location, v.city" +
                    "FROM Rent r, Vehicle v " +
                    "WHERE fromDateTime <= to_timestamp('" + date + ":00:00', 'YYYY-MM-DD:HH24:MI') " +
                    "AND toDateTime >=  to_timestamp('" + date + ":23:59', 'YYYY-MM-DD:HH24:MI') " +
                    "AND r.vid = v.vid " +
                    "ORDER BY v.city, v.location, v.vtname";

            ResultSet rs = stmt.executeQuery(query);

            while (rs.next()) {
                VehicleModel model = new VehicleModel(rs.getInt("v.vid"),
                        rs.getString("v.vlicense"),
                        rs.getString("v.make"), rs.getString("v.model"), rs.getString("v.year"),
                        rs.getString("v.color"), rs.getDouble("v.odometer"), rs.getString("v.status"),
                        rs.getString("v.vtname"), rs.getString("v.location"), rs.getString("v.city"));
                result.add(model);

            }

            rs.close();
            stmt.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return result.toArray(new VehicleModel[result.size()]);

        /*
         VID					   NOT NULL NUMBER(38)
 VLICENSE					    CHAR(6)
 MAKE						    VARCHAR2(30)
 MODEL						    VARCHAR2(30)
 YEAR						    VARCHAR2(4)
 COLOR						    VARCHAR2(20)
 ODOMETER					    NUMBER
 STATUS 					    VARCHAR2(20)
 VTNAME 					    VARCHAR2(9)
 LOCATION					    VARCHAR2(20)
 CITY						    VARCHAR2(20)
         */
    }

    //todo test

    // EFFECTS: returns the number of vehicles rented out on that day grouped by vehicle
    public ReportGroupedByVehilceModel[] getNumOfVehicleDailyRentalGBVehicle(String date) {
        ArrayList<ReportGroupedByVehilceModel> result = new ArrayList<>();
        try {
            Statement stmt = connection.createStatement();
            String query = "SELECT COUNT(*) as total, v.vtname " +
                    "FROM Vehicle v, Rent r " +
                    "WHERE fromDateTime <= to_timestamp('" + date + ":00:00','YYYY-MM-DD:HH24:MI') " +
                    "AND toDateTime >= to_timestamp('" + date + ":23:59','YYYY-MM-DD:HH24:MI') AND r.vid = " +
                    "v.vid " +
                    "GROUP BY v.vtname";

            ResultSet rs = stmt.executeQuery(query);

            while (rs.next()) {
                ReportGroupedByVehilceModel model = new ReportGroupedByVehilceModel(rs.getInt("total"),
                        rs.getString("v.vtname"));
                result.add(model);
            }

            rs.close();
            stmt.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return result.toArray(new ReportGroupedByVehilceModel[result.size()]);

    }
//todo test

    // EFFECTS: returns the number of vehicles rented out on that day grouped by branch
    public ReportGroupByBranchModel[] getNumOfVehicleDailyRentalGBBranch(String date) {
        ArrayList<ReportGroupByBranchModel> result = new ArrayList<>();
        try {
            Statement stmt = connection.createStatement();
            String query = "SELECT COUNT(*) as total, v.location, v.city " +
                    "FROM Vehicle v, Rent r " +
                    "WHERE fromDateTime <=  to_timestamp('" + date + ":00:00','YYYY-MM-DD:HH24:MI') " +
                    "AND toDateTime >=  to_timestamp('" + date + ":23:59','YYYY-MM-DD:HH24:MI') AND r.vid = v.vid " +
                    "GROUP BY v.location, v.city";

            ResultSet rs = stmt.executeQuery(query);

            while (rs.next()) {
                ReportGroupByBranchModel model = new ReportGroupByBranchModel(rs.getInt("total"),
                        rs.getString("v.location"), rs.getString("v.city"));
                result.add(model);
            }

            rs.close();
            stmt.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return result.toArray(new ReportGroupByBranchModel[result.size()]);

    }

    //todo test

    // EFFECTS: returns the number of new rental on that day in the entire company
    public int getNumOfVehicleNewlyDailyRental(String date) {
        int total = -1;
        try {
            Statement stmt = connection.createStatement();
            String query = "SELECT COUNT(*) as total " +
                    "FROM Rent " +
                    "WHERE fromDateTime >=  to_timestamp('2019-01-07:00:00','YYYY-MM-DD:HH24:MI') " +
                    "AND fromDateTime <=  to_timestamp('2019-01-07:23:59','YYYY-MM-DD:HH24:MI')";

            ResultSet rs = stmt.executeQuery(query);

            rs.next();
            total = rs.getInt("total");

            rs.close();
            stmt.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return total;

    }

    // EFFECTS: returns the vehicles rented our on that day on the specified branch
    public VehicleModel[] generateReportDailyRentalsAllVehicleInfoOnBranch(String date, String location, String city) {
        ArrayList<VehicleModel> result = new ArrayList<>();

        try {
            Statement stmt = connection.createStatement();
            String query = "SELECT v.vid, v.vlicense, v.make, v.model, v.year," +
                    "v.color, v.odometer, v.status, v.vtname, v.location, v.city" +
                    "FROM Rent r, Vehicle v" +
                    "WHERE fromDateTime <=  to_timestamp('" + date + ":00:00','YYYY-MM-DD:HH24:MI') " +
                    "AND toDateTime >=  to_timestamp('" + date + ":23:59', 'YYYY-MM-DD:HH24:MI')  " +
                    "AND r.vid = v.vid AND v.location = '" + location + "' AND v.city = '" + city + "' " +
                    "ORDER BY v.vtname";

            ResultSet rs = stmt.executeQuery(query);

            while (rs.next()) {
                VehicleModel model = new VehicleModel(rs.getInt("v.vid"),
                        rs.getString("v.vlicense"),
                        rs.getString("v.make"), rs.getString("v.model"), rs.getString("v.year"),
                        rs.getString("v.color"), rs.getDouble("v.odometer"), rs.getString("v.status"),
                        rs.getString("v.vtname"), rs.getString("v.location"), rs.getString("v.city"));
                result.add(model);

            }

            rs.close();
            stmt.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return result.toArray(new VehicleModel[result.size()]);
    }


    // EFFECTS: returns the number of vehicles rented out on that day grouped by vehicle on the branch
    public ReportGroupedByVehilceModel[] getNumOfVehicleDailyRentalGBVehicleOnBranch(String date, String location, String city) {
        ArrayList<ReportGroupedByVehilceModel> result = new ArrayList<>();
        try {
            Statement stmt = connection.createStatement();
            String query = "SELECT COUNT(*) as total, v.vtname " +
                    "FROM Vehicle v, Rent r " +
                    "WHERE fromDateTime <= to_timestamp('" + date + ":00:00', 'YYYY-MM-DD:HH24:MI')  " +
                    "AND  toDateTime >= to_timestamp('" + date + ":23:59', 'YYYY-MM-DD:HH24:MI') AND r.vid = " +
                    "v.vid " +
                    "AND v.location = '" + location + "' AND v.city = '" + city + "' " +
                    "GROUP BY v.vtname";

            ResultSet rs = stmt.executeQuery(query);

            while (rs.next()) {
                ReportGroupedByVehilceModel model = new ReportGroupedByVehilceModel(rs.getInt("total"),
                        rs.getString("v.vtname"));
                result.add(model);
            }

            rs.close();
            stmt.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return result.toArray(new ReportGroupedByVehilceModel[result.size()]);

    }


    // EFFECTS: returns the number of vehicle rented out on that day in the branch
    public int getNumOfVehicleDailyRentalOnBranch(String date, String location, String city) {
        int total = -1;
        try {
            Statement stmt = connection.createStatement();
            String query = "SELECT COUNT(*) as total " +
                    "FROM Vehicle v, Rent r " +
                    "WHERE fromDateTime <=  to_timestamp('" + date + ":00:00','YYYY-MM-DD:HH24:MI') " +
                    "AND toDateTime >= to_timestamp('" + date + ":23:59', 'YYYY-MM-DD:HH24:MI') AND r.vid = " +
                    "v.vid AND v.location = '" + location + "' AND v.city = '" + city + "'";

            ResultSet rs = stmt.executeQuery(query);

            rs.next();
            total = rs.getInt("total");

            rs.close();
            stmt.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return total;

    }


    // EFFECTS: returns the number of new rental on that day in the branch
    public int getNumOfVehicleNewlyDailyRentalOnBranch(String date, String location, String city) {
        int total = -1;
        try {
            Statement stmt = connection.createStatement();
            String query = "SELECT COUNT(*) as total " +
                    "FROM Rent r, Vehicle v " +
                    "WHERE fromDateTime >= to_timestamp('" + date + ":00:00','YYYY-MM-DD:HH24:MI') " +
                    "AND fromDateTime <= to_timestamp('" + date + ":23:59', 'YYYY-MM-DD:HH24:MI') " +
                    "AND r.vid = v.vid AND v.location = '" + location + "' AND v.city = '" + city + "'";

            ResultSet rs = stmt.executeQuery(query);

            rs.next();
            total = rs.getInt("total");

            rs.close();
            stmt.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return total;

    }


    // EFFECTS: returns all the vehicle returned on that day in the entire ccompany
    public VehicleModel[] generateReportDailyReturnsAllVehicleInfo(String date) {
        ArrayList<VehicleModel> result = new ArrayList<>();
        try {
            Statement stmt = connection.createStatement();
            String query = "SELECT v.vid, v.vlicense, v.make, v.model, v.year, " +
                    "v.color, v.odometer, v.status, v.vtname, v.location, v.city " +
                    "FROM Return r, Rent rt, Vehicle v " +
                    "WHERE r.rid = rt.rid AND rt.vid = v.vid AND r.returnDateTime >= to_timestamp('" + date + ":00:00', 'YYYY-MM-DD:HH24:MI') " +
                    "AND r.returnDateTime <= to_timestamp('" + date + ":23:59', 'YYYY-MM-DD:HH24:MI') " +
                    "ORDER BY v.city, v.location, v.vtname";

            //v.vid, v.vlicense, v.make, v.model, v.year, v.color, v.odometer, v.status, v.vtname, v.location, v.city

            ResultSet rs = stmt.executeQuery(query);

            while (rs.next()) {
                VehicleModel model = new VehicleModel(rs.getInt("v.vid"),
                        rs.getString("v.vlicense"),
                        rs.getString("v.make"), rs.getString("v.model"), rs.getString("v.year"),
                        rs.getString("v.color"), rs.getDouble("v.odometer"), rs.getString("v.status"),
                        rs.getString("v.vtname"), rs.getString("v.location"), rs.getString("v.city"));
                result.add(model);

            }

            rs.close();
            stmt.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return result.toArray(new VehicleModel[result.size()]);

    }


    // EFFECTS: returns the number of vehicles returned on the day grouped by vtname in the entire company
    public ReportGroupedByVehilceModel[] getNumOdVehicleDailyReturnGBVehicle(String date) {
        ArrayList<ReportGroupedByVehilceModel> result = new ArrayList<>();

        try {
            Statement stmt = connection.createStatement();
            String query = "SELECT count(*) as total, v.vtname " +
                    "FROM Return r, Rent rt, Vehicle v " +
                    "WHERE r.rid = rt.rid AND rt.vid = v.vid AND r.returnDateTime >= to_timestamp('" + date + ":00:00', 'YYYY-MM-DD:HH24:MI') " +
                    "AND r.returnDateTime <= to_timestamp('" + date + ":23:59', 'YYYY-MM-DD:HH24:MI') " +
                    "GROUP BY v.vtname";

            ResultSet rs = stmt.executeQuery(query);

            while (rs.next()) {
                ReportGroupedByVehilceModel model = new ReportGroupedByVehilceModel(rs.getInt("total"),
                        rs.getString("v.vtname"));
                result.add(model);
            }

            rs.close();
            stmt.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return result.toArray(new ReportGroupedByVehilceModel[result.size()]);


    }


    // EFFECTS: returns the total revenue per vtname in the entire company
    public RevenueReportGroupedByVehilceModel[] getRevenueDailyReturnGBVehicle(String date) {
        ArrayList<RevenueReportGroupedByVehilceModel> result = new ArrayList<>();

        try {
            Statement stmt = connection.createStatement();
            String query = "SELECT sum(r.value) as total, v.vtname " +
                    "FROM Return r, Rent rt, Vehicle v " +
                    "WHERE r.rid = rt.rid AND rt.vid = v.vid AND r.returnDateTime >= to_timestamp('" + date + ":00:00', 'YYYY-MM-DD:HH24:MI') " +
                    "AND r.returnDateTime <= to_timestamp('" + date + ":23:59', 'YYYY-MM-DD:HH24:MI') " +
                    "GROUP BY v.vtname";

            ResultSet rs = stmt.executeQuery(query);

            while (rs.next()) {
                RevenueReportGroupedByVehilceModel model = new RevenueReportGroupedByVehilceModel(rs.getDouble("total"),
                        rs.getString("v.vtname"));
                result.add(model);
            }

            rs.close();
            stmt.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return result.toArray(new RevenueReportGroupedByVehilceModel[result.size()]);

    }

    public ReportTotalNumAndRevenueGBBranchModel[] getTotalNumAndRevenueGBBranch(String date) {
        ArrayList<ReportTotalNumAndRevenueGBBranchModel> result = new ArrayList<>();

        try {
            Statement stmt = connection.createStatement();
            String query = "SELECT sum(r.value) as totalRev, count(*) as totalNum, v.location, v.city " +
                    "FROM Return r, Rent rt, Vehicle v " +
                    "WHERE r.rid = rt.rid AND rt.vid = v.vid AND r.returnDateTime >= to_timestamp('" + date + ":00:00', 'YYYY-MM-DD:HH24:MI') " +
                    "AND r.returnDateTime <= to_timestamp('" + date + ":23:59', 'YYYY-MM-DD:HH24:MI') " +
                    "GROUP BY v.city, v.location";

            ResultSet rs = stmt.executeQuery(query);

            while (rs.next()) {
                ReportTotalNumAndRevenueGBBranchModel model = new ReportTotalNumAndRevenueGBBranchModel(rs.getInt("totalRev"),
                        rs.getInt("totalNum"), rs.getString("v.location"), rs.getString("v.city"));
                result.add(model);
            }

            rs.close();
            stmt.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return result.toArray(new ReportTotalNumAndRevenueGBBranchModel[result.size()]);

    }


    // EFFECTS: returns the information of vehicle returned on that day on the specified branch
    public VehicleModel[] generateReportDailyReturnsAllVehicleInfoOnBranch(String date, String city, String location) {
        ArrayList<VehicleModel> result = new ArrayList<>();
        try {
            Statement stmt = connection.createStatement();
            String query = "SELECT v.vid, v.vlicense, v.make, v.model, v.year, v.color, v.odometer, v.status, v.vtname, v.location, v.city " +
                    "FROM Return r, Rent rt, Vehicle v " +
                    "WHERE r.rid = rt.rid AND rt.vid = v.vid AND r.returnDateTime >= to_timestamp('" + date + ":00:00','YYYY-MM-DD:HH24:MI') " +
                    "AND r.returnDateTime <= to_timestamp('" + date + ":23:59', 'YYYY-MM-DD:HH24:MI') " +
                    "AND v.location = '" + location + "' AND v.city = '" + city + "' " +
                    "ORDER BY v.vtname";

            //v.vid, v.vlicense, v.make, v.model, v.year, v.color, v.odometer, v.status, v.vtname, v.location, v.city

            ResultSet rs = stmt.executeQuery(query);

            while (rs.next()) {
                VehicleModel model = new VehicleModel(rs.getInt("v.vid"),
                        rs.getString("v.vlicense"),
                        rs.getString("v.make"), rs.getString("v.model"), rs.getString("v.year"),
                        rs.getString("v.color"), rs.getDouble("v.odometer"), rs.getString("v.status"),
                        rs.getString("v.vtname"), rs.getString("v.location"), rs.getString("v.city"));
                result.add(model);

            }

            rs.close();
            stmt.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return result.toArray(new VehicleModel[result.size()]);
    }


    // EFFECTS: returns the number of vehicles returned on the day grouped by vtname on the branch
    public ReportGroupedByVehilceModel[] getNumOdVehicleDailyReturnGBVehicleOnBranch(String date, String location, String city) {
        ArrayList<ReportGroupedByVehilceModel> result = new ArrayList<>();

        try {
            Statement stmt = connection.createStatement();
            String query = "SELECT count(*) as total, v.vtname " +
                    "FROM Return r, Rent rt, Vehicle v " +
                    "WHERE r.rid = rt.rid AND rt.vid = v.vid AND r.returnDateTime >= to_timestamp('" + date + ":00:00','YYYY-MM-DD:HH24:MI') " +
                    "AND r.returnDateTime <= to_timestamp('" + date + ":23:59', 'YYYY-MM-DD:HH24:MI')  " +
                    "AND v.location = '" + location + "' AND v.city = '" + city + "' " +
                    "GROUP BY v.vtname";

            ResultSet rs = stmt.executeQuery(query);

            while (rs.next()) {
                ReportGroupedByVehilceModel model = new ReportGroupedByVehilceModel(rs.getInt("total"),
                        rs.getString("v.vtname"));
                result.add(model);
            }

            rs.close();
            stmt.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return result.toArray(new ReportGroupedByVehilceModel[result.size()]);


    }


    // EFFECTS: returns the total revenue per vtname on the branch
    public RevenueReportGroupedByVehilceModel[] getRevenueDailyReturnGBVehicleOnBranch(String date, String location, String city) {
        ArrayList<RevenueReportGroupedByVehilceModel> result = new ArrayList<>();

        try {
            Statement stmt = connection.createStatement();
            String query = "SELECT sum(r.value) as total, v.vtname " +
                    "FROM Return r, Rent rt, Vehicle v " +
                    "WHERE r.rid = rt.rid AND rt.vid = v.vid AND r.returnDateTime >= to_timestamp('" + date + ":00:00','YYYY-MM-DD:HH24:MI') " +
                    "AND r.returnDateTime <= to_timestamp('" + date + ":23:59', 'YYYY-MM-DD:HH24:MI') " +
                    "AND v.location = '" + location + "' AND v.city = '" + city + "' " +
                    "GROUP BY v.vtname";

            ResultSet rs = stmt.executeQuery(query);

            while (rs.next()) {
                RevenueReportGroupedByVehilceModel model = new RevenueReportGroupedByVehilceModel(rs.getDouble("total"),
                        rs.getString("v.vtname"));
                result.add(model);
            }

            rs.close();
            stmt.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return result.toArray(new RevenueReportGroupedByVehilceModel[result.size()]);

    }

    // EFFECTS: returns the total revenue and total num of cars returned on that day on the branch
    public ReportTotalNumAndRevenueOnBranchModel getTotalRevAndNumRentalsOnBranch(String date, String location, String city) {
        ReportTotalNumAndRevenueOnBranchModel result = null;
        try {
            Statement stmt = connection.createStatement();
            String query = "SELECT sum(r.value) as totalRev, count(*) as totalNum " +
                    "FROM Return r, Rent rt, Vehicle v " +
                    "WHERE r.rid = rt.rid AND rt.vid = v.vid AND r.returnDateTime >= to_timestamp('" + date + ":00:00','YYYY-MM-DD:HH24:MI') " +
                    "AND r.returnDateTime <= to_timestamp('" + date + ":23:59', 'YYYY-MM-DD:HH24:MI') " +
                    "AND v.location = '" + location + "' AND v.city = '" + city + "'";

            ResultSet rs = stmt.executeQuery(query);

            rs.next();
            result = new ReportTotalNumAndRevenueOnBranchModel(rs.getDouble("totalRev"), rs.getInt("totalNum"));


            rs.close();
            stmt.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return result;

    }


    //EFFECTS: returns true if a customer is successfully added
    //          returns false other wise
    public boolean addNewCustomer(String cellphone, String name, String address, String dlicense) {
        boolean isSuccessful = false;
        try {
            PreparedStatement ps = connection.prepareStatement("insert into customer values (?,?,?,?)");
            ps.setString(1, cellphone);
            ps.setString(2, name);
            ps.setString(3, address);
            ps.setString(4, dlicense);

            ps.executeUpdate();
            connection.commit();

            ps.close();
            isSuccessful = true;
        } catch (SQLException e) {
            e.printStackTrace();
            rollbackConnection();
        }
        return isSuccessful;


    }

    //tested
    // REQUIRES: cellphone has to be in a valid format, and cannot be empty
    // EFFECTS: returns true if the customer is already a member
    //          returns false otherwise
    public boolean isCustomerMember(String cellphone) {
        int resultCount = 0;
        try {
            Statement stmt = connection.createStatement();
            String query = "select count(*) as total from customer where cellphone = '" + cellphone + "'";
            ResultSet rs = stmt.executeQuery(query);

            while (rs.next()) {
                resultCount = rs.getInt("total");
            }

            rs.close();
            stmt.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return resultCount > 0;

    }

    //tested
    // REQUIRES: all inputs cannot be null and all inputs are in valid format
    // EFFECTS: returns true if there is overbooking based on the params
    //          returns false otherwise
    public boolean isOverBooked(String vid, String fromDateTime, String toDateTime) {
        int resultCount = 0;
        try {
            Statement stmt = connection.createStatement();
            String query = "select count(*) as total " +
                    "from reservation " +
                    "where vid = " + Integer.parseInt(vid) + " and fromDateTime <= to_timestamp('" + fromDateTime + "', 'YYYY-MM-DD:HH24:MI') " +
                    "and toDateTime >= to_timestamp('" + toDateTime + "', 'YYYY-MM-DD:HH24:MI')";
            ResultSet rs = stmt.executeQuery(query);

            while (rs.next()) {
                resultCount = rs.getInt("total");
            }

            rs.close();
            stmt.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return resultCount > 0;

    }

    // EFFECTS: returns the confirmation message model for renting based on the rid
    public RentConfirmationMessageModel getRentConfMessage(int rid) {
        RentConfirmationMessageModel result = null;
        try {
            Statement stmt = connection.createStatement();
            String query = "select r.rid, r.cellphone, r.fromDateTime, r.toDateTime, r.cardName, r.cardNo, r.expDate, " +
                    "r.odometer, r.confNo, v.vid, v.vlicense, v.vtname, v.location, v.city " +
                    "from Rent r, Vehicle v where r.vid = v.vid and r.rid =" + rid;

            ResultSet rs = stmt.executeQuery(query);

            rs.next();
            result = new RentConfirmationMessageModel(rs.getInt("r.rid"), rs.getString("r.cellphone"),
                    rs.getTimestamp("r.fromDateTime"), rs.getTimestamp("r.toDateTime"),
                    rs.getString("r.cardName"), rs.getString("r.cardNo"), rs.getDate("r.expDate"),
                    rs.getDouble("r.odometer"), rs.getInt("r.confNo"), rs.getInt("v.vid"),
                    rs.getString("v.vlicense"), rs.getString("v.vtname"), rs.getString("v.location"),
                    rs.getString("v.city"));

            rs.close();
            stmt.close();

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;


    }

    //tested
    public VehicleTypeModel getRateInfo(int rid) {
        VehicleTypeModel result = null;
        try {
            Statement stmt = connection.createStatement();
            String query = "select * from VehicleType vt " +
                    "where vt.vtname = ANY( select v.vtname From rent r, " +
                    "vehicle v where r.vid = v.vid and r.rid = " + rid + ")";

            ResultSet rs = stmt.executeQuery(query);

            rs.next();
            result = new VehicleTypeModel(rs.getString("vtname"), rs.getString("features"),
                    rs.getFloat("wrate"), rs.getFloat("drate"), rs.getFloat("hrate"),
                    rs.getFloat("wirate"), rs.getFloat("dirate"), rs.getFloat("hirate"),
                    rs.getFloat("krate"));

            rs.close();
            stmt.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return result;

    }

    // EFFECTS: returns the model for time interval and odometer to calculate cost
    public TimeIntervalOdometerModel getTimeIntervalAndOdometer(int rid) {
        TimeIntervalOdometerModel result = null;
        try {
            Statement stmt = connection.createStatement();
            String query = "select fromDateTime, toDateTime, odometer " +
                    "from rent " +
                    "where rid = " + rid;
            ResultSet rs = stmt.executeQuery(query);

            rs.next();
            result = new TimeIntervalOdometerModel(rs.getTimestamp("fromDateTime"), rs.getTimestamp("toDateTime"),
                    rs.getDouble("odometer"));

            rs.close();
            stmt.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return result;

        /*
        select fromDateTime, toDateTime, odometer
from rent
where rid = 4
         */
    }


}
