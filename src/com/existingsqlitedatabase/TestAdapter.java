package com.existingsqlitedatabase;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class TestAdapter {
	protected static final String TAG = "DataAdapter";

	private final Context mContext;
	private SQLiteDatabase mDb;
	private DataBaseHelper mDbHelper;

	public TestAdapter(Context context) {
		this.mContext = context;
		mDbHelper = new DataBaseHelper(mContext);
	}

	public TestAdapter createDatabase() throws SQLException {
		try {
			mDbHelper.createDataBase();
		} catch (IOException mIOException) {
			Log.e(TAG, mIOException.toString() + "  UnableToCreateDatabase");
			throw new Error("UnableToCreateDatabase");
		}
		return this;
	}

	public TestAdapter open() throws SQLException {
		try {
			mDbHelper.openDataBase();
			mDbHelper.close();
			mDb = mDbHelper.getReadableDatabase();
		} catch (SQLException mSQLException) {
			Log.e(TAG, "open >>" + mSQLException.toString());
			throw mSQLException;
		}
		return this;
	}

	public void close() {
		mDbHelper.close();
	}

	public Cursor getTestData() {
		try {
			String sql = "SELECT * FROm student";

			Cursor mCur = mDb.rawQuery(sql, null);
			if (mCur != null) {
				mCur.moveToLast();
			}
			return mCur;
		} catch (SQLException mSQLException) {
			Log.e(TAG, "getTestData >>" + mSQLException.toString());
			throw mSQLException;
		}
	}

	public ArrayList<Student> getAllStudents() {
		try {
			String sql = "SELECT * FROM student";
			ArrayList<Student> studentList = new ArrayList<Student>();
			Cursor mCur = mDb.rawQuery(sql, null);
			if (mCur != null) {
				if (mCur.moveToFirst()) {
					do {
						Student student = new Student();
						student.setId(Integer.parseInt(mCur.getString(0)));
						student.setName(mCur.getString(1));
						student.setSurname(mCur.getString(2));
						// Adding contact to list
						studentList.add(student);
					} while (mCur.moveToNext());
				}
			}
			return studentList;
		} catch (SQLException mSQLException) {
			Log.e(TAG, "getTestData >>" + mSQLException.toString());
			throw mSQLException;
		}

	}

	public void RemoveStudentById(int id) {
		mDb.delete("student", "id=?", new String[] { Integer.toString(id) });
	}

	public Student getStudentById(int id) {
		try {
			String sql = "SELECT * FROM student WHERE id = "
					+ Integer.toString(id);
			Student student = new Student();
			Cursor mCur = mDb.rawQuery(sql, null);
			if (mCur != null) {
				if (mCur.moveToFirst()) {
					student.setId(Integer.parseInt(mCur.getString(0)));
					student.setName(mCur.getString(1));
					student.setSurname(mCur.getString(2));
				}
			}
			return student;
		} catch (SQLException mSQLException) {
			Log.e(TAG, "getTestData >>" + mSQLException.toString());
			throw mSQLException;
		}

	}

	public boolean SaveEmployee(String name, String email) {
		try {
			ContentValues cv = new ContentValues();
			cv.put("name", name);
			cv.put("surname", email);

			mDb.insert("student", null, cv);

			Log.d("SaveEmployee", "informationsaved");
			return true;

		} catch (Exception ex) {
			Log.d("SaveEmployee", ex.toString());
			return false;
		}
	}
	public boolean AddStudent(String name, String surname) {
		try {
			ContentValues cv = new ContentValues();
			cv.put("name", name);
			cv.put("surname", surname);
			mDb.insert("student", null, cv);

			Log.d(name+ " " + surname + "  ADDED", "informationsaved");
			return true;

		} catch (Exception ex) {
			Log.d("Add student error", ex.toString());
			return false;
		}
	}
	public boolean EditStudent(Student student, int id)
	{
		try{
		String strFilter = "id=" + id;
		ContentValues args = new ContentValues();
		args.put("name", student.getName());
		args.put("surname", student.getName());
		mDb.update("student", args, strFilter, null);
		return true;
		}catch (Exception ex) {
			Log.d("Edit student error", ex.toString());
			return false;
		}
		
	}

}
