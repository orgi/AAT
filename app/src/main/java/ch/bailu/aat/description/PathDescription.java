package ch.bailu.aat.description;

import android.content.Context;

import ch.bailu.aat.R;
import ch.bailu.aat.gpx.GpxInformation;

public class PathDescription extends NameDescription {
    
    public PathDescription(Context context) {
        super(context);
    }

    @Override
    public void onContentUpdated(int iid, GpxInformation info) {
        updateName(info.getPath());
    }
    
    
    @Override
    public String getLabel() {
        return getString(R.string.d_path);
    }
}
