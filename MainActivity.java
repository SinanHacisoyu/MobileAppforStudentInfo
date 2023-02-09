package com.example.testproject02;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;

public class MainActivity extends AppCompatActivity  {

    EditText studentName, studentSurname, studentId, Gpa, additionalInfo, delId;
    TextView idInf, nameSurnameInf, birthDateInf, birthPlaceInf, genderInf,
            facultyInf, departmentInf, gpaInf, aInf, birthDate, birthPlace,
            forSpinFaculty, forSpinDept;
    Button buttonSubmit, buttonExit, buttonReset, buttonDisplay, buttonDelete, buttonUpdate, buttonSearch;

    private Button selectDate;
    private DatePickerDialog datePickerDialog;
    private TextView dateTxt;
    private Calendar calendar;
    private int dayOfMonth, month, year;

    private String[] city = {"city", "Istanbul", "Ankara", "Izmir", "Bursa", "Diyarbakir", "Gaziantep"};
    private String[] faculty = {"faculty", "Engineering", "Architecture", "Law"};
    private String[] departements = {"department", "Computer Engineering", "Law", "Architecture"};
    private String[] cityCode = {"00", "34", "06", "35", "16", "21", "27"};
    private String temp;

    private Spinner spCity, spFaculty, spDept;
    private ArrayAdapter<String> dataAdapterCity, dataAdapterFaculty, dataAdapterDept;

    private RadioGroup rgGender, rgScholarship;
    private RadioButton grb;
    private CheckBox cb;

    private SQLiteDatabase db;
    private TextView msgBox,textviewDepartment;
    ListView listView;
    String path, tempId;
    EditText rename,resurname,redept,refaculty;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        studentName = findViewById(R.id.studentName);
        studentSurname = findViewById(R.id.studentSurname);
        studentId = findViewById(R.id.studentId);
        Gpa = findViewById(R.id.gpaTxt);

        nameSurnameInf = (TextView) findViewById(R.id.nameSurnameInfo);
        idInf = (TextView) findViewById(R.id.idInfo);
        birthDateInf = (TextView) findViewById(R.id.birthDateInfo);
        birthPlaceInf = (TextView) findViewById(R.id.birthPlaceInfo);
        genderInf = (TextView) findViewById(R.id.genderInfo);
        facultyInf = (TextView) findViewById(R.id.facultyInfo);
        departmentInf = (TextView) findViewById(R.id.departmentInf);
        gpaInf = (TextView) findViewById(R.id.gpaInfo);
        aInf = (TextView) findViewById(R.id.aInfo);

        //creating database
        File myDbPath= getApplication().getFilesDir();
        path=myDbPath+"/"+"cmpe408";
        msgBox = findViewById(R.id.msgBox);
        //departmentInf
        msgBox.setText(path);
        listView =findViewById(R.id.listview);
        delId = findViewById(R.id.delId);
        rename = findViewById(R.id.rename);
        resurname = findViewById(R.id.resurname);
        redept = findViewById(R.id.redept);
        refaculty = findViewById(R.id.refaculy);

        // create data base
        try {
            if(!databaseExist()){
                // we don't hava data base
                db=SQLiteDatabase.openDatabase(path,null,SQLiteDatabase.CREATE_IF_NECESSARY);
                // create a table
                String mytable="create table student (recID integer PRIMARY KEY autoincrement, id text, name text, surname text, dept text," +
                        "faculty text, gender text, birthdate text, gpa text);";

                // execute the sql script
                db.execSQL(mytable);
                Toast.makeText(getApplication(),"Table is created", Toast.LENGTH_SHORT).show();

            }

        }
        catch (SQLException e) {
            msgBox.setText(e.getMessage());
        }

        birthDate = findViewById(R.id.bDate);
        birthPlace = findViewById(R.id.bPlace);
        rgGender = findViewById(R.id.gender);
        rgScholarship = findViewById(R.id.Scholarship);
        forSpinFaculty = (TextView) findViewById(R.id.faculty);
        forSpinDept = findViewById(R.id.department);

        additionalInfo = findViewById(R.id.addInfo);

        cb = (CheckBox) findViewById(R.id.checkBox);
        //If the checkbox is selected, then a text Input field is opened
        additionalInfo.setVisibility(View.INVISIBLE);
        cb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (cb.isChecked()) {
                    additionalInfo.setVisibility(View.VISIBLE);
                } else {
                    additionalInfo.setVisibility(View.INVISIBLE);
                }
            }
        });

        buttonSubmit = (Button) findViewById(R.id.btnSubmit);
        buttonReset = (Button) findViewById(R.id.btnReset);
        buttonExit = (Button) findViewById(R.id.btnExit);
        buttonDelete=(Button)findViewById(R.id.btnDelete);
        buttonDisplay=(Button)findViewById(R.id.btnDisplay);
        buttonSearch=(Button)findViewById(R.id.btnSearch);
        buttonUpdate=(Button)findViewById(R.id.btnUpdate);


        dataAdapterCity = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, cityCode);
        spCity = (Spinner) findViewById(R.id.sp1);
        spCity.setAdapter(dataAdapterCity);
        spCity.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                temp = city[i];
                birthPlace.setText(city[i]);
            }


            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });

        dataAdapterDept = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, departements);
        spDept = (Spinner) findViewById(R.id.sp3);
        spDept.setAdapter(dataAdapterDept);
        spDept.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                forSpinDept.setText(adapterView.getSelectedItem().toString());
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });


        dataAdapterFaculty = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, faculty);
        spFaculty = (Spinner) findViewById(R.id.sp2);
        spFaculty.setAdapter(dataAdapterFaculty);
        spFaculty.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                forSpinFaculty.setText(adapterView.getSelectedItem().toString());
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        selectDate = findViewById(R.id.selectDate);
        dateTxt = findViewById(R.id.dateTxt);
        selectDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                calendar = Calendar.getInstance();
                year = calendar.get(Calendar.YEAR);
                month = calendar.get(Calendar.MONTH);
                dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);
                datePickerDialog = new DatePickerDialog(MainActivity.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker datePicker, int day, int month, int year) {
                                // month + 1 because month does not start from 0
                                dateTxt.setText(day + " / " + (month + 1) + " / " + year);
                            }
                        }, year, month, dayOfMonth);
                datePickerDialog.show();
            }
        });


        buttonSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //if we have 11 digits for id number, the requested information is given
                if (studentId.getText().length() == 11) {
                    idInf.setText("ID :" + studentId.getText());
                    nameSurnameInf.setText("name surname :" + studentName.getText() + " " + studentSurname.getText());
                    birthDateInf.setText("birth :" + dateTxt.getText() + "    " + temp);
                    int selectedId = rgGender.getCheckedRadioButtonId();
                    grb = (RadioButton) findViewById(selectedId);
                    genderInf.setText("gender :" + grb.getText());
                    facultyInf.setText(forSpinFaculty.getText() + " - " + forSpinDept.getText());

                    if (gpaInf.getText().length() >= 0) {
                        int n1 = Integer.parseInt(String.valueOf(Gpa.getText()));
                        double n2 = (double) n1;
                        gpaInf.setText("GPA :" + n2);
                    } else {
                        int n1 = 0;
                        double n2 = (double) n1;
                        gpaInf.setText("GPA :" + n2);
                    }
                    if (cb.isChecked()) {
                        aInf.setText(additionalInfo.getText().toString());
                    }
                } else { //if we have NOT 11 digits for id number, gives a warning
                    idInf.setText("");
                    idInf.setHint("enter 11 element");

                    nameSurnameInf.setText("name surname :" + studentName.getText() + " " + studentSurname.getText());
                    birthDateInf.setText("birth :" + dateTxt.getText() + "    " + temp);
                    int selectedId = rgGender.getCheckedRadioButtonId();
                    grb = (RadioButton) findViewById(selectedId);
                    genderInf.setText("gender :" + grb.getText());
                    facultyInf.setText(forSpinFaculty.getText() + " - " + forSpinDept.getText());

                    if (gpaInf.getText().length() >= 0) {
                        int n1 = Integer.parseInt(String.valueOf(Gpa.getText()));
                        double n2 = (double) n1;
                        gpaInf.setText("GPA :" + n2);
                    } else {
                        int n1 = 0;
                        double n2 = (double) n1;
                        gpaInf.setText("GPA :" + n2);
                    }

                    if (cb.isChecked()) {
                        aInf.setText(additionalInfo.getText().toString());
                    }
                }
                //After display our information on the screen, adding them to the sql database with writeData function
                writeData();
            }
        });


        buttonReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                studentId.setText("");
                studentName.setText("");
                studentSurname.setText("");
                additionalInfo.setText("");
                Gpa.setText("");

                idInf.setText("");
                idInf.setHint("");
                nameSurnameInf.setText("");
                birthDateInf.setText("");

                gpaInf.setText("");
                genderInf.setText("");
                birthPlaceInf.setText("");
                genderInf.setText("");
                facultyInf.setText("");
                departmentInf.setText("");
                aInf.setText("");

                // to make additional checkbox empty
                if (cb.isChecked()) {
                    cb.setChecked(false);
                }

                // to make gender and scholarship radio group empty
                rgGender.clearCheck();
                rgScholarship.clearCheck();

                // to make spinner city, department, faculty empty
                spCity.setSelection(0);
                spDept.setSelection(0);
                spFaculty.setSelection(0);
            }
        });

        buttonExit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                System.exit(1);
            }
        });

        buttonDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Delete();
            }
        });

        buttonSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Search();
            }
        });

        buttonUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Update();
            }
        });

        buttonDisplay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                readData();
            }
        });
    }

    //helper method
    private boolean databaseExist() {
        File dbFile= new File(path);
        return  dbFile.exists();
    }

    //for display button, reading data from database
    public void  readData( ) {
        try {
            db=SQLiteDatabase.openDatabase(path,null,SQLiteDatabase.CREATE_IF_NECESSARY);
            String search = "select * from student";
            Cursor cursor = db.rawQuery(search,null);
            ArrayList<String> students = new ArrayList<>();
            ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item,students);
            while(cursor.moveToNext()) {
                @SuppressLint("Range") String id= cursor.getString(cursor.getColumnIndex("id"));
                @SuppressLint("Range") String name= cursor.getString(cursor.getColumnIndex("name"));
                @SuppressLint("Range") String lastName= cursor.getString(cursor.getColumnIndex("surname"));
                @SuppressLint("Range") String dept= cursor.getString(cursor.getColumnIndex("dept"));
                String result=id+"  "+name+" "+lastName+"  "+dept;
                students.add(result);// one student is added to the list..
            }
            listView.setAdapter(adapter);
            db.close();

        }
        catch (SQLException e) {
            msgBox.setText(e.getMessage());
        }
    }


    //insert data to database
    public void writeData() {
        try {
            String id=studentId.getText().toString();
            String name =studentName.getText().toString();
            String surName= studentSurname.getText().toString();
            String dept=forSpinDept.getText().toString();
            String fac=forSpinFaculty.getText().toString();
            String gender=grb.getText().toString();
            String birthdate=dateTxt.getText().toString();
            String gpa=gpaInf.getText().toString();

            db=SQLiteDatabase.openDatabase(path,null, SQLiteDatabase.CREATE_IF_NECESSARY);

            String input ="insert into student (id,name,surname,dept,faculty,gender,birthdate,gpa) values ('"+id+"','"+name+"','"+surName+"','"+dept+"'," +
                    "'"+fac+"', '"+gender+"','"+birthdate+"','"+gpa+"' )";
            // execute the script
            db.execSQL(input);
            Toast.makeText(getApplication(),"data is inserted", Toast.LENGTH_SHORT).show();
            db.close();
        }
        catch (SQLException e) {
            msgBox.setText(e.getMessage());
        }
    }

    public void Search(){
        tempId = delId.getText().toString();
        try {
            db = SQLiteDatabase.openDatabase(path, null, SQLiteDatabase.CREATE_IF_NECESSARY);
            String search = "select * from student where id = '" + tempId + "'";
            Cursor cursor = db.rawQuery(search, null);
            ArrayList<String> students = new ArrayList<>();
            ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, students);
            //students.add("id         name         surname       dept");
            while (cursor.moveToNext()) {
                @SuppressLint("Range") String id = cursor.getString(cursor.getColumnIndex("id"));
                @SuppressLint("Range") String name = cursor.getString(cursor.getColumnIndex("name"));
                @SuppressLint("Range") String lastName = cursor.getString(cursor.getColumnIndex("surname"));
                @SuppressLint("Range") String dept = cursor.getString(cursor.getColumnIndex("dept"));

                String result = id + "   " + name + "   " + lastName + "    " + dept;
                students.add(result);// one student is added to the list..
            }
            listView.setAdapter(adapter);
            db.close();
        } catch (SQLException e) {
            msgBox.setText(e.getMessage());
        }
    }

    public void Delete(){
        tempId = delId.getText().toString();
        try {
            db = SQLiteDatabase.openDatabase(path, null, SQLiteDatabase.CREATE_IF_NECESSARY);
            String delete = "delete from student where id = '" + tempId + "'";
            db.execSQL(delete);
            Toast.makeText(getApplication(), "data is deleted", Toast.LENGTH_SHORT).show();
            db.close();

            delId.setText("");
        } catch (Exception e) {
            msgBox.setText(e.getMessage());
        }
    }



    public void Update(){
        tempId = delId.getText().toString();
        String Name = rename.getText().toString();
        String surName = resurname.getText().toString();
        String studep = redept.getText().toString();
        String stufac = refaculty.getText().toString();

        //id,name,surname,depart
        try {
            db = SQLiteDatabase.openDatabase(path, null, SQLiteDatabase.CREATE_IF_NECESSARY);

            String input = "update student set name = '" + Name + "', surname = '" + surName + "', dept = '" + studep + "', faculty = '" + stufac + "' where id = '" + tempId + "'";
            db.execSQL(input);
            db.close();

            Toast.makeText(getApplication(), "data is updated", Toast.LENGTH_SHORT).show();

            delId.setText("");
            rename.setText("");
            resurname.setText("");
            redept.setText("");
            refaculty.setText("");

        } catch (SQLException e) {
            msgBox.setText(e.getMessage());
        }
    }


    public void MyMenu(Menu menu) {
        int group = 0;
        menu.add(group, 1, 1, "display");
        menu.add(group, 2, 2, "search");
        menu.add(group, 3, 3, "update");
        menu.add(group, 4, 4, "delete");
        menu.add(group, 5, 5, "help");


        menu.add(group, 6, 6, "Submit");
        menu.add(group, 7, 7, "reset");
        menu.add(group, 8, 8, "exit");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MyMenu(menu);
        return true;
    }

    private boolean applyOptions(MenuItem item) {
        int itemID = item.getItemId();
        if (itemID == 8) {
            System.exit(1);
        }
        else if (itemID == 7) {
            studentId.setText("");
            studentName.setText("");
            studentSurname.setText("");
            additionalInfo.setText("");
            Gpa.setText("");

            idInf.setText("");
            idInf.setHint("");
            nameSurnameInf.setText("");
            birthDateInf.setText("");

            gpaInf.setText("");
            genderInf.setText("");
            birthPlaceInf.setText("");
            genderInf.setText("");
            facultyInf.setText("");
            departmentInf.setText("");
            aInf.setText("");

            // to make additional checkbox empty
            if (cb.isChecked()) {
                cb.setChecked(false);
            }

            // to make gender and scholarship radio group empty
            rgGender.clearCheck();
            rgScholarship.clearCheck();

            // to make spinner city, department, faculty empty
            spCity.setSelection(0);
            spDept.setSelection(0);
            spFaculty.setSelection(0);
        }
        else if (itemID == 6) {
            if (studentId.getText().length() == 11) {
                idInf.setText("ID :" + studentId.getText());
                nameSurnameInf.setText("name surname :" + studentName.getText() + " " + studentSurname.getText());
                birthDateInf.setText("birth :" + dateTxt.getText() + "    " + temp);
                int selectedId = rgGender.getCheckedRadioButtonId();
                grb = (RadioButton) findViewById(selectedId);
                genderInf.setText("gender :" + grb.getText());
                facultyInf.setText(forSpinFaculty.getText() + " - " + forSpinDept.getText());

                if (gpaInf.getText().length() >= 0) {
                    int n1 = Integer.parseInt(String.valueOf(Gpa.getText()));
                    double n2 = (double) n1;
                    gpaInf.setText("GPA :" + n2);
                } else {
                    int n1 = 0;
                    double n2 = (double) n1;
                    gpaInf.setText("GPA :" + n2);
                }
                if (cb.isChecked()) {
                    aInf.setText(additionalInfo.getText().toString());
                }
            } else { //if we have NOT 11 digits for id number, gives a warning
                idInf.setText("");
                idInf.setHint("enter 11 element");

                nameSurnameInf.setText("name surname :" + studentName.getText() + " " + studentSurname.getText());
                birthDateInf.setText("birth :" + dateTxt.getText() + "    " + temp);
                int selectedId = rgGender.getCheckedRadioButtonId();
                grb = (RadioButton) findViewById(selectedId);
                genderInf.setText("gender :" + grb.getText());
                facultyInf.setText(forSpinFaculty.getText() + " - " + forSpinDept.getText());

                if (gpaInf.getText().length() >= 0) {
                    int n1 = Integer.parseInt(String.valueOf(Gpa.getText()));
                    double n2 = (double) n1;
                    gpaInf.setText("GPA :" + n2);
                } else {
                    int n1 = 0;
                    double n2 = (double) n1;
                    gpaInf.setText("GPA :" + n2);
                }

                if (cb.isChecked()) {
                    aInf.setText(additionalInfo.getText().toString());
                }
                writeData();
            }}

        else if (itemID == 5) {
            studentId.setText("fill the blanks");
        }

        else if (itemID == 4) {
            Delete();
        }
        else if (itemID == 3) {
            Update();
        }
        else if (itemID == 2) {

            Search();

        }
        else if (itemID == 1) {
            readData();
        }

        return false;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        return applyOptions(item);
    }

}