package ch.bailu.aat.views;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.view.View;
import android.view.View.OnClickListener;
import ch.bailu.aat.description.GpsStateDescription;
import ch.bailu.aat.gpx.GpxInformation;
import ch.bailu.aat.preferences.SolidGPSLock;
import ch.bailu.aat.preferences.Storage;

public class GPSStateButton extends NumberButton implements OnClickListener, OnSharedPreferenceChangeListener {

    private final Storage storage;
    private final SolidGPSLock slock;
    
    public GPSStateButton(Context c) {
        super(new GpsStateDescription(c), GpxInformation.ID.INFO_ID_LOCATION);
        
        
        storage=Storage.global(c);
        storage.register(this);
        
        slock=new SolidGPSLock(storage);
        setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v==this) {
            slock.cycle();
        }
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences,
            String key) {
        if (slock.hasKey(key)) {
            updateAllText();
        }
        
    }
    
    @Override
    public void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        storage.unregister(this);
    }

}
