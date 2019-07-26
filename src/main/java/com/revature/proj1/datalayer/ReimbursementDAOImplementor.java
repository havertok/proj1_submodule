package com.revature.proj1.datalayer;

import java.io.ByteArrayInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Blob;
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

	// 0 is pending, 1 accepted, 2 rejected for reim status
	public ArrayList<Reimbursement> grabReimbursmentList() {
		ArrayList<Reimbursement> reimList = new ArrayList<>();
		String sql = "SELECT * FROM REIMBURSEMENT";
		Reimbursement reim = null;
		try (Connection conn = cF.getConnection()) {
			PreparedStatement prep = conn.prepareStatement(sql);
			ResultSet rs = prep.executeQuery();
			while (rs.next()) {
				// Remember that id is the object's hashcode
				reim = new Reimbursement(rs.getString("EMP_USERNAME"), rs.getInt("STATUS"), rs.getDouble("AMOUNT"),
						rs.getString("NOTES"));
				reim.setDateMade(rs.getDate("DATE_MADE").toLocalDate());
				reim.setReciept(getReimImageByID(reim.getId()));
				reimList.add(reim);
			}
			return reimList;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// log catastrophic failure
		return null;
	}

	public void pushReimbursementList(List<Reimbursement> reimList) {
		for (Reimbursement r : reimList) {
			addReimbursementDB(r);
		}
	}

	public Reimbursement getReimbursementByid(int id) {
		String sql = "SELECT * FROM REIMBURSEMENT WHERE RE_ID = ?";
		Reimbursement reim = null;
		try (Connection conn = cF.getConnection()) {
			PreparedStatement prep = conn.prepareStatement(sql);
			prep.setInt(1, id);
			ResultSet rs = prep.executeQuery();
			while (rs.next()) {
				// Should only return one Reim
				reim = new Reimbursement(rs.getString("EMP_USERNAME"), rs.getInt("STATUS"), rs.getDouble("AMOUNT"),
						rs.getString("NOTES"));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return reim;
	}

	// We can add a single reimbursement to the DB and the list
	public void addReimbursementDB(Reimbursement r) {
		String sql = "CALL ADD_REIMBURSEMENT(?,?,?,?,?,?)";
		if (!Company.reimbursements.contains(r)) {
			Company.reimbursements.add(r);
		}
		try (Connection conn = cF.getConnection()) {
			CallableStatement call = conn.prepareCall(sql);
			call.setInt(1, r.getId()); // will be overwritten by the sequencer
			call.setString(2, r.getEmpUsername());
			call.setInt(3, r.getStatus());
			call.setDouble(4, r.getAmount());
			call.setDate(5, Date.valueOf(r.getDateMade())); // SQL friendly format is Date
			call.setString(6, r.getNotes());
			call.execute();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void addReimImage(int id, byte[] imgByte) {
		if (imgByte != null) {
			String sql = "INSERT INTO REIMBURSEMENT_IMAGE VALUES (?, ?, ?)";
			try (Connection conn = cF.getConnection()) {
				PreparedStatement prep = conn.prepareStatement(sql);
				prep.setInt(1, id);
				prep.setString(2, null);
				prep.setBinaryStream(3, new ByteArrayInputStream(imgByte), imgByte.length);
				prep.execute();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}

	// Manager userName is 1, RE_ID is 2; despite being called approve, this now
	// rejects also
	public void approveReimbursement(int rid, int toStatus, String manName) {
		String sql = "{call APPROVE_REIM(?,?,?)";
		// System.out.println("===="+r.getId()+"====" + ":::" +r.getApprovingManager());
		try (Connection conn = cF.getConnection()) {
			CallableStatement call = conn.prepareCall(sql);
			call.setInt(1, rid);
			call.setLong(2, toStatus);
			call.setString(3, manName);
			call.execute();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	// We don't do anything fancy when rejecting a reimbursement
	// TODO our called proc updates status, this may not be needed
	public void rejectReimbursement(Reimbursement r) {
		String sql = "UPDATE REIMBURSEMENT SET STATUS = 2 WHERE RE_ID = ?";
		r.setStatus(2);
		try (Connection conn = cF.getConnection()) {
			PreparedStatement ps = conn.prepareStatement(sql);
			ps.setInt(1, r.getId());
			ps.execute();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	// This should return a list of integers, which are the ids of the
	// reimbursements
	// approved by the passed in manager (see approveReimbursement above)
	public ArrayList<Reimbursement> getApprovedReimsByManName(String manName) {
		ArrayList<Reimbursement> reimList = new ArrayList<>();
		Reimbursement reim = null;
		String sql = "{call GRAB_REIMS_BY_MAN_NAME(?,?)";
		try (Connection conn = cF.getConnection()) {
			CallableStatement call = conn.prepareCall(sql);
			call.setString(1, manName);
			call.registerOutParameter(2, OracleTypes.CURSOR); // Our SQL returns a cursor, jdbc needs to know that
			call.execute(); // EXECUTE
			ResultSet rs = (ResultSet) call.getObject(2); // Convert our cursor into a result set
			while (rs.next()) {
				reim = new Reimbursement(rs.getString("EMP_USERNAME"), 1, rs.getDouble("AMOUNT"),
						rs.getString("NOTES"));
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

	// returns a list of all integers (Reimbursement IDs) that are approved
	public ArrayList<Reimbursement> getApprovedReims() {
		ArrayList<Reimbursement> reimList = new ArrayList<>();
		String sql = "SELECT * FROM REIMBURSEMENT WHERE STATUS = 1";
		Reimbursement reim = null;
		try (Connection conn = cF.getConnection()) {
			PreparedStatement prep = conn.prepareStatement(sql);
			ResultSet rs = prep.executeQuery();
			while (rs.next()) {
				// Remember that id is the object's hashcode
				reim = new Reimbursement(rs.getString("EMP_USERNAME"), rs.getInt("STATUS"), rs.getDouble("AMOUNT"),
						rs.getString("NOTES"));
				reimList.add(reim);
			}
			return reimList;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// log catastrophic failure
		return null;
	}

	// Calls GRAB_REIMS_BY_U_NAME
	public ArrayList<Reimbursement> getMyReimList(String uname) {
		ArrayList<Reimbursement> reimList = new ArrayList<>();
		Reimbursement reim = null;
		String sql = "{call GRAB_REIMS_BY_U_NAME(?,?)";
		try (Connection conn = cF.getConnection()) {
			CallableStatement call = conn.prepareCall(sql);
			call.setString(1, uname);
			call.registerOutParameter(2, OracleTypes.CURSOR); // Our SQL returns a cursor, jdbc needs to know that
			call.execute(); // EXECUTE
			ResultSet rs = (ResultSet) call.getObject(2); // Convert our cursor into a result set
			while (rs.next()) {
				reim = new Reimbursement(rs.getString("EMP_USERNAME"), rs.getInt("STATUS"), rs.getDouble("AMOUNT"),
						rs.getString("NOTES"));
				reim.setApprovingManager(rs.getString("APPROVING_MANAGER"));
				reim.setDateMade(rs.getDate("DATE_MADE").toLocalDate());
				reim.setReciept(getReimImageByID(reim.getId()));
				reimList.add(reim);
			}
			return reimList;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return reimList;
	}

	public byte[] getReimImageByID(int id) {
		int reimId;
		byte[] reciept = null;
		String sql = "SELECT * FROM REIMBURSEMENT_IMAGE WHERE REIM_ID = ?";
		try (Connection conn = cF.getConnection()) {
			PreparedStatement prep = conn.prepareStatement(sql);
			prep.setInt(1, id);
			ResultSet rs = prep.executeQuery();
			while (rs.next()) {
				reimId = rs.getInt(1);
				Blob b = rs.getBlob("BLOB_CONTENT");
				if (b != null) {
					reciept = b.getBytes(1, (int) b.length());
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return reciept;
	}

}
