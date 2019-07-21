package com.revature.proj1.datalayer;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.revature.proj1.beans.Reimbursement;
import com.revature.proj1.utils.Company;

import oracle.jdbc.OracleTypes;

public class ReimbursementDAOImplementor implements ReimbursementDAOInterface {
	public static ConnFactory cF = ConnFactory.getInstance();
	
	//0 is pending, 1 accepted, 2 rejected for reim status
	public ArrayList<Reimbursement> grabReimbursmentList() {
		ArrayList<Reimbursement> reimList = new ArrayList<>();
		String sql = "SELECT * FROM REIMBURSEMENT";
		Reimbursement reim = null;
		try(Connection conn = cF.getConnection()){
			PreparedStatement prep = conn.prepareStatement(sql);
			ResultSet rs = prep.executeQuery();
			while(rs.next()) {
				//Remember that id is the object's hashcode
				reim = new Reimbursement(rs.getString("EMP_USERNAME"), rs.getInt("STATUS"),
						rs.getDouble("AMOUNT"), rs.getString("NOTES"));
				reimList.add(reim);
			}
			return reimList;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//log catastrophic failure
		return null;
	}

	public void pushReimbursementList(List<Reimbursement> reimList) {
		for(Reimbursement r: reimList) {
			addReimbursementDB(r);
		}
	}
	
	//We can add a single reimbursement to the DB and the list
	public void addReimbursementDB(Reimbursement r) {
		String sql = "CALL ADD_REIMBURSEMENT(?,?,?,?,?,?)";
		if(!Company.reimbursements.contains(r)) {
			Company.reimbursements.add(r);
		}
		try (Connection conn = cF.getConnection()) {
			CallableStatement call = conn.prepareCall(sql);
			call.setInt(1, r.getId()); //will be overwritten by the sequencer
			call.setString(2, r.getEmpUsername());
			call.setInt(3, r.getStatus());
			call.setDouble(4, r.getAmount());
			call.setDate(5, Date.valueOf(r.getDateMade())); //SQL friendly format is Date
			call.setString(6, r.getNotes());
			call.execute();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	//Manager userName is 1, RE_ID is 2
	public void approveReimbursement(Reimbursement r) {
		String sql = "{call APPROVE_REIM(?,?)";
		r.setStatus(1);
		//System.out.println("===="+r.getId()+"====" + ":::" +r.getApprovingManager());
		try (Connection conn = cF.getConnection()){
			CallableStatement call = conn.prepareCall(sql);
			call.setInt(1, r.getId());
			call.setString(2, r.getApprovingManager());
			call.execute();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	//We don't do anything fancy when rejecting a reimbursement
	//TODO our called proc updates status, this may not be needed
	public void rejectReimbursement(Reimbursement r) {
		String sql = "UPDATE REIMBURSEMENT SET STATUS = 2 WHERE RE_ID = ?";
		r.setStatus(2);
		try(Connection conn = cF.getConnection()){
			PreparedStatement ps = conn.prepareStatement(sql);
			ps.setInt(1, r.getId());
			ps.execute();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	//This should return a list of integers, which are the ids of the reimbursements
	//approved by the passed in manager (see approveReimbursement above)
	public ArrayList<Reimbursement> getApprovedReimsByManName(String manName){
		ArrayList<Reimbursement> reimList = new ArrayList<>();
		Reimbursement reim = null;
		String sql = "{call GRAB_REIMS_BY_MAN_NAME(?,?)";
		try(Connection conn = cF.getConnection()){
			CallableStatement call = conn.prepareCall(sql);
			call.setString(1, manName);
			call.registerOutParameter(2, OracleTypes.CURSOR); //Our SQL returns a cursor, jdbc needs to know that
			call.execute(); //EXECUTE
			ResultSet rs = (ResultSet) call.getObject(2);  //Convert our cursor into a result set
			while(rs.next()) {
				reim = new Reimbursement(rs.getString("EMP_USERNAME"), rs.getInt("STATUS"),
						rs.getDouble("AMOUNT"), rs.getString("NOTES"));
				reim.setApprovingManager(rs.getString("APPROVING_MANAGER"));
				reim.setDateMade(rs.getDate("DATE_MADE").toLocalDate());
				reimList.add(reim);
			}
			return reimList;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return reimList;
	}
	
	//returns a list of all integers (Reimbursement IDs) that are approved
	public ArrayList<Reimbursement> getApprovedReims(){
		ArrayList<Reimbursement> reimList = new ArrayList<>();
		String sql = "SELECT * FROM REIMBURSEMENT WHERE STATUS = 1";
		Reimbursement reim = null;
		try(Connection conn = cF.getConnection()){
			PreparedStatement prep = conn.prepareStatement(sql);
			ResultSet rs = prep.executeQuery();
			while(rs.next()) {
				//Remember that id is the object's hashcode
				reim = new Reimbursement(rs.getString("EMP_USERNAME"), rs.getInt("STATUS"),
						rs.getDouble("AMOUNT"), rs.getString("NOTES"));
				reimList.add(reim);
			}
			return reimList;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//log catastrophic failure
		return null;
	}
	
	//Calls GRAB_REIMS_BY_U_NAME
	public ArrayList<Reimbursement> getMyReimList(String uname){
		ArrayList<Reimbursement> reimList = new ArrayList<>();
		Reimbursement reim = null;
		String sql = "{call GRAB_REIMS_BY_U_NAME(?,?)";
		try(Connection conn = cF.getConnection()){
			CallableStatement call = conn.prepareCall(sql);
			call.setString(1, uname);
			call.registerOutParameter(2, OracleTypes.CURSOR); //Our SQL returns a cursor, jdbc needs to know that
			call.execute(); //EXECUTE
			ResultSet rs = (ResultSet) call.getObject(2);  //Convert our cursor into a result set
			while(rs.next()) {
				reim = new Reimbursement(rs.getString("EMP_USERNAME"), rs.getInt("STATUS"),
						rs.getDouble("AMOUNT"), rs.getString("NOTES"));
				reim.setApprovingManager(rs.getString("APPROVING_MANAGER"));
				reim.setDateMade(rs.getDate("DATE_MADE").toLocalDate());
				reimList.add(reim);
			}
			return reimList;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return reimList;
	}

}
