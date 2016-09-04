package ch.bailu.aat.views;

import java.util.Calendar;
import java.util.GregorianCalendar;

import org.osmdroid.views.MapView;

import android.app.DatePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.text.format.DateFormat;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import ch.bailu.aat.coordinates.BoundingBox;
import ch.bailu.aat.helpers.AppTheme;
import ch.bailu.aat.preferences.SolidBoolean;
import ch.bailu.aat.preferences.SolidDirectory;
import ch.bailu.aat.preferences.SolidLong;

public class DirectorySelection extends LinearLayout implements OnClickListener {


    private final MapView map;
    
    private final Button   getBox;
    
    private final SolidDirectory sdirectory;
    
    
    public DirectorySelection(MapView m) {
        super(m.getContext());
        
        
        final SolidCheckBox applayFrom, applayTo, applayGeo;
        map = m;
        
        sdirectory = new SolidDirectory(map.getContext());
        
        setOrientation(VERTICAL);
        
        
        LinearLayout geo = new LinearLayout(map.getContext());
        applayGeo = new SolidCheckBox(geo, sdirectory.getUseGeo());
        applayGeo.setText("Inside map*");
        
        getBox = new Button(map.getContext());
        AppTheme.themify(getBox);
        getBox.setText("pick area from map*");
        geo.addView(getBox);
        getBox.setOnClickListener(this);
        addView(geo);
        
        
        LinearLayout from = new LinearLayout(map.getContext());
        applayFrom = new SolidCheckBox(from, sdirectory.getUseDateStart());
        applayFrom.setText("Start date*");

        new DateSet(from, sdirectory.getDateStart());
        addView(from);

        LinearLayout to = new LinearLayout(map.getContext()); 
        applayTo = new SolidCheckBox(to, sdirectory.getUseDateEnd());
        applayTo.setText("End date*");

        new DateSet(to, sdirectory.getDateEnd());
        addView(to);

    }
   
    
    @Override
    public void onClick(View v) {
        sdirectory.getBoundingBox().setValue(new BoundingBox(map.getBoundingBox()));        
    }
    
 
    public class SolidCheckBox extends CheckBox implements OnCheckedChangeListener {

        private final SolidBoolean sboolean;
        
        public SolidCheckBox(ViewGroup parent, SolidBoolean s) {
            super(parent.getContext());
            sboolean = s;
            
            parent.addView(this);
            
            setChecked(sboolean.getValue());
            setOnCheckedChangeListener(this);
        }

        @Override
        public void onCheckedChanged(CompoundButton buttonView,
                boolean isChecked) {
            sboolean.setValue(isChecked);
        }
    }
    
    
    public class DateSet implements OnClickListener, OnDateSetListener {
        private Button        button;
        private java.text.DateFormat  formater;
        
        private GregorianCalendar calendar=new GregorianCalendar();
        
        private SolidLong slong;
        
        public DateSet(ViewGroup parent, SolidLong l) {
            slong = l;
            formater = DateFormat.getDateFormat(parent.getContext());
            calendar.setTimeInMillis(slong.getValue());
            
            button = new Button(parent.getContext());
            AppTheme.themify(button);
            button.setOnClickListener(this);
            updateButtonText();
            parent.addView(button);
            
        }
        
        private void updateButtonText() {
            button.setText(formater.format(calendar.getTime()));
        }
        
        @Override
        public void onClick(View v) {
            DatePickerDialog picker=new DatePickerDialog(getContext(),
                    this,
                    calendar.get(Calendar.YEAR),
                    calendar.get(Calendar.MONTH),
                    calendar.get(Calendar.DAY_OF_MONTH));
                    
            picker.show();
            
        }

        @Override
        public void onDateSet(DatePicker view, int y, int m, int d) {
            calendar.set(Calendar.YEAR, y);
            calendar.set(Calendar.MONTH, m);
            calendar.set(Calendar.DAY_OF_MONTH, d);
            slong.setValue(calendar.getTimeInMillis());
            updateButtonText();
        }

    }


   

    
}