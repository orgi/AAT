package ch.bailu.aat.helpers.file;

import java.io.File;
import java.io.IOException;

import android.app.Activity;
import android.content.Context;
import ch.bailu.aat.R;
import ch.bailu.aat.helpers.AppDialog;
import ch.bailu.aat.helpers.AppLog;
import ch.bailu.aat.helpers.AppSelectDirectoryDialog;
import ch.bailu.aat.preferences.AddOverlayDialog;
import ch.bailu.aat.preferences.SolidDirectory;
import ch.bailu.aat.preferences.SolidMockLocationFile;
import ch.bailu.aat.services.ServiceContext;

public class FileUI {
    private final File file;
    
    public FileUI(File f) {
        file = f;
    }
    
    public void copyTo(Context context) throws IOException {
        new AppSelectDirectoryDialog(context, file);
    }

    public void copyTo(Context context, File targetDir) throws Exception {
        final File target = new File(targetDir, file.getName());

        if (target.exists()) {
            AppLog.e(context, getExistsMsg(context, target));
        } else {
            new UriAccess(context, file).copy(target);
            AppLog.i(context, target.getAbsolutePath());
        }

    }
    

    public static String getExistsMsg(Context c, File f) {
        StringBuilder msg = new StringBuilder()
                .append(f.getName())
                .append(c.getString(R.string.file_exists));
        return msg.toString();
    }


    public void reloadPreview(ServiceContext scontext) {
        if (file.getParent().equals(new SolidDirectory(scontext.getContext()).getValue())) {
            scontext.getDirectoryService().deleteEntry(file.getAbsolutePath());
        }
    }


    public void delete(Activity a, ServiceContext sc) {
        new FileDeletionDialog(a, sc);
    }


    private class FileDeletionDialog extends AppDialog {
        private final ServiceContext scontext;
        
        public FileDeletionDialog(Activity activity, ServiceContext sc) {
            scontext = sc;
            displayYesNoDialog(activity, activity.getString(R.string.file_delete_ask), file.toString());
        }


        @Override
        protected void onPositiveClick() {
            file.delete();
            rescanDirectory(scontext);
        }
    }


    public void useAsOverlay(Context context) {
        new AddOverlayDialog(context, file);
    }

    public void useForMockLocation(Context context) {
        new SolidMockLocationFile(context).setValue(file.toString());
    }


    
    public void rescanDirectory(ServiceContext scontext) {
        if (file.getParent().equals(new SolidDirectory(scontext.getContext()).getValue())) {
            scontext.getDirectoryService().rescan();
        }
    }

    public CharSequence getName() {
        return file.getName();
    }
}
